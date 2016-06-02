package ml.rl.qlearning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.MLUtils;
import ml.rl.mdp.model.State;
import ml.rl.mdp.model.StateAction;
import ml.rl.mdp.model.Transition;

public class QLearning {

	private Environment environment;
	private double learningRate;
	private double gamma;
	/**
	 * The k in the Boltzman distribution formula.
	 * Higher k <=> higer probability to select non greedy optimal actions 
	 */
	private double explorationRate;
	/**
	 * Mapping from State -> stateAction-qValue mapping
	 */
	private Map<State, Map<StateAction, Double>> qValues = new HashMap<>();
	private boolean hasRun;
	
	public void run(State initialState) {
		State state = initialState;
		do {
			StateAction stateAction = selectAction(state);
			Transition transition = performAction(stateAction);
			updateQValues(transition);
			state = transition.getsPrime();
		} while (!environment.isTerminal(state));
		hasRun = true;
	}
	
	private Transition performAction(StateAction stateAction) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Select the action to follow in the given state.
	 * <p>
	 * Greedy policy with some decaying exploration probability given by Boltzan distribution
	 */
	private StateAction selectAction(State state) {
		List<StateAction> availableActions = environment.availableActions(state); 
		List<Double> probabilities = computeActionProbabilities(state, availableActions);
		return availableActions.get(MLUtils.randomSelectionFromDistribution(probabilities));
	}

	/**
	 * Boltzaman distribution: P(a|s) = e^[(Q(s,a)/k] / sum(j) e^[Q(s,aj)/k]
	 * @param state
	 * @param availableActions
	 * @return
	 */
	private List<Double> computeActionProbabilities(State state, List<StateAction> availableActions) {
		double sum = 0;
		List<Double> res = new ArrayList<Double>();
		for (StateAction stateAction : availableActions) {
			double p = Math.pow(Math.E, getQValue(stateAction)/explorationRate);
			sum += p;
			res.add(p);
			
		}
		for (int i = 0; i < res.size(); i++) {
			res.set(i, res.get(i)/sum);
		}
		return res;
	}

	public StateAction getBestAction(State state) {
		Map<StateAction, Double> stateActions = qValues.get(state);
		return stateActions.keySet().stream().max(Comparator.comparing(sa -> stateActions.get(sa))).get();
	}
	
	public double getQValue(StateAction stateAction) {
		Double res = qValues.get(state).get(stateAction);
		if(res == null) {
			res = 0;
		}
		return res
	}
}
