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
		StringBuilder sb = new StringBuilder("f(x) = ");
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				if(a[i][j] != 0) {
					if(j == 0) {
						sb.append(" + " + a[i][j]);
					}
					if(j == 1) {
						sb.append(" + " + a[i][j] + "*x" + i);
					}
					if(j > 1) {
						sb.append(" + " + a[i][j] + "*x" + i + "^" + j);
					}

				}
			}
		}
		return sb.toString();
	}
}
