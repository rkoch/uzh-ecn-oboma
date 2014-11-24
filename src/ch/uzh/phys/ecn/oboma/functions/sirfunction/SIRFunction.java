package ch.uzh.phys.ecn.oboma.functions.sirfunction;

import ch.uzh.phys.ecn.oboma.agents.api.Agent;
import ch.uzh.phys.ecn.oboma.common.InfectionState;
import ch.uzh.phys.ecn.oboma.functions.api.ITransformationFunction;
import ch.uzh.phys.ecn.oboma.map.api.INode;


public class SIRFunction implements ITransformationFunction{

    @Override
    public InfectionState apply(Agent pAgent, INode pNode) {

        return null;
    }

    private void calculateSIR(double pSusceptible, double pInfected, double pRecovered){
        //See mainForEuler class from ECN1
    }

}
