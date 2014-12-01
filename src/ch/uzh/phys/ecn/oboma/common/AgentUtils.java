package ch.uzh.phys.ecn.oboma.common;

import ch.uzh.phys.ecn.oboma.map.api.INode;


public class AgentUtils {

    public static double[] getDiseaseDistributionInNode(INode pNode){
        double susceptible = 0.0d;
        double infected = 0.0d;
        double recovered = 0.0d;
        double immune = 0.0;

        double diseaseDistribution[] = {susceptible, infected, recovered, immune};

        return diseaseDistribution;
    }

}
