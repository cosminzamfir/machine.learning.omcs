package ml.stat;

import java.util.List;

public class Category {

	private List<Object> values;
	private Object value;

	public Category(List<Object> values, Object value) {
		super();
		this.values = values;
		this.value = value;
	}

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		if(!values.contains(value)) {
			throw new RuntimeException(value + " does not belong to " + values);
		}
		this.value = value;
	}

}
