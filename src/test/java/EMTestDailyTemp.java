import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import ml.ul.CompatibilityChecker;
import ml.ul.DiscreteProbabilityDistribution;
import ml.ul.EM;

public class EMTestDailyTemp {

	public static void main(String[] args) {
		List<?> thetaValues = Arrays.asList(new String[] { "Summer", "Spring", "Winter", "Autumn" });

		DiscreteProbabilityDistribution summer = new DiscreteProbabilityDistribution();
		DiscreteProbabilityDistribution spring = new DiscreteProbabilityDistribution();
		DiscreteProbabilityDistribution winter = new DiscreteProbabilityDistribution();
		DiscreteProbabilityDistribution autumn = new DiscreteProbabilityDistribution();
		HashMap<Object, DiscreteProbabilityDistribution> map = new LinkedHashMap<Object, DiscreteProbabilityDistribution>();
		map.put("Spring", spring);
		map.put("Summer", summer);
		map.put("Winter", winter);
		map.put("Autumn", autumn);
		for (Object season : map.keySet()) {
			for (int i = 0; i < 10; i++) {
				map.get(season).addObservation(getHourlyTemp(getAvgTemp((String) season)));
			}
		}

		CompatibilityChecker checker = new CompatibilityChecker() {
			@Override
			public boolean isCompatible(Object instance, Object observation) {
				List<Integer> hourlyTemp = (List<Integer>) instance;
				Integer mean = (Integer) observation;
				int sum = 0;
				for (Integer el : hourlyTemp) {
					sum = sum + el;
				}
				double listMean = ((double)sum) /hourlyTemp.size();
				boolean compatible = listMean/mean > 0.8 && listMean/mean < 1.0/0.8;
				return compatible;
			}
		};

		EM em = new EM(thetaValues, map, checker, 20);
		em.run();
	}

	private static List<Integer> getHourlyTemp(double dailyAverage) {
		List<Integer> res = new ArrayList<>();
		for (int i = 0; i < 24; i++) {
			double spread = 1;
			res.add((int) (dailyAverage + spread*new Random().nextGaussian()));
		}
		return res;
	}

	private static Double getAvgTemp(String season) {
		if (season.equals("Winter")) {
			return -3.0;
		}
		if (season.equals("Spring")) {
			return 8.0;
		}
		if (season.equals("Summer")) {
			return 20.0;
		}
		if (season.equals("Autumn")) {
			return 11.0;
		}
		throw new RuntimeException();

	}

}
