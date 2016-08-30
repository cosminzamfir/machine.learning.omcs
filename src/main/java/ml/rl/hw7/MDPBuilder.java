package ml.rl.hw7;

import ml.rl.mdp.model.Action;
import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.State;
import ml.rl.mdp.model.StateAction;

public class MDPBuilder {

	private int numStates;
	private int numActions;
	private double[][][] transitions;
	private double[][][] rewards;
	
	public MDPBuilder(double[][][] transitions, double[][][] rewards) {
		super();
		this.transitions = transitions;
		this.rewards = rewards;
	}


	public MDP build() {
		MDP mdp = MDP.instance();
		for (int stateIndex = 0; stateIndex < transitions.length; stateIndex++) {
			double[][] stateTransitions = transitions[stateIndex];
			for (int actionIndex = 0; actionIndex < stateTransitions.length; actionIndex++) {
				double[] stateActionTransitions = stateTransitions[actionIndex];
				StateAction stateAction = StateAction.instance(State.instance(stateIndex), Action.instance(String.valueOf(actionIndex)));
				for (int sprimeIndex = 0; sprimeIndex < stateActionTransitions.length; sprimeIndex++) {
					double probability = stateActionTransitions[sprimeIndex];
					double reward = rewards[stateIndex][actionIndex][sprimeIndex];
					if(probability > 0.00001) {
						stateAction.addTransition(State.instance(sprimeIndex), reward, probability);
					}
				}
				mdp.addStateAction(stateAction);
			}
		}
		return mdp;
	}
	
}
