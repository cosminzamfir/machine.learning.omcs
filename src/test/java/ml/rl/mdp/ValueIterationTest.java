package ml.rl.mdp;
import ml.rl.mdp.ValueIteration;
import ml.rl.mdp.model.Action;
import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.State;
import ml.rl.td.view.MDPViewer;
import util.MLUtils;

import org.junit.Assert;
import org.junit.Test;

public class ValueIterationTest {

	@Test
	public void test1() throws Exception {
		MDP mdp = MDP.instance();
		mdp.addSingleOutcomStateAction(State.instance(1), State.instance(2), 10, Action.defaultName);
		ValueIteration vi = new ValueIteration(mdp);
		vi.run();
		Assert.assertEquals(10, State.instance(1).getValue(), 0.1);
	}

	@Test
	public void test2() throws Exception {
		MDP mdp = MDP.instance();
		mdp.addDoubleOutcomStateAction(State.instance(0), State.instance(1), State.instance(2), 7.9, -5.1, 0.81, 0.19, Action.defaultName);
		mdp.addSingleOutcomStateAction(State.instance(1), State.instance(3), 2.5, Action.defaultName);
		mdp.addSingleOutcomStateAction(State.instance(2), State.instance(3), -7.2, Action.defaultName);
		mdp.addSingleOutcomStateAction(State.instance(3), State.instance(4), 9.0, Action.defaultName);
		mdp.addSingleOutcomStateAction(State.instance(4), State.instance(5), 0, Action.defaultName);
		mdp.addSingleOutcomStateAction(State.instance(5), State.instance(6), 1.6, Action.defaultName);

		MDPViewer.instance(mdp).display();

		ValueIteration vi = new ValueIteration(mdp);
		vi.run();
		Assert.assertEquals(16.687, State.instance(0).getValue(), 0.1);
		
		MLUtils.readFromConsole("Press any key");
	}

}
