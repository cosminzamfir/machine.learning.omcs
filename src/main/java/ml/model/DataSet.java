package ml.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jfree.data.general.Dataset;

import util.MLUtils;

/**
 * A collection of {@link Observation}s
 * @author Cosmin Zamfir
 *
 */
public class DataSet {

	private static final Logger log = Logger.getLogger(Dataset.class);

	private List<Observation> data;
	private List<Attribute> attributes;
	private Attribute targetAttribute;
	/** Map each {@link #targetAttribute} value to the list of Observations having this targetAttribute value */
	private Map<Object, List<Observation>> categoriesMapping;

	public DataSet(List<Attribute> attributes, List<Observation> observations, Attribute targetAttribute) {
		super();
		this.data = observations;
		this.attributes = attributes;
		this.targetAttribute = targetAttribute;
		buildTargetAttributes();
		setAttributesPossibleValues();
		setTargetAttributePossibleValues();
	}

	private void setTargetAttributePossibleValues() {
		for (Observation instance : data) {
			targetAttribute.addPossibleValue(instance.getTargetAttributeValue());
		}
	}

	private void setAttributesPossibleValues() {
		for (Attribute attribute : attributes) {
			for (Observation instance : data) {
				attribute.addPossibleValue(instance.getValue(attribute));
			}
		}
	}

	private void buildTargetAttributes() {
		categoriesMapping = new HashMap<Object, List<Observation>>();
		for (Observation observatation : data) {
			Object targetAttributeValue = observatation.getTargetAttributeValue();
			List<Observation> l = categoriesMapping.get(targetAttributeValue);
			if (l == null) {
				l = new ArrayList<>();
			}
			l.add(observatation);
			categoriesMapping.put(targetAttributeValue, l);
		}
	}

	public Attribute getTargetAttribute() {
		return targetAttribute;
	}
	
	public List<Observation> getObservations() {
		return data;
	}

	public void setObservations(List<Observation> observations) {
		this.data = observations;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	/** @return the percentage of Observations having the given targetAttributeValue */
	public double probability(Object targetAttributeValue) {
		int res = 0;
		for (Observation observation : data) {
			if (observation.getTargetAttributeValue().equals(targetAttributeValue)) {
				res++;
			}
		}
		return res / (double) data.size();
	}

	/** @return the probabilty of having the specified targetAttributeValue, given that it has the specified value for the specified Attribute */
	public double conditionalProbability(Attribute attribute, Object value, Object targetAttributeValue) {
		List<Observation> candidates = categoriesMapping.get(targetAttributeValue);
		int res = 0;
		for (Observation example : candidates) {
			if (example.getValue(attribute).equals(value)) {
				res++;
			}
		}
		return res / (double) candidates.size();
	}

	public Set<Object> getCategories() {
		return categoriesMapping.keySet();
	}

	public Map<Object, List<Observation>> getCategoriesMapping() {
		return categoriesMapping;
	}

	/** Split this instance on the values of the given attribute */
	public Map<Object, DataSet> split(Attribute attribute) {
		Map<Object, DataSet> res = new LinkedHashMap<>();
		for (Observation instance : data) {
			Object value = instance.getValue(attribute);
			DataSet dataSet = res.get(value);
			if (dataSet == null) {
				dataSet = new DataSet(attributes, new ArrayList<Observation>(), targetAttribute);
				res.put(value, dataSet);
			}
			dataSet.add(instance);
		}
		for (DataSet dataSet : res.values()) {
			dataSet.buildTargetAttributes();
		}
		return res;
	}

	private void add(Observation instance) {
		data.add(instance);
		for (Attribute attribute : attributes) {
			attribute.addPossibleValue(instance.getValue(attribute));
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Observation example : data) {
			sb.append(example).append("\n");
		}
		return sb.toString();
	}
	
	public String sumary() {
		StringBuilder sb = new StringBuilder("DataSet");
		for (Object categoryValue : categoriesMapping.keySet()) {
			sb.append("[" + categoryValue + ":" + categoriesMapping.get(categoryValue).size() + "]"); 
		}
		return sb.toString();
	}

	public Object mostLikelyOutput() {
		int maxInstances = 0;
		Object res = null;
		for (Object value : categoriesMapping.keySet()) {
			if (categoriesMapping.get(value).size() > maxInstances) {
				maxInstances = categoriesMapping.get(value).size();
				res = value;
			}
		}
		return res;
	}

	public boolean isEmpty() {
		return data.isEmpty();
	}

	public int size() {
		return data.size();
	}

	public double entropy() {
		double res = 0;
		for (List<Observation> l : categoriesMapping.values()) {
			double p = l.size() / (double) size();
			double logp = MLUtils.log2(p);
			res = res - p * logp;
		}
		return res;
	}
	
	public DataSet subSet(int start, int end) {
		return new DataSet(attributes, MLUtils.clone(data, start, end), targetAttribute);
	}

	
	/**
	 * Get the j'th value of the i'th Observation
	 */
	public Object getValue(int i, int j) {
		return data.get(i).getValues()[j];
	}
	
	/**
	 * Get the targetAttribute value of the i'th Observation
	 */
	public Object getTargetAttributeValue(int i) {
		return data.get(i).getTargetAttributeValue();
	}
	
	public Object[][] data() {
		Object[][] res = new Object[size()][attributes.size() + 1];
		for (int i = 0; i < data.size(); i++) {
			for (int j = 0; j < attributes.size(); j++) {
				res[i][j] = getValue(i, j);
			}
			res[i][attributes.size()] = getTargetAttributeValue(i);
		}
		return res;
	}

	public Observation getObservation(int i) {
		return this.getObservations().get(i);
	}

	public int getWidth() {
		return attributes.size();
	}
	
	public int getHeight() {
		return data.size();
	}

}
