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

public class HW2 {
	public double epsilon = 0.005;
	public double lambdaStep =0.001;
	@Test
	public void test1() {
		double[] valueEstimates = new double[] { 0.0, 4.0, 25.7, 0.0, 20.1, 12.2, 0.0 };
		double[] rewards = new double[] { 7.9, -5.1, 2.5, -7.2, 9.0, 0.0, 1.6 };
		double probToState1 = 0.81;
		double lambda = run(probToState1, valueEstimates, rewards);
		Assert.assertTrue(MLUtils.equals(lambda, 0.623, 0.001));
	}

	@Test
	public void test2() {
		double[] valueEstimates = new double[] { 0.0, -5.2, 0.0, 25.4, 10.6, 9.2, 12.3 };
		double[] rewards = new double[] { -2.4, 0.8, 4.0, 2.5, 8.6, -6.4, 6.1 };
		double probToState1 = 0.22;
		double lambda = run(probToState1, valueEstimates, rewards);
		Assert.assertTrue(MLUtils.equals(lambda, 0.496, 0.001));
	}

	@Test
	public void test3() {
		double[] valueEstimates = new double[] { 0.0, 4.9, 7.8, -2.3, 25.5, -10.2, -6.5 };
		double[] rewards = new double[] { -2.4, 9.6, -7.8, 0.1, 3.4, -2.1, 7.9 };
		double probToState1 = 0.64;
		double lambda = run(probToState1, valueEstimates, rewards);
		Assert.assertTrue(MLUtils.equals(lambda, 0.206, 0.005));
	}
	
	@Test
	public void test4() {
		double[] valueEstimates = new double[] {0.0,12.4,0.0,18.8,4.3,0.4,8.1};
		double[] rewards = new double[] {-4.5,-2.8,8.1,7.8,3.0,0.0,0.3};
		double probToState1 = 0.11;
		double lambda = run(probToState1, valueEstimates, rewards);
	}
	
	@Test
	public void test5() {
		double[] valueEstimates = new double[] {0.0,4.2,-3.9,0.0,18.1,20.3,13.0};
		double[] rewards = new double[] {7.7,-4.3,4.4,-2.7,-0.5,0.2,7.0} ;
		double probToState1 = 0.79;
		double lambda = run(probToState1, valueEstimates, rewards);
	}
	
	@Test
	public void test6() {
		double[] valueEstimates = new double[] {0.0,9.3,0.0,12.8,0.0,22.4,3.6};
		double[] rewards = new double[] {9.0,0.0,0.4,8.9,6.7,0.6,1.9} ;
		double probToState1 = 0.76;
		double lambda = run(probToState1, valueEstimates, rewards);
	}
	
	@Test
	public void test7() {
		double[] valueEstimates = new double[] {0.0,0.0,0.0,18.8,0.0,21.1,-2.7};
		double[] rewards = new double[] {-1.8,5.1,0.6,8.5,1.4,0.6,3.4} ;
		double probToState1 = 0.77;
		double lambda = run(probToState1, valueEstimates, rewards);
	}
	
	
	@Test
	public void test8() {
		double[] valueEstimates = new double[] {0.0,-3.4,6.3,10.7,0.0,5.0,11.2};
		double[] rewards = new double[] {-2.1,7.9,-4.4,4.8,-1.9,0.0,5.4} ;
		double probToState1 = 0.2;
		double lambda = run(probToState1, valueEstimates, rewards);
	}
	

	public double run(double probToState1, double[] valueEstimates, double[] rewards) {
		double[] referenceValues = runTDLammbda(probToState1, null, rewards, 1);
		MLUtils.printDoubleArray(referenceValues);
		double lambda = 0;
		while (lambda < 1) {
			double[] values = runTDLammbda(probToState1, valueEstimates, rewards, lambda);
			MLUtils.printDoubleArray(values);
			if (MLUtils.equals(referenceValues[0], values[0], epsilon)) {
				System.out.println("Found lambda: " + lambda);
				return lambda;
			}
			lambda += lambdaStep;
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

		double[] estimates1 = new double[6];
		double[] estimates2 = new double[6];
		if (valueEstimates != null) {
			estimates1[0] = valueEstimates[0];
			estimates1[1] = valueEstimates[1];
			estimates1[2] = valueEstimates[3];
			estimates1[3] = valueEstimates[4];
			estimates1[4] = valueEstimates[5];
			estimates1[5] = valueEstimates[6];

			estimates2[0] = valueEstimates[0];
			estimates2[1] = valueEstimates[2];
			estimates2[2] = valueEstimates[3];
			estimates2[3] = valueEstimates[4];
			estimates2[4] = valueEstimates[5];
			estimates2[5] = valueEstimates[6];
		}


		episode = EpisodeBuilder.instance().addRewards(rewards1).build();
		if (valueEstimates != null) {
			setStateValueEstimates(episode, estimates1);
		}
		new TDLambdaEpisode(episode, lambda, gamma, 1).run();
		storeStateValues(episode, stateValues);

		episode = EpisodeBuilder.instance().addRewards(rewards2).build();
		if (valueEstimates != null) {
			setStateValueEstimates(episode, estimates2);
		}
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

	private void setStateValueEstimates(Episode episode, double[] estimates) {
		List<State> states = episode.getAllStates();
		for (int i = 0; i < estimates.length; i++) {
			states.get(i).setValue(estimates[i]);
		}
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
