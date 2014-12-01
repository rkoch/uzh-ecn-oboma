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
package ch.uzh.phys.ecn.oboma.agents.model;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;

import org.apache.commons.lang3.tuple.Pair;

import ch.uzh.phys.ecn.oboma.agents.api.IAgent;
import ch.uzh.phys.ecn.oboma.common.InfectionState;


@EqualsAndHashCode
public class Agent implements IAgent {

    private final String                      mId;
    private InfectionState                    mState;
    // The list is sorted and contains all waypoints AND preferred time of staying there
    private final List<Pair<String, Integer>> mRoute; // NodeId, # Timesteps on the node

    public Agent(String pId) {
        checkArgument(pId != null);

        mId = pId;
        mState = InfectionState.SUSCEPTIBLE;
        mRoute = new ArrayList<>();
    }


    public String getId() {
        return mId;
    }

    public int getPreferredTimeOfStay(String pNodeId) {
        // TODO: find pref time of stay of this current waypoint
        return 0;
    }

    public String getNextWaypoint(String pCurrentNodeId) {
        // TODO: find current waypoint and return next
        return "";
    }

    public InfectionState getState() {
        return mState;
    }

    public void setState(InfectionState pState) {
        mState = pState;
    }

}
