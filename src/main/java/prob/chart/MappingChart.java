package prob.chart;

import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * One series represented by a map x->y
 * @author Cosmin Zamfir
 *
 */
public class MappingChart extends ApplicationFrame {

	private static final long serialVersionUID = 1L;

	public MappingChart(Map<Number,Number> map, final String title) {
		super(title);
		final XYSeries series = new XYSeries("Mapping chart");

		map.keySet().forEach((x) -> series.add(x, map.get(x)));
		
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
}
