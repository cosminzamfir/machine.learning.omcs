package ml.rl.td;

import java.util.Observable;

import ml.rl.mdp.model.Episode;
import ml.rl.mdp.model.State;
import ml.rl.mdp.model.Transition;

import org.apache.log4j.Logger;

/**
 * 
 * For reference only, is same as TDLambda(lambda=0) 
 *
 */
public class TDZeroEpisode extends Observable  {

	private static final Logger log = Logger.getLogger(TDZeroEpisode.class);
	private Episode episode;
	private double gamma;
	private double alpha;
	
	public TDZeroEpisode(Episode episode, double gamma, double alpha) {
		super();
		this.episode = episode;
		this.gamma = gamma;
		this.alpha = alpha;
	}
	
	public void run() {
		log.info("Running episode: " + episode + ";gamma:" + gamma + ";alpha:" + alpha);
		for (Transition transition : episode.getTransitions()) {
			updateState(transition);
			setChanged();
			notifyObservers(transition);
		}
		episode.printStateValues();
	}

	private void updateState(Transition transition) {
		State s = transition.getS();
		State sprime = transition.getsPrime();
		double newValue = s.getValue() + alpha * (transition.getReward() + gamma * sprime.getValue() - s.getValue());
		s.setValue(newValue);
	}
}
