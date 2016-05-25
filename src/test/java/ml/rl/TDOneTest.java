package ml.rl;

import java.util.ArrayList;
import java.util.List;

import ml.rl.builder.EpisodeBuilder;
import ml.rl.mdp.model.Episode;
import ml.rl.td.TDOne;

import org.junit.Test;

public class TDOneTest {

	@Test
	public void test1() throws Exception {
		List<Episode> episodes = new ArrayList<>();
		episodes.add(EpisodeBuilder.instance().addTransitions(1,1,3,0,4,1,6).build());
		episodes.add(EpisodeBuilder.instance().addTransitions(1,1,3,0,5,10,6).build());
		episodes.add(EpisodeBuilder.instance().addTransitions(1,1,3,0,4,1,6).build());
		episodes.add(EpisodeBuilder.instance().addTransitions(1,1,3,0,4,1,6).build());
		episodes.add(EpisodeBuilder.instance().addTransitions(2,2,3,0,5,10,6).build());
		TDOne tdOne = new TDOne(1.0, episodes);
		tdOne.run();
	}
}
