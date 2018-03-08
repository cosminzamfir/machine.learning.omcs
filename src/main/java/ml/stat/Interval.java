package ml.stat;

public class Interval {

	private double min;
	private double max;

	public Interval(double min, double max) {
		super();
		this.min = min;
		this.max = max;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

}
