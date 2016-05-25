package prob.chart.problems.grinstead;

import java.util.ArrayList;
import java.util.List;

import ml.utils.Utils;

/**
 *  The Polya urn model for contagion is as follows: We start with an urn which
 	contains one white ball and one black ball. At each second we choose a ball
	at random from the urn and replace this ball and add one more of the color
	chosen. Write a program to simulate this model, and see if you can make
	any predictions about the proportion of white balls in the urn after a large
	number of draws. Is there a tendency to have a large fraction of balls of the
	same color in the long run?
 *  @author eh2zamf
 *
 */
public class PolyaUrnModel {

	public static void main(String[] args) {
		new PolyaUrnModel().run();
	}
	
	private void run() {
		int n = 10000;
		List<Integer> l = new ArrayList<>();
		l.add(0);
		l.add(1);
		for (int i = 0; i < n; i++) {
			Integer val = Utils.randomElement(l);
			l.add(val);
			int zeros = Utils.count(l,Integer.valueOf(0));
			int ones = Utils.count(l, Integer.valueOf(1));
			System.out.println("Proportion of zeros to ones: " + zeros/(double)ones);
		}
	}
}
