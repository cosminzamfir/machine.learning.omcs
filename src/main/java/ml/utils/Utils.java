package ml.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ml.ulearning.kmeans.model.EuclideanPoint2D;
import ml.ulearning.kmeans.model.Points2DSet;

import org.apache.commons.lang.StringUtils;

public class Utils {

	public static NumberFormat nf;

	static {
		nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setGroupingUsed(false);
	}

	public static final List<Integer> orderedList(int n) {
		List<Integer> res = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			res.add(i);
		}
		return res;
	}

	public static final List<Integer> randomPermutation(int n) {
		List<Integer> l = orderedList(n);
		Collections.shuffle(l);
		return l;
	}

	public static long factorial(int n) {
		long result = 1;
		for (int i = 1; i <= n; i++) {
			result *= i;
		}
		return result;
	}

	public static BigDecimal factorialBD(int n) {
		BigInteger result = BigInteger.ONE;
		for (int i = 1; i <= n; i++) {
			result = result.multiply(BigInteger.valueOf(i));
		}
		return new BigDecimal(result);
	}

	/**
	 * Approximation of n! using the Stirling formula
	 */
	public static final long stirlingFactorial(int n) {
		return (long) (Math.pow(n, n) / Math.pow(Math.E, n) * Math.sqrt(2 * Math.PI * n));
	}

	public static final BigDecimal stirlingFactorialBD(int n) {
		return BigDecimal.valueOf(n).pow(n).divide(BigDecimal.valueOf(Math.E).pow(n), 6, RoundingMode.HALF_UP)
				.multiply(BigDecimal.valueOf(Math.sqrt(2 * Math.PI * n)));
	}

	public static long downTo(int n, int k) {
		long res = 1;
		for (int i = n; i >= k; i--) {
			res *= i;
		}
		return res;
	}

	public static BigDecimal downToBD(int n, int k) {
		BigInteger res = BigInteger.ONE;
		for (int i = n; i >= k; i--) {
			res = res.multiply(BigInteger.valueOf(i));
		}
		return new BigDecimal(res);
	}

	public static double choose(int n, int k) {
		long res = n;
		for (int i = 1; i < k; i++) {
			res = res * (n - i);
		}
		return (double)res / factorial(k);
	}

	public static BigDecimal chooseBD(int n, int k) {
		return downToBD(n, n - k + 1).divide(factorialBD(k), 6, RoundingMode.HALF_UP);
	}

	/**
	 * The probability that in a Bernoully experiment of n trials with success probability p, there are exactly k successes
	 */
	public static double binomial(int n, int k, double p) {
		return choose(n, k) * Math.pow(p, k) * Math.pow(1 - p, n - k);
	}

	public static BigDecimal binomialBD(int n, int k, double p) {
		return chooseBD(n, k).multiply(BigDecimal.valueOf(p).pow(k)).multiply(BigDecimal.valueOf(1 - p).pow(n - k));
	}

	/**
	 * binomial distribution using Stirling formula for factorials 
	 */
	public static double binomialStirling(int n, int k, double p) {
		return chooseStirling(n, k) * Math.pow(p, k) * Math.pow(1 - p, n - k);
	}

	public static BigDecimal binomialStirlingBD(int n, int k, double p) {
		return chooseStirlingBD(n, k).multiply(BigDecimal.valueOf(p).pow(k)).multiply(BigDecimal.valueOf(1 - p).pow(n - k));
	}

	/**
	 * Choose(n,k) using Striling formula for factorials
	 * @param n
	 * @param k
	 * @return
	 */
	private static double chooseStirling(int n, int k) {
		return stirlingFactorial(n) / (double) stirlingFactorial(k) / (double) stirlingFactorial(n - k);
	}

	private static BigDecimal chooseStirlingBD(int n, int k) {
		return stirlingFactorialBD(n).divide(stirlingFactorialBD(k), 6, RoundingMode.HALF_UP).divide(stirlingFactorialBD(n - k), 6, RoundingMode.HALF_UP);
	}

	/**
	 * Radnom int from 1 to n, uniform distribution
	 * @param max
	 * @return
	 */
	public static int random(int max) {
		return new Random().nextInt(max) + 1;
	}

	public static double randomDouble(double min, double max) {
		return min + Math.random() * (max - min);
	}

	public static <T> T randomElement(List<T> l) {
		return l.get(random(l.size()) - 1);
	}

	public static <T> List<T> minus(List<T> l, T... elems) {
		List<T> res = new ArrayList<>(l);
		for (T t : elems) {
			res.remove(t);
		}
		return res;

	}

	public static <T> List<T> randomChoice(List<T> sampleSpace, int count, boolean replacement) {
		List<T> res = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			T elem = randomElement(sampleSpace);
			res.add(elem);
			if (replacement) {
				sampleSpace.remove(elem);
			}
		}
		return res;
	}

	public static int count(List<? extends Object> l, Object o) {
		int res = 0;
		for (Object elem : l) {
			if (elem.equals(o)) {
				res++;
			}
		}
		return res;
	}

	public static double[] randomArray(int size) {
		double[] res = new double[size];
		for (int i = 0; i < size; i++) {
			res[i] = Math.random();
		}
		return res;
	}

	public static double[][] randomDiscreteDistribution(int n) {
		double[][] res = new double[n][2];
		for (int i = 0; i < n; i++) {
			res[i][0] = i;
			res[i][1] = Math.random();
		}
		return res;
	}

	public static void showClusterChart(Map<EuclideanPoint2D, List<EuclideanPoint2D>> map) {
		List<double[][]> series = Points2DSet.asListOfArrays(map);
		new PointsChart("k-means", series);
	}

	public static void appendAligned(StringBuilder sb, Object o, int length, AlignType alignType) {
		if (alignType == AlignType.RIGHT) {
			sb.append(StringUtils.leftPad(o.toString(), length));
		} else if (alignType == AlignType.LEFT) {
			sb.append(StringUtils.rightPad(o.toString(), length));
		} else {
			sb.append(StringUtils.center(o.toString(), length));
		}
	}

	public static void appendAligned(StringBuilder sb, Collection<?> coll, int length, AlignType alignType) {
		for (Object o : coll) {
			appendAligned(sb, o, length, alignType);
		}
	}

	public static String printNumber(Number n, int length) {
		if(Math.abs(n.doubleValue()) < 0.00001) {
			return StringUtils.leftPad("0", length);
		}
		return StringUtils.leftPad(nf.format(n), length);
	}

	public static enum AlignType {
		LEFT, RIGHT, CENTER
	}
	
	public static boolean trueWithProbability(double p) {
		if(randomDouble(0, 1) < p) {
			return true;
		} else {
			return false;
		}
	}

	public static List<Integer> months() {
		return orderedList(12);
	}
	
	public static double log2(double x) {
		return Math.log(x) / Math.log(2);
	}
	
	public static List clone(List input, int start, int end) {
		List res = new ArrayList<>();
		for (int i = start; i < end; i++) {
			res.add(input.get(i));
		}
		return res;

	}
	
	public static <T> String toString(double[] d) {
		StringBuilder res = new StringBuilder("[");
		for (int i = 0; i < d.length; i++) {
			res.append(d[i]);
			if(i < d.length -1) {
				res.append(",");
			} else {
				res.append("]");
			}
		}
		return res.toString();
	}
	
}
