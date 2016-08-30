package ml.model.function;

/** When {@link AbstractFunction} is a probability distribution */
public class Mean {

	public double compute(Function function, double from, double to) {
		int intervals = 10;
		double current;
		double next;
		do {
			current = compute(function, from, to, intervals);
			intervals *= 2;
			next = compute(function, from, to, intervals);
		} while(Math.abs(current - next) > 0.0000001);
		return next;
	}

	/** x's weighted by f(x) value */
	private double compute(Function function, double from, double to, int intervals) {
		double total = 0;
		for (int i = 0; i < intervals; i++) {
			double start = from + (to - from) * i /intervals;
			double end = start + (to - from)/intervals;
			total = total + (function.definiteIntegral(start, end, 0.000001)) * (start + end)/2;
		}
		return total;
	}

}
