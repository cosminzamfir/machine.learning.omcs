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
 * Implementation of PolicyIteration as described at http://webdocs.cs.ualberta.ca/~sutton/book/ebook/node43.html#fig:policy-iteration
 * <p>
 * Whether a policy improvement took place for any state is determined by comparing the previous StateAction with the current StateAction for the State 
 * <p>
 * This implementation seems to converge slower in special cases (it may go back-and-forth between 2 StateActions for a given State when values are very close, 
 * ie 0.00...1)
 * @author Cosmin Zamfir
 *
 */
public class PolicyIterationSutton {

	private static final Logger log = Logger.getLogger(PolicyIterationSutton.class);
	private static final double epsilon = 1E-7;
	private MDP mdp;
	private double gamma;
	private MDPPolicy policy;
	private int iterationCount;

	public PolicyIterationSutton(MDP mdp, double gamma) {
		super();
		this.mdp = mdp;
		this.gamma = gamma;
	}

	public void run() {
		LogManager.getRootLogger().setLevel(Level.INFO);
		mdp.resetStateValues();
		iterationCount = 0;
		policy = mdp.initialDeterministicPolicy();
		do {
			evaluatePolicy(policy);
			if(iterationCount > 20) {
				LogManager.getRootLogger().setLevel(Level.DEBUG);
			}
			iterationCount++;
		} while (improvePolicy(policy));
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
	 * Compute and set the MDP state values according to the given policy
	 * @param policy
	 */
	private void evaluatePolicy(MDPPolicy policy) {
		if (log.isDebugEnabled()) {
			log.debug("Iteration " + iterationCount + ". Evaluating policy: " + policy + "Iteration: " + iterationCount);
		}
		double delta;
		do { //value iteration given policy pi
			delta = 0;
			for (State state : mdp.getNonTerminalStates()) {
				double value = 0;
				for (StateAction stateAction : mdp.getStateActions(state)) {
					value += stateAction.evaluate(gamma) * policy.getProbability(state, stateAction); //a single StateAction will have probability > 0
				}
				delta = Math.max(delta, Math.abs(value - state.getValue()));
				state.setValue(value);
			}
		} while (delta > epsilon);
		if (log.isDebugEnabled()) {
			log.debug("Evaluation done." + mdp.printStateValues());
		}

	}

	/**
	 * 
	 * @param policy
	 * @return <code>true</code> if any improvement done to the given policy;
	 *         otherwise <code>false</code>
	 * 
	 */
	private boolean improvePolicy(MDPPolicy policy) {
		boolean policyImproved = false;
		for (State state : mdp.getNonTerminalStates()) {
			StatePolicy bestPolicy = getBestPolicy(state);
			if (!bestPolicy.equals(policy.getStatePolicy(state))) {
				if (log.isDebugEnabled()) {
					log.debug(state + 
							". PreviousPolicy: " + policy.getStatePolicy(state) + "=" + policy.getStatePolicy(state).evaluate(gamma) + 
							". NewPolicy:" + bestPolicy + "=" + bestPolicy.evaluate(gamma));
				}
				policy.setStatePolicy(state, bestPolicy);
				policyImproved = true;
			}
		}
		return policyImproved;
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
