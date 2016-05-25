package prob.distribution;

import ml.model.function.AbstractFunction;
import prob.chart.DataChart;

/**
 * P(X=j) = q^j-1 * p
 * Can be simulated by the following random variable Y: choose a random double from 0 to 1
 * The value of Y is the smallest number j, such that 1-q^j > random.
 * That's random > 1 - q^j => random -1 > -q^j => 1-random < q^j => ln(1-random> < j*ln(q) => j > ln(1-random)/ln(q)
 * So compute j as the least integer bigger or equal with  ln(1-random)/ln(q)
 * @author eh2zamf
 *
 */
public class GeometricDistribution {

	private double p;
	private double q;
	private DiscreteDistributionResult result = new DiscreteDistributionResult();

	public GeometricDistribution(double p) {
		this.p = p;
		this.q = 1 - p;
	}

	public void simulate(int n) {
		for (int i = 0; i < n; i++) {
			double rand = Math.random();
			int j = (int) Math.ceil(Math.log(1 - rand) / Math.log(q));
			result.add(j);
		}
	}
	
	public void actualSimulate(int n) {
		for (int i = 0; i < n; i++) {
			int flips = 0;
			boolean success = false;
			while (!success) {
				double rand = Math.random();
				flips ++;
				if(rand <p) {
					success = true;
				}
			}
			result.add(flips);
		}
	}
	
	/**
	 * P(X=j) = q^j-1 * p
	 * @param j
	 * @return
	 */
	public double compute(int j) {
		return Math.pow(q, j-1) * p;
	}
	
	public DiscreteDistributionResult getResult() {
		return result;
	}
	
	public double getP() {
		return p;
	}

	public static void main(String[] args) {
		final GeometricDistribution dist = new GeometricDistribution(0.1);
		dist.actualSimulate(100000);
		new DataChart("Geom dist with p = " + dist.getP(), dist.getResult().asArray());
		
		AbstractFunction function = new AbstractFunction() {
			
			@Override
			public double evaluate(double x) {
				return dist.compute((int) x);
			}
		};
		function.plot(0, 100);
	}

}
