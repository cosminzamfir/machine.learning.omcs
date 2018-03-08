package mit.diffeqn;

import prob.chart.ChartBuilder;
import prob.chart.PlotType;
import ml.model.function.Function;
import static java.lang.Math.*;

public class SumOfSinCos {

	public static void main(String[] args) {
		double c1 = 1;
		double c2 = 1;
		double w = 1;
		Function f = t-> c1*cos(w*t) + c2*sin(w*t);
		new ChartBuilder().add(f, 0, 10, 10000, "sin-cos", PlotType.Line).build();
	}
}
