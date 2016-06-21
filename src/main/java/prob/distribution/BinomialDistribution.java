package prob.distribution;

import prob.chart.DataChart;

/**
 * Binomial distribution with success probability p and n trials 
 * @author eh2zamf
 *
 */
public class BinomialDistribution {

	private double p;
	private int n;
	private DiscreteDistributionResult<Integer> result = new DiscreteDistributionResult<Integer>();

	public BinomialDistribution(double p, int n) {
		this.p = p;
		this.n = n;
	}

	public void simulate(int samples) {
		for (int i = 0; i < samples; i++) {
			int res = 0;
			for (int j = 0; j < n; j++) {
				double rand = Math.random();
				if(rand < p) {
					res ++;
				}
			}
			result.add(res);
		}
	}
	
	public DiscreteDistributionResult<Integer> getResult() {
		return result;
	}
	
	public double getP() {
		return p;
	}

	public static void main(String[] args) {
		BinomialDistribution dist = new BinomialDistribution(0.5,100);
		dist.simulate(10000000);
		new DataChart("Binomial dist with p = " + dist.getP(), dist.getResult().asArray());
	}

}
