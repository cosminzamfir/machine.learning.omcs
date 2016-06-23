package ml.model.function;

import linalg.Vector;
import ml.slearning.regression.GradientDescent;
import util.MLUtils;

/**
 * A differentiable {@link MultivariableFunction} usable in regression ( {@link GradientDescent})
 * @author Cosmin Zamfir
 *
 */
public abstract class DifferentiableMultivariableFunction  extends MultivariableFunction {

	public abstract void generateRandomCoefficients(double minValue, double maxValue);
	
	public abstract double[] getCoefficients();


	/**
	 * Compute and return the partial derivative for variable i at value x
	 * @param x the x1,...,xn variable array
	 * @param i the i'th variable: 0 for the sysnthetic x0; k for xk, k = 1..n
	 * 
	 */
	public abstract double partialDerivativeAt(double[] x, int i);
	
	public Vector gradient(double[] x) {
		if(x.length != this.n) {
			throw new RuntimeException("Invalid variable array size: " + x.length + ", but expected: " + this.n);
		}
		double[] res = new double[n];
		for (int i = 0; i < res.length; i++) {
			res[i] = partialDerivativeAt(x, i);
		}
		return new Vector(res);
	}


	
}
