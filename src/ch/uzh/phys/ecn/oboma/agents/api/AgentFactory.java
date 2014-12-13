package ch.uzh.phys.ecn.oboma.agents.api;

import java.util.List;

import ch.uzh.phys.ecn.oboma.agents.build.AgentBuilder;
import ch.uzh.phys.ecn.oboma.map.api.INode;
import ch.uzh.phys.ecn.oboma.map.api.INodeMap;


public class AgentFactory {

    private final static double INFECTION_PROBABILITY = 0.25;
    private final static double IMMUNE_PROBABILITY    = 0.25;

    private final static int    POPULATION_FACTOR     = 100;

    public static void placeAgents(INodeMap nodeMap) {

        // TODO: foreach node in nodeMap
//        INode node = null;
//
//        // number of agents on a node is factor * amountOfConnectionsInNode
//        int amountOfAgents = POPULATION_FACTOR * node.getDestinationNodes().size();
//
//        List<IAgent> agents = AgentBuilder.generateAgents(amountOfAgents, INFECTION_PROBABILITY,
//                IMMUNE_PROBABILITY, node);
//
//        for (IAgent agent : agents) {
//            node.place(agent);
//        }
    }
}
