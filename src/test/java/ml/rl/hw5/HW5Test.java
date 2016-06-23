package ml.rl.hw5;

import ml.rl.mdp.PolicyIteration;
import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.MDPPolicy;
import ml.rl.mdp.model.State;
import ml.rl.mdp.view.MDPViewer;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author Cosmin Zamfir
 *
 */
public class HW5Test {

	private HW5Environment env;

	@Test
	public void test1() throws Exception {
		double[][] movementMean = { { -0.002, 0.049, 0.076, 0.008, 0.161, 0.175, 0.124, 0.159 }, { 0.129, 0.169, 0.168, 0.059, -0.047, 0.142, -0.07, 0.188 },
				{ -0.052, 0.099, -0.067, 0.172, 0.081, -0.095, 0.121, -0.095 }, { 0.142, 0.025, -0.055, 0.187, -0.099, 0.125, 0.056, 0.024 },
				{ -0.072, 0.125, 0.076, 0.07, -0.069, 0.032, -0.044, -0.094 } };
		double[][] movementSD = { { 0.077, 0.062, 0.064, 0.042, 0.057, 0.07, 0.063, 0.055 }, { 0.058, 0.042, 0.077, 0.089, 0.052, 0.074, 0.053, 0.088 },
				{ 0.057, 0.071, 0.051, 0.063, 0.072, 0.075, 0.056, 0.042 }, { 0.062, 0.058, 0.063, 0.087, 0.078, 0.089, 0.046, 0.042 },
				{ 0.064, 0.053, 0.051, 0.075, 0.087, 0.091, 0.048, 0.047 } };
		double[] sampleLocations = { 0.67, 0.69, 0.74, 0.77, 0.85, 0.89 };
		int[] bestActions = { 3, 3, 3, 3, 1, 1 };
		test(movementMean, movementSD, sampleLocations, bestActions);
	}

	@Test
	public void test2() throws Exception {
		double[][] movementMean = { { 0.213, 0.164, -0.219, 0.202, 0.313, -0.041, -0.075, 0.301 },
				{ 0.241, -0.079, 0.11, -0.061, -0.17, -0.033, -0.195, 0.05 }, { -0.213, -0.116, 0.295, -0.143, 0.096, 0.136, 0.206, 0.242 },
				{ 0.161, 0.069, -0.096, 0.298, -0.177, 0.19, -0.016, 0.0 }, { 0.271, 0.038, -0.041, -0.154, -0.143, 0.213, -0.043, 0.265 } };
		double[][] movementSD = { { 0.105, 0.096, 0.088, 0.116, 0.123, 0.088, 0.082, 0.11 }, { 0.133, 0.09, 0.093, 0.085, 0.081, 0.124, 0.13, 0.116 },
				{ 0.086, 0.123, 0.117, 0.083, 0.103, 0.092, 0.131, 0.093 }, { 0.117, 0.095, 0.108, 0.109, 0.111, 0.117, 0.119, 0.101 },
				{ 0.131, 0.09, 0.11, 0.129, 0.105, 0.125, 0.094, 0.105 } };
		double[] sampleLocations = { 0.17, 0.21, 0.5, 0.65, 0.69, 0.75, 0.9, 0.99, 0.99 };
		int[] bestActions = { 4, 0, 2, 3, 3, 3, 7, 7, 7 };
		test(movementMean, movementSD, sampleLocations, bestActions);
	}

	@Test
	public void test3() throws Exception {
		double[][] movementMean = { { 0.208, 0.189, 0.054, 0.12, -0.031, 0.25, -0.001, 0.156 }, { 0.164, 0.226, 0.107, 0.094, -0.065, 0.076, 0.017, 0.198 },
				{ 0.081, 0.078, -0.13, -0.09, 0.033, -0.142, -0.05, 0.083 }, { -0.134, -0.131, 0.237, -0.125, -0.149, -0.03, 0.235, -0.072 },
				{ 0.046, 0.052, 0.112, -0.082, -0.026, 0.081, -0.017, 0.276 } };
		double[][] movementSD = { { 0.089, 0.106, 0.088, 0.117, 0.101, 0.078, 0.079, 0.109 }, { 0.104, 0.103, 0.085, 0.082, 0.108, 0.102, 0.108, 0.093 },
				{ 0.091, 0.11, 0.093, 0.102, 0.101, 0.072, 0.08, 0.084 }, { 0.095, 0.098, 0.102, 0.104, 0.078, 0.113, 0.086, 0.097 },
				{ 0.098, 0.091, 0.095, 0.096, 0.118, 0.113, 0.086, 0.078 } };
		double[] sampleLocations = { 0.12, 0.27, 0.29, 0.31, 0.67, 0.75 };
		int[] bestActions = { 5, 1, 1, 1, 2, 2 };
		test(movementMean, movementSD, sampleLocations, bestActions);
	}

	@Test
	public void test4() throws Exception {
		double[][] movementMean = { { 0.196, -0.045, -0.124, 0.118, 0.226, 0.052, 0.075, 0.103 }, { 0.124, 0.176, 0.033, 0.074, 0.168, -0.05, 0.12, -0.106 },
				{ 0.149, 0.211, 0.176, 0.193, 0.217, 0.061, 0.002, 0.068 }, { -0.141, -0.062, -0.108, 0.053, -0.141, -0.037, -0.086, -0.141 },
				{ -0.109, 0.012, 0.108, 0.227, -0.086, 0.175, 0.115, 0.189 } };
		double[][] movementSD = {{0.019,0.04,0.041,0.013,0.055,0.03,0.04,0.056},{0.034,0.019,0.019,0.027,0.03,0.007,0.052,0.017},{0.02,0.022,0.017,0.007,0.035,0.013,0.028,0.011},{0.041,0.01,0.036,0.042,0.053,0.032,0.02,0.009},{0.006,0.018,0.028,0.03,0.039,0.038,0.008,0.049}};
		double[] sampleLocations = {0.2,0.56,0.69,0.78}; 
		int[] bestActions = {};
		test(movementMean, movementSD, sampleLocations, bestActions);
	}

	public void test(double[][] movementMean, double[][] movementSD, double[] sampleLocations, int[] bestActions) throws Exception {
		env = new HW5Environment(100, movementMean, movementSD);
		MDP mdp = env.generateMDP();
		MDPViewer.instance(mdp).display();

		System.out.println(mdp.printStateData(HW5Environment.TERRAIN_TYPE));
		System.out.println(mdp.printStateData(HW5Environment.POSITION));

		PolicyIteration pi = new PolicyIteration(mdp);
		pi.run();
		MDPPolicy policy = pi.getPolicy();
		System.out.print("bestActions={"); 
		for (int i = 0; i < sampleLocations.length; i++) {
			//int expectedBestAction = bestActions[i];
			State state = env.getState(sampleLocations[i]);
			int actualBestAction = Integer.valueOf(policy.getAction(state).getName());
			System.out.print(actualBestAction);
			if(i < sampleLocations.length - 1) {
				System.out.print(",");
			}
			//Assert.assertEquals(expectedBestAction, actualBestAction);
		}
		System.out.println("}");
	}

}
