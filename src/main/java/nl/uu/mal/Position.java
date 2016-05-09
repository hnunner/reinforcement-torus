package nl.uu.mal;

public class Position {

	private double x;
	private double y;


	/**
	 * Constructor - creating a new position
	 *
	 * @param x
	 * 			the x-coordinate of the position
	 * @param y
	 * 			the y-coordinate of the position
	 */
	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}


	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

}
