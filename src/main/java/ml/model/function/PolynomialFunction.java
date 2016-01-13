package ml.model.function;

import java.io.IOException;

/**
 * 
 * @author Cosmin Zamfir
 *
 */
public class PolynomialFunction extends Function {

	/** The coefficients */
	private double[] a;

	public PolynomialFunction(double... a) {
		super();
		this.a = a;
	}

	@Override
	public double evaluate(double x) {
		double res = 0;
		for (int i = 0; i < a.length; i++) {
			res += a[i] * Math.pow(x, i);
		}
		return res;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("f(x) = ");
		for (int i = 0; i < a.length; i++) {
			if (a[i] != 0) {
				if (i > 0) {
					sb.append(" + ");
				}
				if (i == 0) {
					sb.append(a[i]);
				} else if(i==1) {
					sb.append(a[i] + "*x");
				} else {
					sb.append(a[i] + "*x^" + i);
				}
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) throws IOException {
		PolynomialFunction qf = new PolynomialFunction(1, 1, 1);
		qf.plot(0, 10);
		System.in.read();
		double d = qf.integral(0, 20, 0.01);
	}

}
