package ml.slearning.classification.neuralnet;

import ml.model.DataSet;
import ml.model.Observation;

/** An Perceptron (unthreasholded) with a non-linear, differentiable output - the continuous analogue of an {@link ThresholdedPerceptron}
 * 
 * @author eh2zamf
 *
 */
public class Sigmoid extends Perceptron {

	@Override
	public double evaluate(Observation observation) {
		return 0;
	}

}
