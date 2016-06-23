package prob.chart.problems.grinstead;

import java.util.Arrays;
import java.util.List;

import util.MLUtils;

public class MontyHall {

	private static int n = 100000000;

	/**
	 * Run the MontyHall experiment n times
	 * @param switchDoor whether the player switich the door at the second choice
	 * @return the simultated success probability
	 */
	public double run(boolean switchDoor) {
		int successes = 0;
		for (int i = 0; i < n; i++) {
			List<Integer> doors = Arrays.asList(1, 2, 3);
			int winningDoor = MLUtils.randomElement(doors);
			int firstChoice = MLUtils.randomElement(doors);
			int badDoor = (winningDoor == firstChoice ?
					MLUtils.randomElement(MLUtils.minus(doors, winningDoor)) :
					MLUtils.minus(doors, winningDoor, firstChoice).get(0));
			int secondChoice = switchDoor ? MLUtils.minus(doors, firstChoice, badDoor).get(0) : firstChoice;
			if (secondChoice == winningDoor) {
				successes++;
			}
			//System.out.println("Wining door:" + winningDoor + ";First choice:" + firstChoice + ";Bad door:" + badDoor + ";Second choice:" + secondChoice + "Total successes:" + successes);
		}
		return successes / (double) n;
	}

	public static void main(String[] args) {
		MontyHall m = new MontyHall();
		System.out.println("No switch success rate: " + m.run(false) * 3 + "; Switch success rate: " + m.run(true) * 3 / 2);
	}
}
