package prob.distribution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Data container for a distribution simulation
 * @author eh2zamf
 *
 */
public class DiscreteDistributionResult {

	/**Map outcomes to number of occurences*/
	private Map<Integer, Integer> outcomes = new TreeMap<Integer, Integer>();
	private List<DiscreteDistributionPoint> points = new ArrayList<>();

	public void add(Integer res) {
		if (outcomes.containsKey(res)) {
			outcomes.put(res, outcomes.get(res) + 1);
		} else {
			outcomes.put(res, 1);
		}
	}
	
	public void add(Integer value, double p) {
		points.add(new DiscreteDistributionPoint(value, p));
	}
	
	public List<DiscreteDistributionPoint> get() {
		int n = n();
		List<DiscreteDistributionPoint> res = new ArrayList<>();
		for (Integer outcome : outcomes.keySet()) {
			res.add(new DiscreteDistributionPoint(outcome, outcomes.get(outcome) / (double) n));
		}
		return res;
	}
	
	public double[][] asArray() {
		if(!points.isEmpty()) {
			return DiscreteDistributionPoint.asArray(points);
		}
		double[][] res = new double[outcomes.size()][2];
		int index = 0;
		int n = n();
		for (Integer value : outcomes.keySet()) {
			res[index][0] = value;
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
