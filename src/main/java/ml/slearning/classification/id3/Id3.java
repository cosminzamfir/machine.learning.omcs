package ml.slearning.classification.id3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ml.model.Attribute;
import ml.model.DataSet;

import org.apache.log4j.Logger;

/**
 * Pseudocode:
 * <ul>
 * <li>id3(examples, classificationAttribute, allAttributes)
 * <li>Create a root node for the tree
 * <li>If all examples are of one clasification, set the clasification as label to the node and return it
 * <li>Else-If there are no attributes left, set the most popular clasification to the node and return it
 * <li>Else
 * <ul>
 * <li>Choose the best attribute from allAttributes (based on information gain function)
 * <li>Assign the best attribute to the root node
 * <li>For each value in the bestAttribute
 * <ul>
 * <li>Add branch below root node for the value
 * <li>Branch examples = examples with the value for the bestAttribute
 * <li>If branchExample is empty add leaf node with most popular clasificationAttribute label
 * <li>Else add id3(branchExamples, classficationAttribute, allAttributes-bestAttribute)
 * </ul>
 * </ul>
 * @author Cosmin Zamfir
 *
 */
public class Id3 {

	private static final Logger log = Logger.getLogger(Id3.class);

	public Id3Node createDecistionTree(DataSet dataSet) {
		return run(dataSet, dataSet.getAttributes());
	}

	private Id3Node run(DataSet dataSet, List<Attribute> attributes) {
		log.debug("Run for " + dataSet.sumary() + " and attributes: " + attributes);
		Id3Node res = new Id3Node();
		Set<Double> categories = dataSet.getCategories();
		if (categories.size() == 1) {
			res.setLabel(categories.iterator().next());
			log.debug("Found one category: " + categories + ". Setting the label: " + res.getLabel());
			return res;
		} else if (attributes.isEmpty()) {
			res.setLabel(dataSet.mostLikelyOutput());
			log.debug("No more candidate attributes. Setting the label as most likely category: " + res.getLabel());
			return res;
		} else {
			Attribute bestAttribute = computeBestAttribute(dataSet, attributes);
			res.setAttribute(bestAttribute);
			Map<Object, DataSet> split = dataSet.split(bestAttribute);
			for (Object attributeValue : bestAttribute.getPossibleValues()) {
				if (split.get(attributeValue) == null || split.get(attributeValue).isEmpty()) {
					Id3Node child = new Id3Node();
					child.setLabel(dataSet.mostLikelyOutput());
					res.getBranches().put(attributeValue, child);
				} else {
					Id3Node child = run(split.get(attributeValue), remove(attributes, bestAttribute));
					res.getBranches().put(attributeValue, child);
				}
			}
			return res;
		}
	}

	private List<Attribute> remove(List<Attribute> attributes, Attribute bestAttribute) {
		List<Attribute> res = new ArrayList<>(attributes);
		res.remove(bestAttribute);
		return res;
	}

	private Attribute computeBestAttribute(DataSet dataSet, List<Attribute> attributes) {
		double parentSetEntropy = dataSet.entropy();
		double minEntropy = Double.POSITIVE_INFINITY;
		Attribute res = null;
		for (Attribute attribute : attributes) {
			Map<Object, DataSet> split = dataSet.split(attribute);
			double entropy = 0;
			for (Object attributeValue : split.keySet()) {
				DataSet subSet = split.get(attributeValue);
				entropy = entropy + subSet.entropy() * subSet.size() / dataSet.size();
			}
			log.debug("Information gain for splitting by " + attribute + ": " + (parentSetEntropy - entropy));
			if (entropy < minEntropy) {
				minEntropy = entropy;
				res = attribute;
			}
		}
		log.debug("Choose bestAttribute:" + res);
		return res;
	}

}
