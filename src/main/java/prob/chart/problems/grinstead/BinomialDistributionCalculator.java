package prob.chart.problems.grinstead;

import java.text.NumberFormat;

import util.MLUtils;
public class BinomialDistributionCalculator {

	private static final NumberFormat nf = NumberFormat.getInstance();
	private int n;
	private double p;
	private double q;
	
	public BinomialDistributionCalculator(int n, double p) {
		super();
		this.n = n;
		this.p = p;
		this.q = 1 - p;
	}
	
	public void printOutcomes() {
		double cumulativeProbability = 0;
		for (int i = 0; i <=n ; i++) {
			double prob = MLUtils.choose(n,i)* Math.pow(p, i) * Math.pow(q, n-i);
			cumulativeProbability += prob;
			System.out.println(i + "  -  "  + (n-i) + "  -  " + nf.format(prob) + "  -  " + nf.format(cumulativeProbability));
		}
	}
	
	
	public static void main(String[] args) {
		new BinomialDistributionCalculator(10, 0.5).printOutcomes();
	}
	
	
}
