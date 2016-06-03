package ml.rl.mdp;

import ml.rl.mdp.model.Action;
import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.State;

import org.junit.Test;

public class PolicyIterationRunnerTest {

	int numStates = 3;
	MDP mdp;

	@Test
	public void test2() throws Exception {
		mdp = MDP.instance();
		addSingleOutcomeAction(1, 1, 1);
		addSingleOutcomeAction(1, 2, 2.5);
		PolicyIterationRunner pir = new PolicyIterationRunner(mdp);
		pir.setGamma(0.75);
		pir.run();
		System.in.read();
	}

	/** From State i to State j */
	private void addSingleOutcomeAction(int i, int j, double reward) {
		if (i < 0)
			i = numStates - 1;
		if (i > numStates - 1)
			i = 0;
		if (j < 0)
			j = numStates - 1;
		if (j > numStates - 1)
			j = 0;
		mdp.addSingleOutcomStateAction(State.instance(i), State.instance(j), reward, "Action_" + i + "_" + j);
	}

}
