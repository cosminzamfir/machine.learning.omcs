package ml.rl.td;

import ml.rl.mdp.model.Episode;
import ml.rl.mdp.model.Transition;

/**
 * 
 * @author Cosmin Zamfir
 *
 */
public class KEstimator {

	
	/** K-Estimate the value of the first state of the episode */
	public double estimate(Episode episode, int k,double gamma) {
		double res = 0;
		for (int i = 0; i < k; i++) {
			if(i > episode.getTransitions().size() -1) {
				return res;
			}
			Transition transition = episode.getTransition(i);
			res += Math.pow(gamma,i+1) * transition.getReward();
		}
		res += episode.getStateValueAt(k)*Math.pow(gamma, k) - episode.getStateValueAt(0);
		return res;
	}
}
