package ml.utils;

import java.awt.BasicStroke;

import ml.model.function.Function;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.LineRenderer3D;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
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

	/**
	 * Plot the given function from with domain from min to max
	 */
	public FunctionChart(String title, Function function, double min, double max) {
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

	/**
	 * Plot both the fuction and the observations on the same chart panel
	 * <p>
	 * min(domain) and max(domain) are determined by the obsrvations
	 */
	public FunctionChart(String title, Function function, Object[][] observations) {
		super(title);
		this.function = function;
		double min = getMin(observations);
		double max = getMax(observations);

		//the plot for the 2 charts
		XYPlot plot = new XYPlot();
        plot.setDomainAxis(new NumberAxis("X"));
        plot.setRangeAxis(new NumberAxis("Y"));
        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);

		
		//the function chart data
		final XYSeries functionSeries = new XYSeries("Function chart");
		for (int i = 0; i < samples; i++) {
			double x = min + i * (max - min) / (double) samples;
			double y = f(x);
			functionSeries.add(x, y);
		}
		final XYSeriesCollection functionDataSet = new XYSeriesCollection(functionSeries);
		XYLineAndShapeRenderer functionRenderer = new XYLineAndShapeRenderer();
		//functionRenderer.setSeriesStroke(0, new BasicStroke(0.5f));
		functionRenderer.setSeriesShapesVisible(0, false);
		plot.setDataset(functionDataSet);
		plot.setRenderer(functionRenderer);

		//the observations chart data
		final XYSeries observedSeries = new XYSeries("Observed data");
		for (int i = 0; i < observations.length; i++) {
			observedSeries.add((Double)observations[i][0], (Double)observations[i][1]);
		}
		final XYSeriesCollection observedDataSet = new XYSeriesCollection(observedSeries);
		XYItemRenderer observedDataRenderer = new XYLineAndShapeRenderer();
		observedDataRenderer.setSeriesStroke(0, new BasicStroke(0.0f));
		plot.setDataset(2,observedDataSet);
		plot.setRenderer(2,observedDataRenderer);
		
		//the chart
		final JFreeChart chart = new JFreeChart(plot);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
		pack();
		RefineryUtilities.centerFrameOnScreen(this);
		setVisible(true);
	}

	private double getMax(Object[][] observations) {
		double max = Double.NEGATIVE_INFINITY;
		for (Object[] ds : observations) {
			if(max < (Double)ds[0]) {
				max = (Double) ds[0];
			}
		}
		return max;
	}

	private double getMin(Object[][] observations) {
		double min = Double.POSITIVE_INFINITY;
		for (Object[] ds : observations) {
			if(min > (Double) ds[0]) {
				min = (Double) ds[0];
			}
		}
		return min;
	}
}
