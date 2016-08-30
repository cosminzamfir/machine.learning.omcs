package ml.model.function;

/**
 * 
 * @author Cosmin Zamfir
 *
 */
public class Solver {

	public double solve(Function f, double target, double error) {
		return solve(f, target, error, 0.5);
	}

	public double solve(Function f, double target, double error, double initialGuess) {
		double err = Double.MAX_VALUE;
		double x0 = initialGuess;

		do {
			double f0 = f.evaluate(x0);
			double deltaf = target - f0;
			double derivative = new Derivative().compute(f, x0, 0.000001);
			double deltax = deltaf / derivative;
			x0 = x0 + deltax;
			err = target - f.evaluate(x0);
		} while (Math.abs(err) > error);
		return x0;
	}
	
	
	public static void main(String[] args) {
		double x0 = new Solver().solve((x) -> x*x, 100, 0.001);
		System.out.println(x0);
	}
}
