package ml.slearning.classification.logreg;

import java.text.MessageFormat;
import java.util.List;

import prob.chart.ChartBuilder;
import prob.chart.PlotType;
import ml.model.DataSet;
import ml.model.function.Function;
import ml.utils.DataSetCsvParser;

public class LogisticRegressionTest {

	public static void main1(String[] args) {
		DataSet ds = DataSetCsvParser.parseNumericDataSet("bc.txt", false);
		
		//remove id
		ds.removeFeature(0);
		//ds.standardize();
		List<DataSet> partition = ds.partition(0.7, true);
		DataSet training = partition.get(0);
		DataSet test = partition.get(1);
		LogisticRegression lr = new LogisticRegression(training);
		lr.setLearningRate(0.05);
		lr.setMaxEpochs(300);
		lr.train();
		System.out.println(MessageFormat.format("Accuracy: {0}", lr.accuracy(test)));
	}
	
	public static void main(String[] args) {
		DataSet ds = DataSetCsvParser.parseNumericDataSet("bc.txt", false);
		//remove id
		ds.removeFeature(0);
		//ds.standardize();
		List<DataSet> partition = ds.partition(0.7, true);
		DataSet training = partition.get(0);
		DataSet test = partition.get(1);
		new ChartBuilder().add(new Function() {
			
			@Override
			public double evaluate(double x) {
				System.out.println("Training with maxEphocs: " + (int)x);
				LogisticRegression lr = new LogisticRegression(training);
				lr.setMaxEpochs((int) x);
				lr.train();
				return lr.accuracy(test);
			}
		}, 1, 10000, 15, "Perf vs. epochs", PlotType.Scatter).build();
	}
}
