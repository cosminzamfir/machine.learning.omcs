package ml.model.function;

import org.apache.log4j.Logger;

import ml.utils.FunctionChart;

public abstract class Function {

	private static final Logger log = Logger.getLogger(Function.class);
	public abstract double evaluate(double x);
	
	public void plot(double from ,double to) {
		new FunctionChart(this, from, to, toString());
	}
	
	public double integral(double from, double to, double maxError) {
		int intervals= 10;
		double res = computeIntegral(from, to , intervals);
		double newRes;
		double prevRes;
		do  {
			newRes = computeIntegral(from, to , ++intervals); 
			prevRes = res;
			log.debug("Intervals:" + intervals + ":Current result: " + newRes + ";Previous result: " + res + ";Error: " + (newRes - res));
			res = newRes;
		} while (Math.abs(newRes - prevRes) > maxError);
		return newRes;
	}

	private double computeIntegral(double from, double to, int intervals) {
		double res = 0;
		double intervalSize = (to-from)/intervals;
		double start = from;
		for (int i = 0; i < intervals; i++) {
			res += (evaluate(start)+ evaluate(start+intervalSize))/ 2 * intervalSize;
			start = start + intervalSize;
		}
		return res;
	}
}
