package prob.chart;

import java.awt.Color;
import java.awt.Shape;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.ShapeUtilities;

public class ChartUtils {

	public static void configureChartDefaults(JFreeChart chart) {
		chart.getTitle().setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14));
		chart.getTitle().setVisible(false);
		XYPlot plot = chart.getXYPlot();
		((NumberAxis) plot.getRangeAxis()).setAutoRangeIncludesZero(false);
		plot.setBackgroundPaint(Color.WHITE);
		plot.setDomainGridlinePaint(Color.lightGray);
		plot.setRangeGridlinePaint(Color.lightGray);
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);
		
		LegendTitle legend = chart.getLegend();
		legend.setPosition(RectangleEdge.RIGHT);
		legend.setFrame(BlockBorder.NONE);
		legend.setBackgroundPaint(Color.lightGray);

	}
	
	public static void configureAsLineShape(JFreeChart chart, int index) {
		XYPlot plot = chart.getXYPlot();
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
		Shape diamond = ShapeUtilities.createDiamond(3);
		renderer.setSeriesShape(index, diamond);
		renderer.setSeriesShapesVisible(index, true);
		renderer.setSeriesVisibleInLegend(index, true);
	}

	public static void configureAsLine(JFreeChart chart, int index) {
		XYPlot plot = chart.getXYPlot();
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
		renderer.setSeriesShapesVisible(index, false);
		renderer.setSeriesVisibleInLegend(index, true);
	}

	public static void configureAsScatter(JFreeChart chart, int index) {
		XYPlot plot = chart.getXYPlot();
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
		renderer.setSeriesShapesVisible(index, true);
		renderer.setSeriesLinesVisible(index, false);
		renderer.setSeriesShape(index, ShapeUtilities.createDiamond(2));
		renderer.setSeriesVisibleInLegend(index, true);
	}
	
	public static void configureMultipleSeriesPlot(JFreeChart chart) {
		XYPlot plot = chart.getXYPlot();
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
		Shape diamond = ShapeUtilities.createDiamond(3);
		for (int i = 0; i < plot.getSeriesCount(); i++) {
			renderer.setSeriesShape(i, diamond);
			renderer.setSeriesShapesVisible(i, true);
			renderer.setSeriesVisibleInLegend(i, true);
		}
		LegendTitle legend = chart.getLegend();
		legend.setPosition(RectangleEdge.RIGHT);
		legend.setFrame(BlockBorder.NONE);
		legend.setBackgroundPaint(Color.lightGray);
	}

	public static void setColor(JFreeChart chart, int index, Color color) {
		XYPlot plot = chart.getXYPlot();
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
		renderer.setSeriesShapesVisible(index, true);
		renderer.setSeriesFillPaint(index, color);
	}

}
