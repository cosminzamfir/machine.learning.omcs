package ml.slearning.classification.bayes;

import java.util.Collection;

import ml.model.DataSet;
import ml.model.Attribute;
import ml.model.Observation;

/**
 * Implementation of the naive bayse algo.
 * Algo description:
 * <p>
 * Given {@link FeatureValue} x1, x2, x3, ..., xn for an {@link Observation}, find the {@link Output} category ci which maximizes the following formula:
 * P(C=cj|X1=x1,...,Xn=xn).
 * <p>
 * Using Bayes formula: P(A|B) = P(A^B)/P(B) = P(B|A) * P (A) / P(B) we can rewrite the above expression:
 * <p>
 * P(C=cj|X1=x1,...,Xn=xn) = P(X1=x1,X2=x2,...|C=cj) * P(C=cj) / P(X1=x1,X2=x2,...)
 * <p>
 * As the denomitor of the above expression is independent of cj, is not relevant for the maximization
 * <p>
 * Furthermore, we assume that X1,X2,..,Xn are mutually independent with respect to (given) cj. The, the quantity to maximize can be written as: 
 * <p>
 * P(X1=x1|C=cj) * P(X2=x2|C=cj) * ... * P(Xn=xn|C=cj) * P(C=cj)
 * <p>
 * Algorithm: Given an {@link Observation}, the steps to assign the right {@link Output} to it:
 * <ol>
 * <li> Compute P(C=cj) from the {@link DataSet} - simple iteration through all {@link Observation}s in the set
 * <li> Iterate over all {@link Output}s - the {@link DataSet} must have a discrete set of {@link Output}s
 * <li> For each compute P(X1=x1|C=cj) * P(X2=x2|C=cj) * ... * P(Xn=xn|C=cj) * P(C=cj)
 * <li> Choose the Response which maximized the expression above
 * <ol>
 * 
 * @author Cosmin Zamfir
 *
 */
public class NaiveBayes {

	private DataSet dataSet;
	
	public NaiveBayes(DataSet dataSet) {
		super();
		this.dataSet = dataSet;
	}
	
	public DataSet getDataSet() {
		return dataSet;
	}

	public Object predict(Observation example) {
		Collection<Double> candidates = dataSet.getCategories();
		double p = 0;
		Object res = null;
		for (double candidate : candidates) {
			double cp = dataSet.probability(candidate);
			for (int i=0; i<dataSet.getAttributes().size(); i++) {
				Attribute feature = dataSet.getAttributes().get(i);
				cp = cp * dataSet.conditionalProbability(feature, example.getValue(feature), candidate);
			}
			if(cp > p) {
				p = cp;
				res = candidate;
			}
		}
		return res;
	}
	
}
