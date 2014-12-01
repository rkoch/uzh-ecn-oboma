package ch.uzh.phys.ecn.oboma.functions.sisfunction;

import java.util.Random;

import ch.uzh.phys.ecn.oboma.agents.model.Agent;
import ch.uzh.phys.ecn.oboma.common.AgentUtils;
import ch.uzh.phys.ecn.oboma.common.DiseaseConstants;
import ch.uzh.phys.ecn.oboma.common.InfectionState;
import ch.uzh.phys.ecn.oboma.functions.api.ITransformationFunction;
import ch.uzh.phys.ecn.oboma.map.api.INode;


public class SISFunction implements ITransformationFunction{

   private double[] mDiseaseDistributionInNode = new double[4];
   private double[] mNewDiseaseDistributionInNode = new double[3];

   private double mInfectionPercentage;
   private double mRecoveryPercentage;

    @Override
    public void onBeforeTimestep(INode pNode){
        mDiseaseDistributionInNode = AgentUtils.getDiseaseDistributionInNode(pNode);
        mNewDiseaseDistributionInNode = calculateSIR(mDiseaseDistributionInNode[0], mDiseaseDistributionInNode[1], mDiseaseDistributionInNode[2] + mDiseaseDistributionInNode[3]);

        mInfectionPercentage = getPercentage(pNode.getAllAgents().size(), (mDiseaseDistributionInNode[0] - mNewDiseaseDistributionInNode[0]));
        mRecoveryPercentage = getPercentage(pNode.getAllAgents().size(), mDiseaseDistributionInNode[2] + mNewDiseaseDistributionInNode[2]);
    }
    
    @Override
    public void onAfterTimestep(INode pNode){
        
    }

    @Override
    public InfectionState apply(Agent pAgent, INode pNode) {

        if(pAgent.getState() == InfectionState.IMMUNE){
            return InfectionState.IMMUNE;
        }

        InfectionState newAgentInfectionState = getNewInfectionState(pAgent);

        if(newAgentInfectionState == InfectionState.RECOVERED || newAgentInfectionState == null){
            newAgentInfectionState = InfectionState.SUSCEPTIBLE;
        }

        return newAgentInfectionState;
    }

    private double[] calculateSIR(double pSusceptible, double pInfected, double pRecovered){
        ODESIR sir = new ODESIR(DiseaseConstants.TRAIN_INFECTION_RATE);
        ODERK2 rk2 = new ODERK2();

        double dt = 1;
        double tn = 1;
        double[] yn = {pSusceptible, pInfected, pRecovered};
        double[] yn1 = new double[3];

        yn1 = rk2.step(yn, tn, dt, sir);

        return yn1;
    }

    private double getPercentage(double pNumberOfAgents, double pNew){
        return pNew/pNumberOfAgents;
    }

    private InfectionState getNewInfectionState(Agent pAgent){
        InfectionState infectionState = pAgent.getState();
        InfectionState newInfectionState;
        Random r = new Random();
        double compare = r.nextDouble();
        
        if(infectionState == InfectionState.SUSCEPTIBLE){
            if(compare < mInfectionPercentage){
                newInfectionState = InfectionState.INFECTED;
            } else {
                newInfectionState = InfectionState.SUSCEPTIBLE;
            }   
        } else if (infectionState == InfectionState.INFECTED){
            if(compare < mRecoveryPercentage){
                newInfectionState = InfectionState.RECOVERED;
            } else {
                newInfectionState = InfectionState.INFECTED;
            }   
        } else {
            newInfectionState = null;
        }

        return newInfectionState;
    }

}
