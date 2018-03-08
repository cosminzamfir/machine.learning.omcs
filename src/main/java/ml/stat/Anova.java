package ml.stat;

import java.util.ArrayList;
import java.util.List;

public class Anova {

	private List<NumericDataPoint> dataPoints = new ArrayList<>();
	private List<List<NumericDataPoint>> groups = new ArrayList<>();
	private double mean;
	private int n;
	private int k;

	public Anova() {
		super();
	}
	
	public Anova(List<List<NumericDataPoint>> groups) {
		super();
		this.groups = groups;
		groups.stream().map(l -> dataPoints.addAll(l));
		mean = mean(NumericDataPoint.values(dataPoints));
		n = dataPoints.size();
		k = groups.size();
	}


	private double mean(List<Double> l) {
		return l.stream().mapToDouble(a -> a).sum();
	}

	public double MSG() {
		return groups.stream().mapToDouble(l -> l.size()*Math.pow(mean(NumericDataPoint.values(l)) - mean, 2)).sum() / (k-1);
	}
	
	public double SST() {
		return dataPoints.stream().mapToDouble(point -> Math.pow(point.getTargetValue() - mean, 2)).sum();
	}
	
	public double SSE() {
		return MSG() - SST();
	}
	
	public double MSE() {
		return SSE() / (n-k);
	}
	
	public double F() {
		return MSG() / MSE();
	}
}
