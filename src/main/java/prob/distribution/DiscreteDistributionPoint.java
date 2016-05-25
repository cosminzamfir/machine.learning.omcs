package prob.distribution;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * One pair (value, P(X=value)) for a discrete random variable outcome
 * @author eh2zamf
 *
 */
public class DiscreteDistributionPoint {

	private static NumberFormat nf = NumberFormat.getInstance();
	
	/**The value of the random variable X*/
	public int value;

	/** The probablity that X=value */
	public double p;

	public DiscreteDistributionPoint(int value, double p) {
		super();
		this.value = value;
		this.p = p;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}

	public static double[][] asArray(List<DiscreteDistributionPoint> points) {
		double[][] res = new double[points.size()][2];
		int index = 0;
		for (DiscreteDistributionPoint point : points) {
			res[index][0] = point.getValue();
			res[index++][1] = point.getP();
		}
		return res;
	}
	
	@Override
	public String toString() {
		return "[" + value + ":" +  nf.format(p) + "]";
	}

}
