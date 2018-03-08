package mit.diffeqn;

import static java.lang.Math.*;
import prob.chart.ChartBuilder;
import prob.chart.PlotType;
import ml.model.function.Function;
public class DampedOscTest {

	public static void main(String[] args) {
		double b = 10;
		double c1= + 20;
		double c2 = -100;
		Function f = new Function() {
			
			@Override
			public double evaluate(double t) {
				double t1 = t*(-b/2 - sqrt(b*b - 64)/2);
				double t2 = t*(-b/2 + sqrt(b*b - 64)/2);
				double e1 = c1 * pow(E,t1);
				double e2 = c2 * pow(E,t2);
				return e1 + e2;
			}
		}; 
		new ChartBuilder().add(f, 0, 10, 1000, "position", PlotType.Line).build();
		
	}
}
