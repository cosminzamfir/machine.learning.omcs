package ml.ul;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Map each instance in the sample space to its probability
 * @author eh2zamf
 *
 */
public class DiscreteProbabilityDistribution {

	private Map<Object, Double> data = new LinkedHashMap<Object, Double>();
	
	public double p(Object instance) {
		Double res = data.get(instance);
		return res == null ? 0 : res;
	}
	
	public void addInstance(Object instance, double probability) {
		data.put(instance, probability);
	}
	
	public Map<Object, Double> getData() {
		return data;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Object  instance : data.keySet()) {
			sb.append(instance + ":" + p(instance)).append("\n");
		}
		return sb.toString();
	}
	
}
