package prob.chart.problems.stat110;

import util.MLUtils;

public class Practice2_3_5 extends Simulation {

	@Override
	protected Boolean simulate() {
		double winIfBeginner = 0.9;
		double winIfIntermediate = 0.5;
		double winIfAdvanced = 0.3;
		
		int opponentLevel = MLUtils.random(3);
		switch (opponentLevel) {
		case 1:
			if(!MLUtils.trueWithProbability(winIfBeginner)) {
				return null;
			}
			break;
		case 2:
			if(!MLUtils.trueWithProbability(winIfIntermediate)) {
				return null;
			}
		case 3:
			if(!MLUtils.trueWithProbability(winIfAdvanced)) {
				return null;
			}
		default:
			break;
		}
		
		opponentLevel = MLUtils.random(3);
		switch (opponentLevel) {
		case 1:
			return MLUtils.trueWithProbability(winIfBeginner);
		case 2:
			return MLUtils.trueWithProbability(winIfIntermediate);
		case 3:
			return MLUtils.trueWithProbability(winIfAdvanced);
		default:
			break;
		}
		throw new RuntimeException("must not reach here");

	}

	@Override
	protected double computedResult() {
		return 0.68;
	}

}
