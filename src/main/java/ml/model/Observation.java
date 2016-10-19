package ml.model;

import util.MLUtils;
import linalg.Vector;


/**
 * An example/row in the data set
 * @author Cosmin Zamfir
 *
 */
public class Observation {

	private double[] values;
	private double targetAttributeValue;

	public Observation(double targetAttributeValue, double ... values) {
		super();
		this.values = values;
		this.targetAttributeValue = targetAttributeValue;
	}
	
	public double getTargetAttributeValue() {
		return targetAttributeValue;
	}
	
	public void setTargetAttributeValue(double targetAttributeValue) {
		this.targetAttributeValue = targetAttributeValue;
	}
	
	public double[] getValues() {
		return values;
	}
	
	public double getValue(Attribute attribute) {
		return values[attribute.getIndex()];
	}
	
	/**
	 * The numeric values in this numeric observation 
	 */
	public Vector getVectorValues(boolean addBiasTerm) {
		double[] res = new double[getValues().length ];
		for (int i = 0; i < getValues().length; i++) {
			res[i] = (double) getValues()[i];
		}
		if(addBiasTerm) {
			res = MLUtils.insert(1,res);
		}
		return new Vector(res);
	}
	
	public void removeValue(int index) {
		this.values = MLUtils.removeIndex(values, index);
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (double value : values) {
			if (!first) {
				sb.append(":");
			}
			first = false;
			sb.append(MLUtils.format(value));
		}
		sb.append("#");
		sb.append(MLUtils.format(targetAttributeValue));
		return sb.toString();
	}
	
	

}
