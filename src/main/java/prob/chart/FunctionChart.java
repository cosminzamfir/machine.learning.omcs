package prob.chart;

import ml.model.function.Function;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class FunctionChart extends ApplicationFrame {

	private static final long serialVersionUID = 1L;
	private int samples = 1000;
	private Function function;

	private double f(double x) {
		return function.evaluate(x);
	}

	public FunctionChart(Function function, double min, double max, final String title) {
		super(title);
		this.function = function;
		final XYSeries series = new XYSeries("Function chart");

		for (int i = 0; i < samples; i++) {
			double x = min + i * (max - min) / (double) samples;
			double y = f(x);
			series.add(x, y);
		}
		final XYSeriesCollection data = new XYSeriesCollection(series);
		final JFreeChart chart = ChartFactory.createXYLineChart(function.toString(), "X", "Y", data, PlotOrientation.VERTICAL,
				true, true, false
				);

		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
		pack();
		RefineryUtilities.centerFrameOnScreen(this);
		setVisible(true);


	}
}
