package prob.chart.problems.grinstead;

import java.util.Random;

/**
 * Simulate 2 strategies of the 2-armed casino game:
 * Strategy1: play-by-the-best: at each step, compute a 'score' as the probability of winning the next step for each of the 2 machines: P(w) = n(w) +1 / n(w) + n(l) + 2
 * (proof of the above probability in the beta-distribution section). Each time, choose the machine with higher score
 * Strategy2: play-by-the-best: start with a random machine. If you win, keep the machine. If you loose, switch the machine.
 * @author eh2zamf
 *
 */
public class TwoArmedBandit {

	public static void main(String[] args) {
		double bestMachineResultTotal = 0;
		double winMachineResultTotal = 0;
		for (int i = 0; i < 1000; i++) {
			double x = new Random().nextDouble();
			double y = new Random().nextDouble();
			double bestMachineResult = runBestMachine(x, y, 1000);
			double winMachineResult = runWinningMachine(x, y, 1000);
			bestMachineResultTotal += bestMachineResult;
			winMachineResultTotal += winMachineResult;
			if (bestMachineResult >= winMachineResult) {
				System.out.println("bestMachineStrategy beat winningMachineStrategy with " + bestMachineResult + " to " + winMachineResult
						+ ". The winning probabilities of the machines were: " + x + " and " + y);
			} else {
				System.out.println("winningMachineStrategy beat bestMachineStrategy with " + winMachineResult + " to " + bestMachineResult
						+ ". The winning probabilities of the machines were: " + x + " and " + y);
			}
		}
		if (bestMachineResultTotal >= winMachineResultTotal) {
			System.out.println("Overall bestMachineStrategy beat winningMachineStrategy in proportion of " + bestMachineResultTotal/winMachineResultTotal);
		} else {
			System.out.println("Overall winningMachineStrategy beat bestMachineStrategy with " + winMachineResultTotal/bestMachineResultTotal);
		}

	}

	/**
	 * 
	 * @param x winning probability of machine 1
	 * @param y winning probability of machine 2
	 * @param samples number of repetition
	 * @return the amount won
	 */
	private static double runBestMachine(double x, double y, int samples) {
		int xWins = 0;
		int yWins = 0;
		int xLosses = 0;
		int yLosses = 0;
		int currentMachine = new Random().nextDouble() > 0.5 ? 0 : 1;
		for (int i = 0; i < samples; i++) {
			if (currentMachine == 0) {
				if (new Random().nextDouble() < x) {
					xWins++;
				} else {
					xLosses++;
				}
			} else {
				if (new Random().nextDouble() < y) {
					yWins++;
				} else {
					yLosses++;
				}
			}
			currentMachine = (xWins + 1) / (xWins + xLosses + 2) > (yWins + 1) / (yWins + yLosses + 2) ? 0 : 1;
		}
		return xWins + yWins;
	}

	/**
	 * 
	 * @param x winning probability of machine 1
	 * @param y winning probability of machine 2
	 * @param samples number of repetition
	 * @return the amount won
	 */
	private static double runWinningMachine(double x, double y, int samples) {
		int xWins = 0;
		int yWins = 0;
		int currentMachine = new Random().nextDouble() > 0.5 ? 0 : 1;
		for (int i = 0; i < samples; i++) {
			if (currentMachine == 0) {
				if (new Random().nextDouble() < x) {
					xWins++;
				} else {
					currentMachine = 1;
				}
			} else {
				if (new Random().nextDouble() < y) {
					yWins++;
				} else {
					currentMachine = 0;
				}
			}
		}
		return xWins + yWins;
	}
}
