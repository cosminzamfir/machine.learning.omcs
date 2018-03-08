package prob.distribution;

import prob.chart.DataChart;

/**
 * A {@link RayleighDistribution} in 3 dimensions
 * <p>
 * The density of a random variable Z wich is the the sqrt of the squared sum of 3 random variables with normal density
 * @author eh2zamf
 *
 */
public class MaxwellDistribution extends AbstractContinuousDistribution implements ContinousDistribution {

	private NormalDistribution x;
	private NormalDistribution y;
	private NormalDistribution z;

	public MaxwellDistribution(NormalDistribution x, NormalDistribution y, NormalDistribution z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	protected double simulateNext() {
		return Math.sqrt(Math.pow(x.simulateNext(), 2)) + Math.pow(y.simulateNext(), 2) + Math.pow(z.simulateNext(), 2);
	}

	public double compute(double x) {
		return 0;
	}

	public static void main(String[] args) {
		MaxwellDistribution dist = new MaxwellDistribution(new NormalDistribution(0, 1), new NormalDistribution(0, 1), new NormalDistribution(0, 1));
		dist.simulate(100000);
		new DataChart("Maxwell dist", dist.getResult().asArray(50));
	}

}
