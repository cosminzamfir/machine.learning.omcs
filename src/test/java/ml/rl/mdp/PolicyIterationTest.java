package ml.rl.mdp;
import org.junit.Assert;
import org.junit.Test;

import ml.rl.mdp.model.Action;
import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.MDPPolicy;
import ml.rl.mdp.model.State;

public class PolicyIterationTest {

	@Test
	public void test1() throws Exception {
		MDP mdp = MDP.instance();
		mdp.addSingleOutcomAction(State.instance(1), State.instance(2), 10, Action.EAST);
		mdp.addSingleOutcomAction(State.instance(1), State.instance(3), 20, Action.WEST);
		
		PolicyIteration pi = new PolicyIteration(mdp);
		pi.run();
		MDPPolicy policy = pi.getPolicy();
		Assert.assertEquals(policy.getAction(State.instance(1)).getName(), Action.WEST);
		
	}

	@Test
	public void test2() throws Exception {
		MDP mdp = MDP.instance();
		mdp.addSingleOutcomAction(State.instance(1), State.instance(2), -1, Action.NORTH);
		mdp.addSingleOutcomAction(State.instance(1), State.instance(6), -1, Action.EAST);
		mdp.addSingleOutcomAction(State.instance(1), State.instance(4), 10, Action.NORTH_EAST);

		mdp.addSingleOutcomAction(State.instance(2), State.instance(3), -2, Action.NORTH);
		mdp.addSingleOutcomAction(State.instance(2), State.instance(5), -1, Action.EAST);
		
		mdp.addSingleOutcomAction(State.instance(6), State.instance(5), -1, Action.NORTH);
		mdp.addSingleOutcomAction(State.instance(3), State.instance(4), 10, Action.NORTH);		
		mdp.addSingleOutcomAction(State.instance(5), State.instance(4), 10, Action.NORTH);

		
		PolicyIteration pi = new PolicyIteration(mdp);
		pi.run();
		MDPPolicy policy = pi.getPolicy();
		System.out.println(policy);
		
		Assert.assertEquals(policy.getAction(State.instance(1)).getName(), Action.NORTH_EAST);
		Assert.assertEquals(policy.getAction(State.instance(2)).getName(), Action.EAST);
		Assert.assertEquals(policy.getAction(State.instance(3)).getName(), Action.NORTH);
		Assert.assertEquals(policy.getAction(State.instance(5)).getName(), Action.NORTH);
		Assert.assertEquals(policy.getAction(State.instance(6)).getName(), Action.NORTH);
	
	}

}
