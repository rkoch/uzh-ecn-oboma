package ch.uzh.phys.ecn.oboma.agents.build;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import ch.uzh.phys.ecn.oboma.agents.api.IAgent;
import ch.uzh.phys.ecn.oboma.agents.model.Agent;
import ch.uzh.phys.ecn.oboma.common.InfectionState;
import ch.uzh.phys.ecn.oboma.map.api.INode;


public class AgentBuilder {

    private final static int    DEFAULT_AMOUNT_OF_TIME_TO_STAY    = 3;
    private final static int    DEFAULT_AMOUNT_OF_TRAVELNODES     = 5;
    private final static double TRAVELNODES_DEVIATION_PROBABILITY = 0.25;

    public static List<IAgent> generateAgents(int pNumberOfAgents, double pInfectionProbability, double pImuneProbability, INode pSourceNode) {
        List<IAgent> generatedAgents = new ArrayList<>();

        for (int i = 0; i < pNumberOfAgents; i++) {
            Agent agent = new Agent(UUID.randomUUID().toString(), generateRoute(pSourceNode));
            agent.setState(getInfectionState(pInfectionProbability, pImuneProbability));
            generatedAgents.add(agent);
        }

        return generatedAgents;
    }

    private static List<Pair<String, Integer>> generateRoute(INode sourceNode) {
        // amount of nodes on route is reverse-proportional to the size of the city
        // size of the city is amount of outgoing connections.
        Random randomGenerator = new Random();
        int nrOfTravellingNodes = 0;
        if (randomGenerator.nextInt((int) TRAVELNODES_DEVIATION_PROBABILITY * 100) == 0) {
            int minNodesToTravel = 1;
            int maxNodesToTravel = 100;
            nrOfTravellingNodes = (randomGenerator.nextInt((maxNodesToTravel - minNodesToTravel) + 1) + minNodesToTravel) / sourceNode.getDestinations().size();
        } else {
            nrOfTravellingNodes = DEFAULT_AMOUNT_OF_TRAVELNODES / sourceNode.getDestinations().size();
        }

        return getRoute(new ArrayList<Pair<String, Integer>>(), sourceNode, nrOfTravellingNodes);
    }

    private static List<Pair<String, Integer>> getRoute(List<Pair<String, Integer>> pRoute, INode sourceNode, int nrOfNodes) {
        int nrOfTravellingNodes = Math.max(nrOfNodes, sourceNode.getDestinations().size());

        if (nrOfTravellingNodes < 1) {
            return pRoute;
        }

        // select randomly one outgoing Path of the sourceNode
        Map<String, INode> destNodeMap = sourceNode.getDestinations().stream().collect(Collectors.toMap(INode::getId, Function.<INode>identity()));
        List<String> nodeIdentifiers = new ArrayList<>(destNodeMap.keySet());
        String randomKey = nodeIdentifiers.get(new Random().nextInt(nodeIdentifiers.size()));
        if (nrOfTravellingNodes > 1) {
            // TODO: check this again
            int preferredTimeToStay = DEFAULT_AMOUNT_OF_TIME_TO_STAY / nodeIdentifiers.size() * 100;

            pRoute.add(new MutablePair<String, Integer>(randomKey, preferredTimeToStay));
        } else {
            // last node reached, agent stays here for about 7 hours
            // each time step represents 15min
            pRoute.add(new MutablePair<String, Integer>(randomKey, 4 * 7));
        }

        return getRoute(pRoute, destNodeMap.get(randomKey), --nrOfNodes);
    }

    private static InfectionState getInfectionState(double pInfectionProbability, double pImmuneProbability) {
        Random r = new Random();

        if (r.nextInt((int) pInfectionProbability * 100) == 0) {
            return InfectionState.INFECTED;
        }

        if (r.nextInt((int) pImmuneProbability * 100) == 0) {
            return InfectionState.IMMUNE;
        }

        return InfectionState.SUSCEPTIBLE;
    }
}