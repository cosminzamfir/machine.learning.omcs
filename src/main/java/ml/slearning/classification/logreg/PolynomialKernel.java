package ml.slearning.classification.logreg;

public class PolynomialKernel implements Kernel {

	private int grade;
	
	public PolynomialKernel(int grade) {
		this.grade = grade;
	}


	@Override
	public double[] transform(double[] x) {
		double[] res = new double[x.length * grade + 1];
		res[0] = 1;
		for (int i = 1; i <= x.length; i++) {
			for (int g = 1; g <= grade; g++) {
				res[i + (g-1)*x.length] = Math.pow(x[i-1], g);
			}
		}
		return res;
	}

}
