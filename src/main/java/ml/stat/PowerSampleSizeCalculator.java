package ml.stat;

import ml.model.function.Function;
import ml.model.function.NormalFunction;
import ml.model.function.Solver;

/**
 * Compute the minimum sample size which allows to reject the null hypothesis with a given probability
 * <p>
 * Assumptions:
 * <ul>
 * <li> The control and target group have the same size
 * <li> The control and target group have the same standard deviation
 * </ul> 
 * @author Cosmin Zamfir
 *
 */
public class PowerSampleSizeCalculator {

	/** The underlying measurement standard deviation */
	private double sd;

	/** The underlying difference between the target group mean and control group mean */
	private double diff;

	/** If the observed difference between the 2 groups means lies in this interval, H0 cannot be rejected */
	private Interval nullIntervalPercentiles;
	
	public PowerSampleSizeCalculator(double sd, double diff, Interval nullIntervalPercentiles) {
		super();
		this.sd = sd;
		this.diff = diff;
		this.nullIntervalPercentiles = nullIntervalPercentiles;
	}

	/**
	 * 
	 * @param p the required probability that the null hypothesis is rejected 
	 * @return the minimum sample size
	 */
	public int solve(double p) {
		Function f = new Function() {

			@Override
			public double evaluate(double se) {
				Interval nullInterval = new Interval(StatUtils.normalPercentileToValue(0, se, nullIntervalPercentiles.getMin()),
						StatUtils.normalPercentileToValue(0, se, nullIntervalPercentiles.getMax()));
				double res = 1 - new NormalFunction(diff, se).definiteIntegral(nullInterval.getMin(), nullInterval.getMax(), 0.001);
				System.out.println(res);
				return res;
			}
		};
		//the evaluation of the above function is the probability to reject the null hypothesis.Solve for 'se':
		double se = new Solver().solve(f, p, 0.001, Math.abs(diff/3));
		int N = (int) (2 * sd * sd / se / se);
		return N;
	}
	
	public static void main(String[] args) {
		int N = new PowerSampleSizeCalculator(12, -3, new Interval(0.025,1)).solve(0.8);
		System.out.println(N);
	}

}
