package ml.model.function;

import linalg.Vector;
import util.MLUtils;

/**
 * F(x1,x2,...,xn) = 1 / ( 1 + e ^ -(dotProduct(x,coefficients))
 * @author Cosmin Zamfir
 *
 */
public class SigmoidMultivariableFunction extends DifferentiableMultivariableFunction {

	
	/** The first coefficient refers to synthetic variable x0, always equal to 1 */
	protected double[] coefficients;

	public SigmoidMultivariableFunction(double[] coefficients) {
		this.coefficients = coefficients;
		this.n = coefficients.length;
	}
	
	public void generateRandomCoefficients(double minValue, double maxValue) {
		coefficients = new double[n];
		for (int i = 0; i < this.n; i++) {
			coefficients[i] = MLUtils.randomDouble(minValue, maxValue);
		}
	}
	
	public double[] getCoefficients() {
		return coefficients;
	}

	
	@Override
	public double evaluate(double... x) {
		double[] copy = MLUtils.concatenate(1, x);
		double dotProduct = new Vector(copy).dotProduct(new Vector(coefficients));
		return 1 / (1 + Math.pow(Math.E, -dotProduct));
	}
	
	
	@Override
	public double partialDerivativeAt(double[] x, int i) {
		//dS/dx = S(x) * (1 - S(x)), where S is the sigmoid function 
		//using chain rule: dThis/dwi = dThis/d(x*w) * d(x*w)/dw = evaluate(x) * (1 - evaluate(x)) * xi 
		double f = evaluate(x); 
		double firstTerm = f * (1-f);
		if(i == 0) {
			return firstTerm * 1;
		} 
		return firstTerm * x[i];
		
	}
}
