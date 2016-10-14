package ml.model.function;


/**
 * 
 * @author Cosmin Zamfir
 *
 */
public class PolynomialMultivariableFunction extends DifferentiableMultivariableFunction {

	/** The free coeficient */
	private double c;
	/** The coefficients   
	 * a[i] represents the coefficients for the xi 
	 * */
	private double[][] a;

	/** 
	 * @param c the free coeficient
	 * @param a a[i] are the coefficients of xi
	 */
	public PolynomialMultivariableFunction(double c, double[][] a) {
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
				res = res + coefficients[j]*Math.pow(x, (j+1));
			}
		}
		return res + c;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("f(x) = ");
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				if(a[i][j] != 0) {
					if(j == 0) {
						sb.append(" + " + a[i][j] + "*x" + (i+1));
					}
					if(j > 0) {
						sb.append(" + " + a[i][j] + "*x" + (i+1) + "^" + (j+1));
					}

				}
			}
		}
		return sb.toString();
	}

	@Override
	public double partialDerivativeAt(double[] x, int i) {
		// TODO Auto-generated method stub
		return 0;
	}
}
