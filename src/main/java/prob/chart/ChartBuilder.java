package prob.chart;

import java.awt.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ml.model.DataSet;
import ml.model.Observation;
import ml.model.function.Function;
import ml.stat.StatUtils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import prob.distribution.ExponentialDistribution;
import prob.distribution.NormalDistribution;
import prob.distribution.RayleighDistribution;
import util.IntHolder;
import util.MLUtils;

import static java.awt.Color.*;

public class ChartBuilder {

	List<Color> allColors = Arrays.asList(BLUE, MAGENTA, GREEN, BLACK, RED, YELLOW);
	String title;
	String xAxisTitle;
	String yAxisTitle;
	private List<XYSeries> dataSets = new ArrayList<XYSeries>();
	private List<PlotType> plotTypes = new ArrayList<PlotType>();
	private List<Color> colors = new ArrayList<>();

	public ChartBuilder() {
		this("Chart", "x", "y");
	}
	
	public ChartBuilder(String title) {
		this(title, "x", "y");
	}


	public ChartBuilder(String title, String xAxisTitle, String yAxisTitle) {
		super();
		this.title = title;
		this.xAxisTitle = xAxisTitle;
		this.yAxisTitle = yAxisTitle;
	}

	public ChartBuilder add(Map<Double, Double> values, String seriesName, PlotType plotType) {
		final XYSeries series = new XYSeries(seriesName);
		values.keySet().forEach((x) -> series.add(x, values.get(x)));
		dataSets.add(series);
		plotTypes.add(plotType);
		return this;
	}

	public ChartBuilder add(List<Double> xValues, List<Double> yValues, String seriesName, PlotType plotType) {
		final XYSeries series = new XYSeries(seriesName);
		IntHolder yIndex = new IntHolder(0);
		xValues.forEach((x) -> series.add(x, yValues.get(yIndex.getAndAdd(1))));
		dataSets.add(series);
		plotTypes.add(plotType);
		return this;
	}

	/**
	 * Create a qqplot, set1 on the X axis, set2 on the Y axis
	 * https://en.wikipedia.org/wiki/Q%E2%80%93Q_plot
	 */
	public ChartBuilder addQuantileQuantilePlot(List<Double> set1, List<Double> set2, int quantilesNum, String title) {
		List<Double> quantileValues1 = StatUtils.quantiles(set1, quantilesNum);
		List<Double> quantileValues2 = StatUtils.quantiles(set2, quantilesNum);
		add(quantileValues1, quantileValues2, title, PlotType.Scatter);
		return this;
	}

	public ChartBuilder add(Function f, double from, double to, int samples, String seriesName, PlotType plotType) {
		return add(MLUtils.sampleToMap(f, from, to, samples), seriesName, plotType);
	}
	
	/** Create a point chart, using 2 features of the data set specified by xInd and yInd, colored based on the classification */
	public ChartBuilder add(DataSet ds, int xInd, int yInd ) {
		Map<Double, List<Observation>> mapping = ds.getCategoriesMapping();
		int index = 0;
		for (Double category : mapping.keySet()) {
			final XYSeries series = new XYSeries(String.valueOf(category));
			mapping.get(category).forEach((obs) -> series.add(obs.getValues()[xInd], obs.getValues()[yInd]));
			dataSets.add(series);
			plotTypes.add(PlotType.Scatter);
			colors.add(allColors.get(index++));
		}
		return this;
	}


	public void build() {
		final XYSeriesCollection data = new XYSeriesCollection();
		dataSets.forEach((xySeries) -> data.addSeries(xySeries));
		final JFreeChart chart = ChartFactory.createXYLineChart(title, xAxisTitle, yAxisTitle, data, PlotOrientation.VERTICAL, true, true, false);
		for (int i = 0; i < plotTypes.size(); i++) {
			PlotType plotType = plotTypes.get(i);
			if (plotType == PlotType.Line) {
				ChartUtils.configureAsLine(chart, i);
			}
			if (plotType == PlotType.LineWithShape) {
				ChartUtils.configureAsLineShape(chart, i);
			}
			if (plotType == PlotType.Scatter) {
				ChartUtils.configureAsScatter(chart, i);
			}
			if(!colors.isEmpty()) {
				ChartUtils.setColor(chart, i, colors.get(i));
			}
		}

		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		ApplicationFrame frame = new ApplicationFrame("Chart");
		frame.setContentPane(chartPanel);
		frame.pack();
		RefineryUtilities.centerFrameOnScreen(frame);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		List<Double> set1 = MLUtils.sample(new NormalDistribution(0, 1), 1000);
		List<Double> set2 = MLUtils.sample(new NormalDistribution(0, 1), 1000);
		List<Double> set3 = MLUtils.sample(new RayleighDistribution(new NormalDistribution(0, 1), new NormalDistribution(0, 1)), 1000);
		List<Double> set4 = MLUtils.sample(new ExponentialDistribution(1), 1000);

		new ChartBuilder("Q-Q", "set1", "set2").addQuantileQuantilePlot(set1, set2, 100, "Normal-Normal").
			addQuantileQuantilePlot(set1, set3, 1000, "Normal-Rayliegh").
			addQuantileQuantilePlot(set1, set4, 1000, "Normal-Exponential").
			addQuantileQuantilePlot(set3, set4, 1000, "Rayleight-Exponential").
			build();
	}
}
