package ml.rl.project1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import linalg.Vector;
import ml.rl.mdp.model.Episode;
import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.MDPPolicy;
import ml.rl.mdp.model.State;

import org.apache.log4j.Logger;

import prob.chart.MappingChart;
import prob.chart.MultiMappingChart;
import util.DoubleHolder;
import util.MLUtils;

public class RandomWalkTDLambdaSolver {

	private static final Logger log = Logger.getLogger(RandomWalkTDLambdaSolver.class);

	private static int numEpisodesPerTrainingSet = 10;
	private static int numTrainingSets = 100;
	private static Vector expValues = new Vector(1.0 / 6, 2.0 / 6, 3.0 / 6, 4.0 / 6, 5.0 / 6);
	private Vector w;
	private double lambda = 0;
	private double alpha = 0.025;
	private double epsilon = 0.001;
	private MDP mdp;
	private List<List<Episode>> trainingSets = new ArrayList<List<Episode>>();

	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Usage: 'java RandomWalkTDLambdaSolver one|two|three' to run the desired experiment");
			System.exit(0);
		}
		String experiment = args[0];
		RandomWalkTDLambdaSolver solver = new RandomWalkTDLambdaSolver();
		solver.setup(numEpisodesPerTrainingSet, numTrainingSets);
		if(experiment.equals("one")) {
			solver.runExperimentOne();
		}
		if(experiment.equals("two")) {
			solver.runExperimentTwo();
		}
		if(experiment.equals("three")) {
			solver.runExperimentThree();
		}
	}

	private void runExperimentOne() {
		List<Double> lambdas = Arrays.asList(0.0, 0.1, 0.3, 0.5, 0.7, 0.9, 1.0);
		Map<Number, Number> map = new HashMap<>();
		for (Double lambda : lambdas) {
			map.put(lambda, runExperimentOne(lambda));
		}
		new MappingChart(map, "lambda", "rmse", "RMSE (trainingSets=" + numTrainingSets + ")");
	}

	private double runExperimentOne(double lambda) {
		this.lambda = lambda;

		double rmse = 0;
		for (List<Episode> trainingSet : trainingSets) {
			Vector weights = runUntilConvergence(trainingSet);
			Vector prognosis = getPrognosedValues(weights);
			rmse += prognosis.rootMeanSquaredError(expValues);
		}
		rmse /= trainingSets.size();
		System.out.println("lamda=" + lambda + ";RMSE=" + rmse);
		return rmse;
	}

	private void runExperimentTwo() {
		List<Double> lambdas = Arrays.asList(0.0, 0.3, 0.8, 1.0);
		List<Double> alphas = MLUtils.generateList(0.0, 0.60, 0.05);
		
		List<Map<Number, Number>> results = new ArrayList<>(); //one mapping of alpha=>rmse for each lambda
		List<String> legends = new ArrayList<>();
		for (Double lambda : lambdas) {
			legends.add("lambda=" + lambda);
			Map<Number, Number> map = new HashMap<>();
			for (Double alpha : alphas) {
				map.put(alpha, runExperimentTwo(lambda, alpha));
			}
			results.add(map);
		}
		new MultiMappingChart(results, legends, "alpha" , "rmse" , "RMSE vs alpha");
	}

	private double runExperimentTwo(double lambda, double alpha) {
		this.lambda = lambda;
		this.alpha = alpha;
		double rmse = 0;
		for (List<Episode> trainingSet : trainingSets) {
			Vector weights = run(trainingSet);
			Vector prognosedValues = getPrognosedValues(weights);
			double trainingSetError = prognosedValues.rootMeanSquaredError(expValues);
			if(trainingSetError < 0.1) {
				System.out.println("lambda=" + lambda + ";alpha=" + alpha + ";error=" + trainingSetError);
				trainingSet.forEach((episode) -> System.out.println(episode));
			}
			rmse += trainingSetError;
		}
		return rmse/trainingSets.size();
	}
	
	/**
	 * For each lambda from 0 to 1 in increments of 0.1, get the RMSE using the best alfa for that lambda
	 */
	private void runExperimentThree() {
		List<Double> lambdas = MLUtils.generateList(0.0, 1.0, 0.1);
		List<Double> alphas = MLUtils.generateList(0.0, 0.6, 0.05); 
		Map<Number, Number> res = new HashMap<>();
		for (Double lambda : lambdas) {
			double bestRmse = Double.MAX_VALUE;
			for (Double alpha : alphas) {
				double rmse = runExperimentTwo(lambda, alpha);
				if(rmse < bestRmse) {
					bestRmse = rmse;
				}
			}
			res.put(lambda, bestRmse);
		}
		new MappingChart(res, "lambda", "rmse", "Errror vs lambda using best alfa");
	}

	/**
	 * Run the training set once, updating w's after each episode.
	 * Start with initial weights = 0.5
	 * @param trainingSet
	 * @return
	 */
	private Vector run(List<Episode> trainingSet) {
		w = new Vector(0.5, 0.5, 0.5, 0.5, 0.5);
		for (Episode episode : trainingSet) {
			Vector deltaW = computeDeltaW(episode);
			w = w.add(deltaW);
		}
		return w;
	}

	/**
	 * Repeatedly run the training set until convergence of {@link #w}
	 * @param trainingSet
	 * @return the computed weigths for this training set 
	 */
	private Vector runUntilConvergence(List<Episode> trainingSet) {
		w = Vector.randomInstance(5, 0, 1);
		Vector deltaW;
		int iterationCount = 0;
		do {
			deltaW = computeDeltaW(trainingSet);
			w = w.add(deltaW);
			log.trace(deltaW.toRowMatrix());
			iterationCount++;
		} while (deltaW.magnitude() > epsilon);
		log.trace("Converged after " + iterationCount + " iterations");
		return w;
	}

	/**
	 * Compute the change in weights triggered by the trainingSet, as a sum of change in weights for each episode in the trainingSet
	 * @param trainingSet
	 * @return
	 */
	private Vector computeDeltaW(List<Episode> trainingSet) {
		Vector res = Vector.zeroVector(5);
		for (Episode episode : trainingSet) {
			res = res.add(computeDeltaW(episode));
		}
		return res;
	}

	/** 
	 * Compute the change in weights triggered by the given Episode, as a sum of change in weights for each Episode step
	 */
	private Vector computeDeltaW(Episode episode) {
		Vector res = Vector.zeroVector(5);
		for (int k = 0; k < episode.getNonTerminalStates().size(); k++) {
			res = res.add(computeDeltaW(episode, k));
		}
		return res;
	}

	/**
	 * Compute the change in weights triggered by the step t of the episode.
	 * <p>
	 * delta(wt) = alpha*(Pt+1 - Pt)*sum(k=1..t)[lambda^(t-k)*xk], where Pt=transpose(w)*xt and gradient(Pk(w)) is xk
	 */
	private Vector computeDeltaW(Episode episode, int t) {
		double pNext;
		if (t == episode.length() - 2) {
			pNext = episode.getLastState().getValue();
		} else {
			pNext = w.dotProduct(getStateVector(episode, t + 1));
		}
		double p = w.dotProduct(getStateVector(episode, t));

		Vector sumOfGradients = Vector.zeroVector(5);
		for (int k = 0; k <= t; k++) {
			sumOfGradients = sumOfGradients.add(getStateVector(episode, k).multiply(Math.pow(lambda, t - k)));
		}
		return sumOfGradients.multiply(alpha * (pNext - p));
	}

	private Vector getPrognosedValues(Vector weights) {
		double[] res = new double[5];
		res[0] = getStateVector(State.instance("B")).dotProduct(weights);
		res[1] = getStateVector(State.instance("C")).dotProduct(weights);
		res[2] = getStateVector(State.instance("D")).dotProduct(weights);
		res[3] = getStateVector(State.instance("E")).dotProduct(weights);
		res[4] = getStateVector(State.instance("F")).dotProduct(weights);
		return new Vector(res);
	}

	/**
	 * Create the MDP.
	 * Generate episodes
	 * @param numEpisodes total number of Episodes
	 * @param numTrainingSets total number of training set (a training set is a List<Episode>)
	 */
	private void setup(int numEpisodesPerTrainingSet, int numTrainingSets) {
		createMDP();
		MDPPolicy randomWalkPolicy = MDPPolicy.initialNonDeterministicPolicy(mdp);
		for (int i = 0; i < numTrainingSets; i++) {
			List<Episode> trainingSet = new ArrayList<>();
			for (int j = 0; j < numEpisodesPerTrainingSet; j++) {
				trainingSet.add(mdp.generateEpisode(State.instance("D"), randomWalkPolicy));
			}
			trainingSets.add(trainingSet);
		}
	}

	private void createMDP() {
		mdp = MDP.instance();

		mdp.addSingleOutcomAction(State.instance("B"), State.instance("A"), 0, "West");
		mdp.addSingleOutcomAction(State.instance("B"), State.instance("C"), 0, "East");

		mdp.addSingleOutcomAction(State.instance("C"), State.instance("B"), 0, "West");
		mdp.addSingleOutcomAction(State.instance("C"), State.instance("D"), 0, "East");

		mdp.addSingleOutcomAction(State.instance("D"), State.instance("C"), 0, "West");
		mdp.addSingleOutcomAction(State.instance("D"), State.instance("E"), 0, "East");

		mdp.addSingleOutcomAction(State.instance("E"), State.instance("D"), 0, "West");
		mdp.addSingleOutcomAction(State.instance("E"), State.instance("F"), 0, "East");

		mdp.addSingleOutcomAction(State.instance("F"), State.instance("E"), 0, "West");
		mdp.addSingleOutcomAction(State.instance("F"), State.instance("G"), 0, "East");

		State.instance("A").setValue(0);
		State.instance("G").setValue(1);
	}

	/**
	 * The vector representation of the k's State in the episode
	 * @param episode
	 * @param k
	 * @return
	 */
	private Vector getStateVector(Episode episode, int k) {
		return getStateVector(episode.getState(k));
	}

	private Vector getStateVector(State s) {
		if (s.getId().equals("B"))
			return new Vector(1, 0, 0, 0, 0);
		if (s.getId().equals("C"))
			return new Vector(0, 1, 0, 0, 0);
		if (s.getId().equals("D"))
			return new Vector(0, 0, 1, 0, 0);
		if (s.getId().equals("E"))
			return new Vector(0, 0, 0, 1, 0);
		if (s.getId().equals("F"))
			return new Vector(0, 0, 0, 0, 1);
		throw new RuntimeException("Unexepected state: " + s);
	}

}
