package ml.model.function;

public abstract class DifferentiableFunction extends AbstractFunction {

	public abstract double derivativeAt(double x);
}
