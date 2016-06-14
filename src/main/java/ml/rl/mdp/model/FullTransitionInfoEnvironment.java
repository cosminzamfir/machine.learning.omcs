package ml.rl.mdp.model;

import java.util.List;

import ml.rl.model.Environment;
import ml.rl.qlearning.LearningEnvironment;

/**
 * An environment which provides full information about the transitions triggered by each of the available Actions for any State.
 * <p>
 * Such Environment can be used to generate an MDP, as oposed {@link LearningEnvironment} where the transitions can be discovered by executing the Actions 
 * @author Cosmin Zamfir
 *
 */
public interface FullTransitionInfoEnvironment extends Environment {

	/**
	 * @param state the State
	 * @return the complete transition information for the given State, ie all {@link StateAction}s available 
	 * (StateActions objects contains all the transitions with their probabilities) 
	 */
	List<StateAction> getTransitions(State state);
	
	default MDP generateMDP() {
		return new MDPBuilder(this).build();
	}
}
