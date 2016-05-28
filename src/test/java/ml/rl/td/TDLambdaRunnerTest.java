package ml.rl.td;

import org.junit.Assert;
import org.junit.Test;

import ml.rl.builder.EpisodeBuilder;
import ml.rl.mdp.model.Episode;
import ml.rl.mdp.model.State;
import util.MLUtils;

public class TDLambdaRunnerTest {

	@Test
	public void test1() throws Exception {
		Episode e = EpisodeBuilder.instance().addTransitions(1, 10, 2, 10, 3, 10, 4, 10, 5, 10, 6).build();
		TDLambdaRunner runner = new TDLambdaRunner(e, 1);
		runner.run(1, 1);
		Assert.assertEquals(50, State.instance(1).getValue(), 0.1);
		MLUtils.readFromConsole("Press any key to continue");
	}

	@Test
	public void test2() throws Exception {
		Episode e = EpisodeBuilder.instance()
				.addTransitions(1,10,2,10,3,10,4,10,5,10,6,10,3,10,7,10,8,10,9,10,2,10,10,10,11,10,12,10,13)
				.build();
		TDLambdaRunner runner = new TDLambdaRunner(e, 1);
		runner.run(1, 1);
		Assert.assertEquals(220, State.instance(1).getValue(), 0.1);
		MLUtils.readFromConsole("Press any key to continue");
	}


}
