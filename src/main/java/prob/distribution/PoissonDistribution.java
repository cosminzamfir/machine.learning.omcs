package prob.distribution;

import java.math.BigDecimal;

import util.MLUtils;
import prob.chart.DataChart;

public class PoissonDistribution {

	private double lambda;
	private DiscreteDistributionResult<Integer> result = new DiscreteDistributionResult<Integer>();

	public PoissonDistribution(double lambda) {
		super();
		this.lambda = lambda;
	}
	
	public double getLambda() {
		return lambda;
	}
	
	public DiscreteDistributionResult<Integer> getResult() {
		return result;
	}

	/**
	 * P(X=j) = lambda^k * e^(-lambda) / k!
	 * @param k
	 * @return
	 */
	public double compute(int k) {
		return BigDecimal.valueOf(lambda).pow(k).multiply(BigDecimal.valueOf(Math.pow(Math.E, -lambda))).divide(MLUtils.factorialBD(k)).doubleValue();
	}
	
	public void compute() {
		for (int i = 0; i < lambda * 2; i++) {
			result.add(i, compute(i));
		}
	}
	
	/**
	 * Generate a Poisson distribution given an existing exponential distribution simulation - {@link ExponentialDensity#simulateNext()}
	 * <ul>
	 * <li> generate exponentialy random variables summing up to the unit time
	 * <li> the number of occurences(emmission) is the number of generations above
	 * <li> 
	 */
	public void simulate(int samples) {
		ExponentialDensity expDist = new ExponentialDensity(lambda);
		for (int i = 0; i < samples; i++) {
			int emmissionsInInterval = 0;
			double totalTime = 0;
			while((totalTime = totalTime + expDist.simulateNext()) < 1) {
				emmissionsInInterval ++;
			}
			result.add(emmissionsInInterval);
		}
	}

	public static void main(String[] args) {
		PoissonDistribution dist = new PoissonDistribution(100);
		dist.simulate(100000);
		new DataChart("Poisson with lambda=" + dist.getLambda(), dist.getResult().asArray());
	}

}
