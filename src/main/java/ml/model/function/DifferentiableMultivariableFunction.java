package ml.model.function;

import linalg.Vector;
import ml.slearning.regression.GradientDescent;
import ml.utils.Utils;

/**
 * A differentiable {@link MultivariableFunction} usable in regression ( {@link GradientDescent})
 * @author Cosmin Zamfir
 *
 */
public abstract class DifferentiableMultivariableFunction  extends MultivariableFunction {

	/** The first coefficient refers to synthetic variable x0, always equal to 1 */
	protected double[] coefficients;

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
	
	public void generateRandomCoefficients(double minValue, double maxValue) {
		coefficients = new double[n];
		for (int i = 0; i < this.n; i++) {
			coefficients[i] = Utils.randomDouble(minValue, maxValue);
		}
	}
	
	public double[] getCoefficients() {
		return coefficients;
	}
}
