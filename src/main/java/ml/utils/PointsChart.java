package ml.utils;

import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class PointsChart extends ApplicationFrame {

	
	private static final long serialVersionUID = 1L;

	public PointsChart(String title, List<double[][]> l) {
		super("Title");
		final XYSeriesCollection data = new XYSeriesCollection();
		int index = 1;
		for (double[][] array : l) {
			final XYSeries series = new XYSeries("Points " + index++ + "chart");
			for (int i = 0; i < array.length; i++) {
				series.add(array[i][0], array[i][1]);
			}
			data.addSeries(series);
		}
		final JFreeChart chart = ChartFactory.createScatterPlot(title, "X", "Y", data);

		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
		pack();
		RefineryUtilities.centerFrameOnScreen(this);
		setVisible(true);
	}
}
