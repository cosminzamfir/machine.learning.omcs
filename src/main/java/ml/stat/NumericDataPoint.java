package ml.stat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import util.MLUtils;

public class NumericDataPoint extends DataPoint<Double> {

	public NumericDataPoint(List<String> attrNames, List<Object> attrValues, Double targetValue) {
		super(attrNames, attrValues, targetValue);
	}
	
	public NumericDataPoint(Double targetValue) {
		super(null, null, targetValue);
	}
	
	public static List<Double> values(List<NumericDataPoint> points) {
		return points.stream().map(point -> point.getTargetValue()).collect(Collectors.toList());
	}
	
	public static List<NumericDataPoint> randGenerate(int n, double mean, double sd) {
		List<NumericDataPoint> res = new ArrayList<NumericDataPoint>();
		for (int i = 0; i < n; i++) {
			res.add(new NumericDataPoint((new Random().nextGaussian() + mean)*sd));
		}
		return res;
	}
	
	

}
