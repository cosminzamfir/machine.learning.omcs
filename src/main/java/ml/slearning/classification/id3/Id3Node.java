package ml.slearning.classification.id3;

import java.util.LinkedHashMap;
import java.util.Map;

import ml.model.Attribute;
import ml.model.Observation;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * A node in the decision tree
 * @author Cosmin Zamfir
 *
 */
public class Id3Node {

	private static final Logger log = Logger.getLogger(Id3Node.class);
	
	/** A leaf node has a label - represent the category for this node */
	private Object label;

	/** A non-leaf node, the Attribute it splits on */
	private Attribute attribute;

	/** Map one Id3Node to each value of the best attribute */
	private Map<Object, Id3Node> branches = new LinkedHashMap<>();

	/** Back-reference to the  parent node in the decision tree */
	private Id3Node parent;

	/** Compute the targetAttribute value for the given Observation */
	public Object clasify(Observation observation) {
		if(label != null) {
			return label;
		}
		Object attributeValue = observation.getValue(attribute);
		Id3Node next = branches.get(attributeValue);
		return next.clasify(observation);
	}

	public Object getLabel() {
		return label;
	}

	public void setLabel(Object label) {
		this.label = label;
	}

	public Attribute getBestFeature() {
		return attribute;
	}

	public void setAttribute(Attribute bestFeature) {
		this.attribute = bestFeature;
	}

	public Map<Object, Id3Node> getBranches() {
		return branches;
	}

	public void setBranches(Map<Object, Id3Node> branches) {
		this.branches = branches;
	}

	public Id3Node getParent() {
		return parent;
	}

	public void setParent(Id3Node parent) {
		this.parent = parent;
	}

	public String toString(int indentLevel) {
		if (label != null) {
			return label.toString();
		}
		String indent = StringUtils.repeat("  ", indentLevel);
		StringBuilder sb = new StringBuilder();
		for (Object attributeValue : branches.keySet()) {
			sb.append("\n").append(indent).
					append("If " + attribute.getName() + " = " + attributeValue + " then ").
					append(branches.get(attributeValue).toString(indentLevel + 1));
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return toString(0);
	}
}
