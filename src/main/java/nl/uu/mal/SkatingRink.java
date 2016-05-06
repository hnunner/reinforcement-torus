package nl.uu.mal;

import java.util.Iterator;
import java.util.List;

public class SkatingRink {

	private List<Skater> skaters;

	/**
	 * Eagerly initialized singleton.
	 */
	// constant
	private static final SkatingRink instance = new SkatingRink();
	// private constructor
	private SkatingRink() {}
	// getter
	public static SkatingRink getInstance() {
		return instance;
	}

	/**
	 * Simulates movement of skaters for a default number of rounds.
	 */
	public void letThemSkate() {
		this.letThemSkate(Properties.DEFAULT_ROUNDS);
	}

	/**
	 * Simulates movement of skaters for a definable number of rounds.
	 *
	 * @param rounds
	 * 			number of round to be simulated
	 */
	public void letThemSkate(int rounds) {
		for (int i = 0; i < rounds; i++) {
			Iterator<Skater> iterator = skaters.iterator();
			while (iterator.hasNext()) {
				Skater skater = iterator.next();
				skater.move();
			}
		}
	}


	/**
	 * @return the skaters
	 */
	public List<Skater> getSkaters() {
		return skaters;
	}

	/**
	 * @param skaters the skaters to set
	 */
	public void setSkaters(List<Skater> skaters) {
		this.skaters = skaters;
	}

}
