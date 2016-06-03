package ml.rl.mdp;
import ml.rl.mdp.model.Action;
import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.MDPPolicy;
import ml.rl.mdp.model.State;
import ml.rl.mdp.view.MDPViewer;

import org.junit.Assert;
import org.junit.Test;

public class PolicyIterationTest {

	@Test
	public void test1() throws Exception {
		MDP mdp = MDP.instance();
		mdp.addSingleOutcomStateAction(State.instance(1), State.instance(2), 10, Action.EAST);
		mdp.addSingleOutcomStateAction(State.instance(1), State.instance(3), 20, Action.WEST);
		
		PolicyIteration pi = new PolicyIteration(mdp);
		pi.run();
		MDPPolicy policy = pi.getPolicy();
		Assert.assertEquals(policy.getStatePolicy(State.instance(1)).getStateAction().getAction().getName(), Action.WEST);
		
	}

	@Test
	public void test2() throws Exception {
		MDP mdp = MDP.instance();
		mdp.addSingleOutcomStateAction(State.instance(1), State.instance(2), -1, Action.NORTH);
		mdp.addSingleOutcomStateAction(State.instance(1), State.instance(6), -1, Action.EAST);
		mdp.addSingleOutcomStateAction(State.instance(1), State.instance(4), 10, Action.NORTH_EAST);

		mdp.addSingleOutcomStateAction(State.instance(2), State.instance(3), -2, Action.NORTH);
		mdp.addSingleOutcomStateAction(State.instance(2), State.instance(5), -1, Action.EAST);
		
		mdp.addSingleOutcomStateAction(State.instance(6), State.instance(5), -1, Action.NORTH);
		mdp.addSingleOutcomStateAction(State.instance(3), State.instance(4), 10, Action.NORTH);		
		mdp.addSingleOutcomStateAction(State.instance(5), State.instance(4), 10, Action.NORTH);

		
		PolicyIteration pi = new PolicyIteration(mdp);
		pi.run();
		MDPPolicy policy = pi.getPolicy();
		System.out.println(policy);
		
		//MDPViewer viewer = MDPViewer.instance(mdp);
		//viewer.display();
		//viewer.setCompleted();
		//viewer.markPolicy(policy);
		
		Assert.assertEquals(policy.getStatePolicy(State.instance(1)).getStateAction().getAction().getName(), Action.NORTH_EAST);
		Assert.assertEquals(policy.getStatePolicy(State.instance(2)).getStateAction().getAction().getName(), Action.EAST);
		Assert.assertEquals(policy.getStatePolicy(State.instance(3)).getStateAction().getAction().getName(), Action.NORTH);
		Assert.assertEquals(policy.getStatePolicy(State.instance(5)).getStateAction().getAction().getName(), Action.NORTH);
		Assert.assertEquals(policy.getStatePolicy(State.instance(6)).getStateAction().getAction().getName(), Action.NORTH);
	
	}

}
