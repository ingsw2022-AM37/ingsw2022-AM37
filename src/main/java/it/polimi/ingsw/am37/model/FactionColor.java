package it.polimi.ingsw.am37.model;

/**
 * This class is used to identify different teams for students and professors
 */
public enum FactionColor {
	BLUE(0, "blue"),
	RED(1, "red"),
	GREEN(2, "green"),
	YELLOW(3, "yellow"),
	PINK(4, "magenta");

	/**
	 * Each color, which identifies students or professors' team, has an index in order to implement
	 * a map between them
	 */
	private final int index;


	/**
	 * Represent the color name as indicated in Jansi library
	 * @see org.fusesource.jansi.Ansi.Color
	 */
	public final String color;

	/**
	 * Default constructor
	 */
	FactionColor(int index, String color) {
		this.index = index;
		this.color = color;
	}

	/**
	 * @return color's index
	 */
	public int getIndex(){
		return this.index;
	}

}