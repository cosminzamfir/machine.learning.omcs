package ml.rl.td;

import java.util.Observable;

import ml.rl.mdp.model.Episode;
import ml.rl.mdp.model.State;
import ml.rl.mdp.model.Transition;

import org.apache.log4j.Logger;

/**
 * Compute the state values for one {@link Episode}
 */
public class TDLambdaEpisode extends Observable {

	private static final Logger log = Logger.getLogger(TDLambdaEpisode.class);

	private static final String ELIGIBILITY = "elig";

	private Episode episode;
	private double lambda;
	private double gamma;
	private double alpha;

	public TDLambdaEpisode(Episode episode, double lambda, double gamma, double alpha) {
		super();
		this.episode = episode;
		this.lambda = lambda;
		this.gamma = gamma;
		this.alpha = alpha;
		initEligibilities();
	}

	public void run() {
		if (log.isDebugEnabled()) {
			log.debug("Running episode: " + episode + ";lambda:" + lambda + ";gamma:" + gamma + ";alpha:" + alpha);
			log.debug(episode.printStateValues());
			log.debug(episode.printRewards());
		}
		for (Transition transition : episode.getTransitions()) {
			incrementEligibility(transition.getS());
			double stepProfitability = computeProfitability(transition);
			if (log.isDebugEnabled()) {
				log.debug("Profitability for " + transition + ":" + stepProfitability);
			}
			updateStateValues(stepProfitability);
			updateEligibilities();
			setChanged();
			notifyObservers(transition);
		}
		if (log.isDebugEnabled()) {
			log.debug(episode.printStateValues());
		}
	}

	private void updateEligibilities() {
		episode.getAllStates().forEach((s) -> s.put(ELIGIBILITY, (double) s.get(ELIGIBILITY) * lambda * gamma));
	}

	private void updateStateValues(double stepProfitability) {
		episode.getAllStates().forEach((s) -> s.setValue(s.getValue() + alpha * stepProfitability * (double) s.get(ELIGIBILITY)));
	}

	private double computeProfitability(Transition transition) {
		return alpha * (transition.getReward() + gamma * transition.getsPrime().getValue() - transition.getS().getValue());
	}

	private void incrementEligibility(State s) {
		s.put(ELIGIBILITY, (double) s.get(ELIGIBILITY) + 1);
	}

	private void initEligibilities() {
		episode.getAllStates().forEach((s) -> s.put(ELIGIBILITY, 0.0));
	}
}
