package mit.diffeqn;

import ml.model.function.Function;
import ml.model.function.Solver;

public class InterestTest {

	public static void main(String[] args) {
		int t = 18;
		double I = 0.01;
		Function f = x -> 1440*(t/I + 1/(I*I)) + x * Math.pow(Math.E, I*t);
		double res = new Solver().solve(f, 0, 0.01, -100);
		System.out.println(res);
		Function f1 = time -> 1440*(time/I + 1/(I*I)) + res * Math.pow(Math.E, I*time);
		System.out.println(f1.evaluate(0));
	}
}
