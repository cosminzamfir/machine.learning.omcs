package ml.model.function;

/**
 * 
 * @author Cosmin Zamfir
 *
 */
public abstract class DifferentiableFunction implements Function {

	public abstract double derivativeAt(double x);
}
