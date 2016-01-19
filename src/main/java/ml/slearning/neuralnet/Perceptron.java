package ml.slearning.neuralnet;

import linalg.Vector;
import ml.model.DataSet;
import ml.model.Observation;
import ml.utils.Utils;

import org.apache.log4j.Logger;

/**
 * @author Cosmin Zamfir
 *
 */
public abstract class Perceptron {

	private static final Logger log = Logger.getLogger(Perceptron.class);
	protected double[] coefficients;
	
	

	public Perceptron() {
		super();
	}

	/**
	 * Evaluate the specified Observation
	 * @param observation
	 * @return the Perceptron calculated value for the given Observation
	 */
	public abstract double evaluate(Observation observation);
	
	protected void generateRandomCoefficients(DataSet dataSet) {
		coefficients = new double[dataSet.getAttributes().size() + 1];
		for (int i = 0; i < coefficients.length; i++) {
			coefficients[i] = Utils.randomDouble(0, 1);
		}
	}

	protected void generateRandomCoefficients(int size) {
		coefficients = new double[size];
		for (int i = 0; i < coefficients.length; i++) {
			coefficients[i] = Utils.randomDouble(0, 1);
		}
	}

	protected Vector getValues(Observation observation) {
		double[] res = new double[observation.getValues().length + 1];
		res[0] = 1;
		for (int i = 0; i < observation.getValues().length; i++) {
			res[i + 1] = (double) observation.getValues()[i];
		}
		return new Vector(res);
	}
	
	public double[] getCoefficients() {
		return coefficients;
	}
	
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + Utils.toString(coefficients);
	}
}
