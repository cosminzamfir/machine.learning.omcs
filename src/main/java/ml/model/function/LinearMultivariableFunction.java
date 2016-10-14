package ml.model.function;

import linalg.Vector;
import util.MLUtils;

/**
 * F(x1,x2,...,xn) = wo + w1*x1 + ... + wn*xn
 * @author Cosmin Zamfir
 *
 */
public class LinearMultivariableFunction extends DifferentiableMultivariableFunction {

	/** The first coefficient refers to synthetic variable x0, always equal to 1 */
	protected double[] coefficients;

	public LinearMultivariableFunction(double... coefficients) {
		this.coefficients = coefficients;
		this.n = coefficients.length;
	}

	@Override
	public double evaluate(double... x) {
		double[] copy = x;
		if (coefficients.length == x.length + 1) {
			copy = MLUtils.concatenate(1, x);
		}
		return new Vector(copy).dotProduct(new Vector(coefficients));
	}

	@Override
	public double partialDerivativeAt(double[] x, int i) {
		if (i == 0) {
			return 1;
		}
		return x[i - 1];
	}

	public void generateRandomCoefficients(double minValue, double maxValue) {
		coefficients = new double[n];
		for (int i = 0; i < this.n; i++) {
			coefficients[i] = MLUtils.randomDouble(minValue, maxValue);
		}
	}

	public double[] getCoefficients() {
		return coefficients;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("f(x) = ");
		for (int i = 0; i < coefficients.length; i++) {
			if (coefficients[i] != 0) {
				if (i == 0) {
					sb.append(" + " + coefficients[i]);
				} else {
					sb.append(" + " + coefficients[i] + "*x" + i);
				}

			}
		}
		return sb.toString();
	}

}
