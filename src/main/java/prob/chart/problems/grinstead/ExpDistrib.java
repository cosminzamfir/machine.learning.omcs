package prob.chart.problems.grinstead;

/**
 * Simulate the distribution: lambda * e ^ (-lambda*t)
 * <p>
 * It may represent the probability distribution of waiting time between the occurence of 2 events, where lambda is the reciprocal of the average of X
 * @author eh2zamf
 *
 */
public class ExpDistrib {

	public static double averageLifeTime = 10;
	public static double lambda = 1d/averageLifeTime;
	public static int timeStamp = 100;
	public static int n = 100000;
	

	public static void main(String[] args) {
		double total = 0;
		int samples = 0;
		for (int i = 0; i < n; i++) {
			double sample = (-1 / lambda) * Math.log(Math.random());
			if (sample > timeStamp) {
				total = total + sample;
				samples++;
			}
		}
		double average = total / samples - timeStamp;
		System.out.println("Average of exponential distribution with average " + 1 / lambda + " is: " + average);
	}
}
