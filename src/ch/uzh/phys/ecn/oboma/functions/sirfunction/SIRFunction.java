package ch.uzh.phys.ecn.oboma.functions.sirfunction;

import ch.uzh.phys.ecn.oboma.agents.api.Agent;
import ch.uzh.phys.ecn.oboma.common.InfectionState;
import ch.uzh.phys.ecn.oboma.functions.api.ITransformationFunction;
import ch.uzh.phys.ecn.oboma.map.api.INode;


public class SIRFunction implements ITransformationFunction{

    @Override
    public InfectionState apply(Agent pAgent, INode pNode) {

        double[] sirInNode = getSIRInNode(pNode);
        double[] newDiseaseDistributionInNode = calculateSIR(sirInNode[0], sirInNode[1], sirInNode[2]);
        InfectionState newAgentInfectionState = getNewInfectionState(newDiseaseDistributionInNode);

        return newAgentInfectionState;
    }

    private double[] getSIRInNode(INode pNode){
        //TODO: extract S, I and R from pNode

        return null;
    }

    private double[] calculateSIR(double pSusceptible, double pInfected, double pRecovered){
        ODESIR sir = new ODESIR();
        ODERK2 rk2 = new ODERK2();

        double dt = 1;
        double tn = 1;
        double[] yn = {pSusceptible, pInfected, pRecovered};
        double[] yn1 = new double[3];

        yn1 = rk2.step(yn, tn, dt, sir);

        return yn1;
    }

    private InfectionState getNewInfectionState(double[] pInfectionDistribution){
        //TODO calculate new InfectionState of agent

        return null;
    }

}
