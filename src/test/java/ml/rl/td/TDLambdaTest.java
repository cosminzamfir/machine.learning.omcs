package ml.rl.td;

import ml.rl.builder.EpisodeBuilder;
import ml.rl.mdp.model.Episode;

public class TDLambdaTest {

	public static void main(String[] args) {
		double lambda = 0.623;
		double gamma = 1;
		
		Episode episode = EpisodeBuilder.instance().addRewards(7.9,2.5,9.0,0.0,1.6).build();
		//new TDLambdaEpisode(episode, lambda, gamma, 1).run();
		//episode.printStateValues();

		episode = EpisodeBuilder.instance().addRewards(-5.1,-7.2,9.0,0.0,1.6).build();
		new TDLambdaEpisode(episode, lambda, gamma, 1).run();
		new TDLambdaRunner(episode, lambda).run(gamma, 1);
		episode.printStateValues();

	}
}
