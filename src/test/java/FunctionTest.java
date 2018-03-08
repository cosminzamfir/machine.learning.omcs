import prob.chart.DataChart;
import prob.distribution.AbstractContinuousDistribution;
import prob.distribution.ContinousDistribution;
import prob.distribution.ContinuousDistributionResult;
import prob.distribution.NormalDistribution;

public class FunctionTest {

	public static void main(String[] args) {
		NormalDistribution d1 = new NormalDistribution(0, 2);
		NormalDistribution d2 = new NormalDistribution(0, 2);
		ContinousDistribution diff = new AbstractContinuousDistribution() {
			
			@Override
			public double compute(double x) {
				return 0;
			}
			
			@Override
			protected double simulateNext() {
				return d1.simulateNext() - d2.simulateNext();
			}
		};
		
		diff.simulate(1000000);
		ContinuousDistributionResult res = diff.getResult();
		new DataChart("Diff", res.asArray(1000));
	}
}
