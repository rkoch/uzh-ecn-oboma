package ch.uzh.phys.ecn.oboma.functions.sisfunction;

import ch.uzh.phys.ecn.oboma.common.AgentUtils;
import ch.uzh.phys.ecn.oboma.common.DiseaseConstants;
import ch.uzh.phys.ecn.oboma.map.api.INode;


public class SISFunctionTrain
        extends SISFunction {

    @Override
    public void onBeforeTimestep(INode pNode) {
        mDiseaseDistributionInNode = AgentUtils.getDiseaseDistributionInNode(pNode);
        mNewDiseaseDistributionInNode = calculateSIR(mDiseaseDistributionInNode[0], mDiseaseDistributionInNode[1], mDiseaseDistributionInNode[2] + mDiseaseDistributionInNode[3]);

        mInfectionPercentage = getPercentage(pNode.getAllAgents().size(), Math.abs(mDiseaseDistributionInNode[0] - mNewDiseaseDistributionInNode[0]));
        mRecoveryPercentage = getPercentage(pNode.getAllAgents().size(), Math.abs(mDiseaseDistributionInNode[2] - mNewDiseaseDistributionInNode[2]));
    }

    protected double[] calculateSIR(double pSusceptible, double pInfected, double pRecovered) {
        ODESIR sir = new ODESIR(DiseaseConstants.TRAIN_INFECTION_RATE);
        ODERK2 rk2 = new ODERK2();

        double dt = 1;
        double tn = 1;
        double[] yn = { pSusceptible, pInfected, pRecovered };
        double[] yn1 = new double[3];

        yn1 = rk2.step(yn, tn, dt, sir);

        return yn1;
    }

    protected double getPercentage(double pNumberOfAgents, double pNew) {
        return pNew / pNumberOfAgents;
    }


}
