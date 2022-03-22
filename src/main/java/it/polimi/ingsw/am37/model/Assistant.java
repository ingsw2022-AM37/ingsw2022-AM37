package it.polimi.ingsw.am37.model;

/**
 * There are ten assistant cards for each wizard, if possible equal cards can't be used in the same turn
 */
public class Assistant extends Wizard {

	/**
	 * Default constructor
	 */
	public Assistant(WizardTeam team, int cardValue, int moveMNMovement) {
		super(team);
		this.cardValue = cardValue;
		this.moveMNMovement = moveMNMovement;
	}

	/**
	 * Each assistant has a cardValue which is useful to establish the order of players'
	 * turns, a lower value gives more priority when deciding the order
	 */
	private final int cardValue;

	/**
	 * Each assistant has a moveMNMovement which specific the maximum number of islands on which you
	 * can move MotherNature in clockwise
	 */
	private final int moveMNMovement;


	/**
	 * @return the value of the card
	 */
	public int getCardValue() {
		return this.cardValue;
	}

	/**
	 * @return the possible movement for MotherNature
	 */
	public int getMNMovement() {
		return this.moveMNMovement;
	}

}