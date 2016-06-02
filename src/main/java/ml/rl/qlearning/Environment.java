package ml.rl.qlearning;

import java.util.List;

import ml.rl.mdp.model.State;
import ml.rl.mdp.model.StateAction;

public interface Environment {

	List<StateAction> availableActions(State state);
	boolean isTerminal(State state);
	
}
