package nl.uu.mal;

/**
 * Properties file containing parameters for tuning layout and behavior.
 *
 * @author h.nunner
 */
public final class Properties {

	// player/action/behavior
	public static final int ACTION_COUNT = 8;
	public static final int STD_DISTANCE = 1;
	public static final int DISTANCE_FRAGMENTATION = 10;
	public static final double DISTANCE_INCREMENT = Double.valueOf(STD_DISTANCE) / Double.valueOf(DISTANCE_FRAGMENTATION);
	public static final double EPSILON_GREEDY = 0.1;

	// gameplay/layout
	public static final int PLAYER_COUNT = 10;
	public static final int DEFAULT_ROUNDS = 100;
	public static final int TORUS_WIDTH = 15;
	public static final int TORUS_HEIGHT = 15;
	public static final double COLLISION_RADIUS = 1;
	// rewards
	public static final int HIGH_REWARD = 10;
	public static final int LOW_REWARD = 1;
}
