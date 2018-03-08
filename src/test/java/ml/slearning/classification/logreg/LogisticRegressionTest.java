package ml.slearning.classification.logreg;

import java.text.MessageFormat;
import java.util.List;

import ml.model.DataSet;
import ml.model.Observation;
import ml.model.function.Function;
import ml.utils.DataSetCsvParser;
import prob.chart.ChartBuilder;
import prob.chart.PlotType;

public class LogisticRegressionTest {

	public static void main(String[] args) {
		DataSet ds = DataSetCsvParser.parseNumericDataSet("bc.txt", false);
		ds.removeFeature(0);
		ds.removeFeature(0);
		ds.standardize();

		for (int i = 0; i < 10; i++) {

			List<DataSet> partition = ds.partition(0.7, true);
			DataSet training = partition.get(0);
			DataSet test = partition.get(1);
			LogisticRegression lr = new LogisticRegression(training);
			lr.setLearningRate(0.1);
			lr.train();
			System.out.println(lr.getTheta());
			System.out.println(MessageFormat.format("Accuracy on training set: {0}", lr.accuracy(training)));
			System.out.println(MessageFormat.format("Accuracy on test set: {0}", lr.accuracy(test)));
		}

	}

	public static void main1(String[] args) {
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
				System.out.println("Training with maxEphocs: " + (int) x);
				LogisticRegression lr = new LogisticRegression(training);
				lr.setMaxEpochs((int) x);
				lr.train();
				return lr.accuracy(test);
			}
		}, 1, 10000, 15, "Perf vs. epochs", PlotType.Scatter).build();
	}

	public static void main2(String[] args) {
		DataSet ds = DataSetCsvParser.parseNumericDataSet("eug.txt", true);
		ds.standardize();
		for (Observation obs : ds.getObservations()) {
			//			double[] d = obs.getValues();
			//			double[] d1 = new double[d.length -1];
			//			for (int i = 0; i < d1.length; i++) {
			//				d1[i] = d[i];
			//			}
			//			obs.setValues(d1);
			System.out.println(obs);
		}
		List<DataSet> partition = ds.partition(0.7, true);
		DataSet training = partition.get(0);
		DataSet test = partition.get(1);
		LogisticRegression lr = new LogisticRegression(training);
		lr.setLearningRate(0.1);
		//		lr.setKernel(new Kernel() {
		//
		//			@Override
		//			public double[] transform(double[] x) {
		//				double x1 = x[0];
		//				double x2 = x[1];
		//				double x3 = x[2];
		//				return new double[] { 1, x1, x2, x3, x1 * x2 * x3, x1*x2, x1*x3, x2*x3};
		//			}
		//		});

		lr.train();

		System.out.println(lr.getTheta());
		System.out.println(MessageFormat.format("Accuracy on training set: {0}", lr.accuracy(training)));
		System.out.println(MessageFormat.format("Accuracy on test set: {0}", lr.accuracy(test)));

		System.out.println("Training set:");
		lr.accuracyOnClass(training);

		System.out.println("Test set:");
		lr.accuracyOnClass(test);
	}
}
