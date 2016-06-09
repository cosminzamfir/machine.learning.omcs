package ml.rl.qlearning;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ml.rl.mdp.model.FullTransitionInfoEnvironment;
import ml.rl.mdp.model.State;
import ml.rl.mdp.model.StateAction;
import util.MLUtils;

public class BoltzmanLearningPolicy implements LearningPolicy {

	private Map<State, Map<StateAction, Double>> qValues;
	private FullTransitionInfoEnvironment environment;
	/**
	 * The k in the Boltzman distribution formula.
	 * Higher k <=> higer probability to select non greedy optimal actions 
	 */
	private double explorationRate;

	public BoltzmanLearningPolicy(Map<State, Map<StateAction, Double>> qValues, FullTransitionInfoEnvironment environment, double explorationRate) {
		super();
		this.qValues = qValues;
		this.environment = environment;
		this.explorationRate = explorationRate;
	}

	/**
	 * Select the action to follow in the given state.
	 * <p>
	 * Greedy policy with some decaying exploration probability given by Boltzan distribution
	 */
	public StateAction selectAction(State state) {
		List<StateAction> availableActions = environment.getTransitions(state);
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
	
	public double getQValue(StateAction stateAction) {
		Double res = qValues.get(stateAction.getState()).get(stateAction);
		if(res == null) {
			res = 0d;
		}
		return res;
	}

}
