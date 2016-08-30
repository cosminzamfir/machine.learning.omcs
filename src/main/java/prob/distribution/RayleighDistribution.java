package prob.distribution;

import prob.chart.DataChart;

/**
 * The density of a random variable Z wich is the the sqrt of the squared sum of 2 random variables with normal density
 * @author eh2zamf
 *
 */
public class RayleighDistribution implements ContinousDistribution {

	private ContinuousDistributionResult result;
	private NormalDistribution x;
	private NormalDistribution y;
	
	public RayleighDistribution(NormalDistribution x, NormalDistribution y) {
		super();
		this.x = x;
		this.y = y;
		result = new ContinuousDistributionResult();
	}

	public void simulate(int n) {
		for (int i = 0; i < n; i++) {
			result.add(Math.sqrt(Math.pow(x.simulateNext(),2)) + Math.pow(y.simulateNext(), 2));
		}
	}


	public double compute(double x) {
		return 0;
	}

	public ContinuousDistributionResult getResult() {
		return result;
	}

	public static void main(String[] args) {
		RayleighDistribution dist = new RayleighDistribution(new NormalDistribution(0, 1), new NormalDistribution(0, 1));
		dist.simulate(1000000);
		new DataChart("Rayleigh dist" , dist.getResult().asArray(100));
	}

}
