package nl.uu.mal;

import org.jfree.ui.RefineryUtilities;

/**
 * Class representing the composition of the simulation. Contains: initialization, simulation and graphical output of the results.
 *
 * @author h.nunner
 */
public class Simulation {

	/**
	 * Starting point for the simulation.
	 *
	 * @param args
	 * 			ignored
	 */
	public static void main(String[] args) {

		// initialize skating rink and skaters
		SkatingRink skatingRink = SkatingRink.getInstance();
		for (int i = 0; i < Properties.PLAYER_COUNT; i++) {
			Skater skater = new Skater(skatingRink);
			skatingRink.addSkater(skater);
		}
		skatingRink.initPlots();

		// simulate skating rounds
		skatingRink.letThemSkate();

		// plot to graph
		final String title = "Mean rewards per angle";
		final String xLabel = "simulation round";
		final String yLabel = "mean reward over all (" + skatingRink.getSkaters().size() + ") skaters";
		final LineChart chart = new LineChart(title, xLabel, yLabel, skatingRink.getPlots().values());
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
	}

}
