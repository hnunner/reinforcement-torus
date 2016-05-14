package nl.uu.mal;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing an action, performable by a {@link Skater}. The basic action consists of an angle describing the
 * direction within the global coordinate system of the {@link SkatingRink} and a fixed distance per simulation round.
 * Additionally there are values for cumulated and mean payoffs, used for reinforcement learning methods.
 *
 * Actions are supposed to be used within a list, holding all possible actions for each {@link Skater}. Therefore
 * {@link Comparable} is being implemented to realize an ordering of actions by payoffs, which is needed when an
 * exploitation strategy leads to collision and the action with the next highest payoff is being chosen.
 *
 * @author h.nunner
 */
public class Action implements Comparable<Action> {

	// discription of movement within global coordinate system of the skating rink
	private int angle;
	private int distance;

	// payoffs for reinforcement learning
	private int cumulatedPayoff;
	private double meanPayoff;


	/**
	 * Constructor - creating a new action
	 *
	 * @param angle
	 * 			the angle of movement
	 * @param distance
	 * 			the distance of movement
	 */
	private Action(int angle, int distance) {
		this.angle = angle;
		this.distance = distance;
		this.cumulatedPayoff = 0;
		this.meanPayoff = 0.0;
	}


	/**
	 * Creates a list of available actions.
	 *
	 * @return the list of available actions
	 */
	public static List<Action> createAvailableActions() {
		// step through the available angles and initialize actions
		List<Action> availableActions = new ArrayList<Action>();
		int currentAngle = 0;
		while (currentAngle < 360) {
			availableActions.add(new Action(currentAngle, Properties.STD_DISTANCE));
			currentAngle += Properties.BASE_ANGLE;
		}
		return availableActions;
	}


	/**
	 * For ordering a list of actions by cumulated payoff.
	 */
	public int compareTo(Action o) {
		return Integer.compare(o.getCumulatedPayoff(), this.cumulatedPayoff);
	}


	/**
	 * @return the angle
	 */
	public int getAngle() {
		return angle;
	}

	/**
	 * @return the distance
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * @return the cumulatedPayoff
	 */
	public int getCumulatedPayoff() {
		return cumulatedPayoff;
	}

	/**
	 * @deprecated - use giveHighReward() and giveLowReward() instead. Kept only for testing purposes.
	 *
	 * @param cumulatedPayoff the cumulatedPayoff to set
	 */
	@Deprecated
	public void setCumulatedPayoff(int cumulatedPayoff) {
		this.cumulatedPayoff = cumulatedPayoff;
	}

	/**
	 * Increases the cumulated payoff with a high reward
	 */
	public void giveHighReward() {
		this.cumulatedPayoff += Properties.HIGH_REWARD;
	}

	/**
	 * Increases the cumulated payoff with a low reward
	 */
	public void giveLowReward() {
		this.cumulatedPayoff += Properties.LOW_REWARD;
	}

	/**
	 * @return the meanPayoff
	 */
	public double getMeanPayoff() {
		return meanPayoff;
	}

	/**
	 * Updates the mean payoff based on the cumulated payoff and the simulation round.
	 *
	 * @param simulationRound
	 * 			the simulation round (starting with round 1 - do not confuse with index starting at 0)
	 */
	public void updateMeanPayoff(int simulationRound) {
		this.meanPayoff = Double.valueOf(cumulatedPayoff) / Double.valueOf(simulationRound);
	}

}
