package ch.uzh.phys.ecn.oboma.functions.sisfunction;

import ch.uzh.phys.ecn.oboma.agents.api.Agent;
import ch.uzh.phys.ecn.oboma.common.AgentUtils;
import ch.uzh.phys.ecn.oboma.common.InfectionState;
import ch.uzh.phys.ecn.oboma.functions.api.ITransformationFunction;
import ch.uzh.phys.ecn.oboma.map.api.INode;


public class SISFunction implements ITransformationFunction{

   private double[] mDiseaseDistributionInNode = new double[3];
   private double[] mNewDiseaseDistributionInNode = new double[3];

   private double mInfectionPercentage;
   private double mRecoveryPercentage;

    public SISFunction(INode pNode){
        mDiseaseDistributionInNode = AgentUtils.getDiseaseDistributionInNode(pNode);
        mNewDiseaseDistributionInNode = calculateSIR(mDiseaseDistributionInNode[0], mDiseaseDistributionInNode[1], mDiseaseDistributionInNode[2]);

        mInfectionPercentage = getInfectionPercentage(mDiseaseDistributionInNode[1], mNewDiseaseDistributionInNode[1]);
        mRecoveryPercentage = getRecoveryPercentage(mDiseaseDistributionInNode[2], mNewDiseaseDistributionInNode[2]);
    }

    @Override
    public InfectionState apply(Agent pAgent, INode pNode) {

        if(pAgent.getmInfectionState() == InfectionState.IMMUNE){
            return InfectionState.IMMUNE;
        }

        InfectionState newAgentInfectionState = getNewInfectionState(pAgent);

        if(newAgentInfectionState == InfectionState.RECOVERED){
            return InfectionState.SUSCEPTIBLE;
        }

        return newAgentInfectionState;
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

    private double getInfectionPercentage(double pOldInfected, double pNewInfected){
        return 1 - (pOldInfected/pNewInfected);
    }

 private double getRecoveryPercentage(double pOldRecovered, double pNewRecovered){
        return 1 - (pOldRecovered/pNewRecovered);
    }

    private InfectionState getNewInfectionState(Agent pAgent){
        //TODO calculate new InfectionState of agent

        return null;
    }

}
