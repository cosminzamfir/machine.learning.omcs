package ml.model.function;

import java.io.IOException;

import ml.utils.FunctionChart;

/**
 * f(x) = 1 / ( 1 + e ^ (-x))
 * @author Cosmin Zamfir
 *
 */
public class SigmoidFunction extends AbstractFunction {

	public SigmoidFunction() {
		super();
	}

	@Override
	public double evaluate(double x) {
		return 1 / (1 + Math.pow(Math.E, -x));
	}
	
	public double derivativeAt(double x) {
		double f = evaluate(x);
		return f*(1-f);
	}

	@Override
	public String toString() {
		return "SigmoidFunction: ";
	}

	public static void main(String[] args) throws IOException {
		SigmoidFunction qf = new SigmoidFunction();
		new FunctionChart("Sigmoid", new SigmoidFunction(), -50, 50);
	}

}
