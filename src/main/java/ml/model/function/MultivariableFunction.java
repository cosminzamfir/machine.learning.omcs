package ml.model.function;

/**
 * Functions from Rn to R
 * @author Cosmin Zamfir
 *
 */
public abstract class MultivariableFunction {
	
	/** The number of variables */
	protected int n;
	public abstract double evaluate(double ... x);
	
}
