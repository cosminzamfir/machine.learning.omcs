package ml.model.function;

import org.apache.log4j.Logger;

/**
 * 
 * @author Cosmin Zamfir
 *
 */
public class DefiniteIntegral {

	private static final Logger log = Logger.getLogger(DefiniteIntegral.class);

	public double compute(Function function, double from, double to, double maxError) {
		int intervals = 10;
		double res = computeIntegral(function, from, to, intervals);
		double newRes;
		double prevRes;
		do {
			newRes = computeIntegral(function, from, to, ++intervals);
			prevRes = res;
			if (log.isDebugEnabled()) {
				log.debug("Intervals:" + intervals + ":Current result: " + newRes + ";Previous result: " + res + ";Error: " + (newRes - res));
			}
			res = newRes;
		} while (Math.abs(newRes - prevRes) > maxError);
		return newRes;
	}

	private double computeIntegral(Function function, double from, double to, int intervals) {
		double res = 0;
		double intervalSize = (to - from) / intervals;
		double start = from;
		for (int i = 0; i < intervals; i++) {
			res += (function.evaluate(start) + function.evaluate(start + intervalSize)) / 2 * intervalSize;
			start = start + intervalSize;
		}
		return res;
	}
}
