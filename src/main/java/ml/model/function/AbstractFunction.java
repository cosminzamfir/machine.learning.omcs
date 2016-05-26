package ml.model.function;

import ml.utils.FunctionChart;

public abstract class AbstractFunction implements Function {

	@Override
	public abstract double evaluate(double x);
	
	public void plot(double from ,double to) {
		new FunctionChart(toString(), this, from, to);
	}
	
	public double definiteIntegral(double from, double to, double maxError) {
		return new DefiniteIntegral().compute(this, from, to, maxError);
	}
	
	public double derivativeAt(double x, double error) {
		return new Derivative().compute(this, x, error);
	}
}
