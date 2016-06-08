package ml.rl.mdp;

import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.State;

import org.junit.Test;

public class HW4Manual {

	int n = 4;
	MDP mdp;
	boolean showView = false;
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
		addSingleOutcomeAction(1, 1, 1);
		addSingleOutcomeAction(1, 2, 0);

		addSingleOutcomeAction(2, 2, 1);
		addSingleOutcomeAction(2, 3, 0);

		addSingleOutcomeAction(3, 3, 1);
		addSingleOutcomeAction(3, 4, 0);

		addSingleOutcomeAction(4, 4, 256);
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

	//@Test
	public void test1() throws Exception {
		mdp = MDP.instance();
		addDoubleOutcomeAction(1, 1, 3, 10, 10, 0.9, 0.1);
		addSingleOutcomeAction(3, 1, 10);
		addSingleOutcomeAction(3, 3, 10);
		PolicyIterationRunner pi = new PolicyIterationRunner(mdp);
		pi.run();
	}

	public static void main(String[] args) {
		double p1;
		double r1;
		double p2;
		double r2;
		for (double i = 0; i < 1; i += 0.01) {
			for (int j = 0; j < 1000; j++) {
				for (int k = 0; k < 1000; k++) {
					for (int vs4 = 1024; vs4 < 2000; vs4++) {
						p1 = i;
						p2 = 1 - i;
						r1 = j;
						r2 = k;
						double expr1 = p1 * (r1 + 432) + p2 * (r2 + 768);
						double expr2 = p1 * (r1 + 0.75 * 0.75 * 0.75 * vs4) + p2 * (r2 + 0.75 * vs4);
						if (expr1 < 576 && expr2 > 0.75 * 0.75 * vs4) {
							System.out.println(p1 + "," + p1 + "," + r1 + "," + r2);
						}
					}
				}
			}
		}
	}
}
