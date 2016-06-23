package prob.chart.problems.stat110;

import util.MLUtils;

public class Homework2_4c extends Simulation {

	@Override
	protected Boolean simulate() {
		boolean cIsFair = MLUtils.trueWithProbability(0.5);
		double headProb;
		if(cIsFair) {
			headProb = 0.5;
		} else {
			headProb = 0.25;
		}
		int headCounts = 0;
		for (int i = 0; i < 10; i++) {
			if(MLUtils.trueWithProbability(headProb)) {
				headCounts ++;
			}
		}
		return headCounts == 3;
	}

	@Override
	protected double computedResult() {
		 return (MLUtils.choose(10, 3)*Math.pow(0.5, 3)*Math.pow(0.5, 7) + MLUtils.choose(10, 3)*Math.pow(0.25, 3)*Math.pow(0.75, 7))/2; 
	}

}
