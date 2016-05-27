import ml.rl.mdp.ValueIterationRunner;
import ml.rl.mdp.model.Action;
import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.State;

import org.junit.Test;

public class ValueIterationRunnerTest {


	@Test
	public void test2() throws Exception {
		MDP mdp = MDP.instance();
		mdp.addDoubleOutcomStateAction(State.instance(0), State.instance(1), State.instance(2), 7.9, -5.1, 0.81, 0.19, Action.defaultName);
		mdp.addSingleOutcomStateAction(State.instance(1), State.instance(3), 2.5, Action.defaultName);
		mdp.addSingleOutcomStateAction(State.instance(2), State.instance(3), -7.2, Action.defaultName);
		mdp.addSingleOutcomStateAction(State.instance(3), State.instance(4), 9, Action.defaultName);
		mdp.addSingleOutcomStateAction(State.instance(4), State.instance(5), 0, Action.defaultName);
		mdp.addSingleOutcomStateAction(State.instance(5), State.instance(6), 1.6, Action.defaultName);
		ValueIterationRunner vir = new ValueIterationRunner(mdp);
		vir.run();
		System.in.read();
	}

	@Test
	public void test3() throws Exception {
		MDP mdp = MDP.instance();
		mdp.addDoubleOutcomStateAction(State.instance(0), State.instance(1), State.instance(2), 7.9, -5.1, 0.81, 0.19, Action.defaultName);
		mdp.addDoubleOutcomStateAction(State.instance(0), State.instance(3), State.instance(4), 7.9, -5.1, 0.81, 0.19, Action.defaultName);
		mdp.addSingleOutcomStateAction(State.instance(1), State.instance(5), 2.5, Action.defaultName);
		mdp.addSingleOutcomStateAction(State.instance(2), State.instance(5), -7.2, Action.defaultName);
		mdp.addSingleOutcomStateAction(State.instance(3), State.instance(6), 9, Action.defaultName);
		mdp.addSingleOutcomStateAction(State.instance(4), State.instance(7), 0, Action.defaultName);
		mdp.addSingleOutcomStateAction(State.instance(5), State.instance(8), 1.6, Action.defaultName);
		mdp.addSingleOutcomStateAction(State.instance(6), State.instance(9), 1.6, Action.defaultName);
		mdp.addSingleOutcomStateAction(State.instance(9), State.instance(9), 1.6, Action.defaultName);
		mdp.addSingleOutcomStateAction(State.instance(9), State.instance(10), 1.6, Action.defaultName);

		ValueIterationRunner vir = new ValueIterationRunner(mdp);
		vir.setGamma(0.5);
		vir.run();
		System.in.read();
	}

}
