import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import ml.ul.CompatibilityChecker;
import ml.ul.DiscreteProbabilityDistribution;
import ml.ul.EM;

public class EMTest1 {

	public static void main(String[] args) {
		List<?> thetaValues = Arrays.asList(new String[] { "Summer", "Spring", "Winter", "Autumn" });
		DiscreteProbabilityDistribution summer = new DiscreteProbabilityDistribution();
		DiscreteProbabilityDistribution spring = new DiscreteProbabilityDistribution();
		DiscreteProbabilityDistribution winter = new DiscreteProbabilityDistribution();
		DiscreteProbabilityDistribution autumn = new DiscreteProbabilityDistribution();
		HashMap<Object, DiscreteProbabilityDistribution> map = new LinkedHashMap();
		map.put("Summer", summer);
		map.put("Spring", spring);
		map.put("Winter", winter);
		map.put("Autumn", autumn);
		generateProbabilityDistribution(1024*1024,summer);
		generateProbabilityDistribution(1024*1024,spring);
		generateProbabilityDistribution(1024*1024,autumn);
		generateProbabilityDistribution(1024*1024,winter);

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
		
		EM em = new EM(thetaValues, map, checker, Integer.valueOf(10));
		em.run();
	}

	private static void generateProbabilityDistribution(int i, DiscreteProbabilityDistribution summer) {
		List<List<Integer>> l = generateAllLists(i);
		List<Double> probabilities = generateProbabilities(l);
		for (int j = 0; j < l.size(); j++) {
			summer.addInstance(l.get(j), probabilities.get(j));
		}
	}

	private static List<Double> generateProbabilities(List<List<Integer>> l) {
		List<Double> res = new ArrayList<>();
		int size = l.size();
		for (int i = 0; i < size; i++) {
			res.add(new Random().nextDouble());
		}
		double sum = 0;
		for (Double elem : res) {
			sum += elem;
		}
		for (int i = 0; i < res.size(); i++) {
			res.set(i, res.get(i)/sum);
		}
		return res;
	}

	private static List<List<Integer>> generateAllLists(int n) {
		List<List<Integer>> res = new ArrayList<List<Integer>>();
		int length = Integer.toBinaryString(n).length();
		for (int i = 0; i < n; i++) {
			String s = Integer.toBinaryString(i);
			s = StringUtils.leftPad(s, length, '0');
			List<Integer> l = new ArrayList<>();
			for (Character ch : s.toCharArray()) {
				if(ch.equals('0')) {
					l.add(0);
				} else {
					l.add(1);
				}
			}
			res.add(l);
		}
		return res;
	}
}
