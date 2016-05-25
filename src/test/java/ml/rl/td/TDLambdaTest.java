package ml.rl.td;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import ml.rl.builder.EpisodeBuilder;
import ml.rl.mdp.model.Episode;
import ml.rl.mdp.model.State;
import util.MLUtils;

public class TDLambdaTest {

	@Test
	public void test1() {
		double[] valueEstimates = new double[] {0.0,4.0,25.7,0.0,20.1,12.2,0.0};
		double[] rewards = new double[] {7.9,-5.1,2.5,-7.2,9.0,0.0,1.6};
		double probToState1 = 0.81;
		double lambda = run(probToState1, valueEstimates, rewards);
		Assert.assertTrue(MLUtils.equals(lambda, 0.623, 0.001));
	}
	
	
	public double run(double probToState1, double[] valueEstimates, double[] rewards) {
		double[] referenceValues = runTDLammbda(probToState1, valueEstimates, rewards, 1);
		MLUtils.printDoubleArray(referenceValues); 
		double lambda = 0;
		while(lambda <1) {
			double[] values = runTDLammbda(probToState1, valueEstimates, rewards, lambda);
			MLUtils.printDoubleArray(values);
			if(MLUtils.equals(referenceValues, values, 0.1)) {
				System.out.println("Found lambda: " + lambda);
				return lambda;
			}
			lambda+=0.001;
		}
		return 1;
	}

	public double[] runTDLammbda(double probToState1, double[] valueEstimates, double[] rewards, double lambda) {
		Map<String, List<Double>> stateValues = new LinkedHashMap();
		double gamma = 1;
		Episode episode;
		double[] rewards1 = new double[5];
		double[] rewards2 = new double[5];
		rewards1[0] = rewards[0];
		rewards1[1] = rewards[2];
		rewards1[2] = rewards[4];
		rewards1[3] = rewards[5];
		rewards1[4] = rewards[6];

		rewards2[0] = rewards[1];
		rewards2[1] = rewards[3];
		rewards2[2] = rewards[4];
		rewards2[3] = rewards[5];
		rewards2[4] = rewards[6];


		episode = EpisodeBuilder.instance().addRewards(rewards1).build();
		new TDLambdaEpisode(episode, lambda, gamma, 1).run();
		storeStateValues(episode, stateValues);

		episode = EpisodeBuilder.instance().addRewards(rewards2).build();
		new TDLambdaEpisode(episode, lambda, gamma, 1).run();
		storeStateValues(episode, stateValues);

		double[] res = new double[stateValues.size()];
		int i = 0;
		for (String key : stateValues.keySet()) {
			List<Double> vals = stateValues.get(key);
			double avg = vals.size() == 1 ? vals.get(0) : vals.get(0) * probToState1 + vals.get(1) * (1 - probToState1);
			res[i] = avg;
			i++;
		}
		return res;

	}

	private void storeStateValues(Episode episode, Map<String, List<Double>> stateValues) {
		for (State s : episode.getAllStates()) {
			if (stateValues.get(s.toString()) == null) {
				List<Double> l = new ArrayList<Double>();
				l.add(s.getValue());
				stateValues.put(s.toString(), l);
			} else {
				stateValues.get(s.toString()).add(s.getValue());
			}
		}
	}
}
