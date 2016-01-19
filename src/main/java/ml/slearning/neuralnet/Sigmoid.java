package ml.slearning.neuralnet;

import linalg.Vector;
import ml.model.Observation;
import ml.model.function.SigmoidFunction;

/** An Perceptron (unthreasholded) with a non-linear, differentiable output - the continuous analogue of an {@link ThresholdedPerceptron}
 * 
 * @author eh2zamf
 *
 */
public class Sigmoid extends UnthresholdedPerceptron {

	private SigmoidFunction function = new SigmoidFunction();
	private double currentValue;
	private double currentErrorTerm;
	
	@Override
	public double evaluate(Observation observation) {
		double dotProduct = super.evaluate(observation);
		return function.evaluate(dotProduct); 
	}
	
	public double evaluate(double[] input) {
		double dotProduct = new Vector(input).dotProduct(new Vector(coefficients));
		return function.evaluate(dotProduct);
	}
	
	
	
	public double getCurrentValue() {
		return currentValue;
	}
	
	public void setCurrentValue(double currentValue) {
		this.currentValue = currentValue;
	}
	
	public double getCurrentErrorTerm() {
		return currentErrorTerm;
	}
	
	public void setCurrentErrorTerm(double currentErrorTerm) {
		this.currentErrorTerm = currentErrorTerm;
	}
}
