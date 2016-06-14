package ml.rl.td;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import util.IntHolder;
import ml.rl.mdp.model.Episode;

/**
 * Compute the state values for a sequence of {@link Episode}s
 * @author Cosmin Zamfir
 *
 */
public class TDLambda {

	private Double lambda;
	private Double gamma;
	private List<Episode> episodes = new ArrayList<Episode>();
	
	public TDLambda(double lambda, double gamma, List<Episode> episodes) {
		super();
		this.lambda = lambda;
		this.gamma = gamma;
		this.episodes = episodes;
	}
	
	public void addEpisode(Episode episode) {
		this.episodes.add(episode);
	}
	
	public void run() {
		final IntHolder T = new IntHolder(0);
		episodes.forEach((episode) -> new TDLambdaEpisode(episode, lambda, gamma, (1.0/(T.addAndGet(1)))).run());
	}
	
	
}
