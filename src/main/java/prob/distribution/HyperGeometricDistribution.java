package prob.distribution;

import java.math.BigDecimal;
import java.math.RoundingMode;

import util.MLUtils;
import prob.chart.DataChart;

/**
 * - Start with N balls, wither white or black
 * - k of the balls are white, N-k are black
 * - we choose n balls at random, without replacement
 * - denote X the random variable denoting the number of white balls
 * - What is P(X=x): 
 * 		- there are (N,n) possible combinations
 * 		- there are (k,x) possible combinations of having x white balls out of the k ones
 * 		- there are (N-k,n-x) possible combinations for the n-x black balls
 *    => P(X=x) = (N-k,n-x) * (k,x) / (N,n)
 * 
 * @author eh2zamf
 *
 */
public class HyperGeometricDistribution implements DiscreteDistribution {

	private int N;
	private int k;
	private int smallN;
	private DiscreteDistributionResult<Integer> result = new DiscreteDistributionResult<Integer>();

	public HyperGeometricDistribution(int N, int k, int n) {
		this.N = N;
		this.k = k;
		this.smallN = n;
	}

	/**
	 * Start with N balls, k of which are white, N-k black
	 * Get a random number; if it is less than k/N add 1 to the white count; decrement N, decrement k; otherwise, add 1 to the black count; decrement N
	 * @param samples
	 */
	public void simulate(int samples) {
		for (int i = 0; i < samples; i++) {
			int tmpN = N;
			int tmpK = k;
			int whiteCount = 0;
			for (int j = 0; j < smallN; j++) {
				double r = tmpK / (double) tmpN;
				double rnd = Math.random();
				if (rnd < r) {
					whiteCount++;
					tmpN--;
					tmpK--;
				} else {
					tmpN--;
				}
			}
			result.add(whiteCount);
		}
	}
	
	/**
	 * Compute the probability of having exactly x white balls (sucessess etc)
	 * This is: (k,x)*(N-k,smallN-x) / (N,smallN)
	 * @param x
	 * @return
	 */
	public BigDecimal compute(int x) {
		BigDecimal res = MLUtils.chooseBD(k, x).multiply(MLUtils.chooseBD(N-k, smallN-x)).divide(MLUtils.chooseBD(N, smallN),10,RoundingMode.HALF_UP);
		return res;
	
	}

	public DiscreteDistributionResult<Integer> getResult() {
		return result;
	}
	
	public int getN() {
		return N;
	}
	
	public int getK() {
		return k;
	}
	
	public int getSmallN() {
		return smallN;
	}
	
	


	public static void main(String[] args) {
		HyperGeometricDistribution dist = new HyperGeometricDistribution(1000,500,500);
		dist.simulate(300000);
		new DataChart("HyperGeometric dist with N,k,n = " + dist.getN() + "," + dist.getK() + "," + dist.getSmallN(), dist.getResult().asArray());
	}

}
