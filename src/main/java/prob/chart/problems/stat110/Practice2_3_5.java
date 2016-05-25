package prob.chart.problems.stat110;

import ml.utils.Utils;

public class Practice2_3_5 extends Simulation {

	@Override
	protected Boolean simulate() {
		double winIfBeginner = 0.9;
		double winIfIntermediate = 0.5;
		double winIfAdvanced = 0.3;
		
		int opponentLevel = Utils.random(3);
		switch (opponentLevel) {
		case 1:
			if(!Utils.trueWithProbability(winIfBeginner)) {
				return null;
			}
			break;
		case 2:
			if(!Utils.trueWithProbability(winIfIntermediate)) {
				return null;
			}
		case 3:
			if(!Utils.trueWithProbability(winIfAdvanced)) {
				return null;
			}
		default:
			break;
		}
		
		opponentLevel = Utils.random(3);
		switch (opponentLevel) {
		case 1:
			return Utils.trueWithProbability(winIfBeginner);
		case 2:
			return Utils.trueWithProbability(winIfIntermediate);
		case 3:
			return Utils.trueWithProbability(winIfAdvanced);
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
