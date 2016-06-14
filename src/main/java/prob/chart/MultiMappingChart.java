package prob.chart;

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class MultiMappingChart extends ApplicationFrame {

	private static final long serialVersionUID = 1L;

	public MultiMappingChart(List<Map<Number,Number>> maps, List<String> legends, String xAxis, String yAxis, String title) {
		super(title);
		final XYSeriesCollection data = new XYSeriesCollection();

		for (int i = 0; i < maps.size(); i++) {
			final XYSeries series = new XYSeries(legends.get(i));
			Map<Number,Number> map = maps.get(i);
			map.keySet().forEach((x) -> series.add(x, map.get(x)));
			data.addSeries(series);
		}
		
		final JFreeChart chart = ChartFactory.createXYLineChart(title, xAxis, yAxis, data, PlotOrientation.VERTICAL,
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
