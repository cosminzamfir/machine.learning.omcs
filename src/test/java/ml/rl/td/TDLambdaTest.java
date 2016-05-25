package ml.rl.td;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ml.rl.builder.EpisodeBuilder;
import ml.rl.mdp.model.Episode;
import ml.rl.mdp.model.State;

public class TDLambdaTest {

	public static void main(String[] args) {
		test1();
	}
	
	public static void test1() {
		Map<String, List<Double>> stateValues = new LinkedHashMap();
		double lambda = 0.623;
		double gamma = 1;
		double probToState1 = 0.81;
		
		Episode episode = EpisodeBuilder.instance().addRewards(7.9,2.5,9.0,0.0,1.6).build();
		new TDLambdaEpisode(episode, lambda, gamma, 1).run();
		episode.printStateValues();
		for (State s : episode.getAllStates()) {
			if(stateValues.get(s.toString()) == null) {
				List<Double> l = new ArrayList<Double>();
				l.add(s.getValue());
				stateValues.put(s.toString(), l);
			} else {
				stateValues.get(s.toString()).add(s.getValue());
			}
		}

		episode = EpisodeBuilder.instance().addRewards(-5.1,-7.2,9.0,0.0,1.6).build();
		new TDLambdaEpisode(episode, lambda, gamma, 1).run();
		episode.printStateValues();
		for (State s : episode.getAllStates()) {
			if(stateValues.get(s.toString()) == null) {
				List<Double> l = new ArrayList<Double>();
				l.add(s.getValue());
				stateValues.put(s.toString(), l);
			} else {
				stateValues.get(s.toString()).add(s.getValue());
			}
		}
		
		for (String s : stateValues.keySet()) {
			double avg = stateValues.get(s).stream().mapToDouble(Double::doubleValue).average().getAsDouble();
			System.out.println(s + ":" + avg);
		}

	}
}
