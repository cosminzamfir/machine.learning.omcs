package ml.rl.mdp;

import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.MDPPolicy;
import ml.rl.mdp.model.State;
import ml.rl.mdp.model.StateAction;
import ml.rl.mdp.model.StatePolicy;
import ml.rl.mdp.model.Transition;
import util.DoubleHolder;

public class PolicyIteration {

	private MDP mdp;
	private double gamma;

	public void run() {
		mdp.resetStateValues();
		MDPPolicy policy = mdp.initialDeterministicPolicy();
	}

	private void policyEvaluation(MDPPolicy policy) {
		double epsilon = 0.001;
		double delta;
		do {
			delta = 0;
			for (State state : mdp.getStates()) {
				double v = state.getValue();
				double value = 0;
				for (StateAction stateAction : mdp.getStateActions(state)) {
					double stateActionValue = 0;
					for (Transition transition : stateAction.getTransitions().keySet()) {
						stateActionValue += stateAction.getProbability(transition)
								* (transition.getReward() + transition.getsPrime().getValue());
					}
					value += stateActionValue * policy.getProbability(state, stateAction);
					delta = Math.max(delta, Math.abs(value - state.getValue()));
				}
			}
		} while (delta < epsilon);

	}

	/**
	 * 
	 * @param policy
	 * @return <code>true</code> if any improvement done to the given policy;
	 *         otherwise <code>false</code>
	 * 
	 */
	private boolean policyImprovement(MDPPolicy policy) {
		for (State state : mdp.getStates()) {
			StatePolicy bestPolicy = getBestPolicy(state);
			if (!bestPolicy.equals(policy.getStatePolicy(state))) {
				policy.setStatePolicy(state, bestPolicy);
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param state
	 * @return the best (deterministic) policy for the given state, i.e.
	 *         the StateAction having the max value for stateAction.reward +
	 *         stateAction.sPrime.value
	 */
	private StatePolicy getBestPolicy(State state) {
		double max = Double.NEGATIVE_INFINITY;
		StateAction bestStateAction = null;
		for (StateAction stateAction : mdp.getStateActions(state)) {
			DoubleHolder val = new DoubleHolder(0);
			stateAction.getTransitions().keySet().forEach(
					(t) -> val.add(stateAction.getProbability(t) * (t.getReward() + gamma * t.getsPrime().getValue())));
			if(val.get() > max) {
				max = val.get();
				bestStateAction = stateAction;
			}
		}
		StatePolicy res = StatePolicy.instace(state);
		res.setStateAction(bestStateAction);
		return res;

	}
}
