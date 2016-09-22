package ml.model.function;

import static java.lang.Math.*;

import java.math.BigDecimal;

import util.MLUtils;

/**
 *  integral[0..infinity] t^(x-1) * e^(-t) dt 
 * @author Cosmin Zamfir
 *
 */
public class GammaFunction implements Function {

	@Override
	public double evaluate(double x) {
		//Lanczos approximation

		double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
	      double ser = 1.0 + 76.18009173    / (x + 0)   - 86.50532033    / (x + 1)
	                       + 24.01409822    / (x + 2)   -  1.231739516   / (x + 3)
	                       +  0.00120858003 / (x + 4)   -  0.00000536382 / (x + 5);
	      double log = tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
	      return exp(log);
	}

	public static void main(String[] args) {
		GammaFunction g = new GammaFunction();
		int n = 10;
		double d = g.evaluate(n);
		BigDecimal bd = MLUtils.factorialBD(n - 1);
		System.out.println(MLUtils.format6(d - bd.doubleValue())); 

	}

}
