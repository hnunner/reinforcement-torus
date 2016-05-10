package nl.uu.mal;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Class representing a skater moving around the skating rink. It contains information about the position and the logic for
 * triggering movements on the {@link SkatingRink} based on the available actions and the implemented learning algorithm.
 *
 * @author h.nunner
 */
public class Skater {

	// simple tool for logging purposes
	private static final Logger LOG = Logger.getLogger(Skater.class.getName());

	// parameters
	private int simRound;
	private SkatingRink skatingRink;
	private Position position;
	private List<Action> availableActions;


	/**
	 * Constructor - creates a new skater within the {@link SkatingRink}.
	 *
	 * @param skatingRink
	 * 			the skating rink the player is located in
	 */
	public Skater(SkatingRink skatingRink) {
		this.simRound = 0;
		this.skatingRink = skatingRink;
		initPosition(skatingRink);
		this.availableActions = Action.createAvailableActions();
	}

	/**
	 * Initializes the starting position of the skater within a given {@link SkatingRink}.
	 *
	 * @param skatingRink
	 * 			the skating rink to position the skater in
	 * @return the initial position of the skater within the skating rink
	 */
	private void initPosition(SkatingRink skatingRink) {
		// requirements
		int width = skatingRink.getWidth();
		int height = skatingRink.getHeight();
		List<Skater> otherSkaters = skatingRink.getOtherSkaters(this);

		// try a random position first
		Random rand = new Random();
		Position initialPosition = new Position(Double.valueOf(rand.nextInt(width)), Double.valueOf(rand.nextInt(height)));
		// try random positions, if / while colliding with other skater
		while (isColliding(initialPosition, otherSkaters)) {
			initialPosition.setX(Double.valueOf(rand.nextInt(width)));
			initialPosition.setY(Double.valueOf(rand.nextInt(height)));
		}
		this.position = initialPosition;
	}


	/**
	 * Performs a single movement of the skater to the next position. The movement is based on the action chosen
	 * by the implemented learning algorithm, the actual position of the skater and the implementation of the
	 * {@link SkatingRink} the skater is located in.
	 */
	public void move() {
		// requirements/initializations
		this.simRound++;
		Action prospectiveAction = null;
		Position prospectivePosition = null;
		boolean foundNextPosition = false;
		List<Skater> otherSkaters = this.skatingRink.getOtherSkaters(this);
		// order actions, so that the list is either in order of payoffs (exploitation) or in random order (exploration)
		List<Action> orderedActions = orderActions(this.availableActions, this.simRound);

		// iterate over ordered list of actions
		Iterator<Action> actionsIt = orderedActions.iterator();
		while (actionsIt.hasNext() && !foundNextPosition) {
			prospectiveAction = actionsIt.next();

			// determine prospective non-collisional position based on current position, action and skate rink
			prospectivePosition = skatingRink.getNewPosition(position, prospectiveAction);
			foundNextPosition = !isColliding(prospectivePosition, otherSkaters);

			// give low reward in case action lead to collision
			if (!foundNextPosition) {
				prospectiveAction.giveLowReward();;
			}
		}

		// skip turn, if unable to determine next position
		if (!foundNextPosition || prospectiveAction == null || prospectivePosition == null) {
			LOG.info("Could not find an appropriate next position. Skipping current turn.");
			return;
		}

		// set new position
		this.position = prospectivePosition;

		// set new payoffs
		prospectiveAction.giveHighReward();
		actionsIt = orderedActions.iterator();
		while (actionsIt.hasNext()) {
			Action action = actionsIt.next();
			action.updateMeanPayoff(this.simRound);
		}
	}

	/**
	 * Orders a list of actions strategically for a defined ratio of exploration and exploitation.
	 *
	 * @return the ordered list of actions
	 */
	public List<Action> orderActions(List<Action> actions, int simRound) {
		Random rand = new Random();

		// in the beginning or in 1-epsilon percent of the cases: play a random action (explore)
		if (simRound <= 1 || rand.nextDouble() > Properties.EPSILON_GREEDY) {
			Collections.shuffle(actions);
		}

		// in epsilon percent of the case choose action ordered by payoffs (exploit)
		else {
			Collections.sort(actions);
		}

		return actions;
	}

	/**
	 * Check if the player is colliding with another player. This is defined by a collision radius / threshold
	 * of the distance between two skaters.
	 *
	 * @param position
	 * 			position of the skater
	 * @param skaters
	 * 			skaters to compare with
	 * @return true if the position of the skater lies within the collision radius of another skater, false otherwise
	 */
	private boolean isColliding(Position position, List<Skater> skaters) {
		boolean collision = false;

		// test for all skaters, if the position of the two skater lie within the collision radius
		Iterator<Skater> skatersIt = skaters.iterator();
		while (skatersIt.hasNext() && !collision) {
			Skater skater = skatersIt.next();
			// calculation based on Pythagoras' theorem
			collision = (Math.sqrt(Math.pow(position.getX() - skater.getPosition().getX(), 2)
					+ Math.pow(position.getY() - skater.getPosition().getY(), 2)))
					< Properties.COLLISION_RADIUS;
		}
		return collision;
	}

	/**
	 * Gets the cumulated payoff of the skater for a given angle.
	 *
	 * @param angle
	 * 			the angle of the cumulated payoff
	 * @return the cumulated payoff of the skater for the given angle
	 */
	public int getCumulatedPayoffForAngle(int angle) {
		Iterator<Action> actionsIt = this.availableActions.iterator();
		while (actionsIt.hasNext()) {
			Action action = actionsIt.next();
			if (action.getAngle() == angle) {
				return action.getCumulatedPayoff();
			}
		}
		return 0;
	}

	/**
	 * Gets the meam payoff of the skater for a given angle.
	 *
	 * @param angle
	 * 			the angle of the mean payoff
	 * @return the mean payoff of the skater for the given angle
	 */
	public double getMeanPayoffForAngle(int angle) {
		Iterator<Action> actionsIt = this.availableActions.iterator();
		while (actionsIt.hasNext()) {
			Action action = actionsIt.next();
			if (action.getAngle() == angle) {
				return action.getMeanPayoff();
			}
		}
		return 0;
	}

	/**
	 * @return the position of the skater
	 */
	public Position getPosition() {
		return position;
	}

}
