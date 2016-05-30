package ml.rl.mdp;

import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.State;
import ml.rl.mdp.view.MDPViewer;

import org.junit.Test;

public class MDPViwerTest {

	@Test
	public void test1() throws Exception {
		MDP mdp = MDP.instance();
		mdp.addSingleOutcomStateAction(State.instance(1), State.instance(2), 10, "Action");
		MDPViewer.instance(mdp).display();
		System.in.read();
	}

	@Test
	public void test2() throws Exception {
		MDP mdp = MDP.instance();
		mdp.addDoubleOutcomStateAction(State.instance(1), State.instance(2), State.instance(3), 10, 20, 0.5, 0.5, "Action");
		MDPViewer.instance(mdp).display();
		System.in.read();
	}

	@Test
	public void test3() throws Exception {
		MDP mdp = MDP.instance();
		mdp.addDoubleOutcomStateAction(State.instance(1), State.instance(2), State.instance(3), 10, 20, 0.5, 0.5, "Action");
		mdp.addSingleOutcomStateAction(State.instance(2), State.instance(4), 10, "Action");
		mdp.addSingleOutcomStateAction(State.instance(3), State.instance(4), 10, "Action");
		mdp.addSingleOutcomStateAction(State.instance(4), State.instance(5), 10, "Action");
		mdp.addSingleOutcomStateAction(State.instance(5), State.instance(6), 10, "Action");
		mdp.addSingleOutcomStateAction(State.instance(6), State.instance(7), 10, "Action");
		mdp.addSingleOutcomStateAction(State.instance(6), State.instance(8), 10, "Action");

		MDPViewer.instance(mdp).display();
		System.in.read();
	}

}
