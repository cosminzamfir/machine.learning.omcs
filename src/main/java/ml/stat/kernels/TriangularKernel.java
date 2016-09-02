package ml.stat.kernels;

import ml.model.function.Function;

/**
 * F(x) = 1 - |x|, |x| < 1
 * @author Cosmin Zamfir
 *
 */
public class TriangularKernel implements Function {

	@Override
	public double evaluate(double x) {
		return Math.abs(x) < 1 ? 1 - Math.abs(x) : 0;
	}
}
