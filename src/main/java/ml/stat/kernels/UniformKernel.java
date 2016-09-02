package ml.stat.kernels;

import ml.model.function.Function;

/**
 * F(u) = 1/2, |u| < 1
 * @author Cosmin Zamfir
 *
 */
public class UniformKernel implements Function{

	@Override
	public double evaluate(double x) {
		return Math.abs(x) > 1 ? 0 : 0.5;
	}

}
