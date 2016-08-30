package prob.distribution;


import prob.chart.DataChart;
import util.MLUtils;

/**
 * The distribution of mean squared difference of the observed values over a number of categories, where the expected value is the maximum likelyhood of 
 * the hypergeometric distribution
 * Example: N items, k1 of Criterion1.category1 and N-k1 of Criterion1.category2. Given a Criterion2 with k2 in Criterion2.category1 and N-k2 in 
 * Criterion2.category2, and assumming the Criterion1 and Criterion2 are independent, the chiSquared is the mean squared difference of the distribution
 * of Criterion1.category1 and 2 in the subsets determined by Criterion2.
 * Initial impl assumes 2 Criteria, first with 2 categories, second with unlimited number of categories
 *  
 * @author eh2zamf
 *
 */
public class ChiSquaredDistribution implements ContinousDistribution {

	private int N;
	private int[] criterion1Splits = new int[2];
	private int[] criterion2Splits;
	private ContinuousDistributionResult result = new ContinuousDistributionResult();
	/**The expected number of items from Criterion1Category1 and 2 in the data sets split by Criterion2*/
	private int[][] expectedValues;

	public ChiSquaredDistribution(int N, int criterion1Category1Size, int criterion1Category2Size, int... criterion2Splits) {
		this.N = N;
		criterion1Splits[0] = criterion1Category1Size;
		criterion1Splits[1] = criterion1Category2Size;
		this.criterion2Splits = criterion2Splits;
		initializeExpectedValues();
	}

	/**
	 * Compute the expected numbers of Criterion1.category
	 */
	private void initializeExpectedValues() {
		expectedValues = new int[criterion2Splits.length][2];
		double crit1C1Perc = criterion1Splits[0] / (double) N;
		for (int i = 0; i < criterion2Splits.length; i++) {
			expectedValues[i][0] = (int) (criterion2Splits[i] * crit1C1Perc);
			expectedValues[i][1] = criterion2Splits[i] - expectedValues[i][0];
		}
	}

	public void simulate(int samples) {
		for (int i = 0; i < samples; i++) {
			double  d = simulate();
			result.add(d);
		}
	}

	/**
	 * @return sum[Criterion2.categories] (Observed - Expected)^2 / Expected for one simulation
	 */
	private double simulate() {
		double res = 0;
		for (int i = 0; i < criterion2Splits.length; i++) {
			int n = criterion2Splits[i];
			int k = criterion1Splits[0];
			HyperGeometricDistribution hd = new HyperGeometricDistribution(this.N, k, n);
			hd.simulate(1);
			int observedValueC11 = hd.getResult().get().get(0).getValue();
			int observedValueC12 = n - observedValueC11;
			int expectedValueC11 = expectedValues[i][0];
			int expectedValueC12 = expectedValues[i][1];
			res += Math.pow(observedValueC11 - expectedValueC11,2) / (double) expectedValueC11;
			res += Math.pow(observedValueC12 - expectedValueC12, 2) / (double) expectedValueC12;
		}
		return res;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		MLUtils.appendAligned(sb, "", 20, MLUtils.AlignType.LEFT);
		MLUtils.appendAligned(sb, "Criterion1.Cat1", 20, MLUtils.AlignType.LEFT);
		MLUtils.appendAligned(sb, "Criterion1.Cat2", 20, MLUtils.AlignType.LEFT);
		sb.append("\n");
		for (int i = 0; i < expectedValues.length; i++) {
			MLUtils.appendAligned(sb, "Criterion2.Cat" + i, 20, MLUtils.AlignType.LEFT);
			MLUtils.appendAligned(sb, expectedValues[i][0], 20, MLUtils.AlignType.LEFT);
			MLUtils.appendAligned(sb, expectedValues[i][0], 20, MLUtils.AlignType.LEFT);
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public ContinuousDistributionResult getResult() {
		return result;
	}
	
	@Override
	public double compute(double x) {
		throw new UnsupportedOperationException("TBI");
	}


	public static void main(String[] args) {
		ChiSquaredDistribution d = new ChiSquaredDistribution(100, 50, 50, 10, 20, 30, 40);
		d.simulate(1000000);
		new DataChart("ChiSquared", d.getResult().asArray(30));
	}

}
