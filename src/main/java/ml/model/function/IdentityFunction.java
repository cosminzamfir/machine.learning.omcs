package ml.model.function;

/**
 * 
 * @author Cosmin Zamfir
 *
 */
public class IdentityFunction extends DifferentiableFunction {

	@Override
	public double derivativeAt(double x) {
		return 1;
	}

	@Override
	public double evaluate(double x) {
		return x;
	}

}
