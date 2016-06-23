package prob.chart.problems.stat110;

import java.util.List;

import util.MLUtils;
import static util.MLUtils.*;

public class Homework2_6b extends Simulation {

	@Override
	protected Boolean simulate() {
		List<util.MLUtils.Gender> genders = MLUtils.genders();
		List<Integer> months = MLUtils.months();
		
		util.MLUtils.Gender firstChildGender = randomElement(genders);
		int firstChildMonth = MLUtils.randomElement(months);
		
		util.MLUtils.Gender secondChildGender = randomElement(genders);
		int secondChildMonth = MLUtils.randomElement(months);
		
		if((firstChildGender == util.MLUtils.Gender.F && firstChildMonth == 2) || (secondChildGender == util.MLUtils.Gender.F && secondChildMonth == 2)) {
			if(firstChildGender == secondChildGender) {
				return true;
			} else {
				return false;
			}
		} else {
			return null;
		}
		
		
	}

	@Override
	protected double computedResult() {
		return 0.489;
	}

}
