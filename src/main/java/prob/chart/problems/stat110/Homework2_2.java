package prob.chart.problems.stat110;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ml.utils.Utils;
import static ml.utils.Utils.*;

public class Homework2_2 extends Simulation {

	@Override
	protected Boolean simulate() {
		List<Integer> list = Utils.orderedList(52);
		Map<Integer, Integer> days = new HashMap<>();
		List<Integer> randomChoice = Utils.randomChoice(list, 13, true);
		for (Integer choice : randomChoice) {
			days.put(choice / 13, choice);
		}
		return days.size() < 4;
	}

	@Override
	protected double computedResult() {
		BigDecimal p1 = chooseBD(4, 1).multiply(chooseBD(39,13)).divide(chooseBD(52,13), RoundingMode.UP);
		BigDecimal p2 = chooseBD(4, 2).multiply(chooseBD(26,13)).divide(chooseBD(52,13), RoundingMode.UP);
		BigDecimal p3 = chooseBD(4, 3).multiply(chooseBD(13,13)).divide(chooseBD(52,13), RoundingMode.UP);
		return p1.subtract(p2).add(p3).doubleValue();
	}

}
