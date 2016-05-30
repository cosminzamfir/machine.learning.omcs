package ml.rl.mdp.model;

import java.util.LinkedHashMap;
import java.util.Map;

import util.MLUtils;

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
		if (statePolicy == null) {
			statePolicy = StatePolicy.instace(state);
			statePolicies.put(state, statePolicy);
		}
		statePolicy.setActionProbability(stateAction, p);
	}

	public double getProbability(State state, StateAction stateAction) {
		return statePolicies.get(state).getProbability(stateAction);
	}

	/** Generate initial policy - equal probabilities for all decisions */
	public static MDPPolicy initialPolicy(MDP mdp) {
		MDPPolicy res = instance(mdp);
		for (State state : mdp.getStates()) {
			StatePolicy statePolicy = StatePolicy.instace(state);
			int count = mdp.getStateActions(state).size();
			mdp.getStateActions(state).forEach((stateAction) -> statePolicy.setActionProbability(stateAction, 1.0 / count));
			res.statePolicies.put(state, statePolicy);
		}
		return res;
	}

	/**
	 * Generate an initial Policy - choose randomly for each state a single StateAction with probability 1
	 * @return
	 */
	public static MDPPolicy initialDeterministicPolicy(MDP mdp) {
		MDPPolicy res = instance(mdp);
		for (State state : mdp.getStates()) {
			if (!mdp.isTerminal(state)) {
				StatePolicy statePolicy = StatePolicy.instace(state);
				StateAction stateAction = MLUtils.randomElement(mdp.getStateActions(state));
				statePolicy.setStateAction(stateAction);
				res.statePolicies.put(state, statePolicy);
			}
		}
		return res;
	}

	public StatePolicy getStatePolicy(State state) {
		return statePolicies.get(state);
	}

	public void setStatePolicy(State state, StatePolicy statePolicy) {
		statePolicies.put(state, statePolicy);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("MDPPolicy");
		statePolicies.values().forEach((sp) -> sb.append("\n   -").append(sp.toString()));
		return sb.toString();
	}
}
