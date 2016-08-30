package prob.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ml.model.function.Function;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import util.IntHolder;
import util.MLUtils;

public class ChartBuilder {

	String title;
	String xAxisTitle;
	String yAxisTitle;
	private List<XYSeries> dataSets = new ArrayList<XYSeries>();
	private List<PlotType> plotTypes = new ArrayList<PlotType>();

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

	public ChartBuilder add(Function f, double from, double to, int samples, String seriesName, PlotType plotType) {
		return add(MLUtils.sampleToMap(f, from, to, samples), seriesName, plotType);
	}

	public void build() {
		final XYSeriesCollection data = new XYSeriesCollection();
		dataSets.forEach((xySeries) -> data.addSeries(xySeries));
		final JFreeChart chart = ChartFactory.createXYLineChart(title, xAxisTitle, yAxisTitle, data, PlotOrientation.VERTICAL, true, true, false);
		for (int i = 0; i < plotTypes.size(); i++) {
			PlotType plotType = plotTypes.get(i);
			if(plotType == PlotType.Line) {
				ChartUtils.configureAsLine(chart, i);
			}
			if(plotType == PlotType.LineWithShape) {
				ChartUtils.configureAsLineShape(chart, i);
			}
			if(plotType == PlotType.Scatter) {
				ChartUtils.configureAsScatter(chart, i);
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
}
