import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import ml.ul.CompatibilityChecker;
import ml.ul.DiscreteProbabilityDistribution;
import ml.ul.EM;

public class EMTest {

	public static void main(String[] args) {
		List<?> thetaValues = Arrays.asList(new String[] { "Summer", "Spring", "Winter", "Autumn" });
		List<Integer> zero = Arrays.asList(new Integer[] { 0, 0, 0 });
		List<Integer> one = Arrays.asList(new Integer[] { 0, 0, 1 });
		List<Integer> two = Arrays.asList(new Integer[] { 0, 1, 0 });
		List<Integer> three = Arrays.asList(new Integer[] { 0, 0, 1 });
		List<Integer> four = Arrays.asList(new Integer[] { 1, 0, 0 });
		List<Integer> five = Arrays.asList(new Integer[] { 1, 0, 1 });
		List<Integer> six = Arrays.asList(new Integer[] { 1, 1, 0 });
		List<Integer> seven = Arrays.asList(new Integer[] { 1, 1, 1 });

		DiscreteProbabilityDistribution summer = new DiscreteProbabilityDistribution();
		DiscreteProbabilityDistribution spring = new DiscreteProbabilityDistribution();
		DiscreteProbabilityDistribution winter = new DiscreteProbabilityDistribution();
		DiscreteProbabilityDistribution autumn = new DiscreteProbabilityDistribution();
		HashMap<Object, DiscreteProbabilityDistribution> map = new LinkedHashMap<Object, DiscreteProbabilityDistribution>();
		map.put("Summer", summer);
		map.put("Spring", spring);
		map.put("Winter", winter);
		map.put("Autumn", autumn);
		

		summer.addInstance(zero, 0.1);
		summer.addInstance(one, 0.2);
		summer.addInstance(two, 0.1);
		summer.addInstance(three, 0.1);
		summer.addInstance(four, 0.1);
		summer.addInstance(five, 0.1);
		summer.addInstance(six, 0.2);
		summer.addInstance(seven, 0.1);

		spring.addInstance(zero, 0.3);
		spring.addInstance(one, 0.1);
		spring.addInstance(two, 0.1);
		spring.addInstance(three, 0.1);
		spring.addInstance(four, 0.1);
		spring.addInstance(five, 0.1);
		spring.addInstance(six, 0.1);
		spring.addInstance(seven, 0.1);

		winter.addInstance(zero, 0.1);
		winter.addInstance(one, 0.1);
		winter.addInstance(two, 0.1);
		winter.addInstance(three, 0.1);
		winter.addInstance(four, 0.1);
		winter.addInstance(five, 0.1);
		winter.addInstance(six, 0.1);
		winter.addInstance(seven, 0.3);

		autumn.addInstance(zero, 0.15);
		autumn.addInstance(one, 0.15);
		autumn.addInstance(two, 0.1);
		autumn.addInstance(three, 0.1);
		autumn.addInstance(four, 0.1);
		autumn.addInstance(five, 0.1);
		autumn.addInstance(six, 0.2);
		autumn.addInstance(seven, 0.1);

		CompatibilityChecker checker = new CompatibilityChecker() {

			@Override
			public boolean isCompatible(Object instance, Object observation) {
				List<Integer> l = (List<Integer>) instance;
				Integer obs = (Integer) observation;
				int sum = 0;
				for (Integer el : l) {
					sum = sum + el;
				}
				return sum == obs;
			}
		};
		
		EM em = new EM(thetaValues, map, checker, Integer.valueOf(3));
		em.run();

	}
}
