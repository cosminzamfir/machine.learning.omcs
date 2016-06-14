package ml.model.function;

/**
 * 
 * @author Cosmin Zamfir
 *
 */
public class Derivative {

	public double compute(Function f, double x, double error) {
		double epsilon = 1;
		double prev = Double.MAX_VALUE;
		double current;
		while(Math.abs((current = (evaluate(f, x, epsilon))) - prev) > error ) {
			prev = current;
			epsilon = epsilon/2;
		}
		return current;
	}
	
	private double evaluate(Function f, double x, double epsilon) {
		return (f.evaluate(x+epsilon) - f.evaluate(x-epsilon)) / (2*epsilon);
	}
	
	public static void main(String[] args) {
		double derivative = new Derivative().compute((x) -> x*x, 100, 0.001);
		System.out.print(derivative);
	}
}
