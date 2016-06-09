package ml.rl.model;

import ml.rl.mdp.model.State;


/**
 * The basic Environment interface
 * @author eh2zamf
 *
 */
public interface Environment {

	/** Whether the given State is terminal, i.e. if any action/transition is possible from that State */
	boolean isTerminal(State state);
	
	/** Returns the initial State, i.e. the starting point form where the agent can start acting in the Environment */
	State initialState();
}
