package ml.slearning.classification.logreg;

public class QuadraticKernel implements Kernel {

	@Override
	public double[] transform(double[] x) {
		double[] res = new double[x.length * 2 + 1];
		res[0] = 1;
		for (int i = 1; i <= x.length; i++) {
			res[i] = x[i-1];
			res[i + x.length] = x[i-1] * x[i-1];
		}
		return res;
	}

}
