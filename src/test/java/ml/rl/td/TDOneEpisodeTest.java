package ml.rl.td;

import java.text.MessageFormat;

import ml.rl.builder.EpisodeBuilder;
import ml.rl.mdp.model.Episode;
import ml.rl.td.TDOneEpisode;

import org.junit.Test;

public class TDOneEpisodeTest {

	@Test
	public void test1() throws Exception {
		Episode e = EpisodeBuilder.defaultInstance(3, 1).build();
		TDOneEpisode tdOne = new TDOneEpisode(e,0.5,1);
		tdOne.run();
		e.getAllStates().forEach((s) -> System.out.println(MessageFormat.format("{0} : {1}", s, s.getValue())));
	}
}
