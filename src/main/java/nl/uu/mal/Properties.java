package nl.uu.mal;

/**
 * Properties file containing parameters for tuning layout and behavior.
 *
 * @author h.nunner
 */
public final class Properties {

	// player/action/behavior
	public static final int ACTION_COUNT = 8;									// n
	public static final int BASE_ANGLE = 360 / Properties.ACTION_COUNT;			// k
	public static final int STD_DISTANCE = 1;									// δ = standard distance per turn
	public static final int DISTANCE_FRAGMENTATION = 10;
	public static final double DISTANCE_INCREMENT = Double.valueOf(STD_DISTANCE)
			/ Double.valueOf(DISTANCE_FRAGMENTATION);
	public static final double EPSILON_GREEDY = 0.1;							// ε

	// gameplay/layout
	public static final int DEFAULT_ROUNDS = 1000;
	public static final int PLAYER_COUNT = 25;									// N
	public static final int TORUS_WIDTH = 5;									// w
	public static final int TORUS_HEIGHT = 5;									// h
	public static final double COLLISION_RADIUS = 0.8;							// r

	// rewards
	public static final int HIGH_REWARD = 10;									// R1
	public static final int LOW_REWARD = 0;										// R2
}
