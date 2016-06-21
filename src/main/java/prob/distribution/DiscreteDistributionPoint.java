package prob.distribution;

import java.text.NumberFormat;
import java.util.List;

/**
 * One pair (value, P(X=value)) for a discrete distribution
 * @author eh2zamf
 *
 */
public class DiscreteDistributionPoint<T> {

	private static NumberFormat nf = NumberFormat.getInstance();
	
	/**The value. Might be an object representing and event or a number representing a random variable */
	public T value;

	/** The probablity that X=value */
	public double p;

	public DiscreteDistributionPoint(T value, double p) {
		super();
		this.value = value;
		this.p = p;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static double[][] asArray(List<DiscreteDistributionPoint> points) {
		double[][] res = new double[points.size()][2];
		int index = 0;
		for (DiscreteDistributionPoint<Double> point : points) {
			res[index][0] = (double) point.getValue();
			res[index++][1] = point.getP();
		}
		return res;
	}
	
	@Override
	public String toString() {
		return "[" + value + ":" +  nf.format(p) + "]";
	}

}
