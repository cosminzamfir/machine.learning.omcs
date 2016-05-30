package ml.rl.mdp;

import ml.rl.mdp.model.Action;
import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.State;
import ml.rl.td.view.MDPViewer;

import org.junit.Test;

import util.MLUtils;

public class HW4Test {

	int numStates = 30;
	int maxReward = 100;
	
	@Test
	public void test() throws Exception {
		while (true) {
			MDP mdp = createRadomMDP(numStates, maxReward);
			PolicyIteration pi = new PolicyIteration(mdp, 0.75);
			pi.run();
			if (pi.getIterationCount() > 43) {
				System.out.println("Iterations: " + pi.getIterationCount());
				mdp.saveAsString("mdp" + System.currentTimeMillis() + "_" + pi.getIterationCount() + ".txt");
			}
		}
	}

	private MDP createRadomMDP(int numStates, int maxReward) {
		MDP res = MDP.instance();
		for (int i = 0; i < numStates; i++) {
			for (int j = 0; j < 2; j++) {
				double prob1 = (double) Math.round(Math.random() * 10) / 10;
				double prob2 = 1 - prob1;
				State sprime1 = State.instance(MLUtils.random(numStates));
				State sprime2 = State.instance(MLUtils.random(numStates));
				int reward1 = MLUtils.random(maxReward);
				int reward2 = MLUtils.random(maxReward);
				res.addDoubleOutcomStateAction(State.instance(i), sprime1, sprime2, reward1, reward2, prob1, prob2, j == 0 ? Action.NORTH : Action.SOUTH);
			}
		}
		MDPViewer.instance(res).display();
		return res;
	}
}
