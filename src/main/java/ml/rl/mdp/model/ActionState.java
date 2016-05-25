package ml.rl.mdp.model;

import java.util.LinkedHashMap;
import java.util.Map;

/** An action performed in a state, stochastic outcome*/
public class ActionState {

	private State s;
	private Action a;
	private Map<Transition, Double> transitionProbabilites = new LinkedHashMap<>();
	
}
