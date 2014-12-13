package ch.uzh.phys.ecn.oboma.agents.api;

import ch.uzh.phys.ecn.oboma.common.InfectionState;

public interface IAgent {

	public String getId();

	public int getPreferredTimeOfStay(String pNodeId);

	public String getNextWaypoint(String pCurrentNodeId);

	public InfectionState getState();

	public void setState(InfectionState pState);
}
