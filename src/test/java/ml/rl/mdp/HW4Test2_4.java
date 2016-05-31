package ml.rl.mdp;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.junit.Test;

import ml.rl.mdp.model.Action;
import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.State;
import ml.rl.mdp.model.StateAction;
import util.MLUtils;

public class HW4Test2_4 {

	private static final Logger log = Logger.getLogger(HW4Test2_4.class);
	int numStates = 30;
	int maxReward = 1000000;

	@Test
	public void test() throws Exception {
		while (true) {
			MDP mdp = createRadomMDP(numStates, maxReward);
			PolicyIteration pi = new PolicyIteration(mdp, 0.75);
			pi.run();
			int iterations = pi.getIterationCount();
			while (true) {
				iterations = setNextBetterMdp(mdp, iterations);

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
			addRandomStateActions(s, i, res);
		}
		return res;
	}

	private void addRandomStateActions(State s, int i, MDP res) {
		for (int j = 0; j < 2; j++) {
			double prob1 = 0;
			while (prob1 == 0) {
				prob1 = (double) Math.round(Math.random() * 100) / 100;
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

	private State getSprime2(State s, State sprime1, int sId) {
		return State.instance(MLUtils.random(numStates - 1));
	}

	private State getSprime1(State s, int sId) {
		return State.instance(MLUtils.random(numStates - 1));
	}

	private int setNextBetterMdp(MDP mdp, int iterations) {
		log.info("Try to find MDP better than " + iterations);
		PolicyIteration pi = null;
		do {
			State state1 = State.instance(MLUtils.random(numStates - 1));
			int id1 = state1.getId();
			List<StateAction> sas1 = new ArrayList<>(mdp.getStateActions(state1));
			
			List<StateAction> stateActions1 = mdp.getStateActions(state1);
			stateActions1.clear();
			addRandomStateActions(state1, id1, mdp);
			
			pi = new PolicyIteration(mdp, 0.75);
			
			pi.run();
			if(pi.getIterationCount() <= iterations) {
				mdp.getStateActions(state1).clear();
				mdp.getStateActions(state1).addAll(sas1);

			}
		} while (pi.getIterationCount() <= iterations);
		log.info("Got better mdp: " + pi.getIterationCount());
		if(pi.getIterationCount() > 15) {
			mdp.saveAsJson("mdp_" + pi.getIterationCount() + ".json", 0.75);
		}
		return pi.getIterationCount();
	}
}
