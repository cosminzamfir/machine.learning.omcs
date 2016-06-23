package ml.ul;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import util.MLUtils;

public class EM {

	private static final Logger log = Logger.getLogger(EM.class);
	private int iterations = 5;
	private List<?> thetaValues;
	private Map<Object, DiscreteProbabilityDistribution> probabilitityDistributions = new LinkedHashMap<Object, DiscreteProbabilityDistribution>();
	private Object observation;
	private Object targetTheta;
	private CompatibilityChecker compatibilityChecker;
	
	
	public EM(List<?> thetaValues, Map<Object, DiscreteProbabilityDistribution> conditionalProbabilitites, CompatibilityChecker compatibilityChecker, Object observation) {
		super();
		this.thetaValues = thetaValues;
		this.probabilitityDistributions = conditionalProbabilitites;
		this.compatibilityChecker = compatibilityChecker;
		this.observation = observation;
	}

	public void run() {
		logStartConditions();
		log.info("Computing most likely theta for observation: " + observation);
		targetTheta = MLUtils.randomElement(thetaValues);
		log.info("Choose random theta: " +targetTheta);
		DiscreteProbabilityDistribution distribution = computeProbabilityDistribution(targetTheta, observation);
		//log.info("Probability distribution given initial theta and the observed event: \n" + distribution);
		for (int i = 0; i < iterations; i++) {
			targetTheta = maxLikelyhoodEstimation(distribution);
			distribution = computeProbabilityDistribution(targetTheta, observation);
			log.info("Iteration " + i);
			//log.info("Probability distribution given current theta and the observed event: \n" + distribution);
		}
	}


	private void logStartConditions() {
		log.info("Problem setup");
		for (Object theta : probabilitityDistributions.keySet()) {
			//log.info("ProbabilityDistribution for : " + theta + "\n" + probabilitityDistributions.get(theta));
		}
		log.info("Observed event:" + observation);
	}

	/**
	 * Compute for each theta: sum(P(x|theta(m+1)) * P(x|observation, theta(m)) and choose the argmax(theta)
	 * @param distribution the distribution computed at previous step (gives the second multiplier in the formula above) 
	 * @return
	 */
	private Object maxLikelyhoodEstimation(DiscreteProbabilityDistribution distribution) {
		double maxLikelyhood = Double.NEGATIVE_INFINITY;
		Object targetTheta = null;
		for (Object candidateTheta : thetaValues) {
			double p = 0;
			DiscreteProbabilityDistribution candidateThetaDistribution = probabilitityDistributions.get(candidateTheta);
			for (Object instance : distribution.getProbabilities().keySet()) {
				p += distribution.p(instance) * candidateThetaDistribution.p(instance);
			}
			if(p > maxLikelyhood)  {
				maxLikelyhood = p;
				targetTheta = candidateTheta;
			}
		}
		log.info("New best theta found: " + targetTheta + " with likelyhood estimation = " + maxLikelyhood);
		return targetTheta;
	}

	private DiscreteProbabilityDistribution computeProbabilityDistribution(Object theta, Object observation) {
		DiscreteProbabilityDistribution distribution = probabilitityDistributions.get(theta);
		DiscreteProbabilityDistribution res = new DiscreteProbabilityDistribution();
		double normalizingFactor = summAllPossibleInstances(distribution, observation);
		for (Object instance : distribution.getProbabilities().keySet()) {
			if(compatibilityChecker.isCompatible(instance, observation)) {
				res.addInstance(instance, distribution.p(instance)/normalizingFactor);
			} else {
				res.addInstance(instance, 0);
			}
		}
		return res;
	}

	private double summAllPossibleInstances(DiscreteProbabilityDistribution distribution, Object observation2) {
		double res = 0;
		for (Object instance : distribution.getProbabilities().keySet()) {
			if(compatibilityChecker.isCompatible(instance, observation)) {
				res += distribution.p(instance);
			}
		}
		return res;
	}
}
