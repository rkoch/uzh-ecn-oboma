
public class ODEEuler {

    double[] step(double[] yn, double tn, double dt, DGL func){
        double[] yn1 = new double[yn.length];
        double[] dy = func.dydt(yn, tn);

        for(int i = 0; i < yn.length; i++){
            yn1[i] = yn[i] + dt*dy[i];
        }

        return yn1;
    }

}
