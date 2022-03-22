package it.polimi.ingsw.am37.model;

/**
 * This class is used to identify different teams for students and professors
 */
public enum FactionColor {
	BLUE(0),
	RED(1),
	GREEN(2),
	YELLOW(3),
	PINK(4);

	/**
	 * Each color, which identifies students or professors' team, has an index in order to implement
	 * a map between them
	 */
	private final int index;

	/**
	 * Default constructor
	 */
	FactionColor(int index) {
		this.index = index;
	}

	/**
	 * @return color's index
	 */
	public int getIndex(){
		return this.index;
	}

}