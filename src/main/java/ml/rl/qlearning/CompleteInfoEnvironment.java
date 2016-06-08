package ml.rl.qlearning;

import java.util.List;

import ml.rl.mdp.model.State;
import ml.rl.mdp.model.StateAction;

/**
 * An environment which provides full information about all the actions in each state, together with the state transitions
 * <p>
 * Can be used to generate an MDP 
 * @author eh2zamf
 *
 */
public interface CompleteInfoEnvironment extends Environment {

	/**
	 * @param state the State
	 * @return the complete transition information for the given State, ie all {@link StateAction}s available 
	 * (StateActions objects contains all the transitions with their probabilities_ 
	 */
	List<StateAction> getStateActions(State state);
}
