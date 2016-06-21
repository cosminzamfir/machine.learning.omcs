package ml.ul;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Map each instance in the sample space to its probability
 * @author eh2zamf
 *
 */
public class DiscreteProbabilityDistribution {

	private Map<Object, Double> probabilitites = new LinkedHashMap<Object, Double>();
	private List<Object> observations = new ArrayList<Object>();
	
	public double p(Object instance) {
		Double res = getProbabilities().get(instance);
		return res == null ? 0 : res;
	}
	
	public void addInstance(Object instance, double probability) {
		probabilitites.put(instance, probability);
	}
	
	public void addObservation(Object observation) {
		this.observations.add(observation);
	}
	
	public void computeProbabilities() {
		for (Object observation : observations) {
			if(probabilitites.containsKey(observation)) {
				probabilitites.put(observation, probabilitites.get(observation) + 1);
			} else {
				probabilitites.put(observation, 1.0);
			}
		}
		for (Object observation : probabilitites.keySet()) {
			probabilitites.put(observation, probabilitites.get(observation)/observations.size());
		}
	}
	
	public Map<Object, Double> getProbabilities() {
		if(probabilitites.isEmpty()) {
			computeProbabilities();
		}
		return probabilitites;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Object  instance : getProbabilities().keySet()) {
			sb.append(instance + ":" + p(instance)).append("\n");
		}
		return sb.toString();
	}
	
}
