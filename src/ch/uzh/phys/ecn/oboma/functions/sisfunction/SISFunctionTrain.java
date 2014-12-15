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

        mInfectionPercentage = getPercentage(mDiseaseDistributionInNode[0], Math.abs(mDiseaseDistributionInNode[0] - mNewDiseaseDistributionInNode[0]));
        mRecoveryPercentage = 0.05d;
//        mRecoveryPercentage = getPercentage(mDiseaseDistributionInNode[2], Math.abs(mDiseaseDistributionInNode[2] - mNewDiseaseDistributionInNode[2]));
//        System.out.println(mRecoveryPercentage);
    }

    protected double[] calculateSIR(double pSusceptible, double pInfected, double pRecovered) {
        ODESIR sir = new ODESIR(DiseaseConstants.TRAIN_INFECTION_RATE, DiseaseConstants.TRAIN_RECOVERY_RATE);
        ODERK2 rk2 = new ODERK2();

        double dt = 1;
        double tn = 1;
        double[] yn = { pSusceptible, pInfected, pRecovered };
        double[] yn1 = new double[3];

        yn1 = rk2.step(yn, tn, dt, sir);

        return yn1;
    }

    protected double getPercentage(double pOld, double pNew) {
        if (Double.compare(pOld, 0.0d) == 0.0) {
            return 0d;
        }
        if (pOld > pNew) {
            return pNew / pOld;
        } else {
            return pOld /pNew;
        }
    }


}
