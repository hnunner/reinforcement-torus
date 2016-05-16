package nl.uu.mal;

import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

/**
 * Class representing a simple generic line chart.
 *
 * @author h.nunner
 */
@SuppressWarnings("serial")
public class LineChart extends ApplicationFrame {

    /**
     * Creates a new line chart.
     *
     * @param title
     * 			the title
     * @param xLabel
     * 			the label for the x axis
     * @param yLabel
     * 			the label for the y axis
     * @param plots
     * 			a list of plots for display
     */
    public LineChart(final String title, final String xLabel, final String yLabel, Collection<XYSeries> plots) {
        super(title);
        final XYDataset dataset = createDatasetFromSkatingRink(plots);
        final JFreeChart chart = createChart(title, xLabel, yLabel, dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1200, 600));
        setContentPane(chartPanel);
    }

    /**
     * Creates a dataset from a list of plots.
     *
     * @param plots
     * 			the plots for display
     * @return the dataset based on the list of plots
     */
    private XYDataset createDatasetFromSkatingRink(Collection<XYSeries> plots) {
        final XYSeriesCollection dataset = new XYSeriesCollection();
        Iterator<XYSeries> iterator = plots.iterator();
        while (iterator.hasNext()) {
        	dataset.addSeries(iterator.next());
        }
        return dataset;
    }

    /**
     * Creates a formatted chart.
     *
     * @param title
     * 			the title
     * @param xLabel
     * 			the label for the x axis
     * @param yLabel
     * 			the label for the y axis
     * @param dataset
     * 			the dataset containing the plot data
     * @return the formatted chart
     */
    private JFreeChart createChart(String title, String xLabel, String yLabel, final XYDataset dataset) {

        final JFreeChart chart = ChartFactory.createXYLineChart(title, xLabel, yLabel,
        		dataset, PlotOrientation.VERTICAL, true, true, false);

        chart.setBackgroundPaint(Color.lightGray);
        chart.getXYPlot().setBackgroundPaint(Color.white);
        chart.getXYPlot().setDomainGridlinePaint(Color.gray);
        chart.getXYPlot().setRangeGridlinePaint(Color.gray);
        chart.getXYPlot().setRenderer(new XYLineAndShapeRenderer(true, false));
//        chart.getXYPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        return chart;
    }

}