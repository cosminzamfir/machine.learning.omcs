package ml.rl.mdp.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class StatePolicy {

	private State state;
	private Map<StateAction, Double> policy = new LinkedHashMap<StateAction, Double>();
	
	public static StatePolicy instace(State state) {
		StatePolicy res = new StatePolicy();
		res.state = state;
		return res;
	}
	
	public void setActionProbability(StateAction stateAction, double p) {
		policy.put(stateAction, p);
	}
	
	public Map<StateAction, Double> getPolicy() {
		return policy;
	}
	
	public State getState() {
		return state;
	}
	
	public StateAction generate() {
		double p = Math.random();
		double d = 0;
		for (StateAction stateAction : policy.keySet()) {
			d += policy.get(stateAction);
			if(p <= d) {
				return stateAction;
			}
		}
		throw new RuntimeException("Assertion error in generating ActionState from StatePolicy");
	}
}
