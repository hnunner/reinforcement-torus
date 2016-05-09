package nl.uu.mal;

import java.awt.Color;
import java.util.Iterator;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

/**
 * A simple demonstration application showing how to create a line chart using data from an {@link XYDataset}.
 */
@SuppressWarnings("serial")
public class LineChart extends ApplicationFrame {

    /**
     * Creates a new line chart.
     *
     * @param title
     * 			the frame title.
     */
    public LineChart(final String title, SkatingRink skatingRink) {
        super(title);
        final XYDataset dataset = createDatasetFromSkatingRink(skatingRink);
        final JFreeChart chart = createChart(title, dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1200, 600));
        setContentPane(chartPanel);
    }

    /**
     * Creates a dataset from the list of skating rink.
     *
     * @return the dataset based on the list of skaters.
     */
    private XYDataset createDatasetFromSkatingRink(SkatingRink skatingRink) {
        final XYSeriesCollection dataset = new XYSeriesCollection();
        Iterator<XYSeries> iterator = skatingRink.getPlots().values().iterator();
        while (iterator.hasNext()) {
        	dataset.addSeries(iterator.next());
        }
        return dataset;

    }

    /**
     * Creates a chart.
     *
     * @param dataset
     * 			the data for the chart.
     *
     * @return a chart.
     */
    private JFreeChart createChart(String title, final XYDataset dataset) {

        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
            title,      				// chart title
            "X",                      	// x axis label
            "Y",                      	// y axis label
            dataset,                  	// data
            PlotOrientation.VERTICAL,
            true,                     	// include legend
            true,                     	// tooltips
            false                     	// urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);


        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(1, false);
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;
    }

}