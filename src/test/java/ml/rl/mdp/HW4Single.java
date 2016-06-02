package ml.rl.mdp;

import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.State;
import ml.rl.mdp.view.MDPViewer;

import org.junit.Test;

public class HW4Single {

	int numStates = 6000;
	MDP mdp;
	boolean showView = true;
	double gamma = 0.75;

	@Test
	public void test() throws Exception {
		createMDP();
		PolicyIteration pi = new PolicyIteration(mdp, 0.75);
		pi.run();
		System.out.println("Iterations:" + pi.getIterationCount());
		mdp.saveAsJson("mymdp.json", 0.75);
		if (showView) {
			MDPViewer v = MDPViewer.instance(mdp);
			v.display();
			v.setCompleted();
			v.markPolicy(pi.getPolicy());
			System.in.read();
		}

	}

	private void createMDP() {
		double rnn = 10;
		double rs = 1;
		double multiplier = 1 / (1-gamma); 
		State first = State.instance(0);
		State last = State.instance(numStates -1);
		double v0s0 = multiplier * rs;
		double v0sn = multiplier * rnn;
		double vs0 = discountFrom(first, last)*multiplier*rnn;
		double rn0 = between(v0sn - gamma*vs0, v0sn - v0s0);
		
		mdp = MDP.instance();
		for (int i = 0; i < numStates - 1; i++) {
			//... guess what
		}
	}
	
	private double between(double d1, double d2) {
		return (d1 + d2)/2;
	}

	private double discountFrom(State s1, State s2) {
		return Math.pow(gamma, s2.getId() - s1.getId());
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

	/** Stochastic outcome action from State i to States j1 and j2 with given rewards and probabilities*/
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
