package ch.uzh.phys.ecn.oboma.functions.api;

import ch.uzh.phys.ecn.oboma.agents.api.Agent;
import ch.uzh.phys.ecn.oboma.map.api.INode;


public interface ITransformationFunction {

    void apply(Agent pAgent, INode pNode);

}
