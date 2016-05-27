package util;

/**
 * To allow incrementing doubles in lambda expressions (compiler complains about final)
 * @author eh2zamf
 *
 */
public class DoubleHolder {
	private double value;

	public DoubleHolder(double value) {
		super();
		this.value = value;
	}
	
	public double get() {
		return value;
	}

	public void set(double value) {
		this.value = value;
	}
	
	public void add(double value) {
		this.value += value;
	}
	

}
