package ch.uzh.phys.ecn.oboma.functions.infectfunction;

import ch.uzh.phys.ecn.oboma.agents.model.Agent;
import ch.uzh.phys.ecn.oboma.common.InfectionState;
import ch.uzh.phys.ecn.oboma.functions.api.ITransformationFunction;
import ch.uzh.phys.ecn.oboma.map.api.INode;


public class InfectionFunction
        implements ITransformationFunction {

    @Override
    public InfectionState apply(Agent pAgent, INode pNode) {
        return InfectionState.INFECTED;
    }

}
