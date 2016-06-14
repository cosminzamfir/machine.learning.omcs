package ml.rl.qlearning;

import ml.rl.mdp.model.State;
import ml.rl.mdp.model.StateAction;

/**
 * Defines strategy to select the action to take in a given state during a learning episode
 * @author Cosmin Zamfir
 */
public interface LearningPolicy {
	
	StateAction selectAction(State state);
}
