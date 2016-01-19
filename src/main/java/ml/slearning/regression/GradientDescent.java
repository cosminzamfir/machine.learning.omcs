package ml.slearning.regression;

import linalg.Vector;
import ml.model.DataSet;
import ml.model.Observation;
import ml.model.function.DifferentiableMultivariableFunction;

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
	 * <p>dE/dwi = sum[k in training set] (y' - y) * d/dwi (targetOutput - targetFunction) =
	 * <p>sum[k in training set] (y' - y) * (-1) * d/dwi (targetFunction)
	 * @param maxError
	 * @param maxIterations
	 */
	public void train(double maxError, int maxIterations, double learningRate) {
		targetFunction.generateRandomCoefficients(0, 0.1);
		int iteration = 0;
		while (iteration < maxIterations) {
			double error = runIteration(learningRate);
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
			;
			y[i] = (double) observation.getTargetAttributeValue();
			i++;
		}
		partialDerivatives = computePartialDerivatives(yPrime, y);
		for (int j = 0; j < partialDerivatives.length; j++) {
			targetFunction.getCoefficients()[j] = targetFunction.getCoefficients()[j] + (-1) * partialDerivatives[j] * learningRate;
		}
		return evaluateErrorFunction(yPrime, y);
	}

	private double evaluateErrorFunction(double[] yPrime, double[] y) {
		double res = 0;
		for (int i = 0; i < y.length; i++) {
			res += Math.pow((yPrime[i] - y[i]),2);
		}
		return res;
	}

	private double[] computePartialDerivatives(double[] yPrime, double[] y) {
		return null;
	}
}
