package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Formatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import linalg.Matrix;
import linalg.Vector;
import ml.model.function.Function;
import ml.ulearning.kmeans.model.EuclideanPoint2D;
import ml.ulearning.kmeans.model.Points2DSet;
import ml.utils.PointsChart;

import org.apache.commons.lang.StringUtils;

import prob.distribution.ContinousDistribution;

public class MLUtils {
	private static NumberFormat nf = NumberFormat.getInstance();
	private static NumberFormat nf6 = NumberFormat.getInstance();
	private static NumberFormat nf12 = NumberFormat.getInstance();
	private static NumberFormat nf4 = NumberFormat.getInstance();
	private static NumberFormat intf = NumberFormat.getIntegerInstance();
	private static NumberFormat nfScientific = new DecimalFormat("0.###E0");
	private static NumberFormat nf6_2 = NumberFormat.getInstance();

	static {
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);

		nf6.setMaximumFractionDigits(6);
		nf6.setMinimumFractionDigits(6);

		nf4.setMaximumFractionDigits(4);
		nf4.setMinimumFractionDigits(4);

		nf12.setMaximumFractionDigits(12);
		nf12.setMinimumFractionDigits(12);

		intf.setGroupingUsed(true);
		
		nf6_2.setMinimumIntegerDigits(6);
		nf6_2.setMaximumIntegerDigits(6);
		nf6_2.setMaximumFractionDigits(2);
		nf6_2.setMinimumFractionDigits(2);
		
	}

	private static NumberFormat integerf = NumberFormat.getInstance();
	static {
		integerf.setMaximumFractionDigits(0);
		integerf.setMinimumFractionDigits(0);
		integerf.setGroupingUsed(true);
	}

	public static String readFromConsole(String message) {
		if (message != null) {
			System.out.print(message);
		}

		try {
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			return bufferRead.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void printDoubleArray(double[] d) {
		for (double e : d) {
			System.out.print(nf.format(e) + "   ");
		}
		System.out.println();
	}

	public static boolean equals(double[] d1, double[] d2, double precision) {
		if (d1.length != d2.length) {
			return false;
		}
		for (int i = 0; i < d2.length; i++) {
			if (Math.abs(d1[i] - d2[i]) > precision) {
				return false;
			}
		}
		return true;
	}

	public static boolean equals(double d1, double d2, double precision) {
		return Math.abs(d1 - d2) < precision;
	}

	public static boolean effectiveEquals(double d1, double d2) {
		return equals(d1, d2, 0.0000001);
	}

	public static String asString(Object o, String... attributeNames) {
		StringBuilder res = new StringBuilder();
		res.append(o.getClass().getSimpleName());
		for (String attributeName : attributeNames) {
			if (attributeName.endsWith("\n")) {
				attributeName = attributeName.replace("\n", "");
				res.append("\n");
			}
			res.append("[").append(attributeName).append(":")
					.append(output(ReflectionHelper.getFieldValue(attributeName, o))).append("]");
		}
		return res.toString();
	}
	
	

	public static String output(Object o) {
		if (o == null) {
			return "null";
		}
		if (o instanceof Date) {
			return DateUtils.format((Date) o);
		}
		if (o instanceof Collection<?>) {
			Collection<?> col = (Collection<?>) o;
			return asCommaSeparated(col);
		}
		if (o instanceof Map<?, ?>) {
			StringBuilder s = new StringBuilder();
			for (Object key : ((Map<?, ?>) o).keySet()) {
				s.append(key).append(":").append(((Map<?, ?>) o).get(key));
			}
			return s.toString();
		}

		return o.toString();
	}

	public static String asCommaSeparated(Collection<?> coll) {
		StringBuilder sb = new StringBuilder("(");
		for (Object o : coll) {
			sb.append(o).append(",");
		}
		return StringUtils.removeEnd(sb.toString(), ",") + ")";
	}

	/** min and max - inclusive */
	public static int random(int min, int max) {
		if (min > max) {
			throw new RuntimeException("min > max");
		}
		if (min == max) {
			return min;
		}
		return min + random(max - min);
	}

	public static double randomDouble(double min, double max) {
		return min + Math.random() * (max - min);
	}

	public static <T> T randomElement(List<T> l) {
		return l.get(random(l.size()) - 1);
	}
	
	public static <T> T randomElement(List<T> l, boolean withReplacement) {
		int i = random(l.size()) - 1;
		T res = l.get(i);
		if(!withReplacement) {
			l.remove(i);
		}
		return res;
	}


	public static String format(Object d) {
		if (d == null) {
			return "N.A.";
		}
		if (d instanceof Integer || d instanceof Long) {
			return intf.format(d);
		}
		if (d instanceof Double && (double) d > 1E5) {
			return nfScientific.format(d);
		}
		if (d instanceof Number) {
			return nf4.format(d);
		}
		return d.toString();
	}

	public static String format6(double d) {
		return nf6.format(d);
	}

	public static String format12(double d) {
		return nf12.format(d);
	}
	
	public static String format6_2(double d) {
		return nf6_2.format(d);
	}

	public static String format(int n) {
		return integerf.format(n);
	}

	public static <T> List<T> minus(List<T> source, T... obj) {
		List<T> res = new ArrayList<T>(source);
		for (T t : obj) {
			res.remove(t);

		}
		return res;
	}

	/**
	 * Given a List of probabilities summing to 1, select randomly an element index based on its probability value
	 * <p>
	 * Ex: Given [0.5,0.3,0.2] select first elem with prob 0.5, second with prob 0.3, third with prob 0.2
	 */
	public static int randomSelectionFromDistribution(List<Double> probs) {
		List<Double> clone = new ArrayList<>(probs);
		Collections.sort(clone);
		Double p = Math.random();
		double sum = 0;
		for (int i = 0; i < clone.size(); i++) {
			sum += probs.get(i);
			if (p < sum) {
				return i;
			}
		}
		throw new RuntimeException("Assertion error. List does not sum to 1? " + probs);
	}

	public static List<Double> generateList(Double startValue, Double endValue, Double step) {
		List<Double> res = new ArrayList<>();
		while (startValue <= endValue) {
			res.add(startValue);
			startValue += step;
		}
		return res;
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
		return (double) res / factorial(k);
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

	public static <T> List<T> randomChoice(List<T> sampleSpace, int count, boolean replacement) {
		List<T> res = new ArrayList<>();
		List<T> copy = new ArrayList<>(sampleSpace);
		for (int i = 0; i < count; i++) {
			T elem = randomElement(copy);
			res.add(elem);
			if (replacement) {
				copy.remove(elem);
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
			res[i] = MLUtils.randomDouble(-1, +1);
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
		if (Math.abs(n.doubleValue()) < 0.00001) {
			return StringUtils.leftPad("0", length);
		}
		return StringUtils.leftPad(nf.format(n), length);
	}

	public static String printIntNumber(Number n, int length) {
		return StringUtils.leftPad(intf.format(n), length);
	}

	public static enum AlignType {
		LEFT, RIGHT, CENTER
	}

	public static boolean trueWithProbability(double p) {
		if (randomDouble(0, 1) < p) {
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
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
			res.append(format(d[i]));
			if (i < d.length - 1) {
				res.append(",");
			} else {
				res.append("]");
			}
		}
		return res.toString();
	}

	public static <T> String toString(List<T> l) {
		StringBuilder res = new StringBuilder();
		l.forEach(x -> res.append(x).append("\n"));
		return res.toString();
	}

	public static <T> String toString(Object[] o) {
		StringBuilder res = new StringBuilder("[");
		for (int i = 0; i < o.length; i++) {
			res.append(format(o[i]));
			if (i < o.length - 1) {
				res.append(",");
			} else {
				res.append("]");
			}
		}
		return res.toString();
	}


	public static double[] concatenate(double d, double[] array) {
		double[] res = new double[array.length + 1];
		res[0] = d;
		for (int i = 1; i < res.length; i++) {
			res[i] = array[i - 1];
		}
		return res;
	}

	public <T> List<T> intersect(List<T> l1, List<T> l2) {
		List<T> res = new ArrayList<>();
		for (T t1 : l1) {
			if (l2.contains(t1)) {
				res.add(t1);
			}
		}
		return res;
	}

	/** Elements in L but not in l */
	public <T> List<T> minus(List<T> L, List<T> l) {
		List<T> res = new ArrayList<>();
		for (T t : L) {
			if (!l.contains(t)) {
				res.add(t);
			}
		}
		return res;
	}
	
	public static List<Double> randomList(int size, double min, double max) {
		List<Double> res = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			res.add(randomDouble(min, max));
		}
		return res;
	}

	public static List<Gender> genders() {
		List<Gender> res = new ArrayList<>();
		res.add(Gender.F);
		res.add(Gender.M);
		return res;
	}

	public enum Gender {
		M, F;
	}

	public static List<String> readFile(String string) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(string)));
			List<String> res = new ArrayList<>();
			String line = null;
			while ((line = br.readLine()) != null) {
				res.add(line);
			}
			return res;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				throw new RuntimeException("",e);
			}
		}
	}
	
	public  static Map<Double, Double> sampleToMap(Function f, double from, double to, int samples) {
		Map<Double, Double> res = new LinkedHashMap<>();
		double step = (to - from) / samples;
		for (int i = 0; i < samples; i++) {
			res.put(from + step * i, f.evaluate(from + step * i));
		}
		return res;
	}

	public  static List<Double> sampleToList(Function f, double from, double to, int samples) {
		List<Double> res = new ArrayList<>();
		double step = (to - from) / samples;
		for (int i = 0; i < samples; i++) {
			res.add(f.evaluate(from + step * i));
		}
		return res;
	}

	public  static List<Double> noisySampleToList(Function f, double from, double to, int samples, double noise) {
		List<Double> res = new ArrayList<>();
		double step = (to - from) / samples;
		for (int i = 0; i < samples; i++) {
			double x = from + step*i;
			double y = f.evaluate(x);
			double gaussianNoise = new Random().nextGaussian() * noise;
			res.add(y + gaussianNoise);
		}
		return res;
	}
	
	public static void printF(String s, Object ... args) {
		System.out.println(MessageFormat.format(s,args));
	}
	
	public static List<Double> readFromResource(String resourceName, String separator, int index) {
		List<Double> res = new ArrayList<>();
		List<String> lines = FileUtils.readLines(resourceName);
		lines.remove(0);
		for (String line : lines) {
			res.add(Double.valueOf(line.split(separator)[index]));
		}
		return res;
	}
	
	public double min(Collection<Double> coll) {
		return coll.stream().min((x,y) -> x.compareTo(y)).get();
	}

	public double max(Collection<Double> coll) {
		return coll.stream().max((x,y) -> x.compareTo(y)).get();
	}
	
	/**
	 * Sample from the given distribution
	 * @return the List of sample results
	 */
	public static List<Double> sample(ContinousDistribution dist, int samples) {
		dist.simulate(samples);
		return dist.getResult().asList();
	}

	public static Object[] removeIndex(Object[] o, int marginalizedIdx) {
		Object[] res = new Object[o.length -1];
		for (int i = 0; i < marginalizedIdx; i++) {
			res[i] = o[i];
		}
		for (int i = marginalizedIdx + 1; i < o.length; i++) {
			res[i-1] = o[i];
		}
		return res;
	}
	
	public static double[] removeIndex(double[] o, int marginalizedIdx) {
		double[] res = new double[o.length -1];
		for (int i = 0; i < marginalizedIdx; i++) {
			res[i] = o[i];
		}
		for (int i = marginalizedIdx + 1; i < o.length; i++) {
			res[i-1] = o[i];
		}
		return res;
	}

	public static <T> List<T> asList(T[] array) {
		List<T> res = new ArrayList<>();
		for (int i = 0; i < array.length; i++) {
			res.add(array[i]);
		}
		return res;
	}

	public static double[] insert(int i, double[] a) {
		double[] res = new double[a.length + 1];
		res[0] = i;
		for (int j = 1; j < res.length; j++) {
			res[j] = a[j-1];
		}
		return res;
	}

	public static double[] randomArray(int n, double min, double max) {
		double[] res = new double[n];
		for (int i = 0; i < res.length; i++) {
			res[i] = randomDouble(min, max);
		}
		return res;
	}
	
	//	public static Matrix randomOrtoghonalMatrix(int size, double minValue, double maxValue) {
	//		double[][] res = new double[size][size];
	//		double[] d0 = randomArray(size, minValue, maxValue);
	//		res[0] = d0;
	//		for (int i = 1; i < size; i++) {
	//			double[] d =  randomArray(size, minValue, maxValue);
	//			double sum = 0;
	//			for (int j = 0; j < d.length-1; j++) {
	//				sum += d0[j] * d[j];
	//			}
	//			d[size-1] = -sum / d0[size-1];
	//			res[i] = d;
	//		}
	//		return new Matrix(res);
	//	}

}