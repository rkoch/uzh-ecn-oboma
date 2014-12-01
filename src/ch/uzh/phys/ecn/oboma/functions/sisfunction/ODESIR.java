package ch.uzh.phys.ecn.oboma.functions.sisfunction;

import ch.uzh.phys.ecn.oboma.common.DiseaseConstants;
import ch.uzh.phys.ecn.oboma.functions.api.IODE;


public class ODESIR implements IODE {

    private double mInfectionRate;
    private double mRecoveryRate = DiseaseConstants.RECOVERY_RATE;
    private double[] dy = new double[3];
    
   public ODESIR(double pInfectionRate){
      mInfectionRate = pInfectionRate;
   }

    @Override
    public double[] dydt(double[] y, double t){
        dy[0] = -mInfectionRate * y[1]*y[0]; //S
        dy[2] = mRecoveryRate * y[1]; //R
        dy[1] = - dy[0] - dy[2]; //I

        return dy;
    }

}
