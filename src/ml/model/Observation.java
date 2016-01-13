package ml.model;


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
