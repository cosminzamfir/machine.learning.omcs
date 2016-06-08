package ml.rl.mdp.model;

import java.util.Arrays;

import ml.rl.mdp.MDPViwerTest;
import ml.rl.mdp.PolicyIteration;
import ml.rl.mdp.ValueIteration;
import ml.rl.mdp.view.MDPViewer;

import org.junit.Test;

public class MDPBuilderTest {

	DieNEnvironment environment;
	
	@Test
	public void test1() throws Exception {
		DieNEnvironment environment = new DieNEnvironment(4,Arrays.asList(1,2,3));
		MDP mdp = new MDPBuilder(environment).build();
		//MDPViewer.instance(mdp).display();
		System.out.println(mdp.printStateValues());
		ValueIteration vi = new ValueIteration(mdp);
		vi.setGamma(1);
		vi.run();
		System.out.println(mdp.printStateValues());
		
		PolicyIteration pi = new PolicyIteration(mdp);
		pi.setGamma(1);
		pi.run();
		System.out.println(mdp.printStateValues());
		System.out.println(pi.getPolicy());
			
	}
	
	
}
