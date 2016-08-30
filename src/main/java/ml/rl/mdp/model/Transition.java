package ml.rl.mdp.model;

import util.MLUtils;

/**
 * The transition from s to s' with reward triggered by a {@link StateAction}
 * @author Cosmin Zamfir
 *
 */
public class Transition {

	private static int count = 1;
	private Action action;
	private State s;
	private State sPrime;
	private double reward;

	public static Transition instance(Action action, State s, State sPrime, double reward) {
		return new Transition(action, s, sPrime, reward);
	}

	private Transition(Action action, State s, State sPrime, double reward) {
		super();
		this.action = action;
		this.s = s;
		this.sPrime = sPrime;
		this.reward = reward;
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
	
	public void setReward(double reward) {
		this.reward = reward;
	}
	
	@Override
	public String toString() {
		//return MessageFormat.format("{3}:{0} --({1})--> {2}", s, reward,sPrime,index);
		return MLUtils.asString(this, "s", "sPrime", "reward");
	}

}
