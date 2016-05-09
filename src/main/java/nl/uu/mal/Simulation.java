package nl.uu.mal;

import java.util.ArrayList;
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
		List<Skater> skaters = new ArrayList<Skater>();
		for (int i = 0; i < Properties.PLAYER_COUNT; i++) {
			Skater skater = new Skater("Skater " + String.valueOf(i+1), skatingRink, skaters);
			skaters.add(skater);
		}
		skatingRink.setSkaters(skaters);
		skatingRink.initPlots(skaters.get(0).getAvailableActions());

		// simulate skating rounds
		skatingRink.letThemSkate();

		// plot to graph
		final LineChart chart = new LineChart("Mean rewards per angle", skatingRink);
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
	}

}
