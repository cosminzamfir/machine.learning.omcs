package prob.distribution;

import prob.chart.DataChart;

/**
 * 
 * P(X=x) = lambda * e^(-lambda*x)
 * @author eh2zamf
 *
 */
public class ExponentialDistribution extends AbstractContinuousDistribution implements ContinousDistribution {

	private double lambda;

	public ExponentialDistribution(double lambda) {
		super();
		this.lambda = lambda;
	}

	/**
	 * Simulates the next single random experiment
	 * <p>To get P(Y=y)=1-e^(-lambda*x), choose y = 1/lambda * ln(1/random()) =>
	 * <p>P(Y&lty) = P(1/lambda*ln(1/X) < y) 
	 * <p>= P(ln(1/X) &lt y*lambda) 
	 * <p>= P(1/X &lt e^y*lambda)
	 * <p>= P(X &gt e^(-y*lambda))
	 * <p>= 1 - e^(-y*lambda)
	 */
	public double simulateNext() {
		double rand = Math.random();
		double res = 1/lambda * Math.log(1/rand);
		return res;
		
	}
	
	/**
	 * P(X=x) = lambda * e^(-lambda*x)
	 * @param j
	 * @return
	 */
	public double compute(double x) {
		return lambda * Math.pow(Math.E, -lambda * x);
	}
	
	public double getLambda() {
		return lambda;
	}

	public static void main(String[] args) {
		ExponentialDistribution dist = new ExponentialDistribution(1);
		dist.simulate(100000);
		new DataChart("Exponential dist with lambda = " + dist.getLambda(), dist.getResult().asArray(100));
	}

}
