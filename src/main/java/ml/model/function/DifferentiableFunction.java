package ml.model.function;

public abstract class DifferentiableFunction extends Function {

	public abstract double derivativeAt(double x);
}
