package prob.chart.problems.stat110;

import java.util.HashMap;
import java.util.Map;

import ml.utils.Utils;

public class Practice2_1 extends Simulation {

	@Override
	protected Boolean simulate() {
		Map<Integer, Integer> map = new HashMap<>();
		for (int i = 0; i < 7; i++) {
			int season = Utils.random(4);
			map.put(season, season);
			if(map.size() == 4) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected double computedResult() {
		return 0.51;
	}

	
}
