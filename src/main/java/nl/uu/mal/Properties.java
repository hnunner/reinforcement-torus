package nl.uu.mal;

/**
 * Properties file containing parameters for tuning layout and behavior.
 *
 * @author h.nunner
 */
public final class Properties {

	// player/action/behavior
	public static final int ACTION_COUNT = 6;
	public static final int STD_DISTANCE = 1;
	public static final double EPSILON_GREEDY = 0.90;

	// gameplay/layout
	public static final int PLAYER_COUNT = 3;
	public static final int DEFAULT_ROUNDS = 200;
	public static final int TORUS_WIDTH = 15;
	public static final int TORUS_HEIGHT = 15;
	public static final double COLLISION_RADIUS = 3;

	// rewards
	public static final int HIGH_REWARD = 10;
	public static final int LOW_REWARD = 1;
}
