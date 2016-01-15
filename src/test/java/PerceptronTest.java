import ml.model.DataSet;
import ml.model.Observation;
import ml.slearning.classification.neuralnet.Perceptron;
import ml.slearning.classification.neuralnet.ThresholdedPerceptron;
import ml.slearning.classification.neuralnet.UnthresholdedPerceptron;
import ml.utils.DataSetCsvParser;

import org.junit.Assert;
import org.junit.Test;

public class PerceptronTest {

	@Test
	public void testOneVariable() throws Exception {
		DataSet ds = DataSetCsvParser.parseNumericDataSet("perceptron_test_one_variable.txt", false);
		ThresholdedPerceptron perc = new ThresholdedPerceptron();
		perc.train(ds, 0.1, 1000);
		for (Observation obs : ds.getObservations()) {
			Assert.assertTrue(perc.validate(obs));
			System.out.println(perc.validate(obs));
		}
	}

	@Test
	public void testTwoVariables() throws Exception {
		DataSet ds = DataSetCsvParser.parseNumericDataSet("perceptron_test_two_variables.txt", false);
		ThresholdedPerceptron perc = new ThresholdedPerceptron();
		perc.train(ds, 0.1, 1000);
		for (Observation obs : ds.getObservations()) {
			Assert.assertTrue(perc.validate(obs));
			System.out.println(perc.validate(obs));
		}
	}

	@Test
	public void testSixVariables() throws Exception {
		DataSet ds = DataSetCsvParser.parseNumericDataSet("perceptron_test_six_variables.txt", false);
		ThresholdedPerceptron perc = new ThresholdedPerceptron();
		perc.train(ds, 0.1, 1000);
		for (Observation obs : ds.getObservations()) {
			Assert.assertTrue(perc.validate(obs));
			System.out.println(perc.validate(obs));
		}
	}

	@Test
	public void testSixVariablesGradientDescend() throws Exception {
		DataSet ds = DataSetCsvParser.parseNumericDataSet("perceptron_test_gradient_descend_six_variables.txt", false);
		UnthresholdedPerceptron perc = new UnthresholdedPerceptron();
		perc.train(ds, 0.000001, 5, 1000);
		for (Observation obs : ds.getObservations()) {
			Assert.assertTrue(perc.evaluate(obs) - (double) obs.getTargetAttributeValue() < 1);
		}
	}

}
