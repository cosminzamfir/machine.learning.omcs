package ml.slearning.neuralnet;

import linalg.Vector;
import ml.model.DataSet;
import ml.model.Observation;
import ml.utils.Utils;

import org.apache.log4j.Logger;

/**
 * A neural network unit which tries to compute the hyperplane which separates best a list of n-dimensional Observations
 * @author Cosmin Zamfir
 *
 */
public class ThresholdedPerceptron extends Perceptron{

	private static final Logger log = Logger.getLogger(ThresholdedPerceptron.class);

	public ThresholdedPerceptron() {
		super();
	}

	/**
	 * Apply the perceptron rule algo for thresholded output
	 * Try to separate the points in the given DataSet
	 * Each Attribute must be numeric. Suppose there are n attributes: xi..xn.
	 * The target Attribute has possible values +/- 1.
	 * The target is to find the coefficients w0 ... wn, such that sgn(sum[i=0..n](xi*wi)] matches the target attribute of every observation.
	 * x0 is a synthetic variable with value 1
	 * Then, the w coefficients describe a hyperplance which separates the +1 from -1 Observations
	 */
	public void train(DataSet dataSet, double learningRate, int maxIterations) {
		generateRandomCoefficients(dataSet);
		int iteration = 0;
		boolean done = false; //true when all training Observations are correctly classified
		while (iteration < maxIterations && !done) {
			done = true;
			for (Observation observation : dataSet.getObservations()) {
				if(tuneCoefficients(observation, learningRate)) {
					done = false;
				}
			}
			iteration ++;
		}
		log.debug("Done in " + iteration + " iterations.");
	}
	
	
	/**
	 * Evaluate the specified Observation
	 * @param observation
	 * @return the Perceptron calculated value for the given Observation
	 */
	public double evaluate(Observation observation) {
		Vector coef = new Vector(coefficients);
		Vector variables = getValues(observation);
		return coef.dotProduct(variables) >= 0 ? 1 : -1;
	}

	
	/**
	 * @return true if the evaluation return the same result at the {@link Observation#getTargetAttributeValue()}
	 */
	public boolean validate(Observation observation) {
		return evaluate(observation) == (double) observation.getTargetAttributeValue();
	}

	/** Tune the coefficients one-by-one if the (one step for each coefficient) if the targetAttributeValue of the Observation is 
	 * is not equal to the computed value 
	 * @return true if any tuning was performed
	 */
	private boolean tuneCoefficients(Observation observation, double learningRate) {
		double target = (double) observation.getTargetAttributeValue();
		double actual = evaluate(observation);
		if (target == actual) {
			log.debug("OK for " + observation + " - " + this);
			return false;
		}
		for (int i = 0; i < coefficients.length; i++) {
			double attributeValue = i == 0 ? 1 :(double) observation.getValues()[i-1];
			double delta = learningRate * (target - actual) * attributeValue;
			coefficients[i] = coefficients[i] + delta;
			log.debug("Tuned coefficient " + i + ":" + observation + " - " + this);
			actual = evaluate(observation);
			if (actual == target) {
				log.debug("OK for " + observation + " - " + this);
				return true;
			}
		}
		log.debug("Not yet OK for " + observation + " - " + this);
		return true;
	}

	@Override
	public String toString() {
		return "Perceptron:" + Utils.toString(coefficients);
	}
}
