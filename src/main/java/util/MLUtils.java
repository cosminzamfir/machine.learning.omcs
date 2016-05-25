package util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;

public class MLUtils {

	private static NumberFormat nf = NumberFormat.getInstance();
	static {
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
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
			if(Math.abs(d1[i]-d2[i]) > precision) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean equals(double d1, double d2, double precision) {
			return Math.abs(d1-d2) < precision;
	}

}
