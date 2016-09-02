package ml.stat.kernels;

import ml.model.function.Function;
import ml.model.function.NormalFunction;

/**
 * F(x) = 1/sqrt(2*PI) * e ^ (-x^2)
 * @author Cosmin Zamfir
 *
 */
public class GaussianKernel extends NormalFunction implements Function {

	public GaussianKernel() {
		super(0, 1);
	}

}
