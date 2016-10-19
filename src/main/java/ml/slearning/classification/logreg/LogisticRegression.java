package ml.slearning.classification.logreg;

import static java.lang.Math.*;
import static java.text.MessageFormat.*;
import static util.MLUtils.*;

import java.text.MessageFormat;

import linalg.Vector;
import ml.model.DataSet;
import ml.model.Observation;
import ml.model.builder.DataSetBuilder;
import ml.model.function.MultivariableFunction;
import ml.model.function.PolynomialMultivariableFunction;
import ml.model.function.SigmoidFunction;

import org.apache.log4j.Logger;

import prob.chart.ChartBuilder;
import util.MLUtils;

/**
 * @author Cosmin Zamfir
 *
 */
public class LogisticRegression {

	private static final Logger log = Logger.getLogger(LogisticRegression.class);

	private DataSet dataSet;
	/**The model parameters*/
	private Vector theta;
	/** The probability function */
	private SigmoidFunction f;
	/** Feature vector dimension */
	private int d;
	/** The error threshold */
	private double e = 0.001;
	private double learningRate = 0.1;
	/** Decay of learning rate */
	private double decayRate = 0.99;
	/** Max number of epochs, default -1 (unlimited) */
	private int maxEpochs = -1;
	private Kernel kernel;

	public LogisticRegression(DataSet dataSet) {
		super();
		this.dataSet = dataSet;
		d = dataSet.getWidth() + 1;
		f = new SigmoidFunction();
	}

	public void setKernel(Kernel kernel) {
		this.kernel = kernel;
	}
	
	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public void setMaxEpochs(int maxEpochs) {
		this.maxEpochs = maxEpochs;
	}

	/**
	 * Stochastic gradient descent
	 * dp = <theta * x_n>
	 * <p>Classification probability: sigmoid(y_n * dp)
	 * <p>Error term: log(1/sigmoid(y_n*dp))
	 * <p>Partial derivative of error term vs theta_i: -yn*x_i_n*sigmoid(-y_n*dp)
	 */
	public void train() {
		theta = new Vector(randomArray(getDim()));
		double e1 = runEpoch();
		double e2;
		int ind = 1;
		double rate = learningRate;
		do {
			if (maxEpochs > 0 && ind++ >= maxEpochs) {
				break;
			}
			rate *= decayRate;
			e2 = abs(runEpoch());
			if (abs(e2 - e1) < e) {
				break;
			}
			e1 = e2;
		} while (true);
	}

	private int getDim() {
		return kernel == null ? d + 1 : kernel.transform(dataSet.getObservation(0).getValues()).length;
	}

	private double runEpoch() {
		double res = 0;
		for (Observation obs : dataSet.getObservations()) {
			double y = (double) obs.getTargetAttributeValue();
			Vector x;
			if (kernel == null) {
				x = obs.getVectorValues(true);
			} else {
				x = new Vector(kernel.transform(obs.getValues()));
			}
			double dp = theta.dotProduct(x);
			double errorMeasure = log(1 / f.evaluate(y * dp));
			double[] pd = new double[getDim()];
			for (int i = 0; i < pd.length; i++) {
				pd[i] = -y * x.get(i) * f.evaluate(-y * dp);
			}
			Vector delta = new Vector(pd).multiply(-1).multiply(learningRate);
			theta = theta.add(delta);
			res += errorMeasure;
			if (log.isDebugEnabled()) {
				log.debug(format("Observation={0}, y={1}, x={2}, dotProduct={3}, errorMeasure={4}, delta={5}", obs.getVectorValues(true),
						obs.getTargetAttributeValue(), obs.getVectorValues(true), dp, errorMeasure, delta));
			}
		}

		if (log.isDebugEnabled()) {
			log.debug(format("New epoch total error={0}", res));
		}
		return res;
	}

	public LogisticClassification classify(Observation example) {
		Vector x = kernel == null ? example.getVectorValues(true) : new Vector(kernel.transform(example.getValues()));
		double product = x.dotProduct(theta);
		double p = 1 / (1 + pow(E, -product));
		return p >= 0.5 ? new LogisticClassification(1, p) : new LogisticClassification(-1, 1 - p);
	}

	public void classify(DataSet dataSet) {
		dataSet.getObservations().forEach((obs) -> obs.setTargetAttributeValue(classify(obs).getValue()));
	}

	public double accuracy(DataSet dataSet) {
		int correct = 0;
		for (Observation obs : dataSet.getObservations()) {
			double val = classify(obs).getValue();
			if (MLUtils.effectiveEquals(val, obs.getTargetAttributeValue())) {
				correct++;
			}
		}
		return (double) correct / dataSet.getObservations().size();
	}

	public static void main(String[] args) {
		MultivariableFunction f = new PolynomialMultivariableFunction(0, new double[][] { { 0, 1}, { 0, 1} });
		DataSet training = new DataSetBuilder().width(2).heigth(1000).separationFunction(f, 0.5).build();
		DataSet test = new DataSetBuilder().width(2).heigth(1000).separationFunction(f, 0.5).build();

		LogisticRegression lr = new LogisticRegression(training);
		lr.setMaxEpochs(100);
		lr.train();
		//training/test sets with non linear boundary. Poor accuracy expected
		System.out.println(MessageFormat.format("No kernel - Accuracy on training set {0}", lr.accuracy(training)));
		System.out.println(MessageFormat.format("No kernel - Accuracy on test set {0}", lr.accuracy(test)));

		//using a polynomial kernel. High accuracy expected
		lr.setKernel(new PolynomialKernel(2));
		lr.train();
		System.out.println(MessageFormat.format("Polynomial kernel - Accuracy on training set {0}", lr.accuracy(training)));
		System.out.println(MessageFormat.format("Polynomial kernel - Accuracy on test set {0}", lr.accuracy(test)));
		
		new ChartBuilder("Kernel logistic regression").add(training, 0, 1).build();
	}

}
