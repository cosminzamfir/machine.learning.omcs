package ml.rl.mdp;

import org.apache.log4j.Logger;
import org.junit.Test;

import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.State;
import ml.rl.mdp.view.MDPViewer;

public class HW4Single {

	private static final Logger log = Logger.getLogger(HW4Single.class);
	int numStates =30;
	int numMDPTried = 0;
	MDP mdp;

	@Test
	public void test() throws Exception {
		createMDP();
		PolicyIterationBurlap pi = new PolicyIterationBurlap(mdp, 0.75);
		pi.run();
		System.out.println("Iterations:" + pi.getIterationCount());
		mdp.saveAsJson("mymdp.json", 0.75);
		MDPViewer v = MDPViewer.instance(mdp);
		v.display();
		v.setCompleted();
		System.in.read();

	}

	private void createMDP() {
		double multiplier = 1000000;
		mdp = MDP.instance();
		for (int i = 0; i < numStates-1; i++) {
		}
	}
	

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

	private void addDoubleOutcomeAction(int i, int j1, int j2, double reward1, double reward2, double p1, double p2) {
		if (i < 0)
			i = numStates - 1;
		if (i > numStates - 1)
			i = numStates - 1;
		if (j1 < 0)
			j1 = 1;
		if (j1 > numStates - 1)
			j1 = numStates - 1;
		if (j2 < 0)
			j2 = 1;
		if (j2 > numStates - 1)
			j2 = numStates - 1;
		mdp.addDoubleOutcomStateAction(State.instance(i), State.instance(j1), State.instance(j1), reward1, reward2, p1, p2,
				"Action_" + i + "_" + j1 + "_" + j2);
	}

}
