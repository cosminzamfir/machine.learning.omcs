package prob.distribution;

import prob.chart.DataChart;

/**
 * A {@link RayleighDensity} in 3 dimensions
 * <p>
 * The density of a random variable Z wich is the the sqrt of the squared sum of 3 random variables with normal density
 * @author eh2zamf
 *
 */
public class MaxwellDensity {

	private ContinuousDistributionResult result;
	private NormalDensity x;
	private NormalDensity y;
	private NormalDensity z;
	
	public MaxwellDensity(NormalDensity x, NormalDensity y, NormalDensity z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		result = new ContinuousDistributionResult();
	}

	public void simulate(int n) {
		for (int i = 0; i < n; i++) {
			result.add(Math.sqrt(Math.pow(x.simulateNext(),2)) + Math.pow(y.simulateNext(), 2) + Math.pow(z.simulateNext(), 2));
		}
	}


	public double compute(double x) {
		return 0;
	}

	public ContinuousDistributionResult getResult() {
		return result;
	}

	public static void main(String[] args) {
		MaxwellDensity dist = new MaxwellDensity(new NormalDensity(0, 1), new NormalDensity(0, 1), new NormalDensity(0, 1));
		dist.simulate(100000);
		new DataChart("Maxwell dist" , dist.getResult().asArray(50));
	}

}
