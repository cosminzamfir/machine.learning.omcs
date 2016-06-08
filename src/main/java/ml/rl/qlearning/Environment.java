package ml.rl.qlearning;

import java.util.List;

import ml.rl.mdp.model.Action;
import ml.rl.mdp.model.State;
import ml.rl.mdp.model.Transition;


/**
 * An environment where an agent can learn using algorithms like {@link QLearning}
 * <p>
 * In any state, the full set of {@link Action} available is known, but the ooutcome of the action (all Transition with their probabilities) 
 * are not know in advance. They have to be experimented and learned by the agent. 
 * @author eh2zamf
 *
 */
public interface Environment {

	List<Action> availableActions(State state);
	boolean isTerminal(State state);
	Transition executeAction(State state, Action action);
	State initialState();
}
