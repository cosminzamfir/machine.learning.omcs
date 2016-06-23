package prob.chart;

import util.MLUtils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class DataChart extends ApplicationFrame {

	
	private static final long serialVersionUID = 1L;

	public DataChart(String title, double[][] array) {
		super(title);
		final XYSeries series = new XYSeries("Function chart");
		
		for (int i = 0; i < array.length; i++) {
			series.add(array[i][0], array[i][1]);
		}
		final XYSeriesCollection data = new XYSeriesCollection(series);
		final JFreeChart chart = ChartFactory.createXYLineChart(title, "X", "Y", data, PlotOrientation.VERTICAL,
				true, true, false
				);

		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
		pack();
		RefineryUtilities.centerFrameOnScreen(this);
		setVisible(true);
	}

	public static void main(final String[] args) {
		new DataChart("Random", MLUtils.randomDiscreteDistribution(1000));
	}

}
