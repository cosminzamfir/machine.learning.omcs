package ml.rl.builder;

import java.util.ArrayList;
import java.util.List;

import ml.rl.mdp.model.Action;
import ml.rl.mdp.model.Episode;
import ml.rl.mdp.model.State;
import ml.rl.mdp.model.Transition;

public class EpisodeBuilder {

	private List<Transition> transitions = new ArrayList<>();

	public static EpisodeBuilder instance() {
		State.removeAll();
		return new EpisodeBuilder();
	}
	
	public static EpisodeBuilder defaultInstance(int numTransitions, double reward) {
		EpisodeBuilder builder = new EpisodeBuilder();
		for (int i = 0; i < numTransitions; i++) {
			builder.addTransition(i+1, i+2, reward);
		}
		return builder;
	}

	
	public EpisodeBuilder addTransition(int sId, int sPrimeId, double reward) {
		transitions.add(new Transition(Action.defaultAction(), State.instance(sId), State.instance(sPrimeId), reward));
		return this;
	}
	
	/**Shortcut method: values are: s1 reward s2 reward s3 ....*/
	public EpisodeBuilder addTransitions(int ... values) {
		for (int i = 0; i < values.length-2; i = i + 2) {
			addTransition(values[i], values[i+2], values[i+1]);
		}
		return this;
	}
	
	/**Shortcut method: values are: r1, r2. States are added by default ....*/
	public EpisodeBuilder addRewards(double ... rewards) {
		for (int i = 0; i < rewards.length; i++) {
			addTransition(i, i+1, rewards[i]);
		}
		return this;
	}


	public Episode build() {
		Episode res = new Episode();
		transitions.forEach((t) -> res.addTransition(t));
		return res;
	}
	
}
