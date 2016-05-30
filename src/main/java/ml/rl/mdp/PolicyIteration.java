package ml.rl.mdp;

import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.MDPPolicy;
import ml.rl.mdp.model.State;
import ml.rl.mdp.model.StateAction;
import ml.rl.mdp.model.StatePolicy;

import org.apache.log4j.Logger;

public class PolicyIteration {

	private static final Logger log = Logger.getLogger(PolicyIteration.class);
	private MDP mdp;
	private double gamma;
	private MDPPolicy policy;
	private int iterationCount;
	
	public PolicyIteration(MDP mdp, double gamma) {
		super();
		this.mdp = mdp;
		this.gamma = gamma;
	}

	public void run() {
		mdp.resetStateValues();
		iterationCount=0;
		policy = mdp.initialDeterministicPolicy();
		do {
			evaluatePolicy(policy);
			iterationCount ++;
		} while (improvePolicy(policy));
	}
	
	public MDPPolicy getPolicy() {
		return policy;
	}
	
	public int getIterationCount() {
		return iterationCount;
	}
	

	/**
	 * Compute and set the MDP state values according to the given policy
	 * @param policy
	 */
	private void evaluatePolicy(MDPPolicy policy) {
		log.debug("Evaluating policy: " + policy + "Iteration: " + iterationCount);
		double epsilon = 0.001;
		double delta;
		do { //value iteration given policy pi
			delta = 0;
			for (State state : mdp.getNonTerminalStates()) {
				double value = 0;
				for (StateAction stateAction : mdp.getStateActions(state)) {
					value += stateAction.evaluate(gamma)* policy.getProbability(state, stateAction); //a single StateAction will have probability > 0
				}
				delta = Math.max(delta, Math.abs(value - state.getValue()));
				state.setValue(value);
			}
			log.debug("Delta: " + delta);
			
		} while (delta > epsilon);

	}

	/**
	 * 
	 * @param policy
	 * @return <code>true</code> if any improvement done to the given policy;
	 *         otherwise <code>false</code>
	 * 
	 */
	private boolean improvePolicy(MDPPolicy policy) {
		for (State state : mdp.getNonTerminalStates()) {
			StatePolicy bestPolicy = getBestPolicy(state);
			if (!bestPolicy.equals(policy.getStatePolicy(state))) {
				log.info("Policy improved for state " + state + ". previousPolicy: " + policy.getStatePolicy(state) + ". newPolicy:" + bestPolicy);
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
			double val = stateAction.evaluate(gamma);
			if (val > max) {
				max = val;
				bestStateAction = stateAction;
			}
		}
		StatePolicy res = StatePolicy.instace(state);
		res.setStateAction(bestStateAction);
		return res;

	}
}
