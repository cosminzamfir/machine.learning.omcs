package prob.chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Shape;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.ShapeUtilities;

import util.DoubleHolder;
import util.MLUtils;

public class ChartUtils {

	public static void configureSingleSeriesPlot(JFreeChart chart) {
		chart.getTitle().setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14));
		chart.getTitle().setVisible(false);
		XYPlot plot = chart.getXYPlot();
		((NumberAxis) plot.getRangeAxis()).setAutoRangeIncludesZero(false);
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setDomainGridlinePaint(Color.lightGray);
		plot.setRangeGridlinePaint(Color.lightGray);
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);
		Shape diamond = ShapeUtilities.createDiamond(3);
		renderer.setSeriesShape(0, diamond);
		renderer.setSeriesShapesVisible(0, true);
		renderer.setSeriesVisibleInLegend(0, false);
	}

	public static void configureMultipleSeriesPlot(JFreeChart chart) {
		chart.getTitle().setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14));
		chart.getTitle().setVisible(false);
		XYPlot plot = chart.getXYPlot();
		((NumberAxis) plot.getRangeAxis()).setAutoRangeIncludesZero(false);
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
		plot.setBackgroundPaint(Color.BLACK);
		plot.setDomainGridlinePaint(Color.lightGray);
		plot.setRangeGridlinePaint(Color.lightGray);
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);
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
}
