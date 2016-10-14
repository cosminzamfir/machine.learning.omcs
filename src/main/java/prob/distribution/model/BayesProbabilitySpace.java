package prob.distribution.model;

import java.util.HashMap;
import java.util.Map;

import util.DoubleHolder;
import util.ObjectHolder;

/**
 * 
 * A 2 random variable (X,Y) probabilty space
 * Givens: 
 * <ul>
 * <li>X distribution
 * <li>Y|X distribution
 * </ul>
 * Goals:
 * <ul>
 * <li>X|Y distribution
 * <li>MAP(X|Y) - maximum aposterriory estimate
 * 
 * @author Cosmin Zamfir
 *
 */
public class BayesProbabilitySpace {

	private JointProbabilityTable XTable;
	private Map<Object, JointProbabilityTable> Y_XTable = new HashMap<Object, JointProbabilityTable>();

	public BayesProbabilitySpace X(JointProbabilityTable t) {
		this.XTable = t;
		return this;
	}

	public BayesProbabilitySpace Y_X(Object xValue, JointProbabilityTable t) {
		this.Y_XTable.put(xValue, t);
		return this;
	}

	public JointProbabilityTable X_Y(Object yValue) {
		String X = XTable.getRvs().get(0);
		JointProbabilityTable res = new JointProbabilityTable(XTable);
		XTable.getRvValues().get(X).forEach(Xvalue -> res.add(XTable.p(Xvalue) * Y_XTable.get(Xvalue).p(yValue), Xvalue));
		res.normalize();
		return res;
	}

	/**
	 * Return the X value with maximum aposteriori probability for the given Y realization
	 * @return
	 */
	public Object MAP(Object yValue) {
		String X = XTable.getRvs().get(0);
		JointProbabilityTable X_YTable = X_Y(yValue);
		DoubleHolder maxP = new DoubleHolder(0);
		ObjectHolder res = new ObjectHolder(null);
		X_YTable.getRvValues().get(X).forEach(value -> {
			if (X_YTable.p(value) > maxP.get()) {
				maxP.set(X_YTable.p(value));
				res.set(value);
			}
		});
		return res.get();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("X:\n");
		sb.append(XTable);
		sb.append("Y|X\n");
		Y_XTable.keySet().forEach(value -> sb.append(value).append("\n").append(Y_XTable.get(value)));
		return sb.toString();
	}
	
	public static void main(String[] args) {
		BayesProbabilitySpace b = new BayesProbabilitySpace();
		JointProbabilityTable XTable = new JointProbabilityTable("healthStatus").add(0.999, "healthy").add(0.001, "infected");
		JointProbabilityTable healthyTable = new JointProbabilityTable("testResult").add(0.99, "negative").add(0.01, "positive");
		JointProbabilityTable infectedTable = new JointProbabilityTable("testResult").add(0.99, "positive").add(0.01, "negative");
		b.X(XTable).Y_X("healthy", healthyTable).Y_X("infected", infectedTable);
		System.out.println(b);
		JointProbabilityTable pos = b.X_Y("positive");
		JointProbabilityTable neg = b.X_Y("negative");
		System.out.println(pos);
		System.out.println(neg);
	}
}
