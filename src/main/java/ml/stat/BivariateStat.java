package ml.stat;

import java.util.List;

import util.MLUtils;
import ml.model.function.PolynomialFunction;
import static ml.stat.StatUtils.*;

public class BivariateStat {

	private double meanX;
	private double meanY;
	private double stdX;
	private double stdY;
	private double r;
	private double stdRms;

	public BivariateStat(double meanX, double stdX, double meanY, double stdY, double r) {
		super();
		this.meanX = meanX;
		this.meanY = meanY;
		this.stdX = stdX;
		this.stdY = stdY;
		this.r = r;
		this.stdRms = Math.sqrt(1-r*r)*stdY;
	}

	public BivariateStat(List<Double> x, List<Double> y) {
		r = correlation(x, y);
		stdX = std(x);
		stdY = std(y);
		meanX = mean(x);
		meanY = mean(y);
	}
	
	public PolynomialFunction regressionLine() {
		double slope = r * stdY / stdX;
		double interceptY = meanY - meanX * slope;
		PolynomialFunction res = new PolynomialFunction(interceptY, slope);
		MLUtils.printF("Correlation: {0}\nStdX: {1}\nStdY: {2}\nmeanX: {3}\nmeanY: {4}\nslope: {5}\ninterceptY: {6}\nRegression line: {7}\nrms: {8}",
				r, stdX, stdY, meanX, meanY, slope, interceptY, res);
		return res;

	}

	public double getMeanX() {
		return meanX;
	}

	public double getMeanY() {
		return meanY;
	}

	public double getStdX() {
		return stdX;
	}

	public double getStdY() {
		return stdY;
	}

	public double getR() {
		return r;
	}
	
	public double getStdRms() {
		return stdRms;
	}
}
