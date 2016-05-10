package nl.uu.mal;

import java.util.ArrayList;
import java.util.List;

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
		List<Skater> skaters = new ArrayList<Skater>();
		for (int i = 0; i < Properties.PLAYER_COUNT; i++) {
			Skater skater = new Skater(skatingRink, skaters);
			skaters.add(skater);
		}
		skatingRink.setSkaters(skaters);
		skatingRink.initPlots(skaters.get(0).getAvailableActions());

		// simulate skating rounds
		skatingRink.letThemSkate();

		// plot to graph
		final String title = "Mean rewards per angle";
		final String xLabel = "simulation round";
		final String yLabel = "mean reward over all (" + skaters.size() + ") skaters";
		final LineChart chart = new LineChart(title, xLabel, yLabel, skatingRink.getPlots().values());
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
	}

}
