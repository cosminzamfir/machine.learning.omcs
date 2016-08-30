package ml.model.function;

import java.io.IOException;

/**
 * Standard normal(sigma=1; miu=0): F(x) = sqrt(1/2*PI) * e ^ (-x^2)
 * <p>
 * General normal: F(x) = 1 / sigma * sqrt(2*PI) * e ^ (-(x-miu)^2 / 2 * sigma ) 
 * @author Cosmin Zamfir
 *
 */
public class NormalFunction implements Function {

	private double miu, sigma;

	public NormalFunction(double miu, double sigma) {
		super();
		this.miu = miu;
		this.sigma = sigma;
	}

	@Override
	public double evaluate(double x) {
		return 1 / (Math.sqrt(2 * Math.PI) * sigma) * Math.pow(Math.E, -(x - miu) * (x - miu) / (2 * sigma * sigma));
	}

	@Override
	public String toString() {
		return "NormalDist : " + miu + " - " + sigma;
	}

	public static void main(String[] args) throws IOException {
		NormalFunction qf = new NormalFunction(0,1);
		double d = qf.definiteIntegral(-10, 10, 0.00000001);
		double mean = qf.mean(-10, 10);
		double std = qf.std(-10, 10);
		System.out.println("Integral is: " + d);
		System.out.println("Mean is: " + mean);
		System.out.println("Std is: " + std);
	}

}
