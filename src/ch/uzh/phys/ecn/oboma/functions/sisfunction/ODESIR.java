package ch.uzh.phys.ecn.oboma.functions.sisfunction;

import ch.uzh.phys.ecn.oboma.common.DiseaseConstants;
import ch.uzh.phys.ecn.oboma.functions.api.IODE;


public class ODESIR implements IODE {

    // Change Infectionrates from DiseasConstants
    public static final double INFECTION_RATE = 0.00218d;
    public static final double RECOVERY_RATE = DiseaseConstants.RECOVERY_RATE;
    private double[] dy = new double[3];

    @Override
    public double[] dydt(double[] y, double t){
        dy[0] = -INFECTION_RATE * y[1]*y[0]; //S
        dy[2] = RECOVERY_RATE * y[1]; //R
        dy[1] = - dy[0] - dy[2]; //I

        return dy;
    }

}
