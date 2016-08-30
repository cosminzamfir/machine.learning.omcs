package ml.model.function;


/**
 * The term inside the integral expression of gamma function:
 * integral(0..infinity) [t^(x-1) * e^(-t) dt]. x is a constant, t is the variable
 * @author Cosmin Zamfir
 *
 */
public class GammaTerm implements Function {

	private double t;
	@Override
	public double evaluate(double x) {
		return 0;
	}
	

}
