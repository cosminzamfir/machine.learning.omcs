package ml.ul;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ml.utils.Utils;

public class EM {

	private static final Logger log = Logger.getLogger(EM.class);
	private int iterations = 5;
	private List<?> thetaValues;
	private Map<Object, DiscreteProbabilityDistribution> conditionalProbabilitites = new LinkedHashMap<Object, DiscreteProbabilityDistribution>();
	private Object observation;
	private Object targetTheta;
	private CompatibilityChecker compatibilityChecker;
	
	
	public EM(List<?> thetaValues, Map<Object, DiscreteProbabilityDistribution> conditionalProbabilitites, CompatibilityChecker compatibilityChecker, Object observation) {
		super();
		this.thetaValues = thetaValues;
		this.conditionalProbabilitites = conditionalProbabilitites;
		this.compatibilityChecker = compatibilityChecker;
		this.observation = observation;
	}

	public void run() {
		log.info("Computing most likely theta for observation: " + observation);
		targetTheta = Utils.randomElement(thetaValues);
		log.info("Choose random theta: " +targetTheta);
		DiscreteProbabilityDistribution distribution = computeProbabilityDistribution(targetTheta, observation);
		log.info("Probability distribution given initial theta: \n" + distribution);
		for (int i = 0; i < iterations; i++) {
			targetTheta = maxLikelyhoodEstimation(distribution);
			distribution = computeProbabilityDistribution(targetTheta, observation);
			log.info("Iteration " + i);
			log.info("New theta: " + targetTheta);
			log.info("Probability distribution given current theta: \n" + distribution);
		}
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
			DiscreteProbabilityDistribution candidateThetaDistribution = conditionalProbabilitites.get(candidateTheta);
			for (Object instance : distribution.getData().keySet()) {
				p += distribution.p(instance) * candidateThetaDistribution.p(instance);
			}
			if(p > maxLikelyhood)  {
				maxLikelyhood = p;
				targetTheta = candidateTheta;
			}
		}
		log.info("Best theta found with likelyhood estimation = " + maxLikelyhood);
		return targetTheta;
	}

	private DiscreteProbabilityDistribution computeProbabilityDistribution(Object theta, Object observation) {
		DiscreteProbabilityDistribution distribution = conditionalProbabilitites.get(theta);
		DiscreteProbabilityDistribution res = new DiscreteProbabilityDistribution();
		double normalizingFactor = summAllPossibleInstances(distribution, observation);
		for (Object instance : distribution.getData().keySet()) {
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
		for (Object instance : distribution.getData().keySet()) {
			if(compatibilityChecker.isCompatible(instance, observation)) {
				res += distribution.p(instance);
			}
		}
		return res;
	}
}