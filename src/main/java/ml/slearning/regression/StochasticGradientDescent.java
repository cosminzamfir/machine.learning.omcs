package ml.slearning.regression;

import org.apache.log4j.Logger;

import linalg.Vector;
import ml.model.DataSet;
import ml.model.Observation;
import ml.model.function.DifferentiableMultivariableFunction;
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
public class StochasticGradientDescent {

	private static final Logger log = Logger.getLogger(StochasticGradientDescent.class);
	public DifferentiableMultivariableFunction targetFunction;
	public DataSet dataSet;

	public StochasticGradientDescent(DifferentiableMultivariableFunction targetFunction, DataSet dataSet) {
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
	 * <p>The error function: E(w0,...,wn) = 1/2 * (y' - y)^2
	 * <p>
	 * <p>compute the target function for all X in the training set
	 * <p>compute the total error by evaluating the error function
	 * <p>compute the gradient of the error function 
	 * <p>tune the coefficients of the target function: move each coefficient in the (-) direction of the partial derivative
	 * <p>
	 * <p>dE/dwi = (y' - y) * d/dwi (y' - y) =
	 * <p>(y' - y) * (-1) * d/dwi (y')
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
			error = newError;
			iteration++;
		}
	}

	private double runIteration(double learningRate) {
		double yPrime = 0;
		double y = 0;
		double[] partialDerivatives;
		int i = 0;
		Observation observation = MLUtils.randomElement(dataSet.getObservations());
		Vector v = observation.getVectorValues();
		yPrime = targetFunction.evaluate(v.getData());
		y = (double) observation.getTargetAttributeValue();
		log.debug("Learning rate:" + learningRate);
		log.debug("Target function coeficients: " + MLUtils.toString(targetFunction.getCoefficients()));
		log.debug("Computed value: " + yPrime);
		log.debug("Actual value: " + y);
		log.debug("Error: " + evaluateErrorFunction(yPrime, y));
		partialDerivatives = computePartialDerivatives(yPrime, y, observation);
		log.debug("Partial derivatives: " + MLUtils.toString(partialDerivatives));
		for (int j = 0; j < partialDerivatives.length; j++) {
			targetFunction.getCoefficients()[j] = targetFunction.getCoefficients()[j] + (-1) * partialDerivatives[j] * learningRate;
		}
		double res = evaluateErrorFunction(yPrime, y);
		log.debug("New coeficient values: " + MLUtils.toString(targetFunction.getCoefficients()));
		printResults();
		return res;
	}

		private void printResults() {
			double[] yPrime = new double[dataSet.size()];
			double[] y = new double[dataSet.size()];
			for (int i = 0; i < y.length; i++) {
				yPrime[i] = targetFunction.evaluate(dataSet.getObservation(i).getVectorValues().getData());
				y[i] = (double) dataSet.getObservation(i).getTargetAttributeValue();
			}
			log.debug(MLUtils.toString(yPrime));
			log.debug(MLUtils.toString(y));
		}

	/** E = 1/2 * sum[k in trainingSet] (y' - y)^2*/
	private double evaluateErrorFunction(double yPrime, double y) {
		return 0.5 * Math.pow((yPrime - y), 2);
	}

	/** dE/dwi = sum[k in training set] (y' - y) * d/dwi (targetOutput - targetFunction) =
	*	<p>
	*   sum[k in training set] (y' - y) * (-1) * d/dwi (targetFunction)
	 * @param observation 
	*/
	private double[] computePartialDerivatives(double yPrime, double y, Observation observation) {
		double[] res = new double[dataSet.getAttributes().size() + 1];
		for (int i = 0; i < res.length; i++) {
			res[i] = 0;
		}
		Vector x = observation.getVectorValues();
		for (int attrIndex = 0; attrIndex < res.length; attrIndex++) {
			res[attrIndex] += (yPrime - y) * targetFunction.partialDerivativeAt(x.getData(), attrIndex);
		}
		return res;
	}
}
