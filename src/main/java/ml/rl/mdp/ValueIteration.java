package ml.rl.mdp;

import java.util.List;
import java.util.Observable;

import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.MDPPolicy;
import ml.rl.mdp.model.State;
import ml.rl.mdp.model.StateAction;
import ml.rl.mdp.model.StatePolicy;

import org.apache.log4j.Logger;

/**
 * 
 * @author Cosmin Zamfir
 *
 */
public class ValueIteration extends Observable {

	private static final Logger log = Logger.getLogger(ValueIteration.class);

	private MDP mdp;
	private double gamma = 1;
	private int maxIterations = 10000;
	private double epsilon = 0.00001;
	private boolean hasRun = false;
	
	public ValueIteration(MDP mdp) {
		super();
		this.mdp = mdp;
	}

	public double getGamma() {
		return gamma;
	}

	public void setGamma(double gamma) {
		this.gamma = gamma;
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	/**
	 * Run the value iteration algorithm and set the values to the MDP states
	 */
	public void run() {
		mdp.resetStateValues();
		double delta;
		int iteration = 1;
		do {
			delta = 0;
			for (State s : mdp.getNonTerminalStates()) {
				double v = s.getValue();
				s.setValue(getMaxValue(s));
				if (log.isDebugEnabled()) {
					log.debug("Iteration " + iteration + ". Setting value for " + s + " to " + s.getValue());
				}
				delta = Math.max(delta, Math.abs(v - s.getValue()));
				iteration++;
				setChanged();
				notifyObservers(s);
			}
		} while (delta > epsilon);
		log.info("Value iteration done in " + iteration + " iterations");
		hasRun = true;
	}

	/**
	 * Output the deterministic policy Pi, such that Pi(s) = argmax(a) sum(s') P(s,a,s') * (R(s,a,s') + gamma*V(s'))   
	 * @return
	 */
	public MDPPolicy getPolicy() {
		if (!hasRun) {
			run();
		}
		MDPPolicy res = MDPPolicy.instance(mdp);
		mdp.getStates().forEach((state) -> res.setStatePolicy(state, computeArgMaxStatePolicy(state)));
		return res;
	}

	/**
	 * Output the deterministic state policy Pi, such that Pi(s) = argmax(a) sum(s') P(s,a,s') * (R(s,a,s') + gamma*V(s'))   
	 */
	private StatePolicy computeArgMaxStatePolicy(State state) {
		double MAX_VALUE = Double.MIN_VALUE;
		StateAction bestStateAction = null;
		for (StateAction stateAction : mdp.getStateActions(state)) {
			double value = stateAction.evaluate(gamma);
			if (value > MAX_VALUE) {
				MAX_VALUE = value;
				bestStateAction = stateAction;
			}
		}
		return StatePolicy.instance(state, bestStateAction);
	}

	/**
	 * @param state
	 * @return the value of the given State given by following the StateAction with the maximum outcome
	 */
	private double getMaxValue(State state) {
		double res = Double.NEGATIVE_INFINITY;
		List<StateAction> stateActions = mdp.getStateActions(state);
		if (stateActions == null || stateActions.isEmpty()) {
			return state.getValue();
		}
		for (StateAction stateAction : stateActions) {
			double val = stateAction.evaluate(gamma);
			if (val > res) {
				res = val;
			}
		}
		return res;
	}

}
