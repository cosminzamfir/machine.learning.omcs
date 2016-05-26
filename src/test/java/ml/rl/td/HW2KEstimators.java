package ml.rl.td;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import ml.model.function.Function;
import ml.model.function.Solver;
import ml.rl.builder.EpisodeBuilder;
import ml.rl.mdp.model.Episode;
import util.MLUtils;

public class HW2KEstimators {

	private static final Logger log = Logger.getLogger(HW2KEstimators.class);
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
		run(probToState1, valueEstimates, rewards);
	}
	
	@Test
	public void test5() {
		double[] valueEstimates = new double[] {0.0,4.2,-3.9,0.0,18.1,20.3,13.0};
		double[] rewards = new double[] {7.7,-4.3,4.4,-2.7,-0.5,0.2,7.0} ;
		double probToState1 = 0.79;
		run(probToState1, valueEstimates, rewards);
	}
	
	@Test
	public void test6() {
		double[] valueEstimates = new double[] {0.0,9.3,0.0,12.8,0.0,22.4,3.6};
		double[] rewards = new double[] {9.0,0.0,0.4,8.9,6.7,0.6,1.9} ;
		double probToState1 = 0.76;
		run(probToState1, valueEstimates, rewards);
	}
	
	@Test
	public void test7() {
		double[] valueEstimates = new double[] {0.0,0.0,0.0,18.8,0.0,21.1,-2.7};
		double[] rewards = new double[] {-1.8,5.1,0.6,8.5,1.4,0.6,3.4} ;
		double probToState1 = 0.77;
		run(probToState1, valueEstimates, rewards);
	}
	
	
	@Test
	public void test8() {
		double[] valueEstimates = new double[] {0.0,-3.4,6.3,10.7,0.0,5.0,11.2};
		double[] rewards = new double[] {-2.1,7.9,-4.4,4.8,-1.9,0.0,5.4} ;
		double probToState1 = 0.2;
		run(probToState1, valueEstimates, rewards);
	}



	public double run(double probToState1, double[] valueEstimates, double[] rewards) {
		double tdOneValue = computeTdOneValue(probToState1, null, rewards);
		double[] ks = computeKEstimatorsValues(probToState1, valueEstimates, rewards);
		//Function f = ((x) -> (1-x) * ks[0] + x*(1-x) * ks[1] + x*x*(1-x) * ks[2] + x*x*x*(1-x) * ks[3] + (1- (1-x) - x*(1-x) - x*x*(1-x) - x*x*x*(1-x)) * ks[4]);
		double ret = computeReturn(probToState1, valueEstimates, rewards);
		Function f = ((x) -> (1-x) * ks[0] + 
						x*(1-x) * ks[1] + 
						x*x*(1-x) * ks[2] + 
						x*x*x*(1-x) * ks[3] + 
						x*x*x*x*(1-x) * ks[4] +
						Math.pow(x, 5) * ret);
						//(1- (1-x) - x*(1-x) - x*x*(1-x) - x*x*x*(1-x) - x*x*x*x*(1-x)*ret));
		double lambda = new Solver().solve(f, tdOneValue, 0.00001);
		System.out.println("Found lambda: " + lambda);
		Assert.assertTrue(Math.abs(tdOneValue) - f.evaluate(lambda) < 0.0001);
		return lambda;
	}


	private double computeReturn(double probToState1, double[] valueEstimates, double[] rewards) {
		Episode e1 = getEpisodeOne(valueEstimates, rewards);
		double res = e1.getTotalReward() * probToState1;
		Episode e2 = getEpisodeTwo(valueEstimates, rewards);
		res += e2.getTotalReward() * (1-probToState1);
		return res;
	}

	private double[] computeKEstimatorsValues(double probToState1, double[] valueEstimates, double[] rewards) {
		double[] res = new double[5];
		for (int k = 1; k <= 5; k++) {
			Episode e1 = getEpisodeOne(valueEstimates, rewards);
			log.debug("Computing " + k + "-Estimator for States:" + e1.printStateValues() + ";Rewards:" + e1.printRewards());
			double kEst1 = new KEstimator().estimate(e1, k, 1); 
			log.debug(kEst1);
			res[k-1] += kEst1*probToState1;
			
			Episode e2 = getEpisodeTwo(valueEstimates, rewards);
			log.debug("Computing " + k + "-Estimator for States:" + e2.printStateValues() + ";Rewards:" + e2.printRewards());
			double kEst2 = new KEstimator().estimate(e2, k, 1); 
			res[k-1] +=  kEst2 * (1-probToState1);
			log.debug(kEst2);
		}
		return res;
	}


	private double computeTdOneValue(double probToState1, Object object, double[] rewards) {
		double res = 0;
		Episode e1 = getEpisodeOne(null, rewards);
		new TDLambdaEpisode(e1, 1, 1, 1).run();
		res += e1.getStateValueAt(0) * probToState1;
		
		Episode e2 = getEpisodeTwo(null, rewards);
		new TDLambdaEpisode(e2, 1, 1, 1).run();
		res += e2.getStateValueAt(0)*(1-probToState1);
		return res;
	}
	
	private Episode getEpisodeOne(double[] valueEstimates, double[] rewards) {
		double[] epRewards = new double[5];
		epRewards[0] = rewards[0];
		epRewards[1] = rewards[2];
		epRewards[2] = rewards[4];
		epRewards[3] = rewards[5];
		epRewards[4] = rewards[6];

		double[] epEstimates = null;
		if (valueEstimates != null) {
			epEstimates = new double[6];
			epEstimates[0] = valueEstimates[0];
			epEstimates[1] = valueEstimates[1];
			epEstimates[2] = valueEstimates[3];
			epEstimates[3] = valueEstimates[4];
			epEstimates[4] = valueEstimates[5];
			epEstimates[5] = valueEstimates[6];
		}

		Episode episode = EpisodeBuilder.instance().addRewards(epRewards).build();
		if (epEstimates != null) {
			setStateValueEstimates(episode, epEstimates);
		}
		return episode;

	}

	private Episode getEpisodeTwo(double[] valueEstimates, double[] rewards) {
		double[] epRewards = new double[5];
		epRewards[0] = rewards[1];
		epRewards[1] = rewards[3];
		epRewards[2] = rewards[4];
		epRewards[3] = rewards[5];
		epRewards[4] = rewards[6];

		double[] epEstimates = null;
		if (valueEstimates != null) {
			epEstimates = new double[6];
			epEstimates[0] = valueEstimates[0];
			epEstimates[1] = valueEstimates[2];
			epEstimates[2] = valueEstimates[3];
			epEstimates[3] = valueEstimates[4];
			epEstimates[4] = valueEstimates[5];
			epEstimates[5] = valueEstimates[6];
		}

		Episode episode = EpisodeBuilder.instance().addRewards(epRewards).build();
		if (epEstimates != null) {
			setStateValueEstimates(episode, epEstimates);
		}
		return episode;
	}

	private void setStateValueEstimates(Episode episode, double[] estimates) {
		AtomicInteger index = new AtomicInteger(0);
		episode.getAllStates().forEach((state) -> state.setValue(estimates[index.getAndIncrement()]));
	}

}
