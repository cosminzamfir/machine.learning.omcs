package ml.model;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Describe an attribute of the data set 
 * @author Cosmin Zamfir
 *
 */
public class Attribute {

	private String name;
	/** the index of the attribute in the dataSet*/
	private int index;
	private Class<?> dataType;
	private Set<Object> possibleValues = new LinkedHashSet<Object>();

	public Attribute(String name, Class<?> dataType, int index) {
		super();
		this.name = name;
		this.dataType = dataType;
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<?> getDataType() {
		return dataType;
	}

	public void setDataType(Class<?> dataType) {
		this.dataType = dataType;
	}
	
	public int getIndex() {
		return index;
	}
	
	public Set<Object> getPossibleValues() {
		return possibleValues;
	}
	
	public void addPossibleValue(Object value) {
		possibleValues.add(value);
		
	}
	
	@Override
	public String toString() {
		return name + possibleValues;
	}
}
