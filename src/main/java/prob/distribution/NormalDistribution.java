package prob.distribution;

import ml.model.function.NormalFunction;
import prob.chart.DataChart;

/**
 * Normal distribution with parameters miu and sigma, where miu is the center of the density and sigma is a measure of the spread of the distribution
 * <p>
 * The density function is: 1/sqrt(2*PI*sigma) * e^(-(x-miu)^2) / 2 * sigma^2
 * <p>
 * The inverse of the normal function cannot be writen in  closed form => the normal algo to use f-1(x) to simulate the distribution cannot be used.
 * <p>
 * It can be however simulated by the following fact: if U and V are random variables with uniform densities on [0..1], the the random variables:
 * X = sqrt(-2*logU)* cos(2*PI*V) and X = sqrt(-2*logU)* sing (2*PI*V) are independent and have the normal distribution.
 * <p>
 * A normal Z random variable with parameters miu = 0 and sigma = 1 is a 'standard' normal variable
 * <p>
 * A random variable defined as X = sigma*Z + miu has a normal distribution with parameters sigma and miu
 * @author eh2zamf
 *
 */
public class NormalDistribution implements ContinousDistribution {

	private ContinuousDistributionResult result;
	private NormalFunction underlyingFunction;
	private double miu;
	private double sigma;

	public NormalDistribution(double miu, double sigma) {
		super();
		this.miu = miu;
		this.sigma = sigma;
		this.underlyingFunction = new NormalFunction(this.miu, this.sigma);
		this.result = new ContinuousDistributionResult();
	}

	/**
	* The inverse of the normal function cannot be writen in  closed form => the normal algo to use f-1(x) to simulate the distribution cannot be used.
	* <p>
	* It can be however simulated by the following fact: if U and V are random variables with uniform densities on [0..1], the the random variables:
	* X = sqrt(-2*logU)* cos(2*PI*V) and X = sqrt(-2*logU)* sing (2*PI*V) are independent and have the normal distribution.
	 */
	public void simulate(int n) {
		for (int i = 0; i < n; i++) {
			result.add(simulateNext());
		}
	}

	/**
	* The inverse of the normal function cannot be writen in  closed form => the normal algo to use f-1(x) to simulate the distribution cannot be used.
	* <p>
	* It can be however simulated by the following fact: if U and V are random variables with uniform densities on [0..1], the the random variables:
	* X = sqrt(-2*logU)* cos(2*PI*V) and X = sqrt(-2*logU)* sing (2*PI*V) are independent and have the normal distribution.
	 */
	public double simulateNext() {
		double u = Math.random();
		double v = Math.random();
		double d = Math.sqrt(-2 * Math.log(u)) * Math.sin(2 * Math.PI * v);
		return d * sigma + miu;
	}

	public double compute(double x) {
		return underlyingFunction.evaluate(x);
	}

	public ContinuousDistributionResult getResult() {
		return result;
	}

	public static void main(String[] args) {
		NormalDistribution dist = new NormalDistribution(2, 5);
		dist.simulate(1000000);
		new DataChart("Normal dist" , dist.getResult().asArray(50));
	}

}
