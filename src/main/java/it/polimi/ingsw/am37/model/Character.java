package it.polimi.ingsw.am37.model;

import java.util.*;

/**
 * 
 */
public class Character implements CharacterEffect {

	/**
	 * Default constructor
	 */
	public Character() {
	}

	/**
	 * 
	 */
	private int startPrice;

	/**
	 * 
	 */
	private int currentPrice;

	/**
	 * 
	 */
	private CharacterEffect effect;


	/**
	 *
	 */
	private void increasePrice() {
		// TODO implement here
	}

	/**
	 * @return current price of the character
	 */
	public int getCurrentPrice() {
		return currentPrice;
	}

	/**
	 * @param option
	 */
	public void useEffect(Option option) {
		// TODO implement here
	}

}