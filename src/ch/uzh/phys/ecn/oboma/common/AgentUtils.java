package ch.uzh.phys.ecn.oboma.common;

import java.util.List;

import ch.uzh.phys.ecn.oboma.agents.model.Agent;
import ch.uzh.phys.ecn.oboma.map.api.INode;


public class AgentUtils {

    public static double[] getDiseaseDistributionInNode(INode pNode){
        List<Agent> agents = pNode.getAllAgents();
        
        int susceptible = 0;
        int infected = 0;
        int recovered = 0;
        int immune = 0;
        
        for(int i = 0; i < agents.size(); i++){
            if(agents.get(i).getState() == InfectionState.SUSCEPTIBLE){
                susceptible++;
            } else if(agents.get(i).getState() == InfectionState.INFECTED){
                infected++;
            } else if (agents.get(i).getState() == InfectionState.RECOVERED){
                recovered++;
            } else {
                immune++;
            }
        }
        
        double diseaseDistribution[] = {susceptible, infected, recovered, immune};

        return diseaseDistribution;
    }

}
