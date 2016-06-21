package prob.distribution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Data container for a distribution simulation
 * 
 * @author Cosmin Zamfir
 *
 */
public class  DiscreteDistributionResult<T> {

	/**Map outcomes to number of occurences*/
	private Map<T, Integer> outcomes;
	private List<DiscreteDistributionPoint<T>> points = new ArrayList<>();

	public void add(T res) {
		if(outcomes == null) {
			if(res instanceof Comparable) {
				outcomes =new TreeMap<T, Integer>();
			} else {
				outcomes = new LinkedHashMap<T, Integer>();
			}
		}
		if (outcomes.containsKey(res)) {
			outcomes.put(res, outcomes.get(res) + 1);
		} else {
			outcomes.put(res, 1);
		}
	}
	
	public void add(T value, double p) {
		points.add(new DiscreteDistributionPoint<T>(value, p));
	}
	
	public List<DiscreteDistributionPoint<T>> get() {
		int n = n();
		List<DiscreteDistributionPoint<T>> res = new ArrayList<>();
		for (T outcome : outcomes.keySet()) {
			res.add(new DiscreteDistributionPoint<T>(outcome, outcomes.get(outcome) / (double) n));
		}
		return res;
	}
	
	@SuppressWarnings("rawtypes")
	public double[][] asArray() {
		if(!points.isEmpty()) {
			List<DiscreteDistributionPoint> copy = new ArrayList<>(points);
			return DiscreteDistributionPoint.asArray(copy);
		}
		double[][] res = new double[outcomes.size()][2];
		int index = 0;
		int n = n();
		for (T value : outcomes.keySet()) {
			res[index][0] = (double) value;
			res[index++][1] = outcomes.get(value) / (double)n;
		}
		return res;
	}

	public int n() {
		int res = 0;
		for (Integer count : outcomes.values()) {
			res += count;
		}
		return res;
	}
	
	@Override
	public String toString() {
		if(!outcomes.isEmpty()) {
			return outcomes.toString();
		}
		return Arrays.asList(points).toString();
	}

}
