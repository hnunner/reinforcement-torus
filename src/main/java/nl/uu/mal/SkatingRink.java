package nl.uu.mal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.jfree.data.xy.XYSeries;


/**
 * Class representing a skating rink in which {@link Skater} can move around. The skating rink defines the shape and dimensions
 * of the surface. In this case it represents a torus that is created from a rectangular surface (for more details, see:
 * https://commons.wikimedia.org/wiki/File:Torus_from_rectangle.gif). For a more generic approach this class can be easily
 * transformed into an abstract class with implementations representing different shapes. In this case only the function
 * "getNewPosition" must be implemented according to the various shapes. This has been left out, due to simplicity, readability
 * and the fact that the assignment focuses only on this simplified version of a torus.
 *
 * @author h.nunner
 */
public class SkatingRink {

	// tool for logging purposes
	public static final Logger LOG = Logger.getLogger(SkatingRink.class.getName());

	// parameters
	private int width;
	private int height;
	private List<Skater> skaters;

	// logging of payoffs
	// TODO: extract to external payoff logger class
	private Map<Integer, XYSeries> payoffsPerAngle;			// key: different angles of the actions,
															// value: mean payoffs for all skaters over time
	private StringBuilder payoffsPerSkater;					// quick and easy way to generate csv-file for payoffs
															// per skater over time

	/**
	 * Eagerly initialized singleton.
	 */
	// constant
	private static final SkatingRink instance = new SkatingRink(Properties.TORUS_WIDTH, Properties.TORUS_HEIGHT);
	// private constructor
	private SkatingRink(int width, int height) {
		this.width = width;
		this.height = height;
		this.skaters = new ArrayList<Skater>();
		this.initPayoffStorages();
	}
	// getter
	public static SkatingRink getInstance() {
		return instance;
	}


	/**
	 * Initialization of the payoff storages, based on the different angles of actions.
	 */
	private void initPayoffStorages() {
		this.payoffsPerAngle = new HashMap<Integer, XYSeries>();
		Iterator<Action> actionsIt = Action.createAvailableActions().iterator();
		while (actionsIt.hasNext()) {
			int angle = actionsIt.next().getAngle();
			this.payoffsPerAngle.put(angle, new XYSeries(String.valueOf(angle + "°")));
		}
		payoffsPerSkater = new StringBuilder();
	}

	/**
	 * Simulates movement of skaters for a default number of rounds.
	 */
	public void letThemSkate() {
		this.letThemSkate(Properties.DEFAULT_ROUNDS);
	}

	/**
	 * Simulates movement of skaters for an arbitrary number of rounds.
	 *
	 * @param rounds
	 * 			number of rounds to be simulated
	 */
	public void letThemSkate(int rounds) {
		// iteration over number of rounds
		for (int simRound = 1; simRound <= rounds; simRound++) {
			// asynchronous simulation of movement for each skater
			Iterator<Skater> skatersIt = skaters.iterator();
			int skaterIndex = 0;
			while (skatersIt.hasNext()) {
				Skater skater = skatersIt.next();
				skater.move(simRound);
				updatePayoffsPerSkater(simRound, skaterIndex++, skater);
			}
			updateMeanPayoffsPerAngle(simRound);
		}
	}
	/**
	 * Appending payoffs per skater.
	 *
	 * @param simRound
	 * 			the simulation round
	 * @param skaterIndex
	 * 			the skater index
	 * @param skater
	 * 			the skater, including the payoffs per action
	 */
	private void updatePayoffsPerSkater(int simRound, int skaterIndex, Skater skater) {
		// append simulation round and skater
		payoffsPerSkater.append(simRound).append(",").append(skaterIndex).append(",");

		// sort by angle
		// TODO: extract to Action and generalize sorting with different {@link Comparator}s
		List<Action> availableActions = skater.getAvailableActions();
		Collections.sort(availableActions, new Comparator<Action>() {
			public int compare(Action o1, Action o2) {
				if (o1.getAngle() <= o2.getAngle()) {
					return -1;
				} else {
					return 1;
				}
			}
		});

		// append payoffs for different angles
		Iterator<Action> actionsIt = availableActions.iterator();
		while (actionsIt.hasNext()) {
			Action action = actionsIt.next();
			payoffsPerSkater.append(action.getCumulatedPayoff());
			if (actionsIt.hasNext()) {
				payoffsPerSkater.append(",");
			} else {
				payoffsPerSkater.append("\n");
			}
		}
	}

	/**
	 * Updates the mean payoffs for all skaters and all angles for the given simulation round.
	 *
	 * @param simRound
	 * 			the simulation round
	 */
	private void updateMeanPayoffsPerAngle(int simRound) {
		Iterator<Skater> skatersIt;
		Iterator<Entry<Integer, XYSeries>> payoffsIt = payoffsPerAngle.entrySet().iterator();

		// iterate through payoffs per angle
		while (payoffsIt.hasNext()) {
			Entry<Integer, XYSeries> payoff = payoffsIt.next();

			double meanPayoffPerAngle = 0;

			// iterate through all skaters and sum up mean payoffs
			skatersIt = skaters.iterator();
			while (skatersIt.hasNext()) {
				Skater skater = skatersIt.next();
				meanPayoffPerAngle += skater.getMeanPayoffForAngle(payoff.getKey());
			}

			// divide summed up mean payoffs by number of skaters
			meanPayoffPerAngle = Double.valueOf(meanPayoffPerAngle) / Double.valueOf(Properties.PLAYER_COUNT);
			// add mean payoff over all skaters and specific angle for the current simulation round
			payoff.getValue().add(simRound, meanPayoffPerAngle);
		}
	}


	/**
	 * Calculates new position within the skating rink, based on a current position, the angle of movement and the distance.
	 * The calculation is based on the shape and layout of the skating rink. In this case it is a simple torus constructed
	 * out of a rectangular, two-dimensional shape.
	 *
	 * @param currentPosition
	 * 			the skater's current position
	 * @param angle
	 * 			the angle of movement
	 * @param distance
	 * 			the distance of movement
	 * @return the new position of the skater
	 */
	public Position getNewPosition(Position currentPosition, int angle, double distance) {
		double radAngle = Math.toRadians(angle);

		// new x-coordinate
		double newX = (Math.cos(radAngle) * distance) + currentPosition.getX();
		// wrap around, if skater skates out of bounds
		if (newX >= this.width) {
			newX = newX - this.width;
		}
		if (newX < 0) {
			newX = newX + this.width;
		}

		// new y-coordinate
		double newY = Math.sin(radAngle) * distance + currentPosition.getY();
		// wrap around, if skater skates out of bounds
		if (newY >= this.height) {
			newY = newY - this.height;
		}
		if (newY < 0) {
			newY = newY + this.height;
		}

		return new Position(newX, newY);
	}

	/**
	 * Gets all skaters except the one handed in as parameter.
	 *
	 * @param skater
	 * 			skater being excluded
	 * @return list of all skaters, excluding the skater handed in as parameter
	 */
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
	 * Adds a skater to the list of all skaters.
	 *
	 * @param skater
	 * 			the skater to add
	 */
	public void addSkater(Skater skater) {
		this.skaters.add(skater);
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
	 * @return the payoffsPerAngle
	 */
	public Map<Integer, XYSeries> getPayoffsPerAngle() {
		return payoffsPerAngle;
	}

	/**
	 * @return the payoffsPerSkater
	 */
	public StringBuilder getPayoffsPerSkater() {
		return payoffsPerSkater;
	}

}
