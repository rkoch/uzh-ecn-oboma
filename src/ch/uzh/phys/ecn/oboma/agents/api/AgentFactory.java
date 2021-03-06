package ch.uzh.phys.ecn.oboma.agents.api;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import ch.uzh.phys.ecn.oboma.agents.build.AgentBuilder;
import ch.uzh.phys.ecn.oboma.agents.model.Agent;
import ch.uzh.phys.ecn.oboma.common.DiseaseConstants;
import ch.uzh.phys.ecn.oboma.map.api.INode;
import ch.uzh.phys.ecn.oboma.map.api.INodeMap;


public class AgentFactory {

    private static final Logger LOGGER = Logger.getLogger(AgentFactory.class.getName());


    public static void placeAgents(INodeMap nodeMap) {
        int countAgents = 0;
        for (INode node : nodeMap.getNodes()) {
            if (node.isConnecting() || node.getOrigins().isEmpty() || node.getDestinations().isEmpty()) {
                // only place agents on stations
                continue;
            }

            int minAmountOfAgents = 10;
            int amountOfAgents = DiseaseConstants.POPULATION_FACTOR * node.getDestinations().size();
            countAgents+= amountOfAgents;
            Random rand = new Random();
            amountOfAgents = (amountOfAgents > 1) ? amountOfAgents : rand.nextInt((DiseaseConstants.MAX_POPULATION - minAmountOfAgents) + 1) + minAmountOfAgents;
            amountOfAgents = Math.min(amountOfAgents, DiseaseConstants.MAX_POPULATION);

            List<Agent> generatedAgents = AgentBuilder.generateAgents(amountOfAgents, DiseaseConstants.INFECTION_PROBABILITY, DiseaseConstants.IMMUNE_PROBABILITY, nodeMap, node);

            for (Agent agent : generatedAgents) {
                node.place(agent);
            }

            LOGGER.info(generatedAgents.size() + " agents added to Node with Id " + node.getId());

        }
        LOGGER.info("Agent placing done: " + countAgents + " agents placed.");
    }
}
