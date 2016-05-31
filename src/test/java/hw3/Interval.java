package hw3;

public class Interval {

	private int min;
	private int max;
	private IntervalType type;

	public Interval(int min, int max, IntervalType type) {
		super();
		this.min = min;
		this.max = max;
		this.type = type;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public IntervalType getType() {
		return type;
	}

	public static int[] getClosestTwoPoints(Interval aInterval, Interval bInterval) {
		int[] res = new int[2];

		Interval first = aInterval.min < bInterval.min ? aInterval : bInterval;
		Interval second = aInterval.min < bInterval.min ? bInterval : aInterval;
		if (second.min < first.max) {
			res[0] = res[1] = second.min;
		} else {
			if (first == aInterval) {
				res[0] = first.max;
				res[1] = second.min;
			} else {
				res[1] = first.max;
				res[0] = second.min;
				
			}
		}
		return res;
	}
	
	public int getClosestPoint(int x) {
		if(x < min) {
			return min;
		} 
		if(x > max) {
			return max;
		}
		return x;
	}
	
	@Override
	public String toString() {
		return "[" + min + "-" + max + "]:" + type; 
	}

}
