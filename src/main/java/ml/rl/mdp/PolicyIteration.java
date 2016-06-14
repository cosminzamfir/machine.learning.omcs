package ml.rl.mdp;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import linalg.Matrix;
import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.MDPPolicy;
import ml.rl.mdp.model.State;
import ml.rl.mdp.model.StateAction;

import org.apache.log4j.Logger;

/**
 * 
 * Whether a policy improvement took place for the whole MDP is determined by looking at the maximum change in state value for all MDP states during the policy evaluation. 
 * If the value change > threshold => policy was improved
 * <p>
 * This implementation seems to converge faster than {@link PolicyIterationSutton} (plus it's the one used to evaluate HW4) 
 * @author Cosmin Zamfir
 *
 */
public class PolicyIteration extends Observable {

	private static final Logger log = Logger.getLogger(PolicyIteration.class);
	private static final double epsilon = 0.01;
	private MDP mdp;
	private double gamma = 0.75;
	private MDPPolicy policy;
	private int iterationCount;
	/** Need to backup the previous iteration state values in order to compute the value improvement */
	private Map<State, Double> prevStateValues = new HashMap<State, Double>();

	public PolicyIteration(MDP mdp) {
		super();
		this.mdp = mdp;
	}
	
	public void setGamma(double gamma) {
		this.gamma = gamma;
	}

	public void run() {
		iterationCount = 0;
		mdp.resetStateValues();
		policy = MDPPolicy.greedyPolicy(mdp, gamma);
		double maxDelta = Double.MAX_VALUE;
		do {
			setChanged();
			notifyObservers(policy); //policy has changed
			
			log.info("Policy:                " + policy);
			maxDelta = evaluatePolicy(policy);
			iterationCount++;
			
			setChanged();
			notifyObservers(mdp); //mdp values have changed
			
			policy  = MDPPolicy.greedyPolicy(mdp, gamma);  //rebuild the policy
		
		} while (maxDelta > epsilon);
		log.info("Done. Iterations: " + iterationCount);
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
		saveCurrentStateValues();
		if (log.isDebugEnabled()) {
			log.debug("Iteration " + iterationCount + ". Evaluating policy: " + policy + "Iteration: " + iterationCount);
		}
		double delta; 
		mdp.resetStateValues();
		Matrix matrix = Matrix.emtpyMatrix();
		do { //value iteration given policy pi
			delta = 0;
			for (State state : mdp.getNonTerminalStates()) {
				double prevValue = state.getValue();
				double newValue = 0;
				for (StateAction stateAction : mdp.getStateActions(state)) {
					newValue += stateAction.evaluate(gamma) * policy.getProbability(state, stateAction); //a single StateAction will have probability = 1
				}
				delta = Math.max(delta, Math.abs(newValue - prevValue));
				state.setValue(newValue);
				if (log.isDebugEnabled()) {
					log.debug(state + ":" + prevValue + "->" + newValue);
				}
			}
			matrix.addRow(mdp.getStateValues());
		} while (delta > epsilon);
		//System.out.println(matrix);
		log.info("Policy evaluation:     " + mdp.printStateValues());
		return getMaxDelta();
	}

	/**
	 * The max difference between current MDP state values and previous iteration MDP state values
	 */
	private double getMaxDelta() {
		return mdp.getStates().stream().map((state) -> Math.abs(state.getValue() - prevStateValues.get(state))).mapToDouble(val->val).max().getAsDouble();
	}

	private void saveCurrentStateValues() {
		mdp.getStates().forEach((state) -> prevStateValues.put(state, state.getValue()) );
	}
}
