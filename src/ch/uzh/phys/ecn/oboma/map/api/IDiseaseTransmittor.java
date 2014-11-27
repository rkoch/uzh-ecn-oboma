package ch.uzh.phys.ecn.oboma.map.api;


public interface IDiseaseTransmittor
        extends ITimeDependant {

    /**
     * Infect:
     * 1. Apply the infection/transformation function to each agent currently occupying the node
     */
    void infect();

}
