package ml.rl.mdp.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import util.DoubleHolder;
import util.MLUtils;

/** An action performed in a state, stochastic outcome*/
public class StateAction {

	private State state;
	private Action action;
	private Map<Transition, Double> transitions = new LinkedHashMap<>();

	public static StateAction instance(State state, Action action) {
		return new StateAction(state, action);
	}

	public StateAction(State state, Action action) {
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

	/**
	 * @return the expected value of [reward + discounted value of sprime]
	 */
	public double evaluate(double gamma) {
		DoubleHolder res = new DoubleHolder(0);
		getTransitions().keySet().forEach((t) -> res.add(getProbability(t) * (t.getReward() + gamma * t.getsPrime().getValue())));
		return res.get();
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		res.append("StateAction:[State:" + state).append(";").append("Action:" + action + "]");
		transitions.keySet().forEach((t) -> res.append("\n").append(t).append(" - probability:" + MLUtils.format(transitions.get(t))));
		return res.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof StateAction)) {
			return false;
		}
		StateAction other = (StateAction) obj;
		return state.equals(other.getState()) && action.equals(other.getAction());
	}

	@Override
	public int hashCode() {
		return state.hashCode() + action.hashCode();
	}
}
