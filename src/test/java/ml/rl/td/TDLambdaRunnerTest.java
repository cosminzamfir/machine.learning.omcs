package ml.rl.td;

import ml.rl.builder.EpisodeBuilder;
import ml.rl.mdp.model.Episode;
import ml.rl.td.TDLambdaRunner;

public class TDLambdaRunnerTest {

	public static void main(String[] args) {
		Episode e = EpisodeBuilder.instance().addTransitions(1,10,2,10,3,10,4,10,5,10,6).build();
		TDLambdaRunner runner = new TDLambdaRunner(e,0);
		runner.run(1,1);
	}
}
