package ch.uzh.phys.ecn.oboma.functions.api;


public class SIR implements IDGL {

    public static final double INFECTION_RATE = 0.00218d;
    public static final double RECOVERY_RATE = 0.5;
    private double[] dy = new double[3];

    public double[] dydt(double[] y, double t){
        dy[0] = -INFECTION_RATE * y[1]*y[0]; //S
        dy[2] = RECOVERY_RATE * y[1]; //R
        dy[1] = - dy[0] - dy[2]; //I

        return dy;
    }

}
