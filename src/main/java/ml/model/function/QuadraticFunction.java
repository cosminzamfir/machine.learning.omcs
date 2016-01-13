package ml.model.function;

import java.io.IOException;

public class QuadraticFunction extends Function {

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
		qf.plot(0, 10);
		System.in.read();
		double d = qf.integral(0, 20, 0.01);
	}

}
