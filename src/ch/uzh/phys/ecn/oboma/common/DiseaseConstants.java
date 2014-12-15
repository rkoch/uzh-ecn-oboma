package ch.uzh.phys.ecn.oboma.common;


public interface DiseaseConstants {

    double TRAIN_INFECTION_RATE              = 0.50d;
    double STATION_INFECTION_RATE            = 0.20d;

    double STATION_RECOVERY_RATE             = 0.5d;
    double TRAIN_RECOVERY_RATE               = 0d;


    /**
     * AgentFactory Constants
     */
    double INFECTION_PROBABILITY             = 0.75;
    double IMMUNE_PROBABILITY                = 0.25;
    int    POPULATION_FACTOR                 = 10;
    int    MAX_POPULATION                    = 1000;

    /**
     * AgentBuilder Constants
     */
    int    DEFAULT_AMOUNT_OF_TIME_TO_STAY    = 3;
    int    DEFAULT_AMOUNT_OF_TRAVELNODES     = 5;
    double TRAVELNODES_DEVIATION_PROBABILITY = 0.25;

}
