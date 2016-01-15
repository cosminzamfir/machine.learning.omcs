package ml.slearning.classification.neuralnet;

import linalg.Vector;
import ml.model.DataSet;
import ml.model.Observation;

import org.apache.log4j.Logger;

/**
 * A neural network unit which used gradient descend for linear regression
 * @author Cosmin Zamfir
 *
 */
public class UnthresholdedPerceptron extends Perceptron {

	private static final Logger log = Logger.getLogger(UnthresholdedPerceptron.class);

	public UnthresholdedPerceptron() {
		super();
	}
	/**
	 * Apply the gradient descend rule for un-thresholded output
	 * <p>Start with random coeffecients
	 * <p>Define the error function E = 1/2 * sum[(targetOutput - computedOutput)^2]
	 * <p>Tune the coefficients to minimize the error function: move into the inverse direction of the gradient of E:
	 * <p>wi = wi + learningRate * (-1) * dE/dwi. The partial derivative turns out to be very similar to the perceptron rule delta:
	 * <p> dE/dwi = sum[targetOutput - computedOutput] * (-) xid =>
	 * <p>wi = wi + learningRate * sum[targetOutput - computedOutput] * xid, where xid is the xi value for the Observation d.
	 *   
	 * 
	 * @param dataSet
	 * @param learningRate
	 * @param precision
	 * @param maxIterations
	 */
	public void train(DataSet dataSet, double learningRate, double precision, int maxIterations) {
		generateRandomCoefficients(dataSet);
		log.debug(this);
		int iteration = 0;
		double error = evaluateErrorFunction(dataSet);
		while (iteration < maxIterations) {
			for (int i = 0; i < coefficients.length; i++) {
				double delta = learningRate * (-1) * errFuncPartialDerivative(dataSet, i);
				coefficients[i] = coefficients[i] + delta;
				log.debug("Updated coefficient " + i + " with: " + delta);
			}
			double newError = evaluateErrorFunction(dataSet);
			log.debug("Previous error: " + error + "; Current error: " + newError);
			if(error - newError < precision) {
				break;
			}
			error = newError;
			iteration ++;
		}
		log.debug("Done in " + iteration + " iterations.");
	}

	private double evaluateErrorFunction(DataSet dataSet) {
		double res = 0;
		for (Observation observation : dataSet.getObservations()) {
			res += Math.pow((double)observation.getTargetAttributeValue() - evaluate(observation),2);
		}
		log.debug("Error function evaluation:" + res/2);
		return res/2;
		
	}
	
	/**
	 * The partial derivative of E with respect to the i'th coefficient
	 * @param i
	 * @return dE/dwi = sum[targetOutput - computedOutput] * (-) xid
	 */
	private double errFuncPartialDerivative(DataSet dataSet, int i) {
		double res = 0;
		//if i = 0, it refers to sysntethic x0 with value 1
		for (Observation observation : dataSet.getObservations()) {
			double xi = (double) (i == 0 ? 1d : observation.getValues()[i-1]);
			res = res + ((double)observation.getTargetAttributeValue() - evaluate(observation)*xi*(-1));
		}
		log.debug("Partial derivative of error function with respect to " + i + " variable: " + res);
		return res;
	}


	/**
	 * Evaluate the specified Observation
	 * @param observation
	 * @return the Perceptron calculated value for the given Observation
	 */
	public double evaluate(Observation observation) {
		Vector coef = new Vector(coefficients);
		Vector variables = getValues(observation);
		return coef.dotProduct(variables);
	}
}
