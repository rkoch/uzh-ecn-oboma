package ch.uzh.phys.ecn.oboma.agents.api;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import ch.uzh.phys.ecn.oboma.agents.build.AgentBuilder;
import ch.uzh.phys.ecn.oboma.agents.model.Agent;
import ch.uzh.phys.ecn.oboma.map.api.INode;
import ch.uzh.phys.ecn.oboma.map.api.INodeMap;


public class AgentFactory {

    private final static double INFECTION_PROBABILITY = 0.25;
    private final static double IMMUNE_PROBABILITY    = 0.10;

    private final static int    POPULATION_FACTOR     = 10;
    private final static int    MAX_POPULATION        = 100;

    private static final Logger LOGGER                = Logger.getLogger(AgentFactory.class.getName());


    public static void placeAgents(INodeMap nodeMap) {
        for (INode node : nodeMap.getNodes()) {
            if (node.isConnecting()) {
                // only place agents on stations
                continue;
            }

            int minAmountOfAgents = 10;
            int amountOfAgents = POPULATION_FACTOR * node.getDestinations().size();
            Random rand = new Random();
            amountOfAgents = (amountOfAgents > 1) ? amountOfAgents : rand.nextInt((MAX_POPULATION - minAmountOfAgents) + 1) + minAmountOfAgents;
            amountOfAgents = Math.min(amountOfAgents, MAX_POPULATION);

            List<Agent> generatedAgents = AgentBuilder.generateAgents(amountOfAgents, INFECTION_PROBABILITY, IMMUNE_PROBABILITY, node);

            for (Agent agent : generatedAgents) {
                node.place(agent);
            }

            LOGGER.info(generatedAgents.size() + " agents added to Node with Id " + node.getId());

        }
        LOGGER.info("Agent placing done");
    }
}
