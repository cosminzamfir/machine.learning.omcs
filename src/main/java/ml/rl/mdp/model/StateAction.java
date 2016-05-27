package ml.rl.mdp.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** An action performed in a state, stochastic outcome*/
public class StateAction {

	private State state;
	private Action action;
	private Map<Transition, Double> transitions = new LinkedHashMap<>();

	public static StateAction instance(State state, Action action) {
		return new StateAction(state, action);
	}

	private StateAction(State state, Action action) {
		this.state = state;
		this.action = action;
	}
	
	public Map<Transition, Double> getTransitions() {
		return transitions;
	}

	public double getProbability(Transition transition) {
		return transitions.get(transition);
	}

	public void addTransition(Transition transition, double p) {
		transitions.put(transition, p);
	}
	
	public void addTransition(State sprime, double reward, double probability) {
		Transition t = Transition.instance(action, state, sprime, reward);
		addTransition(t, probability);
	}

	public State getState() {
		return state;
	}

	public Action getAction() {
		return action;
	}

	public List<State> getAllSprimes() {
		final List<State> res = new ArrayList<State>();
		transitions.keySet().forEach((t) -> res.add(t.getsPrime()));
		return res;
	}

	public Transition generateTransition() {
		double p = Math.random();
		double d = 0;
		for (Transition transition : transitions.keySet()) {
			d += transitions.get(transition);
			if (p <= d) {
				return transition;
			}
		}
		throw new RuntimeException("Assertion error in generating Transition from ActionState");
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		res.append("StateAction:[State:" + state).append(";").append("Action:" + action + "]");
		transitions.keySet().forEach((t) -> res.append("\n").append(t).append(" - probability:" + transitions.get(t)));
		return res.toString();
	}
}
