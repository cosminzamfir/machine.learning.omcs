package ml.stat;

import static util.MLUtils.*;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import org.apache.log4j.Logger;

import ml.model.function.Function;
import ml.model.function.NormalFunction;
import ml.model.function.PolynomialFunction;
import ml.model.function.Solver;
import prob.chart.ChartBuilder;
import prob.chart.PlotType;
import prob.chart.ScatterChart;
import util.DoubleHolder;
import util.MLUtils;

public class StatUtils {

	private static final Logger log = Logger.getLogger(StatUtils.class);

	public static double mean(List<Double> l) {
		DoubleHolder d = new DoubleHolder(0);
		l.forEach((e) -> d.add(e / l.size()));
		return d.get();
	}

	public static double std(List<Double> l) {
		DoubleHolder res = new DoubleHolder(0);
		double mean = mean(l);
		l.forEach((e) -> res.add(Math.pow(e - mean, 2)));
		return Math.sqrt(res.get()/l.size());
	}

	public static List<Double> asStandardUnits(List<Double> l) {
		double mean = mean(l);
		double std = std(l);
		List<Double> res = new ArrayList<Double>();
		l.forEach((e) -> res.add((e - mean) / std));
		return res;
	}

	private static double dotProduct(List<Double> l1, List<Double> l2) {
		double res = 0;
		for (int i = 0; i < l1.size(); i++) {
			res += l1.get(i) * l2.get(i);
		}
		return res;
	}

	private static List<Double> product(List<Double> l1, List<Double> l2) {
		List<Double> res = new ArrayList<>();
		for (int i = 0; i < l1.size(); i++) {
			res.set(0, l1.get(i) * l2.get(i));
		}
		return res;
	}

	public static double correlation(List<Double> l1, List<Double> l2) {
		if (l1.size() != l2.size()) {
			throw new RuntimeException("Lists must have the same size");
		}
		List<Double> s1 = asStandardUnits(l1);
		List<Double> s2 = asStandardUnits(l2);
		return dotProduct(s1, s2);
	}

	public static double covariation(List<Double> l1, List<Double> l2) {
		return mean(product(l1, l2)) - mean(l1) * mean(l2);
	}

	public static double correlation(Function f1, Function f2, double from, double to, int samples) {
		return correlation(sampleToList(f1, from, to, samples), sampleToList(f1, from, to, samples));
	}

	public static double covariation(Function f1, Function f2, double from, double to, int samples) {
		return covariation(sampleToList(f1, from, to, samples), sampleToList(f1, from, to, samples));
	}

	/**
	 * The regresison line of a bi-variate series data (X,Y):
	 * <ul>
	 * <li>crosses the point of averages
	 * <li>has the slope = correlation * std(Y) / std(X)
	 * <ul> 
	 * @return the first grade {@link PolynomialFunction} representing the regression line
	 */
	public static PolynomialFunction regressionLine(List<Double> x, List<Double> y) {
		double r = correlation(x, y);
		double stdX = std(x);
		double stdY = std(y);
		double meanX = mean(x);
		double meanY = mean(y);
		double slope = r * stdY / stdX;
		double interceptY = meanY - meanX * slope;
		PolynomialFunction res = new PolynomialFunction(interceptY, slope);
		double rms = rms(res, x, y);
		MLUtils.printF("Correlation: {0}\nStdX: {1}\nStdY: {2}\nmeanX: {3}\nmeanY: {4}\nslope: {5}\ninterceptY: {6}\nRegression line: {7}\nrms: {8}",
				r, stdX, stdY, meanX, meanY, slope, interceptY, res, rms);
		return res;
	}

	public static double rms(PolynomialFunction regressionLine, List<Double> X, List<Double> Y) {
		double res = 0;
		for (int i = 0; i < X.size(); i++) {
			res += Math.pow(regressionLine.evaluate(X.get(i) - Y.get(i)), 2);
		}
		return Math.sqrt(res / X.size());
	}

	/**
	 * Compute the percentile of the given val for a normal distribution with given mean and std
	 */
	public static double normalValueToPercentile(double miu, double sigma, double val) {
		return new NormalFunction(miu, sigma).definiteIntegral(miu - 15 * sigma, val, 0.0001);
	}

	/**
	 * Compute the value of the point with the given percentile for a normal distribution with given mean and std
	 */
	public static double normalPercentileToValue(double miu, double sigma, double percentile) {
		Function f = new Function() {
			@Override
			public double evaluate(double x) {
				return new NormalFunction(miu, sigma).definiteIntegral(miu - 15 * sigma, x, 0.0000001) - percentile;
			}
		};
		return new Solver().solve(f, 0, 0.0000001, miu + 0.5 * sigma);
	}

	public static double regressionEstimate(BivariateStat bivariateStat, double x) {
		double xStandardUnits = (x - bivariateStat.getMeanX()) / bivariateStat.getStdY();
		double yStandardUnits = xStandardUnits * bivariateStat.getR();
		return bivariateStat.getMeanY() + yStandardUnits * bivariateStat.getStdY();
	}
	

	public static void main(String[] args) {
		final Function nf = new NormalFunction(0, 1);
		Function f = new Function() {
			@Override
			public double evaluate(double x) {
				return nf.evaluate(x) * x * x;
			}
		};
		double std = f.definiteIntegral(-10, 10, 0.000001);
		System.out.println(std);
	}
}
