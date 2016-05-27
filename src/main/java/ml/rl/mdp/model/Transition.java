package ml.rl.mdp.model;

import java.text.MessageFormat;

import util.MLUtils;

public class Transition {

	private static int count = 1;
	private Action action;
	private State s;
	private State sPrime;
	private double reward;
	private int index;

	public static Transition instance(Action action, State s, State sPrime, double reward) {
		return new Transition(action, s, sPrime, reward);
	}

	private Transition(Action action, State s, State sPrime, double reward) {
		super();
		this.action = action;
		this.s = s;
		this.sPrime = sPrime;
		this.reward = reward;
		this.index = count++;
	}

	public Action getAction() {
		return action;
	}

	public State getS() {
		return s;
	}

	public State getsPrime() {
		return sPrime;
	}

	public double getReward() {
		return reward;
	}
	
	@Override
	public String toString() {
		//return MessageFormat.format("{3}:{0} --({1})--> {2}", s, reward,sPrime,index);
		return MLUtils.asString(this, "s", "sPrime", "reward");
	}

}
