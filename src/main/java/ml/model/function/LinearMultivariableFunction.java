package ml.model.function;

import linalg.Vector;
import ml.utils.Utils;

/**
 * F(x1,x2,...,xn) = wo + w1*x1 + ... + wn*xn
 * @author Cosmin Zamfir
 *
 */
public class LinearMultivariableFunction extends DifferentiableMultivariableFunction {

	
	/** The first coefficient refers to synthetic variable x0, always equal to 1 */
	private double[] coefficients;
	
	public LinearMultivariableFunction(double[] coefficients) {
		this.coefficients = coefficients;
		this.n = coefficients.length;
	}
	
	@Override
	public double evaluate(double... x) {
		double[] copy = Utils.concatenate(1, x);
		return new Vector(copy).dotProduct(new Vector(coefficients));
	}
	
	
	@Override
	public double partialDerivativeAt(double[] x, int i) {
		if(i == 0) {
			return 1;
		}
		return x[i+1];
	}
}
