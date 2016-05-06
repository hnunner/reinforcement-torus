package nl.uu.mal;

import java.util.LinkedList;
import java.util.List;

import org.jfree.ui.RefineryUtilities;

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
		List<Skater> skaters = new LinkedList<Skater>();
		for (int i = 0; i < Properties.PLAYER_COUNT; i++) {
			skaters.add(new Skater("Skater " + String.valueOf(i+1)));
		}
		skatingRink.setSkaters(skaters);

		// simulate skating rounds
		skatingRink.letThemSkate();

		// plot to graph
		final LineChart chart = new LineChart("Skaters", skaters);
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
	}

}
