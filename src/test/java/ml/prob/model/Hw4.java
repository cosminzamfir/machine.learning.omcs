package ml.prob.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import prob.distribution.model.JointProbabilityTable;

public class Hw4 {

	private static JointProbabilityTable t;
	private static List<String> rvs = Arrays.asList("R", "C", "W", "T");

	public static void main(String[] args) {
		setup();
		//printIndependencies();
		p14();
		//p5();
	}

	private static void printIndependencies() {
		for (int i = 0; i < rvs.size() - 1; i++) {
			for (int j = i + 1; j < rvs.size(); j++) {
				String rv1 = rvs.get(i);
				String rv2 = rvs.get(j);
				System.out.println(rv1 + "+" + rv2 + " : " + t.isIndependent(rv1, rv2));
			}
		}

		for (String givenRv : rvs) {
			List<String> otherRvs = new ArrayList<>(rvs);
			otherRvs.remove(givenRv);
			for (int i = 0; i < otherRvs.size() - 1; i++) {
				for (int j = i + 1; j < otherRvs.size(); j++) {
					String rv1 = otherRvs.get(i);
					String rv2 = otherRvs.get(j);
					System.out.println(rv1 + "+" + rv2 + "|" + givenRv + " : "+ t.isIndependent(rv1, rv2, givenRv));
				}
			}

		}
	}

	//rc, rt, rw, ct, cw, rw
	//T-R-W-C
	//
	private static void p5() {
		System.out.println("R+C: " + t.isIndependent("R", "C")); //
		System.out.println("R+T: " + t.isIndependent("R", "T"));
		System.out.println("R+W: " + t.isIndependent("R", "W"));
		System.out.println("C+T: " + t.isIndependent("C", "T"));
		System.out.println("C+W: " + t.isIndependent("C", "W"));
		System.out.println("R+W: " + t.isIndependent("R", "W"));
	}

	private static void p14() {
		//System.out.println("Is T+W|R: " + t.isIndependent("T", "W", "R"));
		System.out.println("Is T+W|R,C: " + t.isIndependent("T", "W", "R", "C"));
		//System.out.println("Is R+C|W: " + t.isIndependent("R", "C", "W"));
		//System.out.println("Is R+T: " + t.isIndependent("R", "T"));
	}

	private static void setup() {
		t = new JointProbabilityTable("R", "C", "W", "T");
		t.add(0.25, 0, 0, 1, 0);
		t.add(0.25, 0, 1, 0, 1);
		t.add(0.25, 1, 0, 0, 1);
		t.add(0.25, 1, 1, 1, 0);
	}

}
