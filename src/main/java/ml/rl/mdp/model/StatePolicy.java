package ml.rl.mdp.model;


import java.util.LinkedHashMap;
import java.util.Map;

import util.DoubleHolder;
import util.IntHolder;
import util.MLUtils;

/**
 * The policy for a given State, i.e. the {@link StateAction} -> probability mapping for a given State
 * @author eh2zamf
 *
 */
public class StatePolicy {

	private State state;
	private Map<StateAction, Double> stateActionsProbabilities = new LinkedHashMap<StateAction, Double>();
	
	public static StatePolicy instace(State state) {
		StatePolicy res = new StatePolicy();
		res.state = state;
		return res;
	}
	
	public static StatePolicy instance(State state, StateAction stateAction) {
		StatePolicy res = instace(state);
		res.setStateAction(stateAction);
		return res; 
	}

	
	public void setActionProbability(StateAction stateAction, double p) {
		stateActionsProbabilities.put(stateAction, p);
	}
	
	/**Shortcut for {@link #setActionProbability(StateAction, 1)}*/
	public void setStateAction(StateAction stateAction) {
		stateActionsProbabilities.put(stateAction, 1.0);
	}
	
	public Map<StateAction, Double> getPolicy() {
		return stateActionsProbabilities;
	}
	
	public State getState() {
		return state;
	}
	
	public StateAction generate() {
		double p = Math.random();
		double d = 0;
		for (StateAction stateAction : stateActionsProbabilities.keySet()) {
			d += stateActionsProbabilities.get(stateAction);
			if(p <= d) {
				return stateAction;
			}
		}
		throw new RuntimeException("Assertion error in generating ActionState from StatePolicy");
	}

	public double getProbability(StateAction stateAction) {
		return stateActionsProbabilities.get(stateAction) == null ? 0 : stateActionsProbabilities.get(stateAction);
	}
	
	/**
	 * @param state
	 * @return the single StateAction for this deterministic instance
	 * @throws RuntimeException if this is not a deterministic policy
	 */
	public StateAction getStateAction() {
		if(stateActionsProbabilities.size() > 1) {
			throw new RuntimeException("Cannot get the single StateAction for this non-deterministic instance");
		}
		return stateActionsProbabilities.keySet().iterator().next();
	}

	@Override
	public boolean equals(Object obj) {
		if(! (obj instanceof StatePolicy)) {
			return false;
		}
		StatePolicy other = (StatePolicy) obj;
		for (StateAction stateAction : stateActionsProbabilities.keySet()) {
			if(!MLUtils.equals(other.getProbability(stateAction), getProbability(stateAction), 0.001)) {
				return false;
			}
		}
		return true;
	}
	
	public double evaluate(double gamma) {
		DoubleHolder res = new DoubleHolder(0);
		stateActionsProbabilities.keySet().forEach((sa) -> res.add(getProbability(sa) * sa.evaluate(gamma)));
		return res.get();
	}
	
	@Override
	public int hashCode() {
		IntHolder res = new IntHolder(0);
		stateActionsProbabilities.keySet().forEach((sa) -> res.add(sa.hashCode() + Double.valueOf(getProbability(sa)).hashCode()));
		return res.get();
	}
	
	@Override
	public String toString() {
		if(stateActionsProbabilities.size() == 1) {
			return "[" + state + ":" + stateActionsProbabilities.keySet().iterator().next().getAction() + "]";
		}
		StringBuilder sb = new StringBuilder("StatePolicy[state="+state + "][");
		stateActionsProbabilities.keySet().forEach((sa) -> sb.append(sa.getAction().toString() + "=" + getProbability(sa) + " "));
		sb.append("]");
		return sb.toString();
	}

}
