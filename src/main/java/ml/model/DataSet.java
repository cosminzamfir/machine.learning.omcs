package ml.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ml.stat.StatUtils;

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

	private List<Observation> data = new ArrayList<Observation>();;
	private List<Attribute> attributes = new ArrayList<>();
	private Attribute targetAttribute;
	/** Map each {@link #targetAttribute} value to the list of Observations having this targetAttribute value */
	private Map<Double, List<Observation>> categoriesMapping;

	
	public DataSet(int width) {
		super();
		initAttributes(width);
	}

	public DataSet(List<Attribute> attributes, List<Observation> observations, Attribute targetAttribute) {
		super();
		this.data = observations;
		this.attributes = attributes;
		for (Attribute attribute : attributes) {
			attribute.setDataSet(this);
		}
		this.targetAttribute = targetAttribute;
		buildCategortMappings();
		setAttributesRange();
		setTargetAttributeRange();
	}
	

	private void initAttributes(int width) {
		for (int i = 1; i <= width; i++) {
			attributes.add(new Attribute(this, "x" + i, Double.class));
		}
	}

	
	public DataSet add(double y, double ...x) {
		Observation o = new Observation(y, x);
		data.add(o);
		return this;
	}

	private void setTargetAttributeRange() {
		for (Observation instance : data) {
			targetAttribute.addPossibleValue(instance.getTargetAttributeValue());
		}
	}

	private void setAttributesRange() {
		for (Attribute attribute : attributes) {
			for (Observation instance : data) {
				attribute.addPossibleValue(instance.getValue(attribute));
			}
		}
	}

	private void buildCategortMappings() {
		categoriesMapping = new HashMap<Double, List<Observation>>();
		for (Observation observatation : data) {
			double targetAttributeValue = observatation.getTargetAttributeValue();
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
	public double probability(double targetAttributeValue) {
		int res = 0;
		for (Observation observation : data) {
			if (observation.getTargetAttributeValue() == targetAttributeValue) {
				res++;
			}
		}
		return res / (double) data.size();
	}

	/** @return the probabilty of having the specified targetAttributeValue, given that it has the specified value for the specified Attribute */
	public double conditionalProbability(Attribute attribute, double value, double targetAttributeValue) {
		List<Observation> candidates = categoriesMapping.get(targetAttributeValue);
		int res = 0;
		for (Observation example : candidates) {
			if (example.getValue(attribute)== value) {
				res++;
			}
		}
		return res / (double) candidates.size();
	}

	public Set<Double> getCategories() {
		return categoriesMapping.keySet();
	}

	public Map<Double, List<Observation>> getCategoriesMapping() {
		if(categoriesMapping == null) {
			buildCategortMappings();
		}
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
			dataSet.buildCategortMappings();
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
		sb.append(MessageFormat.format("{0} observations and {1} features", data.size(), getWidth())).append("\n");
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
	public double getValue(int i, int j) {
		return data.get(i).getValues()[j];
	}
	
	/**
	 * Get the targetAttribute value of the i'th Observation
	 */
	public double getTargetAttributeValue(int i) {
		return data.get(i).getTargetAttributeValue();
	}
	
	public double[][] data() {
		double[][] res = new double[size()][attributes.size() + 1];
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
	
	public void removeFeature(int index) {
		attributes.remove(index);
		data.forEach(obs -> obs.removeValue(index));
		
	}
	
	/**
	 * Transform each column into standard units, i.e. mean=0 and std=1: col(i) = [col(i) - mean(col)] / std(col)
	 * 
	 */
	public void standardize() {
		for (int i = 0; i < attributes.size(); i++) {
			List<Double> l1	 = getColumn(i);
			List<Double> l2 = StatUtils.asStandardUnits(l1);
			setColumn(i,l2);
		}
	}
	
	private void setColumn(int index, List<Double> l) {
		for (int i = 0; i < l.size(); i++) {
			data.get(i).getValues()[index] = l.get(i);
		}
	}

	public List<Double> getColumn(int i) {
		List<Double> res = new ArrayList<>();
		data.forEach(obs -> res.add(obs.getValues()[i]));
		return res;
	}
	
	public List<Double> getTarget() {
		List<Double> res = new ArrayList<>();
		data.forEach(obs -> res.add(obs.getTargetAttributeValue()));
		return res;
	}
	
	
	public void shuffle() {
		Collections.shuffle(data);
	}
	
	public List<DataSet> partition(double trainingSetPercentage, boolean shuffle) {
		if(shuffle) {
			shuffle();
		}
		List<DataSet> res = new ArrayList<>();
		int i = (int) (data.size() * trainingSetPercentage);
		List<Observation> l1 = data.subList(0, i);
		List<Observation> l2 = data.subList(i, data.size() - 1);
		res.add(new DataSet(attributes, l1, targetAttribute));
		res.add(new DataSet(attributes, l2, targetAttribute));
		return res;
	}

	public int getIndex(Attribute attribute) {
		return attributes.indexOf(attribute);
	}
	
}
