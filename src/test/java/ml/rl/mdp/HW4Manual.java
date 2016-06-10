package ml.rl.mdp;

import java.util.Arrays;

import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.State;

import org.junit.Test;

public class HW4Manual {

	int n = 4;
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
			PolicyIterationRunner pi = new PolicyIterationRunner(mdp);
			pi.setGamma(gamma);
			pi.run();
			System.in.read();
		}

	}

	private void createMDP() {
		mdp = MDP.instance();
		addSingleOutcomeAction(1, 1, 2);
		addSingleOutcomeAction(3, 3, 1);
		addSingleOutcomeAction(4, 4, 256);
		//addSingleOutcomeAction(2, 2, 1);
		
		addSingleOutcomeAction(1, 2, 0);
		addTrippleOutcomeAction(2, 2, 1, 4, 1, 1, 1, 0.98, 0.009, 0.001);
		addSingleOutcomeAction(2, 3, 0);
		addSingleOutcomeAction(3, 4, 0);

		addSingleOutcomeAction(4, 1, 701);

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

	private void addTrippleOutcomeAction(int i, int j1, int j2, int j3, double r1, double r2, double r3, double p1, double p2, double p3) {
		mdp.addMultipleOutcomeAction(State.instance(i), Arrays.asList(State.instance(j1), State.instance(j2), State.instance(j3)), Arrays.asList(r1, r2, r2),
				Arrays.asList(p1, p2, p3), "Triple");
	}
}
