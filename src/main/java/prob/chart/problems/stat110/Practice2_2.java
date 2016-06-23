package prob.chart.problems.stat110;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.MLUtils;
import static util.MLUtils.*;

public class Practice2_2 extends Simulation {

	@Override
	protected Boolean simulate() {
		List<Integer> list = MLUtils.orderedList(30);
		Map<Integer, Integer> days = new HashMap<>();
		List<Integer> randomChoice = MLUtils.randomChoice(list, 7, true);
		for (Integer choice : randomChoice) {
			days.put(choice / 6, choice);
		}
		return days.size() == 5;
	}

	@Override
	protected double computedResult() {
		return 1 - (5 * choose(24, 7) / choose(30, 7) - choose(5, 2) * choose(18, 7) / choose(30, 7) + choose(5, 3) * choose(12, 7) / choose(30, 7));
	}

}
