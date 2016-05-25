package ml.rl.mdp.view;

import ml.rl.builder.EpisodeBuilder;
import ml.rl.mdp.model.Episode;
import ml.rl.td.TDLambdaRunner;

public class TDLambdaRunnerTest {

	public static void main(String[] args) {
		Episode e = EpisodeBuilder.instance().addTransitions(1,10,2,10,3,10,4,10,5,10,2,10,6,10,7,10,3).build();
		TDLambdaRunner runner = new TDLambdaRunner(e,1);
		runner.run(0.5,1);
	}
}
