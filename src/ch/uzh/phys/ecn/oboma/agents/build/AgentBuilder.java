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

import ch.uzh.phys.ecn.oboma.agents.model.Agent;
import ch.uzh.phys.ecn.oboma.common.DiseaseConstants;
import ch.uzh.phys.ecn.oboma.common.InfectionState;
import ch.uzh.phys.ecn.oboma.map.api.INode;
import ch.uzh.phys.ecn.oboma.map.api.INodeMap;


public class AgentBuilder {

    private static final String       ZURICH_NODE_ID = "8503000";
    private static final String       BERN_NODE_ID   = "8507000";

    private static final List<String> stations       = new ArrayList<>();

    static {
        stations.add(ZURICH_NODE_ID);
        stations.add(BERN_NODE_ID);
    }

    public static List<Agent> generateAgents(int pNumberOfAgents, double pInfectionProbability, double pImuneProbability, INodeMap pNodeMap, INode pSourceNode) {
        List<Agent> generatedAgents = new ArrayList<>();

        for (int i = 0; i < pNumberOfAgents; i++) {
            Agent agent = new Agent(UUID.randomUUID().toString(), generateRoute(pNodeMap, pSourceNode));

            if (stations.contains(pSourceNode.getId())) {
                agent.setState(getInfectionState(pInfectionProbability, pImuneProbability));
            } else {
                agent.setState(InfectionState.SUSCEPTIBLE);
            }
            generatedAgents.add(agent);
        }

        return generatedAgents;
    }

    private static List<Pair<String, Integer>> generateRoute(INodeMap pNodeMap, INode sourceNode) {
        // amount of nodes on route is reverse-proportional to the size of the city
        // size of the city is amount of outgoing connections.
        Random rand = new Random();
        int nrOfTravellingNodes = DiseaseConstants.DEFAULT_AMOUNT_OF_TRAVELNODES;
        if (rand.nextDouble() < DiseaseConstants.TRAVELNODES_DEVIATION_PROBABILITY) {
            int minNodesToTravel = 1;
            int maxNodesToTravel = 100;
            nrOfTravellingNodes = (rand.nextInt((maxNodesToTravel - minNodesToTravel) + 1) + minNodesToTravel)
                    / ((sourceNode.getDestinations().size() > 0) ? sourceNode.getDestinations().size() : 1);
        } else {
            nrOfTravellingNodes = (int) Math.round(((DiseaseConstants.DEFAULT_AMOUNT_OF_TRAVELNODES * 1d) / ((sourceNode.getDestinations().size() > 0) ? sourceNode
                    .getDestinations().size() : 1)));
        }

        return getRoute(pNodeMap, sourceNode, new ArrayList<Pair<String, Integer>>(), nrOfTravellingNodes);
    }

    private static List<Pair<String, Integer>> getRoute(INodeMap pNodeMap, INode sourceNode, List<Pair<String, Integer>> pRoute, int nrOfNodes) {
        int nrOfTravellingNodes = Math.min(nrOfNodes, sourceNode.getDestinations().size());

        if (nrOfTravellingNodes < 1 ||
                nrOfNodes == 0) {
            return pRoute;
        }

        // select randomly one outgoing Path of the sourceNode
        Map<String, INode> destNodeMap = sourceNode.getDestinations().stream().collect(Collectors.toMap(INode::getId, Function.<INode> identity()));

        List<String> nodeIdentifiers = new ArrayList<>(destNodeMap.keySet());
        String randomKey = nodeIdentifiers.get(new Random().nextInt(nodeIdentifiers.size()));
        int timeToStay = 1;
        if (nrOfTravellingNodes > 1) {
            double preferredTimeToStay = (DiseaseConstants.DEFAULT_AMOUNT_OF_TIME_TO_STAY * 1d) / (nodeIdentifiers.size() * 1d);
            // avoid 0-values on casting
            while (preferredTimeToStay < 1d) {
                preferredTimeToStay *= 10;
            }
            timeToStay = (int) Math.round(preferredTimeToStay);
        } else {
            // last node reached, agent stays here for about 7 hours
            // each time step represents 15min
            timeToStay = 4;
        }

        String[] tmpKeys = randomKey.split("-");
        String destNodeKey = tmpKeys[1];

        List<INode> destNodes = pNodeMap.getNodes();

        INode destNode = null;
        for (INode node : destNodes) {
            if (node.getId().equals(destNodeKey)) {
                destNode = node;
            }
        }

        pRoute.add(new MutablePair<String, Integer>(randomKey, timeToStay));

        return getRoute(pNodeMap, destNode, pRoute, --nrOfNodes);
    }

    private static InfectionState getInfectionState(double pInfectionProbability, double pImmuneProbability) {
        Random r = new Random();

        if (r.nextDouble() < pInfectionProbability) {
            return InfectionState.INFECTED;
        }

        if (r.nextDouble() < pImmuneProbability) {
            return InfectionState.IMMUNE;
        }

        return InfectionState.SUSCEPTIBLE;
    }
}
