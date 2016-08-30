package prob.chart;

import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * One series represented by a map x->y
 * @author Cosmin Zamfir
 *
 */
public class ScatterChart extends ApplicationFrame {

	private static final long serialVersionUID = 1L;

	public ScatterChart(List<Double> list,List<Double> list2, String xAxisTitle, String yAxisTitle, final String chartTitle) {
		super(chartTitle);
		final XYSeries series = new XYSeries(yAxisTitle);

		for (int i = 0; i < list.size(); i++) {
			series.add(list.get(i), list2.get(i));
		}

		final XYDataset data = new XYSeriesCollection(series);
		final JFreeChart chart = ChartFactory.createScatterPlot(chartTitle, xAxisTitle, yAxisTitle, data);

		ChartUtils.configureAsScatter(chart, 0);

		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
		pack();
		RefineryUtilities.centerFrameOnScreen(this);
		setVisible(true);

	}
}
