package ml.rl.td;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import ml.rl.mdp.model.Episode;

/**
 * Compute the state values for a sequence of {@link Episode}s
 * For reference only, is same as TDLambda(lambda=1) 
 *
 */
public class TDOne {

	private Double gamma;
	private List<Episode> episodes = new ArrayList<Episode>();
	
	public TDOne(Double gamma, List<Episode> episodes) {
		super();
		this.gamma = gamma;
		this.episodes = episodes;
	}
	
	public void addEpisode(Episode episode) {
		this.episodes.add(episode);
	}
	
	public void run() {
		final AtomicInteger T = new AtomicInteger(0);
		episodes.forEach((episode) -> new TDOneEpisode(episode, gamma, (1.0/(T.incrementAndGet()))).run());
	}
	
	
}
