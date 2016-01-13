package ml.model.function;


/**
 * 
 * @author Cosmin Zamfir
 *
 */
public class PolynomialMultivariableFunction extends MultivariableFunction {

	/** The coefficients */
	private double[][] a;

	public PolynomialMultivariableFunction(double[][] a) {
		super();
		this.a = a;
	}

	@Override
	public double evaluate(double... xs) {
		double res = 0; 
		for (int i = 0; i < xs.length; i++) {
			double x = xs[i];
			double[] coefficients = a[i];
			for (int j = 0; j < coefficients.length; j++) {
				res = res + coefficients[j]*Math.pow(x, j);
			}
		}
		return res;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("f(x) = ....");
		return sb.toString();
	}
}
