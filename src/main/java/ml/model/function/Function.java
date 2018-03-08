package ml.model.function;

import prob.chart.ChartBuilder;
import prob.chart.PlotType;
import ml.utils.FunctionChart;

/**
 * 
 * @author Cosmin Zamfir
 *
 */
@FunctionalInterface
public interface Function {

	double evaluate(double x);

	default void plot(double from, double to, int samples) {
		new ChartBuilder().add(this, from, to, samples, "function", PlotType.Scatter).build();;
	}
	
	default void plot(double from, double to, int samples, String name) {
		new ChartBuilder().add(this, from, to, samples, name, PlotType.Scatter).build();
	}

	default double definiteIntegral(double from, double to, double maxError) {
		return new DefiniteIntegral().compute(this, from, to, maxError);
	}

	default double derivativeAt(double x, double error) {
		return new Derivative().compute(this, x, error);
	}

	/** Relevant if this represents a probability distribution */
	default double mean(double from, double to) {
		return new Mean().compute(this, from, to);
	}

	/** Relevant if this represents a probability distribution */
	default double std(double from, double to) {
		//std as mean square error
		double mean = mean(from, to);
		final Function orig = this;
		return Math.sqrt(new Function() {

			@Override
			public double evaluate(double x) {
				return Math.pow(orig.evaluate(x) - mean, 2);
			}
		}.mean(from, to));
	}
}