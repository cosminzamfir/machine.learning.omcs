package ml.slearning.classification.logreg;

import java.text.MessageFormat;

public class LogisticClassification {

	private double value;
	private double p;
	
	public LogisticClassification(double value, double p) {
		super();
		this.value = value;
		this.p = p;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}
	
	@Override
	public String toString() {
		return MessageFormat.format("{0} with probability {1}", value,p);
	}

}
