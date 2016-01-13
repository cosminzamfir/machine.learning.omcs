package ml.model.function;

import java.io.IOException;

public class NormalFunction extends Function {

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
		double d = qf.integral(-2, 2, 0.00001);
		System.out.println("Integral is: " + d);
	}

}
