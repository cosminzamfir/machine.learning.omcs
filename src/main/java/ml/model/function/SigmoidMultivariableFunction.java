package ml.model.function;

import linalg.Vector;
import ml.utils.Utils;

/**
 * F(x1,x2,...,xn) = 1 / ( 1 + e ^ -(dotProduct(x,coefficients))
 * @author Cosmin Zamfir
 *
 */
public class SigmoidMultivariableFunction extends DifferentiableMultivariableFunction {

	
	public SigmoidMultivariableFunction(double[] coefficients) {
		this.coefficients = coefficients;
		this.n = coefficients.length;
	}
	
	@Override
	public double evaluate(double... x) {
		double[] copy = Utils.concatenate(1, x);
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
		return firstTerm * x[i+1];
		
	}
}
