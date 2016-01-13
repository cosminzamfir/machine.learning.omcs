package ml.utils;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple demonstration application showing how to create a bar chart overlaid
 * with a line chart.
 */
public class OverlaidBarChartDemo extends ApplicationFrame {

    /**
     * Default constructor.
     *
     * @param title  the frame title.
     */
    public OverlaidBarChartDemo(final String title) {

        super(title);
        final CategoryPlot plot = new CategoryPlot();
        plot.setDomainAxis(new CategoryAxis("Category"));
        plot.setRangeAxis(new NumberAxis("Value"));
        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);

        // now create the second dataset and renderer...
        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        dataset2.addValue(9.0, "T1", "Category 1");
        dataset2.addValue(7.0, "T1", "Category 2");
        dataset2.addValue(2.0, "T1", "Category 3");
        dataset2.addValue(6.0, "T1", "Category 4");
        dataset2.addValue(6.0, "T1", "Category 5");
        dataset2.addValue(9.0, "T1", "Category 6");
        dataset2.addValue(5.0, "T1", "Category 7");
        dataset2.addValue(4.0, "T1", "Category 8");

        final CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
        plot.setDataset(dataset2);
        plot.setRenderer(renderer2);

        // create the third dataset and renderer...
        final ValueAxis rangeAxis2 = new NumberAxis("Axis 2");
        plot.setRangeAxis(1, rangeAxis2);

        DefaultCategoryDataset dataset3 = new DefaultCategoryDataset();
        dataset3.addValue(94.0, "R1", "Category 1");
        dataset3.addValue(75.0, "R1", "Category 2");
        dataset3.addValue(22.0, "R1", "Category 3");
        dataset3.addValue(74.0, "R1", "Category 4");
        dataset3.addValue(83.0, "R1", "Category 5");
        dataset3.addValue(9.0, "R1", "Category 6");
        dataset3.addValue(23.0, "R1", "Category 7");
        dataset3.addValue(98.0, "R1", "Category 8");

        plot.setDataset(1, dataset3);
        final CategoryItemRenderer renderer3 = new LineAndShapeRenderer();
        plot.setRenderer(1, renderer3);
        plot.mapDatasetToRangeAxis(2, 1);

        // change the rendering order so the primary dataset appears "behind" the 
        // other datasets...
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        final JFreeChart chart = new JFreeChart(plot);
        chart.setTitle("Overlaid Bar Chart");
      //  chart.setLegend(new StandardLegend());

        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

    }

    // ****************************************************************************
    // * JFREECHART DEVELOPER GUIDE                                               *
    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
    // * to purchase from Object Refinery Limited:                                *
    // *                                                                          *
    // * http://www.object-refinery.com/jfreechart/guide.html                     *
    // *                                                                          *
    // * Sales are used to provide funding for the JFreeChart project - please    * 
    // * support us so that we can continue developing free software.             *
    // ****************************************************************************
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(final String[] args) {

        final OverlaidBarChartDemo demo = new OverlaidBarChartDemo("Overlaid Bar Chart Demo");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}