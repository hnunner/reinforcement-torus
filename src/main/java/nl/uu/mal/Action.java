package nl.uu.mal;

public class Action implements Comparable<Action> {

	private int cumulatedPayoff;
	private double meanPayoff;
	private int angle;
	private int distance;


	/**
	 * Constructor - creating a new action
	 *
	 * @param angle
	 * 			the angle of movement
	 * @param distance
	 * 			the distance of movement
	 */
	public Action(int angle, int distance) {
		this.cumulatedPayoff = 0;
		this.meanPayoff = 0;
		this.angle = angle;
		this.distance = distance;
	}


	/**
	 * @return the cumulated payoff
	 */
	public int getCumulatedPayoff() {
		return cumulatedPayoff;
	}

	/**
	 * @param cumulatedPayoff cumulated payoff to set
	 */
	public void setCumulatedPayoff(int cumulatedPayoff) {
		this.cumulatedPayoff = cumulatedPayoff;
	}

	/**
	 * @return the meanPayoff
	 */
	public double getMeanPayoff() {
		return meanPayoff;
	}

	/**
	 * @param meanPayoff the meanPayoff to set
	 */
	public void setMeanPayoff(double meanPayoff) {
		this.meanPayoff = meanPayoff;
	}

	/**
	 * @return the angle
	 */
	public int getAngle() {
		return angle;
	}

	/**
	 * @param angle the angle to set
	 */
	public void setAngle(int angle) {
		this.angle = angle;
	}

	/**
	 * @return the distance
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}


	/**
	 * For ordering a list of actions by cumulated payoff.
	 */
	public int compareTo(Action o) {
		return Integer.compare(o.getCumulatedPayoff(), this.cumulatedPayoff);
	}

}
