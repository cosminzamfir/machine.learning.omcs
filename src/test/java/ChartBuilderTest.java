import java.util.List;

import prob.chart.ChartBuilder;
import prob.chart.PlotType;
import ml.model.function.Function;
import ml.model.function.IdentityFunction;
import util.MLUtils;

public class ChartBuilderTest {

	public static void main(String[] args) {
		List<Double> l1 = MLUtils.randomList(100, 0, 100);
		List<Double> l2 = MLUtils.randomList(100, 40, 140);
		Function f = new IdentityFunction();
		ChartBuilder builder = new ChartBuilder("Test", "x", "y");
		builder.add(l1, l2, "Scatter", PlotType.Scatter).add(f, 0, 100, 100, "Regression line", PlotType.LineWithShape).build();
	}
}
