package ml.rl.qlearning;

import java.util.List;

import ml.rl.mdp.model.Action;
import ml.rl.mdp.model.State;
import ml.rl.mdp.model.Transition;
import ml.rl.model.Environment;

/**
 * An environment where an agent can learn using algorithms like {@link QLearning}
 * <p>
 * In any state, the full set of {@link Action} available is known, but the ooutcome of the actions (all Transition with their probabilities) 
 * is not know in advance. They have to be experimented and learned by the agent. 
 * @author eh2zamf
 *
 */
public interface LearningEnvironment extends Environment {

	/** All Actions available in the given State*/
	List<Action> availableActions(State state);
	
	/** Execute the given Action and returns the corresponding Transition. The Environment itself can change as result of executing the Action */
	Transition executeAction(State state, Action action);


}
