package ch.uzh.phys.ecn.oboma.functions.sisfunction;

import java.util.Random;

import ch.uzh.phys.ecn.oboma.agents.model.Agent;
import ch.uzh.phys.ecn.oboma.common.InfectionState;
import ch.uzh.phys.ecn.oboma.functions.api.ITransformationFunction;
import ch.uzh.phys.ecn.oboma.map.api.INode;


public abstract class SISFunction
        implements ITransformationFunction {

    protected double[] mDiseaseDistributionInNode    = new double[4];
    protected double[] mNewDiseaseDistributionInNode = new double[3];

    protected double   mInfectionPercentage          = 0.0d;
    protected double   mRecoveryPercentage           = 0.0d;

    @Override
    public abstract void onBeforeTimestep(INode pNode);

    @Override
    public void onAfterTimestep(INode pNode) {
        mDiseaseDistributionInNode = new double[4];
        mNewDiseaseDistributionInNode = new double[3];

        mInfectionPercentage = 0.0d;
        mRecoveryPercentage = 0.0d;
    }

    protected abstract double[] calculateSIR(double pSusceptible, double pInfected, double pRecovered);

    protected abstract double getPercentage(double pNumberOfAgents, double pNew);

    @Override
    public InfectionState apply(Agent pAgent, INode pNode) {

        if (pAgent.getState() == InfectionState.IMMUNE) {
            return InfectionState.IMMUNE;
        }

        InfectionState newAgentInfectionState = getNewInfectionState(pAgent);

        if (newAgentInfectionState == InfectionState.RECOVERED || newAgentInfectionState == null) {
            newAgentInfectionState = InfectionState.RECOVERED;
        }

        return newAgentInfectionState;
    }


    private InfectionState getNewInfectionState(Agent pAgent) {
        InfectionState infectionState = pAgent.getState();
        InfectionState newInfectionState;
        Random r = new Random();
        double compare = r.nextDouble();

        if (infectionState == InfectionState.SUSCEPTIBLE) {
            if (compare < mInfectionPercentage) {
                newInfectionState = InfectionState.INFECTED;
            } else {
                newInfectionState = InfectionState.SUSCEPTIBLE;
            }
        } else if (infectionState == InfectionState.INFECTED) {
            if (compare < mRecoveryPercentage) {
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
