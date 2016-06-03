package ml.rl.qlearning;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import ml.rl.mdp.model.State;
import ml.rl.mdp.model.StateAction;
import ml.rl.mdp.model.Transition;

public class QLearning {

	private Environment environment;
	private LearningPolicy learningPolicy;
	private double gamma;
	private double alfa;
	/** Mapping from State -> stateAction-qValue mapping */
	private Map<State, Map<StateAction, Double>> qValues = new HashMap<>();

	
	/**
	 * Keep running episodes until convergence of Q-values
	 */
	public void run(State initialState) {
		while(true) {
			runLearningEpisode(initialState);
		}
	}
	
	/**
	 * Act in the {@link Environment} until reaching a terminal state, updating the Q-values on the way
	 * @param initialState
	 */
	private void runLearningEpisode(State initialState) {
		State state = initialState;
		do {
			StateAction stateAction = selectAction(state);
			Transition transition = performAction(stateAction);
			updateQValue(stateAction, transition);
			state = transition.getsPrime();
		} while (!environment.isTerminal(state));
	}
	
	
	/** 
	 *  Qt(s,a) = Qt-1(s,a) + alfa * [(R(s,a,s') + gamma * max(a')Qt-1(s',a')) - Qt-1(s,a)]
	 */
	private void updateQValue(StateAction stateAction, Transition transition) {
		double prevQ = getQValue(stateAction);
		double currentQ = transition.getReward() + gamma * getQValue(getBestAction(transition.getsPrime()));
		prevQ = prevQ + alfa * (currentQ - prevQ);
		setQValue(stateAction, currentQ);
		
	}

	private void setQValue(StateAction stateAction, double qValue) {
		qValues.get(stateAction.getState()).put(stateAction, qValue);		
	}

	private Transition performAction(StateAction stateAction) {
		return stateAction.generateTransition();
	}

	/**
	 * Select the action to follow in the given state.
	 */
	private StateAction selectAction(State state) {
		return learningPolicy.selectAction(state);
	}
	
	/**
	 * Seelect the action with maximum qValue for the given state
	 */
	public StateAction getBestAction(State state) {
		return environment.availableActions(state).stream().max(Comparator.comparing(stateAction -> getQValue(stateAction))).get(); 
	}


	public double getQValue(StateAction stateAction) {
		Double res = qValues.get(stateAction.getState()).get(stateAction);
		if(res == null) {
			res = 0.0;
		}
		return res;
	}
}
