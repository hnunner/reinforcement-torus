package nl.uu.mal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class Skater {

	private static final Logger LOG = Logger.getLogger(Skater.class.getName());

	private int simRound;
	private SkatingRink skatingRink;
	private Position position;
	private List<Action> availableActions;


	/**
	 * Constructor - creates a new skater.
	 *
	 * @param name
	 * 			the name of the skater
	 * @param availableActions
	 * 			the list of available actions for the skater
	 * @param skatingRink
	 * 			the skating rink the player is located in
	 */
	public Skater(String name, SkatingRink skatingRink, List<Skater> otherSkaters) {
		this.simRound = 0;
		this.skatingRink = skatingRink;
		this.position = initPosition(skatingRink.getWidth(), skatingRink.getHeight(), otherSkaters);
		this.availableActions = initAvailableActions();
	}

	/**
	 * Initializes the list of available actions.
	 *
	 * @return the available actions
	 */
	public List<Action> initAvailableActions() {
		// possibly read from args
		int baseAngle = Properties.BASE_ANGLE;

		// check [360 mod (base_angle)] = 0
		if ((360 % baseAngle) != 0) {
			throw new RuntimeException("360° is not divisible by chosen base angle (" + Properties.BASE_ANGLE
					+ ") without a remainder");
		}

		// step through the available angles and initialize actions
		List<Action> availableActions = new ArrayList<Action>();
		int currentAngle = 0;
		while (currentAngle < 360) {
			availableActions.add(new Action(currentAngle, Properties.STD_DISTANCE));
			currentAngle += baseAngle;
		}
		return availableActions;
	}

	/**
	 * Initializes the initial position of the skater.
	 *
	 * @param width
	 * 			width of the rink
	 * @param height
	 * 			height of the rink
	 * @param otherSkaters
	 * 			other skaters for collision checks
	 * @return the initila position
	 */
	private Position initPosition(int width, int height, List<Skater> otherSkaters) {
		// try a random position first
		Random rand = new Random();
		Position initialPosition = new Position(Double.valueOf(rand.nextInt(width)), Double.valueOf(rand.nextInt(height)));

		// try random positions, if / while colliding with other skater
		while (isColliding(initialPosition, otherSkaters)) {
			initialPosition.setX(rand.nextInt(width));
			initialPosition.setY(rand.nextInt(height));
		}
		return initialPosition;
	}

	/**
	 * Move to the next position.
	 */
	public void move() {
		this.simRound++;

		// order actions, so that the list is either in order of payoffs (exploitation) or in random order (exploration)
		List<Action> orderedActions = orderActions(this.availableActions, this.simRound);

		Action nextAction = null;
		Position nextPosition = null;
		boolean foundNextPosition = false;
		List<Skater> otherSkaters = this.skatingRink.getOtherSkaters(this);

		// iterate over ordered list of actions
		Iterator<Action> actionsIt = orderedActions.iterator();
		while (actionsIt.hasNext() && !foundNextPosition) {
			nextAction = actionsIt.next();

			// determine prospective non-collisional position based on current position, action and skate rink
			nextPosition = skatingRink.getNextPosition(position, nextAction);
			foundNextPosition = !isColliding(nextPosition, otherSkaters);

			// give low reward in case action lead to collision
			if (!foundNextPosition) {
				nextAction.setCumulatedPayoff(nextAction.getCumulatedPayoff() + Properties.LOW_REWARD);
			}
		}

		// skip turn, if unable to determine next position
		if (!foundNextPosition || nextAction == null || nextPosition == null) {
			LOG.info("Could not find an appropriate next position. Skipping current turn.");
			return;
		}

		// set new position
		this.position = nextPosition;

		// set new payoffs
		nextAction.setCumulatedPayoff(nextAction.getCumulatedPayoff() + Properties.HIGH_REWARD);
		actionsIt = orderedActions.iterator();
		while (actionsIt.hasNext()) {
			Action action = actionsIt.next();
			action.setMeanPayoff(Double.valueOf(action.getCumulatedPayoff()) / Double.valueOf(this.simRound));
		}
	}

	/**
	 * Orders a list of actions strategically for a defined ratio of exploration and exploitation.
	 *
	 * @return teh ordered list of actions
	 */
	public List<Action> orderActions(List<Action> actions, int simRound) {
		Random rand = new Random();

		// in 1-epsilon cases or in the beginning play a random action (explore)
		if (rand.nextDouble() > Properties.EPSILONE_GREEDY || simRound <= 1) {
			Collections.shuffle(actions);
		}
		// in epsilon percent of the case choose action ordered by payoffs (exploit)
		else {
			Collections.sort(actions);
		}
		return actions;
	}

	private boolean isColliding(Position position, List<Skater> skaters) {
		boolean collision = false;

		Iterator<Skater> skatersIt = skaters.iterator();
		while (skatersIt.hasNext() && !collision) {
			Skater skater = skatersIt.next();
			collision = Math.sqrt(Math.pow(position.getX() - skater.getPosition().getX(), 2)
					+ Math.pow(position.getY() - skater.getPosition().getY(), 2))
					< Properties.COLLISION_RADIUS;

//			LOG.info("Collision: (" + position.getX() + ", " + position.getY() + ") <--> ("
//					+ skater.getPosition().getX() + ", " + skater.getPosition().getY() + ")" );
		}

		return collision;
	}

	public int getCumulatedPayoff(int angle) {
		Iterator<Action> actionsIt = this.availableActions.iterator();
		while (actionsIt.hasNext()) {
			Action action = actionsIt.next();
			if (action.getAngle() == angle) {
				return action.getCumulatedPayoff();
			}
		}
		return 0;
	}

	public double getMeanPayoff(int angle) {
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
	 * @return the availableActions
	 */
	public List<Action> getAvailableActions() {
		return availableActions;
	}

	/**
	 * @return the position
	 */
	public Position getPosition() {
		return position;
	}

}
