package nl.uu.mal;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Class representing a skater moving around in a {@link SkatingRink}. It contains information about the position and the
 * logic for triggering movements on the {@link SkatingRink} based on the available actions and the implemented learning
 * algorithm. The learning algorithm is implemented within the class and is based on epsilon-greedy reinforcement learning.
 * For a more generic implementation, this could be easily extracted either in a subclass of the Skater class, or better as
 * a generic behavior implemented as strategy pattern. For simplicity, readability and because generic behavior is not
 * required in the assignment a generalization of the approach has been left out.
 *
 * @author h.nunner
 */
public class Skater {

	// tool for logging purposes
	@SuppressWarnings("unused")
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
		this.simRound++;

		// requirements/initializations
		Action prospectiveAction = chooseAction(this.availableActions);
		int prospectiveAngle = prospectiveAction.getAngle();
		int prospectiveDistance = prospectiveAction.getDistance();

		// determine prospective non-collisional position along the wole way of movement, based on
		// current position, the prospective angle, the prospective distance and the skating rink
		Position prospectivePosition = null;
		boolean isColliding = false;
		List<Skater> otherSkaters = this.skatingRink.getOtherSkaters(this);

		double stepWidth = Properties.DISTANCE_INCREMENT;
		while (stepWidth <= prospectiveDistance && !isColliding) {
			prospectivePosition = skatingRink.getNewPosition(position, prospectiveAngle, stepWidth);
			isColliding = isColliding(prospectivePosition, otherSkaters);
			stepWidth += Properties.DISTANCE_INCREMENT;
		}

		// in case of collision: give low reward
		if (isColliding) {
			prospectiveAction.giveLowReward();
		}
		// in case of no collision: update position and give high reward
		else {
			this.position = prospectivePosition;
			prospectiveAction.giveHighReward();
		}

		updateMeanPayoffs();
	}

	/**
	 * Chooses an action based on a list of available actions for a pre-defined ratio of exploration and exploitation.
	 *
	 * @return the chosen action
	 */
	public Action chooseAction(List<Action> actions) {
		Random rand = new Random();

		// in the beginning or in epsilon % of the cases: play a random action (explore)
		if (simRound <= 1 || rand.nextDouble() <= Properties.EPSILON_GREEDY) {
			Collections.shuffle(actions);
		}

		// in 1-epsilon % of the case choose action with highest payoff (exploit)
		else {
			Collections.sort(actions);
		}

		return actions.get(0);
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
	 * Updates the mean payoffs for all actions.
	 */
	private void updateMeanPayoffs() {
		Iterator<Action> actionsIt = this.availableActions.iterator();
		while (actionsIt.hasNext()) {
			Action action = actionsIt.next();
			action.updateMeanPayoff(this.simRound);
		}
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
