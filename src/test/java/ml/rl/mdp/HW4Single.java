package ml.rl.mdp;

import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.State;

import org.junit.Test;

public class HW4Single {

	int numStates = 5;
	MDP mdp;
	boolean showView = true;
	double gamma = 0.75;

	@Test
	public void test() throws Exception {
		createMDP();
		if (!showView) {
			PolicyIteration pi = new PolicyIteration(mdp);
			pi.setGamma(gamma);
			pi.run();
			mdp.saveAsJson("mymdp.json", 0.75);
		} else {
			PolicyIterationRunner pir = new PolicyIterationRunner(mdp);
			pir.setGamma(gamma);
			pir.setSlideShowDelay(20000);
			pir.run();
			System.in.read();
		}

	}

	private void createMDP() {
		double rnn = 100;
		double rs = 1;
		double multiplier = 1 / (1 - gamma);
		State first = State.instance(0);
		State last = State.instance(numStates - 1);
		double v0s0 = multiplier * rs;
		double v0sn = multiplier * rnn;
		double vs0 = discountFrom(first, last) * multiplier * rnn;
		double rn0 = between(v0sn - gamma * vs0, v0sn - v0s0);

		mdp = MDP.instance();

		// guess what
	}

	private double between(double d1, double d2) {
		return (d1 + d2) / 2;
	}

	private double discountFrom(State s1, State s2) {
		return Math.pow(gamma, s2.getId() - s1.getId());
	}


	/** From State i to State j */
	private void addSingleOutcomeAction(int i, int j, double reward) {
		mdp.addSingleOutcomAction(State.instance(i), State.instance(j), reward, "S" + i + " => S" + j);
	}

	/**
	 * Stochastic outcome action from State i to States j1 and j2 with given
	 * rewards and probabilities
	 */
	private void addDoubleOutcomeAction(int i, int j1, int j2, double reward1, double reward2, double p1, double p2) {
		mdp.addDoubleOutcomAction(State.instance(i), State.instance(j1), State.instance(j2), reward1, reward2, p1,
				p2,
				"S" + i + " => [S" + j1 + ",S" + j2 + "]");
	}

}
