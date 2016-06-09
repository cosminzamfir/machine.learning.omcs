package ml.rl.mdp.model;

import java.util.Arrays;
import java.util.List;

import ml.rl.mdp.PolicyIteration;

import org.junit.Assert;
import org.junit.Test;

public class MDPBuilderDieNTest {

	@Test
	public void test1() throws Exception {
		run(4, Arrays.asList(2,3,4), 0.25);
	}
	
	@Test
	public void test2() throws Exception {
		run(8, Arrays.asList(1,3,5,6,7), 1.8125);
	}

	@Test
	public void test3() throws Exception {
		run(6, Arrays.asList(1,3,4,6), 1.1666);
	}

	
	public void run(int N, List<Integer> badSides, double expectedValue) throws Exception {
		DieNEnvironment environment = new DieNEnvironment(N,badSides);
		MDP mdp = environment.generateMDP();
		PolicyIteration pi = new PolicyIteration(mdp);
		pi.setGamma(1);
		pi.run();
		System.out.println(pi.getPolicy());
		Assert.assertEquals(expectedValue, mdp.getStates().get(0).getValue(), 0.0001);
	}
	
	
}
