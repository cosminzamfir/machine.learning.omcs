package prob.simulation;

import java.util.ArrayList;
import java.util.List;

import linalg.Matrix;
import util.MLUtils;

public class RandCh {

	private int n = 8;
	private Matrix m;

	public static void main(String[] args) {
		int trials = 10000000;
		int successes = 0;
		RandCh r = new RandCh();
		for (int i = 0; i < trials; i++) {
			boolean b = r.runOnce();
			if(b) {
				successes++;
			}
		}
		double d = (double) successes / trials;
		System.out.println(MLUtils.format12(d));
		
	}
	
	public boolean runOnce() {
		m = new Matrix(n, n);
		int x = 0;
		int y = 0;
		m.set(x, y, 1);
		int index = 1;
		while (index < n * n) {
			List<int[]> possibleNextPos = getPossibleNextPos(x, y);
			if (possibleNextPos.isEmpty()) {
				return false;
			}
			int[] nextPos = MLUtils.randomElement(possibleNextPos);
			x = nextPos[0];
			y = nextPos[1];
			m.set(x, y, 1);
			index++;
		}
		return true;
	}

	private List<int[]> getPossibleNextPos(int x, int y) {
		List<int[]> res = new ArrayList<int[]>();
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i < 0 || i > n - 1 || j < 0 || j > n - 1 || m.get(i, j) == 1) {
					continue;
				}
				res.add(new int[] {i,j});				
			}
		}
		return res;
	}
}
