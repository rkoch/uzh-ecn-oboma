package ch.uzh.phys.ecn.oboma.functions.api;


public class RK2 {

    ODEEuler euler = new ODEEuler();

    double[] yn1;
    double[] temp1;
    double[] temp2;
    double[] stepn1 = new double[3];

    public double[] step(double[] yn, double tn, double dt, IDGL func){
        yn1 = euler.step(yn, tn, dt, func);

        temp1 = func.dydt(yn, dt);
        temp2 = func.dydt(yn1, dt);

        for(int i = 0; i < yn.length; i++){
            stepn1[i] = yn[i] + dt*((temp1[i] + temp2[i])/2);
        }

        return stepn1;
    }

}
