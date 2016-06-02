package ml.rl.mdp;

import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.MDPPolicy;
import ml.rl.mdp.model.State;
import ml.rl.mdp.model.StateAction;

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
public class PolicyIteration {

	private static final Logger log = Logger.getLogger(PolicyIteration.class);
	private static final double epsilon = 0.00001;
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
		iterationCount = 0;
		policy = MDPPolicy.greedyPolicy(mdp, gamma);
		double maxDelta = Double.MAX_VALUE;
		do {
			log.info("Policy:                " + policy);
			maxDelta = evaluatePolicy(policy);
			iterationCount++;
			policy  = MDPPolicy.greedyPolicy(mdp, gamma);  //rebuild the policy
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
		double delta; //the max change in state value for one iteration
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
				if (log.isDebugEnabled()) {
					log.info(state + ":" + prevValue + "->" + newValue);
				}
			}
		} while (delta > epsilon);
		if (log.isDebugEnabled()) {
			log.debug("Evaluation done." + mdp.printStateValues());
		}
		log.info("Policy evaluation:     " + mdp.printStateValues());
		return maxDelta;

	}
}
