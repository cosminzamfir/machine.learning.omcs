package ml.stat.kernels;

import ml.model.function.Function;

/**
 * F(x) = (1 - |x|^3)^3, |x| < 1
 * @author Cosmin Zamfir
 *
 */
public class TricubeKernel implements Function {

	@Override
	public double evaluate(double x) {
		return Math.abs(x) < 1 ? Math.pow(1 - Math.abs(x*x*x),3) : 0;
	}
}
