import ml.model.DataSet;
import ml.model.function.DifferentiableFunction;
import ml.model.function.IdentityFunction;
import ml.slearning.regression.GradientDescent;
import ml.utils.DataSetCsvParser;

import org.junit.Test;


public class GradientDescentTest {

	@Test
	public void test1() throws Exception {
		DataSet dataSet = DataSetCsvParser.parseNumericDataSet("house_prices.txt", false);
		DifferentiableFunction targetFunction = new IdentityFunction();
		GradientDescent gd = new GradientDescent(targetFunction, dataSet);
		gd.train(0.01, 20, 0.01);
		System.out.println(targetFunction);
	}

	@Test
	public void test2() throws Exception {
		DataSet dataSet = DataSetCsvParser.parseNumericDataSet("house_prices.txt", false);
		DifferentiableFunction targetFunction = new IdentityFunction();
		GradientDescent gd = new GradientDescent(targetFunction, dataSet);
		gd.train(0, 20, 0.005);
	}

}
