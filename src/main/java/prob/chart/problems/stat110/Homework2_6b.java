package prob.chart.problems.stat110;

import java.util.List;

import ml.utils.Utils;
import static ml.utils.Utils.*;

public class Homework2_6b extends Simulation {

	@Override
	protected Boolean simulate() {
		List<ml.utils.Utils.Gender> genders = Utils.genders();
		List<Integer> months = Utils.months();
		
		ml.utils.Utils.Gender firstChildGender = randomElement(genders);
		int firstChildMonth = Utils.randomElement(months);
		
		ml.utils.Utils.Gender secondChildGender = randomElement(genders);
		int secondChildMonth = Utils.randomElement(months);
		
		if((firstChildGender == ml.utils.Utils.Gender.F && firstChildMonth == 2) || (secondChildGender == ml.utils.Utils.Gender.F && secondChildMonth == 2)) {
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
