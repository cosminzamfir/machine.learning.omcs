package ml.rl.td;

import java.util.Observable;

import ml.rl.mdp.model.Episode;
import ml.rl.mdp.model.State;
import ml.rl.mdp.model.Transition;

import org.apache.log4j.Logger;

/**
 * Compute the state values for one {@link Episode}
 * For reference only, is same as TDLambda(lambda=1) 
 */
public class TDOneEpisode extends Observable {

	private static final Logger log = Logger.getLogger(TDOneEpisode.class);
	
	private static final String ELIGIBILITY = "elig";
	
	private Episode episode;
	private double gamma;
	private double alpha;
	
	
	public TDOneEpisode(Episode episode, double gamma, double alpha) {
		super();
		this.episode = episode;
		this.gamma = gamma;
		this.alpha = alpha;
		initEligibilities();
	}

	public void run() {
		log.info("Running episode: " + episode + ";gamma:" + gamma + ";alpha:" + alpha);
		for (Transition transition : episode.getTransitions()) {
			incrementEligibility(transition.getS());
			double stepProfitability = computeProfitability(transition);
			log.info("Profitability for " + transition + ":" + stepProfitability);
			
			updateStateValues(stepProfitability);
			updateEligibilities();
			setChanged();
			notifyObservers(transition);
		}
		episode.printStateValues();
	}

	private void updateEligibilities() {
		episode.getAllStates().forEach((s) -> s.put(ELIGIBILITY,(double)s.get(ELIGIBILITY) * gamma));
	}

	private void updateStateValues(double stepProfitability) {
		episode.getAllStates().forEach((s) -> s.setValue(s.getValue() + alpha * stepProfitability * (double) s.get(ELIGIBILITY)));
	}

	private double computeProfitability(Transition transition) {
		return alpha * (transition.getReward() + gamma*transition.getsPrime().getValue() - transition.getS().getValue());
	}

	private void incrementEligibility(State s) {
		s.put(ELIGIBILITY, (double)s.get(ELIGIBILITY) + 1);
	}

	private void initEligibilities() {
		episode.getAllStates().forEach((s) -> s.put(ELIGIBILITY,0.0));
	}
}
