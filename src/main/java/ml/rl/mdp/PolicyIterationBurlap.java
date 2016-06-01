package ml.rl.mdp;

import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.MDPPolicy;
import ml.rl.mdp.model.State;
import ml.rl.mdp.model.StateAction;
import ml.rl.mdp.model.StatePolicy;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 * Implementation of PolicyIteration Burlap like
 * <p>
 * Whether a policy improvement took place for the whole MDP is determined by looking at the maximum change in state value for all MDP states during the policy evaluation. 
 * If the value change > threshold => policy was improved
 * <p>
 * This implementation seems to converge faster (plus it's the one used to evaluate HW4) 
 * @author eh2zamf
 *
 */
public class PolicyIterationBurlap {

	private static final Logger log = Logger.getLogger(PolicyIterationBurlap.class);
	private static final double epsilon = 0.00001;
	private MDP mdp;
	private double gamma;
	private MDPPolicy policy;
	private int iterationCount;

	public PolicyIterationBurlap(MDP mdp, double gamma) {
		super();
		this.mdp = mdp;
		this.gamma = gamma;
	}

	public void run() {
		mdp.resetStateValues();
		iterationCount = 0;
		policy = mdp.initialDeterministicPolicy();
		double maxDelta = Double.MAX_VALUE;
		do {
			maxDelta = evaluatePolicy(policy);
			iterationCount++;
			improvePolicy(policy);
		} while (maxDelta > epsilon);
		if (log.isDebugEnabled()) {
			log.debug("Done." + mdp.printStateValues());
		}
	}

	public MDPPolicy getPolicy() {
		return policy;
	}

	public int getIterationCount() {
		return iterationCount;
	}

	/**
	 * Compute and set the MDP state values according to the given policy, e.g. valueIteration given policy
	 * @param policy
	 * @return the maximum change in value over all MDP states 
	 */
	private double evaluatePolicy(MDPPolicy policy) {
		double maxDelta = 0; //the max change in state value for all iterations
		if (log.isDebugEnabled()) {
			log.debug("Iteration " + iterationCount + ". Evaluating policy: " + policy + "Iteration: " + iterationCount);
		}
		double delta;  //the max change in state value for one iteration
		do { //value iteration given policy pi
			delta = 0;
			for (State state : mdp.getNonTerminalStates()) {
				double prevValue = state.getValue();
				double newValue = 0;
				for (StateAction stateAction : mdp.getStateActions(state)) {
					newValue += stateAction.evaluate(gamma) * policy.getProbability(state, stateAction); //a single StateAction will have probability > 0
				}
				delta = Math.max(delta, Math.abs(newValue - prevValue));
				maxDelta = Math.max(delta, maxDelta);
				state.setValue(newValue);
				log.info(state + ":" + prevValue + "->" + newValue);
			}
			log.info("");
		} while (delta > epsilon);
		if (log.isDebugEnabled()) {
			log.debug("Evaluation done." + mdp.printStateValues());
		}
		return maxDelta;

	}

	/**
	 * 
	 * For each State, choose the StateAction which maximizes the {@link StateAction#evaluate(double)} function
	 * 
	 */
	private void improvePolicy(MDPPolicy policy) {
		for (State state : mdp.getNonTerminalStates()) {
			StatePolicy bestPolicy = getBestPolicy(state);
			if (!bestPolicy.equals(policy.getStatePolicy(state))) {
				if (log.isDebugEnabled()) {
					log.debug(state + 
							". PreviousPolicy: " + policy.getStatePolicy(state) + "=" + policy.getStatePolicy(state).evaluate(gamma) + 
							". NewPolicy:" + bestPolicy + "=" + bestPolicy.evaluate(gamma));
				}
				policy.setStatePolicy(state, bestPolicy);
			}
		}
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
		if (log.isDebugEnabled()) {
			log.trace("Best policy for " + state + " is " + bestStateAction + " with value = " + max);
		}
		return res;

	}
}
