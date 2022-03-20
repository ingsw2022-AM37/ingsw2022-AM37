package it.polimi.ingsw.am37.Model;

import it.polimi.ingsw.am37.Model.TowerColor;

import java.util.*;

/**
 * This class is used to store some information about towers and their use, this class is used
 * in the Board one to manage towers
 */
public class LimitedTowerContainer {

	/**
	 * Default constructor
	 */
	public LimitedTowerContainer(int maxSize, int currentSize, TowerColor currentTower) {
		this.maxSize = maxSize;
		this.currentSize = currentSize;
		this.currentTower = currentTower;
	}

	/**
	 * Indicate the color of all towers
	 * @see TowerColor
	 */
	private final TowerColor currentTower;

	/**
	 * It's the maximum number of towers
	 */
	private final int maxSize;

	/**
	 * It's the current number of towers which can be equal or lower compared to maxSize
	 */
	private int currentSize;

	/**
	 * This method add a certain number of towers of the same color after checking it's possible
	 * @param num It's the number of towers to add in the object
	 * @throws IllegalArgumentException The parameter is wrong if negative, zero or the sum exceed the maximum number of towers
	 */
	public void addTowers(int num) throws IllegalArgumentException {
		if(num<=0)
			throw new IllegalArgumentException("Negative number or zero");

		int tmp = this.currentSize + num;
		if(tmp>this.maxSize)
			throw new IllegalArgumentException("Number is too big");

		this.currentSize = this.currentSize + num;
	}

	/**
	 * @return The color of towers
	 * @see TowerColor
	 */
	public TowerColor getcurrentTower() {
		return this.currentTower;
	}

	/**
	 * @return The current number of towers
	 */
	public int getCurrentSize() {
		return this.currentSize;
	}

	/**
	 * This method remove a certain number of towers of the same color after checking it's possible
	 * @param num It's the number of towers to remove in the object
	 * @throws IllegalArgumentException The parameter is wrong if negative, zero or the difference is lower than zero
	 */
	public void removeTowers(int num) throws IllegalArgumentException {
		if(num<0)
			throw new IllegalArgumentException("Negative number or zero");

		int tmp = this.currentSize - num;
		if(tmp < 0)
			throw new IllegalArgumentException("Number is too big");

		this.currentSize = this.currentSize - num;
	}

}