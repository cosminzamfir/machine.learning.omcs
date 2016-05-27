package ml.rl.mdp.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Maintains a probabilistic mapping of State -> StateAction, ie for each State, give the probabilities to following each StateAction available for this state
 * @author eh2zamf
 *
 */
public class MDPPolicy {

	private MDP mdp;
	private Map<State, StatePolicy> statePolicies = new LinkedHashMap<State, StatePolicy>(); 
	
	public static MDPPolicy instance(MDP mdp) {
		MDPPolicy res = new MDPPolicy();
		res.mdp = mdp;
		return res;
	}
	
	public MDP getMdp() {
		return mdp;
	}
	
	public StateAction generate(State state) {
		return statePolicies.get(state).generate();
	}
	
	public void setProbability(State state, StateAction stateAction, double p) {
		StatePolicy statePolicy = statePolicies.get(state);
		if(statePolicy==null) {
			statePolicy = StatePolicy.instace(state);
			statePolicies.put(state, statePolicy);
		}
		statePolicy.setActionProbability(stateAction, p);
	}

	public static MDPPolicy initialPolicy(MDP mdp) {
		MDPPolicy res = instance(mdp);
		for (State state : mdp.getStates()) {
			StatePolicy statePolicy = StatePolicy.instace(state);
			int count = mdp.getStateActions(state).size();
			mdp.getStateActions(state).forEach((stateAction) -> statePolicy.setActionProbability(stateAction, 1.0/count));
			res.statePolicies.put(state, statePolicy);
		}
		return res;
	}
}
