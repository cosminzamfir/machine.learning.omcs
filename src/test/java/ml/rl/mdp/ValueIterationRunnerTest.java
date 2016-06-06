package ml.rl.mdp;

import org.junit.Test;

import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.State;

public class ValueIterationRunnerTest {

	MDP mdp;

	@Test
	public void test1() throws Exception {
		mdp = MDP.instance();
		addDoubleOutcomeAction(0, 1, 2, 2, 3, 0.6, 0.7);
		addSingleOutcomeAction(1, 3, 20);
		addSingleOutcomeAction(2, 4, 30);
		addSingleOutcomeAction(3, 5, 10);
		addSingleOutcomeAction(4, 6, 10);
		ValueIterationRunner vir = new ValueIterationRunner(mdp);
		vir.run();
		System.in.read();
	}

	@Test
	public void test2() throws Exception {
		mdp = MDP.instance();
		addDoubleOutcomeAction(0, 1, 2, 7.9, -5.1, 0.81, 0.19);
		addDoubleOutcomeAction(0, 3, 4, 7.9, -5.1, 0.81, 0.19);
		addSingleOutcomeAction(1, 2, 2.5);
		addSingleOutcomeAction(2, 5, 2.5);
		addSingleOutcomeAction(2, 7, 2.5);
		addSingleOutcomeAction(3, 7, 2.5);
		addSingleOutcomeAction(7, 9, 2.5);
		addSingleOutcomeAction(6, 8, 2.5);
		addSingleOutcomeAction(4, 6, 2.5);
		addSingleOutcomeAction(7, 9, 2.5);
		addSingleOutcomeAction(8, 8, 1E6);


		ValueIterationRunner vir = new ValueIterationRunner(mdp);
		vir.setGamma(0.9);
		vir.run();
		System.in.read();
	}

	private void addSingleOutcomeAction(int s, int sprime, double reward) {
		mdp.addSingleOutcomAction(State.instance(s), State.instance(sprime), reward, "S" + s + " => S" + sprime);
	}

	private void addDoubleOutcomeAction(int i, int j1, int j2, double reward1, double reward2, double p1, double p2) {
		mdp.addDoubleOutcomAction(State.instance(i), State.instance(j1), State.instance(j2), reward1, reward2, p1,
				p2,
				"S" + i + " => [S" + j1 + ",S" + j2 + "]");
	}


}
