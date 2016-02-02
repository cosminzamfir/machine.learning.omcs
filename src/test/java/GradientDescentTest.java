import static org.junit.Assert.*;
import ml.model.DataSet;
import ml.model.function.DifferentiableMultivariableFunction;
import ml.model.function.LinearMultivariableFunction;
import ml.slearning.regression.GradientDescent;
import ml.slearning.regression.StochasticGradientDescent;
import ml.utils.DataSetCsvParser;

import org.junit.Test;


public class GradientDescentTest {

	@Test
	public void test1() throws Exception {
		DataSet dataSet = DataSetCsvParser.parseNumericDataSet("house_prices.txt", false);
		DifferentiableMultivariableFunction targetFunction = new LinearMultivariableFunction(new double[dataSet.getWidth() + 1]);
		GradientDescent gd = new GradientDescent(targetFunction, dataSet);
		gd.train(0.01, 20, 0.005);
	}

	@Test
	public void test2() throws Exception {
		DataSet dataSet = DataSetCsvParser.parseNumericDataSet("house_prices.txt", false);
		DifferentiableMultivariableFunction targetFunction = new LinearMultivariableFunction(new double[dataSet.getWidth() + 1]);
		StochasticGradientDescent gd = new StochasticGradientDescent(targetFunction, dataSet);
		gd.train(0, 20, 0.005);
	}

}
