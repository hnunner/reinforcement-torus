package nl.uu.mal;

import java.util.Random;

import org.jfree.data.xy.XYSeries;

public class Skater {

	private XYSeries series;
	private double posX;
	private double posY;

	/**
	 * Constructor - creates a new skater.
	 *
	 * @param name
	 * 			the name of the skater
	 */
	public Skater(String name) {
		series = new XYSeries(name);

		posX = 0.0;
		posY = 0.0;

		series.add(posX, posY);
	}

	/**
	 * Move to the next position.
	 */
	public void move() {
		Random rand = new Random();
		posX++;
		series.add(posX, posY + rand.nextInt(10));
	}

	/**
	 * @return the series
	 */
	public XYSeries getSeries() {
		return series;
	}

}
