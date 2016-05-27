package ml.rl.mdp;

import java.util.List;
import java.util.Observable;

import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.State;
import ml.rl.mdp.model.StateAction;

import org.apache.log4j.Logger;

import util.DoubleHolder;

public class ValueIteration extends Observable {

	private static final Logger log = Logger.getLogger(ValueIteration.class);

	private MDP mdp;
	private double gamma = 1;
	private int maxIterations = 1000;
	private double epsilon = 0.001;

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

	public void run() {
		mdp.resetStateValues();
		double delta;
		int iteration = 1;
		do {
			delta = 0;
			for (State s : mdp.getStates()) {
				double v = s.getValue();
				s.setValue(getValue(s));
				log.info("Iteration " + iteration + ". Setting value for " + s + " to " + s.getValue());
				delta = Math.max(delta, Math.abs(v - s.getValue()));
				iteration++;
				setChanged();
				notifyObservers(s);
			}
		} while (delta > epsilon);
	}

	private double getValue(State state) {
		double res = Double.NEGATIVE_INFINITY;
		List<StateAction> stateActions = mdp.getStateActions(state);
		if (stateActions == null || stateActions.isEmpty()) {
			return state.getValue();
		}
		for (StateAction stateAction : stateActions) {
			double val = getValue(stateAction);
			if (val > res) {
				res = val;
			}
		}
		return res;
	}

	private double getValue(StateAction stateAction) {
		DoubleHolder res = new DoubleHolder(0);
		stateAction.getTransitions().keySet().forEach((t) -> res.add(stateAction.getProbability(t) * (t.getReward() + gamma * t.getsPrime().getValue())));
		return res.get();
	}

}
