package ml.rl.mdp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.State;
import ml.rl.mdp.model.StateAction;
import ml.rl.mdp.view.MDPViewer;

import org.apache.log4j.Logger;
import org.junit.Test;

import util.MLUtils;

public class HW4Single {

	private static final Logger log = Logger.getLogger(HW4Single.class);
	int numStates = 5;
	int maxReward = 1;
	int actionOutcomes = 1;
	int minActionsPerState = 2;
	int maxActionsPerState = 2;
	int numStatesChangeForHillClimbing = 4;
	double explorationProbability = 0.01;

	int numMDPTried = 0;
	MDP mdp;

	@Test
	public void test() throws Exception {
		createMDP();
		//MDPViewer.instance(mdp).display();
		PolicyIterationBurlap pi = new PolicyIterationBurlap(mdp, 0.75);
		pi.run();
		System.out.println("Iterations:" + pi.getIterationCount());
	}

	private void createMDP() {
		mdp = MDP.instance();
		for (int i = 0; i < numStates; i++) {
			addSingleOutcomeAction(i, i, 100);
			addSingleOutcomeAction(i, i+1, 1);
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

	private void addDoubleOutcomeAction(int i, int j1, int j2) {
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
		double p1 = Math.random();
		double p2 = 1 - p1;
		mdp.addDoubleOutcomStateAction(State.instance(i), State.instance(j1), State.instance(j1), Math.random() * maxReward, Math.random() * maxReward, p1, p2,
				"Action_" + i + "_" + j1 + "_" + j2);
	}

}
