package prob.distribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data container for a continuous distribution simulation
 * @author eh2zamf
 *
 */
public class ContinuousDistributionResult {

	/**
	 * The array of real numbers representing the simulation  
	 */
	private List<Double> outcomes = new ArrayList<Double>();

	public void add(double res) {
		outcomes.add(res);
	}

	/**
	 * Return the probability density associated with the {@link #outcomes} List.
	 * <p>
	 * <ol>
	 * <li>Divide the interval min-max in n subintervals (steps)
	 * <li>Compute for each subinterval the proportion of the outcomes falling in that interval - this is the probability density corresponding to the sub-interval
	 * <li>Add the pair: midInterval-proportion to the result
	 * @return
	 */
	public double[][] asArray(int intervals) {
		double[][] res = new double[intervals][2];
		Collections.sort(outcomes);
		double min = outcomes.get(0);
		double max = outcomes.get(outcomes.size()-1);
		double stepSize =  (max-min) / intervals;
		double currentMin = min;
		double currentMax = currentMin + stepSize;
		double currentCount = 0;
		int currentIndex = 0;
		for (Double outcome : outcomes) {
			if(outcome > currentMax) {
				res[currentIndex][0] = (currentMin + currentMax)/2;
				res[currentIndex++][1] = currentCount/(double)outcomes.size();
				currentMin = currentMax;
				currentMax = currentMin + stepSize;
				currentCount = 0;
			}
			currentCount ++;
		}
		return res;
	}

	public int n() {
		return outcomes.size();
	}

	@Override
	public String toString() {
		return outcomes.toString();
	}

}
