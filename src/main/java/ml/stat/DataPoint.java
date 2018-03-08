package ml.stat;

import java.util.ArrayList;
import java.util.List;

public class DataPoint<T> {

	private List<String> attrNames = new ArrayList<>();
	private List<Object> attrValues = new ArrayList<>();
	private T targetValue;

	public DataPoint(List<String> attrNames, List<Object> attrValues, T targetValue) {
		super();
		this.attrNames = attrNames;
		this.attrValues = attrValues;
		this.targetValue = targetValue;
	}

	public List<String> getAttrNames() {
		return attrNames;
	}

	public void setAttrNames(List<String> attrNames) {
		this.attrNames = attrNames;
	}

	public List<Object> getAttrValues() {
		return attrValues;
	}

	public void setAttrValues(List<Object> attrValues) {
		this.attrValues = attrValues;
	}

	public T getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(T targetValue) {
		this.targetValue = targetValue;
	}

}
