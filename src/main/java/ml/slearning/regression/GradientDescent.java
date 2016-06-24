package ml.slearning.regression;

import linalg.Vector;
import ml.model.DataSet;
import ml.model.function.DifferentiableFunction;

import org.apache.log4j.Logger;

import util.MLUtils;

/** 
 * A general tool for regression
 * <p>
 * The predicted outcome is a {@link DifferentiableFunction} applied to the dotProduct between the coefficients w and observation vector x
 * <p>
 * The error function: E(x) = 1/2 * sum[k in trainingSet] (y'(k) - y(k))^2, where y' = target function value; y - observation value
 * @author Cosmin Zamfir 
 *
 */
public class GradientDescent {

	private static final Logger log = Logger.getLogger(GradientDescent.class);
	public DifferentiableFunction targetFunction;
	public DataSet dataSet;
	/** The coefficients to find */
	public Vector w;
	/** The width of data */
	private int m;
	/** The height of the data */
	private int n;

	public GradientDescent(DifferentiableFunction targetFunction, DataSet dataSet) {
		super();
		this.targetFunction = targetFunction;
		this.dataSet = dataSet;
		w = new Vector(MLUtils.randomArray(dataSet.getWidth()));
		m = dataSet.getWidth();
		n = dataSet.getHeight();
	}

	/** 
	 * Run the gradient descent until the total error is less than maxErorr, but no more than maxIterations
	 * 
	 * <p>variables: x1,...,xn - the attributes of the data set
	 * <p>output: the real value target attribute
	 * <p>The target function: some {@link DifferentiableFunction}
	 * <p>The error function: E(w0,...,wn) = 1/2 * sum[k in trainingSet] (y' - y)^2
	 * <p>
	 * <p>compute the target function for all X in the training set
	 * <p>compute the total error by evaluating the error function
	 * <p>compute the gradient of the error function 
	 * <p>tune the coefficients of the target function: move each coefficient in the (-) direction of the partial derivative
	 * <p>
	 * <p>dE/dwi = sum[k in training set] (y' - y) * d/dwi (y' - y) =
	 * <p>sum[k in training set] (y' - y) * (-1) * d/dx (y') * d/dwi x
	 * @param maxError
	 * @param maxIterations
	 */
	public void train(double maxError, int maxIterations, double learningRate) {
		int iteration = 1;
		double error = Double.MAX_VALUE;
		double deltaError = Double.MAX_VALUE;
		while (iteration < maxIterations && deltaError > 0.001) {
			double newError = runIteration(learningRate);
			deltaError = Math.abs(error - newError);
			error = newError;
			log.debug("Iteration " + iteration + " - Error=" + error + ";w=" + w);
			iteration++;
		}
	}

	/**
	 * Run one loop through the entire training set
	 * Update rule: wi = wi + (y' - y) * -1 * learningRate * xi * dy'/dx
	 * @param learningRate
	 * @return
	 */
	private double runIteration(double learningRate) {
		for (int obsIndex = 0; obsIndex < n; obsIndex++) {
			Vector x = dataSet.getObservation(obsIndex).getVectorValues();
			double dotProduct = x.dotProduct(w);
			double yprime = targetFunction.evaluate(dotProduct);
			double y = (double) dataSet.getTargetAttributeValue(obsIndex);
			double dydx = targetFunction.derivativeAt(dotProduct);
			log.debug("Computed value=" + yprime + ";Observed value=" +y + ";Weights=" + w + ";StateVector=" + x);
			for (int attrIndex = 0; attrIndex < m; attrIndex++) {
				double deltawi = -1 * (yprime - y) * dydx * x.get(attrIndex) * learningRate;
				w.set(attrIndex, w.get(attrIndex) + deltawi);
				log.debug("deltaW" + attrIndex + "=" + deltawi);
			}
			log.debug("Weights=" + w);
		}
		return computeMeanSquareError();
	}

	/** E = 1/2 * sum[k in trainingSet] (y' - y)^2*/
	private double computeMeanSquareError() {
		double res = 0;
		for (int i = 0; i < n; i++) {
			res += computeMeanSquareError(targetFunction.evaluate(dataSet.getObservation(i).getVectorValues().dotProduct(w)),
					(double) dataSet.getTargetAttributeValue(i));
		}
		return res / 2;
	}

	private double computeMeanSquareError(double yPrime, double y) {
		return Math.pow(yPrime - y, 2);
	}
}
