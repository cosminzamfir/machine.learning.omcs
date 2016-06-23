package ml.slearning.regression;

import linalg.Vector;
import ml.model.DataSet;
import ml.model.Observation;
import ml.model.function.DifferentiableMultivariableFunction;

import org.apache.log4j.Logger;

import util.MLUtils;

/** 
 * A general tool for regression:
 * <p>
 * Support any {@link DifferentiableMultivariableFunction} as target function
 * <p>
 * Should support arbitrary {@link DifferentiableMultivariableFunction} as error function 
 * <p>
 * TODO - currently uses hardcoded error function: E(x) = 1/2 * sum[k in trainingSet] (y'(k) - y(k)), where y' = target function value; y - observation value
 * @author Cosmin Zamfir 
 *
 */
public class GradientDescent {

	private static final Logger log = Logger.getLogger(GradientDescent.class);
	public DifferentiableMultivariableFunction targetFunction;
	public DataSet dataSet;

	public GradientDescent(DifferentiableMultivariableFunction targetFunction, DataSet dataSet) {
		super();
		this.targetFunction = targetFunction;
		this.dataSet = dataSet;
	}

	/** 
	 * Run the gradient descent until the total error is less than maxErorr, but no more than maxIterations
	 * 
	 * <p>variables: x1,...,xn - the attributes of the data set
	 * <p>output: the real value target attribute
	 * <p>The target function: some {@link DifferentiableMultivariableFunction}
	 * <p>The error function: E(w0,...,wn) = 1/2 * sum[k in trainingSet] (y' - y)^2
	 * <p>
	 * <p>compute the target function for all X in the training set
	 * <p>compute the total error by evaluating the error function
	 * <p>compute the gradient of the error function 
	 * <p>tune the coefficients of the target function: move each coefficient in the (-) direction of the partial derivative
	 * <p>
	 * <p>dE/dwi = sum[k in training set] (y' - y) * d/dwi (y' - y) =
	 * <p>sum[k in training set] (y' - y) * (-1) * d/dwi (y')
	 * @param maxError
	 * @param maxIterations
	 */
	public void train(double maxError, int maxIterations, double learningRate) {
		targetFunction.generateRandomCoefficients(0, 0.1);
		int iteration = 0;
		double error = Double.POSITIVE_INFINITY;
		while (iteration < maxIterations && error > maxError) {
			log.debug("Iteration " + iteration);
			double newError = runIteration(learningRate);
			if(newError < error) {
				learningRate *=2;
			} else {
				learningRate /= 2;
			}
			error = newError;
			iteration ++;
		}
	}

	private double runIteration(double learningRate) {
		double[] yPrime = new double[dataSet.size()];
		double[] y = new double[dataSet.size()];
		double[] partialDerivatives = new double[dataSet.getAttributes().size() + 1];
		int i = 0;
		for (Observation observation : dataSet.getObservations()) {
			Vector v = observation.getVectorValues();
			yPrime[i] = targetFunction.evaluate(v.getData());
			y[i] = (double) observation.getTargetAttributeValue();
			i++;
		}
		log.debug("Learning rate:" + learningRate);
		log.debug("Target function: " + targetFunction);
		log.debug("Computed values: " + MLUtils.toString(yPrime));
		log.debug("Actual values: " + MLUtils.toString(y));
		log.debug("Error: " + evaluateErrorFunction(yPrime, y));
		partialDerivatives = computePartialDerivatives(yPrime, y);
		log.debug("Partial derivatives: " + MLUtils.toString(partialDerivatives));
		for (int j = 0; j < partialDerivatives.length; j++) {
			targetFunction.getCoefficients()[j] = targetFunction.getCoefficients()[j] + (-1) * partialDerivatives[j] * learningRate;
		}
		double res = evaluateErrorFunction(yPrime, y);
		log.debug("New coeficient values: " + MLUtils.toString(targetFunction.getCoefficients()));
		return res;
	}

	/** E = 1/2 * sum[k in trainingSet] (y' - y)^2*/
	private double evaluateErrorFunction(double[] yPrime, double[] y) {
		double res = 0;
		for (int i = 0; i < y.length; i++) {
			res += Math.pow((yPrime[i] - y[i]),2);
		}
		return res/2;
	}

	/** dE/dwi = sum[k in training set] (y' - y) * d/dwi (targetOutput - targetFunction) =
	*	<p>
	*   sum[k in training set] (y' - y) * (-1) * d/dwi (targetFunction)
	*/
	private double[] computePartialDerivatives(double[] yPrime, double[] y) {
		double[] res = new double[dataSet.getAttributes().size() + 1];
		for (int i = 0; i < res.length; i++) {
			res[i] = 0;
		}
		for (int obsIndex = 0; obsIndex < dataSet.size(); obsIndex++) {
			Vector x = dataSet.getObservation(obsIndex).getVectorValues();
			for (int attrIndex = 0; attrIndex < res.length; attrIndex++) {
				res[attrIndex] += (yPrime[obsIndex] - y[obsIndex]) * targetFunction.partialDerivativeAt(x.getData(), attrIndex);
			}
		}
		return res;
	}
}
