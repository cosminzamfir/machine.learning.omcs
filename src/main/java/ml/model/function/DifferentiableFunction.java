package ml.model.function;

/**
 * 
 * @author Cosmin Zamfir
 *
 */
public abstract class DifferentiableFunction extends AbstractFunction {

	public abstract double derivativeAt(double x);
}
