package hw3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HW3 {

	/** The possible 3 intervals where x can be placed in the given even sized array */
	private List<Interval> getIntervals(int[] array) {
		List<Interval> res = new ArrayList<Interval>();
		int size = array.length;
		res.add(new Interval(Integer.MIN_VALUE, array[size/2 -1], IntervalType.Left));
		res.add(new Interval(array[size/2 -1],array[size/2], IntervalType.Median));
		res.add(new Interval(array[size/2], Integer.MAX_VALUE, IntervalType.Right));
		return res;
	}
	
	private int[] addMissingValue(int[] a, int posX, int x) {
		int[] res = new int[a.length + 1];
		for (int i = 0; i < posX; i++) {
			res[i] = a[i];
		}
		res[posX] = x;
		for(int i = posX; i<a.length; i++) {
			res[i+1] = a[i];
		}
		return res;
		
	}
	
	private Result getBestResult(int[] a, int[] b, int posX, int posY, int medianDiff, Interval aInterval, Interval bInterval) {
		if(aInterval.getType() != IntervalType.Median && bInterval.getType() != IntervalType.Median) {
			return getBestResultWithXYOutsideMedianInterval(a,b,posX, posY, medianDiff, aInterval, bInterval);
		}
		
		if(aInterval.getType() == IntervalType.Median && bInterval.getType() == IntervalType.Median) {
			return getBestResultWithXYInsideMedianInterval(a,b,posX, posY, medianDiff, aInterval, bInterval);
		}
		return null;
	}
	
	/**
	 * Restrictions on X and Y:
	 * 1. They are in some intervals
	 * 2. The difference between them = k
	 * 3. The 
	 */
	private Result getBestResultWithXYInsideMedianInterval(int[] a, int[] b, int posX, int posY, int medianDiff, Interval aInterval, Interval bInterval) {
		int bestX = Integer.MIN_VALUE;
		int bestY = Integer.MIN_VALUE;
		int bestNorm = Integer.MAX_VALUE;
		for (int x = aInterval.getMin(); x < aInterval.getMax(); x++) {
			for (int y = bInterval.getMin(); y < bInterval.getMax(); y++) {
				if(Math.abs(x - y) != medianDiff) {
					continue;
				}
				bestNorm = Math.min(bestNorm, getMaxNorm(addMissingValue(a, posX, x), addMissingValue(b, posY, y)));
			}
		}
		if(bestX == Integer.MIN_VALUE) {
			return null;
		}
		return new Result(bestX, bestY, bestNorm);
			
	}

	private Result getBestResultWithXYOutsideMedianInterval(int[] a, int[] b, int posX, int posY, int medianDiff, Interval aInterval, Interval bInterval) {
		int aMedian = getMedian(a, aInterval.getType());
		int bMedian = getMedian(b, bInterval.getType());
		if(Math.abs(aMedian - bMedian) != medianDiff) {
			return null; //no solution
		}
		if(posX == posY) { //minimize maxNorm if the diff between the 2 elemes are as small as possible 
			int[] closestTwoPoints = Interval.getClosestTwoPoints(aInterval, bInterval);
			int x = closestTwoPoints[0];
			int y = closestTwoPoints[1];
			return new Result(x, y, getMaxNorm(addMissingValue(a, posX, x), addMissingValue(b, posY, y)));
		} else {
			int x = aInterval.getClosestPoint(addMissingValue(b, posY, -999)[posX]);
			int y = bInterval.getClosestPoint(addMissingValue(a, posX, -999)[posX]);
			return new Result(x, y, getMaxNorm(addMissingValue(a, posX, x), addMissingValue(b, posY, y)));
		}
	}


	private int getMaxNorm(int[] a, int[] b) {
		int res = 0;
		for (int i = 0; i < b.length; i++) {
			if(Math.abs(a[i] - b[i]) > res) {
				res = Math.abs(a[i] - b[i]);
			}
		}
		return res;
	}

	private int getMedian(int[] a, IntervalType type) {
		if(type == IntervalType.Left) {
			return getMedianWithXOnLeftInterval(a);
		} else if(type == IntervalType.Right) {
			return getMedianWithXOnTheRightInterval(a);
		}
		throw new RuntimeException();
	}

	/** 1 x 2 3 4 5 6 => median is 4 */
	private int getMedianWithXOnLeftInterval(int[] a) {
		return sort(clone(a))[a.length/2 -1];
	}
	/** 1 2 3 4 5 x 6 */
	private int getMedianWithXOnTheRightInterval(int[] a) {
		return sort(clone(a))[a.length/2];
	}

	private int[] clone(int[] a) {
		int[] res = new int[a.length];
		for (int i = 0; i < res.length; i++) {
			res[i] = a[i];
		}
		return res;
	}
	
	private int[] sort(int[] a) {
		Arrays.sort(a);
		return a;
	}
	
	public static void main(String[] args) {
		int[] a = new int[] {-70,110};
		int[] b = new int[] {32,-240};
		int posX = 1;
		int posY = 1;
		int medianDiff = 115;
		HW3  h = new HW3();
		List<Interval> aIntervals = h.getIntervals(a);
		List<Interval> bIntervals = h.getIntervals(b);
		for (int i = 0; i < aIntervals.size(); i++) {
			for (int j = 0; j < bIntervals.size(); j++) {
				Result result = h.getBestResult(a, b, posX, posY, medianDiff,aIntervals.get(i), bIntervals.get(j));
				System.out.println(result);
			}
		}
		
	}
}
