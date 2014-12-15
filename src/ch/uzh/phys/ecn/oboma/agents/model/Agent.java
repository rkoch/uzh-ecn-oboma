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

import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import lombok.EqualsAndHashCode;

import org.apache.commons.lang3.tuple.Pair;

import ch.uzh.phys.ecn.oboma.agents.api.IAgent;
import ch.uzh.phys.ecn.oboma.common.InfectionState;


@EqualsAndHashCode
public class Agent
        implements IAgent {

    private final String                      mId;
    private InfectionState                    mState;
    // The list is sorted and contains all waypoints AND preferred time of staying there
    private final List<Pair<String, Integer>> mRoute;                                                 // NodeId, # Timesteps on the node

    private RouteDirection                    mRouteDirection;

    private static final Logger               LOGGER = Logger.getLogger(Agent.class.getName());


    public Agent(String pId, List<Pair<String, Integer>> pRoute) {
        checkArgument(pId != null);

        mId = pId;
        mState = InfectionState.SUSCEPTIBLE;

        mRoute = pRoute;
        mRouteDirection = RouteDirection.FORWARD;
    }


    public String getId() {
        return mId;
    }

    public int getPreferredTimeOfStay(String pNodeId) {
        for (Pair<String, Integer> item : this.mRoute) {
            if (item.getKey().equals(pNodeId)) {
                return item.getValue();
            }
        }

        return 1;
    }

    public String getNextWaypoint(String pCurrentNodeId) {
        ListIterator<Pair<String, Integer>> routeIterator = mRoute.listIterator();

        if (mRoute.isEmpty()) {
            return pCurrentNodeId;
        }


        if (pCurrentNodeId.contains("-")) {
            if (mRouteDirection.equals(RouteDirection.FORWARD)) {
                String[] keys = pCurrentNodeId.split("-");
                return keys[1];
            } else {
                // look for correct node to get previous node
                while (routeIterator.hasNext()) {
                    String connectionKey = routeIterator.next().getKey();
                    String[] keys = connectionKey.split("-");

                    if (connectionKey.equals(pCurrentNodeId)) {
                        // get previous node with endId equals startId of pCurrentNodeId
                        ListIterator<Pair<String, Integer>> backwardsIterator = mRoute.listIterator();

                        while (backwardsIterator.hasNext()) {
                            String previousNodeId = backwardsIterator.next().getKey();
                            String[] previousKeys = previousNodeId.split("-");

                            if (previousKeys[1].equals(keys[0])) {
                                if (!routeIterator.hasNext()) {
                                    mRouteDirection = RouteDirection.FORWARD;
                                }

                                return previousKeys[0];
                            }
                        }
                    }
                }
            }
        } else {
            while (routeIterator.hasNext() && mRouteDirection.equals(RouteDirection.FORWARD)) {
                String connectionKey = routeIterator.next().getKey();
                String[] keys = connectionKey.split("-");

                if (keys[0].equals(pCurrentNodeId)) {
                    // target node id
                    return connectionKey;
                }
            }

            mRouteDirection = RouteDirection.BACKWARDS;

            // set iterator to start of list again
            routeIterator = mRoute.listIterator();
            while (routeIterator.hasNext() && mRouteDirection.equals(RouteDirection.BACKWARDS)) {
                String connectionKey = routeIterator.next().getKey();
                String[] keys = connectionKey.split("-");

                if (keys[1].equals(pCurrentNodeId)) {
                    // source node id with currentNodeId as target
                    // -> getNode with sourceNode as target, which represents node before
                    ListIterator<Pair<String, Integer>> backwardsIterator = mRoute.listIterator();
                    while (backwardsIterator.hasNext()) {
                        String previousNodeKey = backwardsIterator.next().getKey();
                        String[] prevKeys = previousNodeKey.split("-");

                        if (prevKeys[1].equals(keys[1])) {
                            if (!routeIterator.hasNext()) {
                                mRouteDirection = RouteDirection.FORWARD;
                            }

                            // return route with direction changed
                            return prevKeys[0] + "-" + prevKeys[1];
                        }
                    }
                }
            }
        }

        LOGGER.warning("CurrentNodeId: " + pCurrentNodeId + ", Direction: " + mRouteDirection);

        throw new IllegalStateException("No route found for given nodeId " + pCurrentNodeId);
    }

    public InfectionState getState() {
        return mState;
    }

    public void setState(InfectionState pState) {
        mState = pState;
    }

}
