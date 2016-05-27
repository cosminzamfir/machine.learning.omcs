package ml.rl.td;

import static org.junit.Assert.*;
import ml.rl.mdp.model.Action;
import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.State;
import ml.rl.mdp.model.StateAction;
import ml.rl.mdp.model.Transition;

import org.junit.Test;

import sun.swing.plaf.synth.DefaultSynthStyle.StateInfo;

public class MDPTest {

	@Test
	public void test1() throws Exception {
		MDP mdp = MDP.instance();
		StateAction sa1 = StateAction.instance(State.instance(1), Action.defaultAction());
		sa1.addTransition(Transition.instance(Action.defaultAction(), State.instance(1), State.instance(2), 100), 0.3);
		sa1.addTransition(Transition.instance(Action.defaultAction(), State.instance(1), State.instance(3), 10), 0.7);
		mdp.addStateAction(sa1);
		System.out.println(mdp);
		
	}

	@Test
	public void test2() throws Exception {
		MDP mdp = MDP.instance();
		StateAction sa1 = StateAction.instance(State.instance(1), Action.defaultAction());
		sa1.addTransition(State.instance(2), 2, 0.5);
		sa1.addTransition(State.instance(3), 1, 0.5);
		mdp.addStateAction(sa1);
		
		StateAction sa2 = StateAction.instance(State.instance(2), Action.defaultAction());
		sa2.addTransition(State.instance(4), 5,1);
		mdp.addStateAction(sa2);

		StateAction sa3 = StateAction.instance(State.instance(3), Action.defaultAction());
		sa3.addTransition(State.instance(5), 4, 1);
		mdp.addStateAction(sa3);

		System.out.println(mdp);

	}

	@Test
	public void test3() throws Exception {
		MDP mdp = MDP.instance();
		StateAction sa = StateAction.instance(State.instance(1), Action.defaultAction());
		sa.addTransition(State.instance(2), 10, 0.5);;
		sa.addTransition(State.instance(3), 5, 0.5);
		mdp.addStateAction(sa);

		sa = StateAction.instance(State.instance(1), Action.defaultAction());
		sa.addTransition(State.instance(4), 10, 0.5);;
		sa.addTransition(State.instance(5), 5, 0.5);
		mdp.addStateAction(sa);
	}

}
