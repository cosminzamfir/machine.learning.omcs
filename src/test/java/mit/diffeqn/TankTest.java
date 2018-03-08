package mit.diffeqn;

import prob.chart.ChartBuilder;
import prob.chart.PlotType;
import ml.model.function.Function;
import ml.model.function.Solver;
import static java.lang.Math.*;

public class TankTest {

	public static void main(String[] args) {
		Function s = t -> 60000 + 150*t - 4.8 * pow(10,8) * pow(400 + t,-1.5);
		
		Function volume = t -> 800 + 2*t;
		Function concentration = t -> s.evaluate(t) / volume.evaluate(t);
		new ChartBuilder().add(concentration, 0, 1000, 10000, "Concentration", PlotType.Line).build();
		
		double timeToTarget = new Solver().solve(concentration, 34, 1e-20, 10);
		System.out.println(timeToTarget);
		System.out.println(concentration.evaluate(timeToTarget));
	}
}
