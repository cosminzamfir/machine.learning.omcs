import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ml.model.function.Function;
import ml.model.function.NormalFunction;
import ml.model.function.Solver;
import ml.stat.StatUtils;


public class Test {

	public static void main1(String[] args) {
		new Function() {public double evaluate(double x) {return 2*x*x + 1;}}.plot(-2, 2);
	}
	
	public static void main3(String[] args) {
		 double percentile = 0.70;
		 Function f = new Function() {
			@Override
			public double evaluate(double x) {
				return new NormalFunction(0, 1).definiteIntegral(-20,x,0.0000001) - percentile;			}
		};
		System.out.println(new Solver().solve(f, 0, 0.0000001, 0.7)); 
	}
	
	public static void main(String[] args) {
		List<Double> l1 = Arrays.asList(-1d,0d,1d,2d,3d);
		double correlation = new StatUtils().correlation(l1, l1);
		System.out.println(correlation);
	}
}