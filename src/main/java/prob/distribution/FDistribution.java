package prob.distribution;

import java.util.ArrayList;
import java.util.List;

import ml.stat.Anova;
import ml.stat.NumericDataPoint;
import prob.chart.DataChart;

public class FDistribution extends AbstractContinuousDistribution implements ContinousDistribution {

	private int numPoints;
	private int numGroups;
	
	public FDistribution(int numPoints, int numGroups) {
		super();
		this.numPoints = numPoints;
		this.numGroups = numGroups;
	}

	protected double simulateNext() {
		int size = numPoints/numGroups;
		List<List<NumericDataPoint>> groups = new ArrayList<List<NumericDataPoint>>();
		for (int i = 0; i < numGroups ; i++) {
			groups.add(NumericDataPoint.randGenerate(size, i, 1));
		}
		return new Anova(groups).F();
	}

	@Override
	public double compute(double x) {
		throw new UnsupportedOperationException();
	}

	public static void main(String[] args) {
		FDistribution dist = new FDistribution(1000, 5);
		dist.simulate(10000);
		new DataChart("F-Distribution", dist.getResult().asArray(300));
	}

	
}
