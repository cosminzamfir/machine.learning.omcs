package prob.simulation;

import java.util.ArrayList;
import java.util.List;

import ml.stat.StatUtils;
import util.MLUtils;

public class Sampling {

	private int n;
	private List<Double> data;

	public static void main(String[] args) {
		Sampling sampling = new Sampling(30000, 100);
		sampling.run(10000, 1000, false);
	}

	public Sampling(int n, int max) {
		this.n = n;
		data = MLUtils.randomList(n, 0, max);
	}

	public void run(int numElements, int numTrials, boolean withReplacement) {
		double mean = StatUtils.mean(data);
		double std = StatUtils.std(data);

		double expectedSampleMean;
		double expectedSampleStd;
		if (withReplacement) {
			expectedSampleMean = mean * numElements;
			expectedSampleStd = std * Math.sqrt(numElements);
		} else {
			expectedSampleMean = mean * numElements;
			expectedSampleStd = std * Math.sqrt(numElements) * Math.sqrt((double)(n-numElements)/(n-1));
		}

		List<Double> samples = sample(numElements, numTrials, withReplacement);

		double actualSampleMean = StatUtils.mean(samples);
		double actualSampleStd = StatUtils.std(samples);
		
		System.out.println("Data mean: " + mean + "\nData std: " + std +
				"\nSample mean:\n - expected: " + expectedSampleMean + "\n - actual: " + actualSampleMean + "\n - relative diff: "
				+ Math.abs(1 - actualSampleMean / expectedSampleMean)
				+ "\nSample std:\n - expected: " + expectedSampleStd + "\n - actual: " + actualSampleStd + "\n - relative diff: "
				+ Math.abs(1 - actualSampleStd / expectedSampleStd));
	}

	/**
	 * Sample from the {@link #data}
	 * @param numElements the number of elements to sample 
	 * @param numTrials how many times to repeat the sampling 
	 * @param withReplacement <code>true</code> if with replacement, otherwise <code>false</code>
	 * @return A list with numElem elements, each elements representing the sum of the sample 
	 */
	public List<Double> sample(int numElements, int numTrials, boolean withReplacement) {
		List<Double> res = new ArrayList<Double>();
		for (int i = 0; i < numTrials; i++) {
			res.add(sample(numElements, withReplacement));
		}
		return res;
	}

	/** Sample numElements from {@link #data}, return the sum of the sample */
	private Double sample(int numElements, boolean withReplacement) {
		List<Double> tmp;

		if (withReplacement) {
			tmp = data;
		} else {
			tmp = new ArrayList<>(data);
		}

		double res = 0;
		for (int i = 0; i < numElements; i++) {
			res += MLUtils.randomElement(tmp, withReplacement);
		}
		return res;
	}

}
