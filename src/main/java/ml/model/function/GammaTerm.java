package ml.model.function;

import static java.lang.Math.*;

/**
 * The term inside the integral expression of gamma function:
 * @author Cosmin Zamfir
 *
 */
public class GammaTerm implements Function {

	private double t;

	public GammaTerm(double t) {
		super();
		this.t = t;
	}

	@Override
	public double evaluate(double x) {
		return pow(E, -x) * pow(x, t-1);
	}

}
