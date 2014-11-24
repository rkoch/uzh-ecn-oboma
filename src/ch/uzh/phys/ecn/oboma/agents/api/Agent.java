package ch.uzh.phys.ecn.oboma.agents.api;

import ch.uzh.phys.ecn.oboma.common.InfectionState;
import ch.uzh.phys.ecn.oboma.map.api.INode;

public class Agent {

	private InfectionState mInfectionState;

	private INode mSourceNode;

	private INode mDestinationNode;

	public Agent(INode pSourceNode, INode pDestinationNode) {
		mInfectionState = InfectionState.SUSCEPTIBLE;
		mSourceNode = pSourceNode;
		mDestinationNode = pDestinationNode;
	}

	public InfectionState getmInfectionState() {
		return mInfectionState;
	}

	public void setmInfectionState(InfectionState mInfectionState) {
		this.mInfectionState = mInfectionState;
	}

	public INode getSourceNode() {
		return mSourceNode;
	}

	public void setSourceNode(INode sourceNode) {
		this.mSourceNode = sourceNode;
	}

	public INode getDestinationNode() {
		return mDestinationNode;
	}

	public void setDestinationNode(INode destinationNode) {
		this.mDestinationNode = destinationNode;
	}

}
