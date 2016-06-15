package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

public class MLUtils {
	private static NumberFormat nf = NumberFormat.getInstance();
	private static NumberFormat nf6 = NumberFormat.getInstance();
	private static NumberFormat nfs = new DecimalFormat("0.###E0");
	static {
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);

		nf6.setMaximumFractionDigits(5);
		nf6.setMinimumFractionDigits(5);
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

	public static int random(int max) {
		return new Random().nextInt(max) + 1;
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

	public static String format(Object d) {
		if(d == null) {
			return "N.A.";
		}
		if(d instanceof Double && (double)d>1E5) {
			return nfs.format(d);
		} if(d instanceof Number) {
			return nf.format(d);
		} return d.toString();
	}

	public static String format6(double d) {
		return nf6.format(d);
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
			if(p < sum) {
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

}
