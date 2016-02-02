package ml.model;

import linalg.Vector;


/**
 * An example/row in the data set
 * @author Cosmin Zamfir
 *
 */
public class Observation {

	private Object[] values;
	private Object targetAttributeValue;

	public Observation(Object targetAttributeValue, Object ... values) {
		super();
		this.values = values;
		this.targetAttributeValue = targetAttributeValue;
	}
	
	public Object getTargetAttributeValue() {
		return targetAttributeValue;
	}
	
	public Object[] getValues() {
		return values;
	}
	
	public Object getValue(Attribute attribute) {
		return values[attribute.getIndex()];
	}
	
	/**
	 * The numeric values in this numeric observation 
	 */
	public Vector getVectorValues() {
		double[] res = new double[getValues().length ];
		for (int i = 0; i < getValues().length; i++) {
			res[i] = (double) getValues()[i];
		}
		return new Vector(res);
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Object value : values) {
			if (!first) {
				sb.append("-");
			}
			first = false;
			sb.append(value);
		}
		sb.append("::");
		sb.append(targetAttributeValue);
		return sb.toString();
	}

}
