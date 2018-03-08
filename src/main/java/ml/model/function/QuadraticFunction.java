package ml.model.function;

import java.io.IOException;

/**
 * 
 * @author Cosmin Zamfir
 *
 */
public class QuadraticFunction implements Function {

	private double a, b, c;

	public QuadraticFunction(double a, double b, double c) {
		super();
		this.a = a;
		this.b = b;
		this.c = c;
	}

	@Override
	public double evaluate(double x) {
		return a * x * x + b * x + c;
	}
	
	@Override
	public String toString() {
		return a + "*x^2 + " + b + "*x + " + c;
	}
	
	public static void main(String[] args) throws IOException {
		QuadraticFunction qf = new QuadraticFunction(1, 1, 1);
		qf.plot(0, 10, 1000);
		System.in.read();
		double d = qf.definiteIntegral(0, 20, 0.001);
	}

}
