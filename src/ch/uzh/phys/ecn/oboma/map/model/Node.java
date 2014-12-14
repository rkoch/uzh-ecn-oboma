/*
 * The MIT License (MIT)
 * Copyright © 2014 different authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the “Software”), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package ch.uzh.phys.ecn.oboma.map.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import lombok.EqualsAndHashCode;

import org.apache.commons.lang3.StringUtils;

import ch.uzh.phys.ecn.oboma.agents.model.Agent;
import ch.uzh.phys.ecn.oboma.common.InfectionState;
import ch.uzh.phys.ecn.oboma.functions.api.ITransformationFunction;
import ch.uzh.phys.ecn.oboma.functions.nop.NopTransformationFunction;
import ch.uzh.phys.ecn.oboma.map.api.INode;


@EqualsAndHashCode
public class Node
        implements INode {

    private final String               mId;
    private final String               mName;
    private final double               mLatitude;
    private final double               mLongitude;
    private final int                  mTimeBlocked;
    private final int                  mSize;
    private ITransformationFunction    mInfectionFunction;
    private final Map<String, INode>   mDestinationNodes;
    private final Map<String, INode>   mOriginNodes;

    private final List<AgentPlacement> mAgents;
    private final List<Agent>          mLeavingAgents;    // No queue (list based fifo-if-free-seats)


    public Node(String pId, String pName, double pLat, double pLng, int pTimeBlocked, int pSize) {
        this(pId, pName, pLat, pLng, pTimeBlocked, pSize, new NopTransformationFunction());
    }

    public Node(String pId, String pName, double pLat, double pLng, int pTimeBlocked, int pSize, ITransformationFunction pInfectionFunction) {
        checkArgument(pId != null);
        checkArgument(pName != null);
        checkArgument(pSize >= 0);
        checkArgument(pInfectionFunction != null);

        mId = pId;
        mName = pName;
        mLatitude = pLat;
        mLongitude = pLng;
        mTimeBlocked = pTimeBlocked;
        mSize = pSize;
        mInfectionFunction = pInfectionFunction;

        mAgents = new ArrayList<>();
        mLeavingAgents = new ArrayList<>();
        mDestinationNodes = new HashMap<>();
        mOriginNodes = new HashMap<>();
    }


    @Override
    public String getId() {
        return mId;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public double getLatitude() {
        return mLatitude;
    }

    @Override
    public double getLongitude() {
        return mLongitude;
    }

    @Override
    public boolean isConnecting() {
        return false;
    }

    @Override
    public List<INode> getDestinations() {
        return new ArrayList<>(mDestinationNodes.values());
    }

    @Override
    public void addDestination(INode pNode) {
        mDestinationNodes.put(pNode.getId(), pNode);
    }

    @Override
    public List<INode> getOrigins() {
        return new ArrayList<>(mOriginNodes.values());
    }

    @Override
    public void addOrigin(INode pNode) {
        mOriginNodes.put(pNode.getId(), pNode);
    }

    @Override
    public boolean place(Agent pAgent) {
        checkArgument(pAgent != null);

        if (countFreeSeats() > 0) {
            int prefTime = pAgent.getPreferredTimeOfStay(mId);
            mAgents.add(new AgentPlacement(pAgent, Math.max(mTimeBlocked, prefTime)));
            return true;
        }
        return false;
    }

    @Override
    public int countFreeSeats() {
        return mSize == 0 ? Integer.MAX_VALUE : (mSize - mAgents.size());
    }

    @Override
    public List<Agent> getAllAgents() {
        List<Agent> ret = new ArrayList<>(mAgents.size() + mLeavingAgents.size());
        for (AgentPlacement ap : mAgents) {
            ret.add(ap.getAgent());
        }
        ret.addAll(mLeavingAgents);
        return ret;
    }

    @Override
    public List<Agent> getLeavingAgents() {
        return new ArrayList<Agent>(mLeavingAgents);
    }

    @Override
    public void setTransformationFunction(ITransformationFunction pInfectionFunction) {
        mInfectionFunction = pInfectionFunction != null ? pInfectionFunction : new NopTransformationFunction();
    }


    @Override
    public void preelapse() {
        // timestep on this node starts
        mInfectionFunction.onBeforeTimestep(this);

        Iterator<AgentPlacement> itr = mAgents.iterator();
        while (itr.hasNext()) {
            AgentPlacement ap = itr.next();
            // Elapse time for current dependant
            ap.preelapse();
            // Move agents to outgoing queue
            if (!ap.isStaying()) {
                mLeavingAgents.add(ap.getAgent());
                itr.remove();
            }
        }
    }

    @Override
    public void postelapse() {
        Iterator<Agent> itr = mLeavingAgents.iterator();
        while (itr.hasNext()) {
            Agent a = itr.next();
            String nextWaypoint = a.getNextWaypoint(mId);
            INode dest;
            if (StringUtils.equals(mId, nextWaypoint) || (nextWaypoint == null)) {
                dest = this;
            } else {
                dest = mDestinationNodes.get(nextWaypoint);
            }

            checkState(dest != null);

            if (dest.place(a)) {
                itr.remove();
            }
        }

        // timestep on this node is done
        mInfectionFunction.onAfterTimestep(this);
    }

    @Override
    public void infect() {
        Map<Agent, InfectionState> agentMods = new HashMap<>();
        final Consumer<Agent> doInfect = a -> {
            InfectionState next = mInfectionFunction.apply(a, this);
            if (next != a.getState()) {
                agentMods.put(a, next);
            }
        };
        mAgents.stream().map(AgentPlacement::getAgent).forEach(doInfect);
        mLeavingAgents.forEach(doInfect);
        agentMods.forEach((a, i) -> a.setState(i));
    }

}
