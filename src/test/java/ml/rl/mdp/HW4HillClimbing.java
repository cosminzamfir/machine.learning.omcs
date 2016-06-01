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

public class HW4HillClimbing {

	private static final Logger log = Logger.getLogger(HW4HillClimbing.class);
	int numStates = 30;
	int maxReward = 1;
	int actionOutcomes = 1;
	int minActionsPerState = 2;
	int maxActionsPerState = 2;
	int numStatesChangeForHillClimbing = 4;
	double explorationProbability = 0.01;
	
	int numMDPTried = 0; 

	@Test
	public void test() throws Exception {
		while (true) {
			MDP mdp = createRadomMDP();
			PolicyIterationBurlap pi = new PolicyIterationBurlap(mdp, 0.75);
			pi.run();
			int iterations = pi.getIterationCount();
			while (true) {
				iterations = hillClimb(mdp, iterations);

			}
		}
	}

	private MDP createRadomMDP() {
		MDP res = MDP.instance();
		for (int i = 0; i < numStates - 1; i++) {
			State.instance(i);
		}
		for (int i = 0; i < numStates; i++) {
			State s = State.instance(i);
			addRandomStateActions(s, res);
		}
		return res;
	}

	private void addRandomStateActions(State s, MDP mdp) {
		int numActions = MLUtils.random(minActionsPerState, maxActionsPerState);
		for (int i = 0; i < numActions; i++) {
			List<State> sPrimes = getRandomSprimes();
			List<Double> rewards = getRandomRewards();
			List<Double> probabilities = getRandomProbabilities();
			mdp.addMultipleOutcomStateAction(s, sPrimes, rewards, probabilities, "Action_" + i);
			
		}

	}

	private List<State> getRandomSprimes() {
		List<State> res = new ArrayList<>();
		for (int i = 0; i < actionOutcomes; i++) {
			res.add(getRandomState());
		}
		return res;
	}

	private List<Double> getRandomRewards() {
		List<Double> res = new ArrayList<>();
		for (int i = 0; i < actionOutcomes; i++) {
			res.add(Math.random() * maxReward);
		}
		return res;
	}

	private List<Double> getRandomProbabilities() {
		List<Double> res = new ArrayList<>();
		double sum = 0;
		for (int i = 0; i < actionOutcomes; i++) {
			double d = Math.random();
			res.add(d);
			sum += d;
		}
		for (int i = 0; i < actionOutcomes; i++) {
			res.set(i, res.get(i) / sum);
		}
		return res;

	}

	private State getRandomState() {
		return State.instance(MLUtils.random(numStates - 1));
	}

	private int hillClimb(MDP mdp, int currentIterations) {
		log.info("Searching MDP better than " + currentIterations);
		PolicyIterationBurlap pi = null;
		do {
			List<State> statesToModify = new ArrayList<>();
			Map<State, List<StateAction>> backup = new HashMap<State, List<StateAction>>();

			//change
			//int numChanges = MLUtils.random(numStatesChangeForHillClimbing);
			int numChanges = numStatesChangeForHillClimbing;
			for (int i = 0; i < numChanges; i++) {
				State state = getRandomState();
				statesToModify.add(state);
				backup.put(state, new ArrayList(mdp.getStateActions(state)));
				mdp.getStateActions(state).clear();
				addRandomStateActions(state, mdp);
			}

			//compute
			pi = new PolicyIterationBurlap(mdp, 0.75);
			pi.run();
			if(++numMDPTried  % 100000 == 0) {
				System.out.println("Tried " + MLUtils.format(numMDPTried) + " MDPs. Best result:" + currentIterations);
			}

			//revert if much worse, revert if worse unless explore
			if ((pi.getIterationCount() < currentIterations - 1) || (pi.getIterationCount() < currentIterations && !explore())) {
				for (State state : statesToModify) {
					mdp.getStateActions(state).clear();
					mdp.getStateActions(state).addAll(backup.get(state));
				}
			}
		} while (pi.getIterationCount() <= currentIterations);
		log.info("Got better MDP: " + pi.getIterationCount());
		if (pi.getIterationCount() > 15) {
			MDPViewer.instance(mdp).display();
			try {
				System.in.read();
			} catch (IOException e) {
				throw new RuntimeException("",e);
			}
			mdp.saveAsJson("mdp_" + pi.getIterationCount() + ".json", 0.75);
			mdp.saveAsXML("mdp_" + pi.getIterationCount() + ".xml");
		}
		return pi.getIterationCount();
	}

	private boolean explore() {
		return Math.random() < explorationProbability;
	}
}
