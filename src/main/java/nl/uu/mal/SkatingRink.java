package nl.uu.mal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.jfree.data.xy.XYSeries;

public class SkatingRink {

	public static final Logger LOG = Logger.getLogger(SkatingRink.class.getName());

	private List<Skater> skaters;
	private int width;
	private int height;
	private Map<Integer, XYSeries> plots;


	/**
	 * Eagerly initialized singleton.
	 */
	// constant
	private static final SkatingRink instance = new SkatingRink(Properties.TORUS_WIDTH, Properties.TORUS_HEIGHT);
	// private constructor
	private SkatingRink(int width, int height) {
		this.width = width;
		this.height = height;
	}
	// getter
	public static SkatingRink getInstance() {
		return instance;
	}


	/**
	 * Initialization of the plots. These are based on the different angles of the actions.
	 *
	 * @param actions
	 * 			the actions the plots are based on
	 */
	public void initPlots(List<Action> actions) {
		this.plots = new HashMap<Integer, XYSeries>();

		Iterator<Action> actionsIt = actions.iterator();
		while (actionsIt.hasNext()) {
			Action action = actionsIt.next();
			this.plots.put(action.getAngle(), new XYSeries(String.valueOf(action.getAngle())));
		}
	}

	/**
	 * Simulates movement of skaters for a default number of rounds.
	 */
	public void letThemSkate() {
		this.letThemSkate(Properties.DEFAULT_ROUNDS);
	}

	/**
	 * Simulates movement of skaters for a definable number of rounds.
	 *
	 * @param rounds
	 * 			number of round to be simulated
	 */
	public void letThemSkate(int rounds) {
		for (int i = 0; i < rounds; i++) {
			Iterator<Skater> skatersIt = skaters.iterator();


/*
			StringBuilder sb = new StringBuilder();
			sb.append("\n\nRound " + i + ": \n");
			int j = 1;

*/
			while (skatersIt.hasNext()) {
				Skater skater = skatersIt.next();
				skater.move();


/*
				sb.append("\tSkater" + j++ + ": \n");
				Iterator<Action> actionIt = skater.getAvailableActions().iterator();
				while (actionIt.hasNext()) {
					Action action = actionIt.next();
					sb.append("\t\tAngle: " + action.getAngle() + "\tCumulated Payoff: " + action.getCumulatedPayoff()
							+ "\tMean Payoff: " + action.getMeanPayoff() + "\n");
				}
*/

			}

//			LOG.info(sb.toString());




			// recalculating mean payoff for every action
			Set<Integer> angles = plots.keySet();
			Iterator<Integer> anglesIt = angles.iterator();
			while (anglesIt.hasNext()) {
				Integer angle = anglesIt.next();
				XYSeries xySeries = plots.get(angle);

				skatersIt = skaters.iterator();
				double meanPayoffPerAngle = 0;
				while (skatersIt.hasNext()) {
					Skater skater = skatersIt.next();
					meanPayoffPerAngle += skater.getMeanPayoff(angle);
				}
				meanPayoffPerAngle = Double.valueOf(meanPayoffPerAngle) / Double.valueOf(Properties.PLAYER_COUNT);
				xySeries.add(i, meanPayoffPerAngle);
 			}


		}
	}


	public Position getNextPosition(Position position, Action action) {
		StringBuilder sb = new StringBuilder();
		sb.append("\taction: " + action.getAngle() + ", " + action.getDistance() + "\n");
		// new x-coordinate
		double newX = (Math.cos(Math.toRadians(action.getAngle())) * action.getDistance()) + position.getX();
		if (newX >= this.width) {
			newX = newX - this.width;
		}
		if (newX < 0) {
			newX = newX + this.width;
		}
		sb.append("\t\told x: " + position.getX() + " --> new x: " + newX);

		// new y-coordinate
		double newY = Math.sin(Math.toRadians(action.getAngle())) * action.getDistance() + position.getY();
		if (newY >= this.height) {
			newY = newY - this.height;
		}
		if (newY < 0) {
			newY = newY + this.height;
		}
		sb.append("\t\told y: " + position.getY() + " --> new y: " + newY);
//		LOG.info(sb.toString());

		return new Position(newX, newY);
	}

	public List<Skater> getOtherSkaters(Skater skater) {
		List<Skater> otherSkaters = new ArrayList<Skater>(skaters);
		otherSkaters.remove(skater);
		return otherSkaters;
	}


	/**
	 * @return the skaters
	 */
	public List<Skater> getSkaters() {
		return skaters;
	}

	/**
	 * @param skaters the skaters to set
	 */
	public void setSkaters(List<Skater> skaters) {
		this.skaters = skaters;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return the plots
	 */
	public Map<Integer, XYSeries> getPlots() {
		return plots;
	}

}
