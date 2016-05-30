package ml.rl.mdp;

import org.junit.Test;

import ml.rl.mdp.model.Action;
import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.State;
import ml.rl.mdp.view.MDPViewer;
import util.JsonEncoder;
import util.MLUtils;

public class HW4Test {

	int numStates = 5;
	int maxReward = 1000000;

	@Test
	public void test() throws Exception {
		int index = 1;
		while (true) {
			MDP mdp = createRadomMDP(numStates, maxReward);
			MDPViewer.instance(mdp).display();
			PolicyIteration pi = new PolicyIteration(mdp, 0.75);
			pi.run();
			if (pi.getIterationCount() > 6) {
				System.out.println("Iterations: " + pi.getIterationCount());
			}
			if (pi.getIterationCount() > 15) {
				System.out.println(new JsonEncoder(mdp).encode(0.75));
				mdp.saveAsJson("mdp_" + System.currentTimeMillis() + "_" + pi.getIterationCount(), 0.75);
			}
			if (index++ % 10000 == 0) {
				System.out.println(index + ",");
			}
		}
	}

	private MDP createRadomMDP(int numStates, int maxReward) {
		MDP res = MDP.instance();
		for (int i = 0; i < numStates - 1; i++) {
			State.instance(i);
		}
		for (int i = 0; i < numStates; i++) {
			State s = State.instance(i);
			for (int j = 0; j < 2; j++) {
				double prob1 = 0;
				while (prob1 == 0) {
					prob1 = (double) Math.round(Math.random() * 10) / 10;
				}
				double prob2 = 1 - prob1;

				State sprime1 = getSprime1(s, i);
				State sprime2 = getSprime2(s, sprime1, i);
				int reward1 = MLUtils.random(maxReward);
				int reward2 = MLUtils.random(maxReward);
				res.addDoubleOutcomStateAction(s, sprime1, sprime2, reward1, reward2, prob1, prob2,
						j == 0 ? Action.NORTH : Action.SOUTH);
			}
		}
		return res;
	}

	private State getSprime2(State s, State sprime1, int sId) {
		if (sId < numStates - 1) {
			return State.instance(sId + 1);
		}
		return State.instance(1);
	}

	private State getSprime1(State s, int sId) {
		return s;
	}
}
