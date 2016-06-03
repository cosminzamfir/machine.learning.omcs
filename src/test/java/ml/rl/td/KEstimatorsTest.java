package ml.rl.td;

import org.junit.Assert;
import org.junit.Test;

import ml.rl.builder.EpisodeBuilder;
import ml.rl.mdp.model.Episode;

public class KEstimatorsTest {

	@Test
	public void test1() throws Exception {
		Episode episode = EpisodeBuilder.instance().addRewards(1, 1, 1).build();
		episode.setStateValueAt(3, 1);
		estimate(episode, 1, 2);
		estimate(episode, 2, 2);
		estimate(episode, 3, 3);
	}

	private void estimate(Episode episode, int k, int expected) {
		double val = new KEstimator().estimate(episode, k, 1);
		Assert.assertEquals(expected, val, 00001);
	}
}
