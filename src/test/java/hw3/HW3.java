package hw3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HW3 {

	private static int[] addMissingValue(int[] a, int posX, int x) {
		int[] res = new int[a.length + 1];
		for (int i = 0; i < posX; i++) {
			res[i] = a[i];
		}
		res[posX] = x;
		for (int i = posX; i < a.length; i++) {
			res[i + 1] = a[i];
		}
		return res;

	}

	private static int getMaxNorm(int[] a, int[] b) {
		int res = 0;
		for (int i = 0; i < b.length; i++) {
			if (Math.abs(a[i] - b[i]) > res) {
				res = Math.abs(a[i] - b[i]);
			}
		}
		return res;
	}

	private static int getMedian(int[] a) {
		return sort(clone(a))[a.length / 2 - 1];
	}

	private static int[] clone(int[] a) {
		int[] res = new int[a.length];
		for (int i = 0; i < res.length; i++) {
			res[i] = a[i];
		}
		return res;
	}

	private static int[] sort(int[] a) {
		Arrays.sort(a);
		return a;
	}

	public static void main(String[] args) {
		int[] a = new int[] { -70, 110 };
		int[] b = new int[] { 32, -240 };
		int posX = 1;
		int posY = 1;
		int medianDiff = 115;
		int bestX = 0;
		int bestY = 0;
		int minNorm = Integer.MAX_VALUE;
		for (int i = -1000; i < 1000; i++) {
			for (int j = -1000; j < 1000; i++) {
				int norm = getMaxNorm(a, b);
				if(norm < minNorm) {
					minNorm = norm;
					bestX = i;
					bestY = j;
				}
			}
		}
		System.out.println("bestX=" + bestX);
		System.out.println("bestX=" + bestY);
		System.out.println("L=" + minNorm);
	}
}
