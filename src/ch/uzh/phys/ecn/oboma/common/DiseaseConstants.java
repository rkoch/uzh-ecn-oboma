package ch.uzh.phys.ecn.oboma.common;


public class DiseaseConstants {

    public static final double TRAIN_INFECTION_RATE              = 0.15d;
    public static final double STATION_INFECTION_RATE            = 0.10d;

    public static final double RECOVERY_RATE                     = 0.05d;


    /**
     * AgentFactory Constants
     */
    public final static double INFECTION_PROBABILITY             = 0.25;
    public final static double IMMUNE_PROBABILITY                = 0.10;
    public final static int    POPULATION_FACTOR                 = 10;
    public final static int    MAX_POPULATION                    = 100;

    /**
     * AgentBuilder Constants
     */
    public final static int    DEFAULT_AMOUNT_OF_TIME_TO_STAY    = 3;
    public final static int    DEFAULT_AMOUNT_OF_TRAVELNODES     = 5;
    public final static double TRAVELNODES_DEVIATION_PROBABILITY = 0.25;

}
