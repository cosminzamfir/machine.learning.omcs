package prob.chart.problems.grinstead;

import java.util.List;

import ml.utils.Utils;

/**
 * There are n applicants for the director of computing. The applicants are interviewed
independently by each member of the three-person search committee
and ranked from 1 to n. A candidate will be hired if he or she is ranked first
by at least two of the three interviewers. Find the probability that a candidate
will be accepted if the members of the committee really have no ability at all
to judge the candidates and just rank the candidates randomly. In particular,
compare this probability for the case of three candidates and the case of ten
candidates.
 * @author eh2zamf
 *
 */
public class Interviews {

	public static final int n = 100;
	
	public static void main(String[] args) {
		int trials = 900000;
		int successes = 0;
		//generate trials permutations. Success if at least 2 permutations have '1' as the first element
		for (int i = 0; i < trials; i++) {
			List<Integer> l1 = Utils.randomPermutation(n);
			List<Integer> l2 = Utils.randomPermutation(n);
			List<Integer> l3 = Utils.randomPermutation(n);
			if(l1.get(0) == 1 && l2.get(0) == 1 || l2.get(0) == 1 && l3.get(0) == 1 || l3.get(0) == 1 && l1.get(0) == 1) {
				successes ++;
			}
		}
		System.out.println("Simulated probability: " + successes/(double)trials);
		double computedValue = (3*n-2) / ((double) n*n*n);
		System.out.println("Computed probability: " + computedValue);
	}
}
