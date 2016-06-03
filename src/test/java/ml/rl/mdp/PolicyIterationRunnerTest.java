package ml.rl.mdp;

import org.junit.Test;

import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.State;
import util.MLUtils;

public class PolicyIterationRunnerTest {

	MDP mdp;

	@Test
	public void test1() throws Exception {
		mdp = MDP.instance();
		addSingleOutcomeAction(1, 2, 1);
		addSingleOutcomeAction(1, 3, 2);
		
		addSingleOutcomeAction(2, 4, 1);
		addSingleOutcomeAction(2, 5, 1);
		
		addSingleOutcomeAction(3, 6, 1);
		addSingleOutcomeAction(3, 7, 1);

		addSingleOutcomeAction(5, 5, 2);
		
		addSingleOutcomeAction(6, 6, 2);

		PolicyIterationRunner pir = new PolicyIterationRunner(mdp);
		pir.setGamma(0.75);
		pir.run();
		MLUtils.readFromConsole("Press any key");
	}
	
	@Test
	public void test2() throws Exception {
		mdp = MDP.instance();
		
		addSingleOutcomeAction(1, 2, 1);
		addSingleOutcomeAction(2, 3, 1);
		addSingleOutcomeAction(3, 4, 1);
		addSingleOutcomeAction(4, 1, 1);
		addDoubleOutcomeAction(4, 5, 6, 1, 1, 0.5, 0.5);
		addSingleOutcomeAction(6, 5, 10);


		PolicyIterationRunner pir = new PolicyIterationRunner(mdp);
		pir.setGamma(0.999);
		pir.run();
		MLUtils.readFromConsole("Press any key");
	}

	private void addSingleOutcomeAction(int s, int sprime, double reward) {
		mdp.addSingleOutcomStateAction(State.instance(s), State.instance(sprime), reward, "S" + s + " => S" + sprime);
	}
	
	private void addDoubleOutcomeAction(int i, int j1, int j2, double reward1, double reward2, double p1, double p2) {
		mdp.addDoubleOutcomStateAction(State.instance(i), State.instance(j1), State.instance(j2), reward1, reward2, p1,
				p2,
				"S" + i + " => [S" + j1 + ",S" + j2 + "]");
	}

}
