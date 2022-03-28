package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.character.Character;
import it.polimi.ingsw.am37.model.character.Option;
import javax.management.InstanceAlreadyExistsException;
import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * This class represents the in-game player, it does not represent the person playing the game, therefore it will not
 * have all the attributes that can be associated with a physical player.
 */
public class Player {

	/**
	 * Default constructor
	 */
	public Player() {
		this.numberOfCoins = 0;
		this.lastAssistantPlayed = null;
		this.team = null;
	}

	/**
	 * It represents the number of coins that the player owns. Coins are needed to play a Character.
	 */
	private int numberOfCoins;

	/**
	 * It represents the school Board, each Player must own a board in order to play.
	 */
	private Board board;

	/**
	 * It represents the set of Assistant cards that every player owns, each Player must own a deck in order to play.
	 */
	private ArrayList<Assistant> assistantsDeck;

	/**
	 * It represents the team chosen by the Player, every player must choose a team. The team determines the Assistant
	 * deck that he will receive.
	 */
	private WizardTeam team;

	/**
	 * It represents the last Assistant Played in the turn, it is needed to calculate the movement of Mother Nature
	 * and the order of the next turn.
	 */
	private Assistant lastAssistantPlayed;

	/**
	 * Gives a coin to the player.
	 */
	public void receiveCoin() {
		this.numberOfCoins++;
	}

	/**
	 * Uses the received Assistant and, if it can be played, updates lastAssistantPlayed and removes it from the deck.
	 * @param assistantToBePlayed Is the specific Assistant that the player wants to play in this round.
	 * @throws InvalidParameterException If assistantToBePlayed isn't in the assistantsDeck.
	 */
	public void useAssistant(Assistant assistantToBePlayed) throws InvalidParameterException {
		if(!this.assistantsDeck.contains(assistantToBePlayed))
			throw new InvalidParameterException("This Assistant can't be played because it is not part of your deck.");
		this.assistantsDeck.remove(assistantToBePlayed);
		this.lastAssistantPlayed = assistantToBePlayed;
	}

	/**
	 * Uses the received Character if there are enough coins. It updates the number of coins.
	 * @param character The Character that the player wants to play.
	 * @param option The parameters that are needed in order to play the Character.
	 * @throws IllegalArgumentException if there aren't enough coins to play the Character.
	 */
	public void useCharacter(Character character, Option option) throws  IllegalArgumentException {
		if(this.numberOfCoins < character.getCurrentPrice())
			throw new IllegalArgumentException("Can't play the character, you don't have enough coins");
		this.numberOfCoins -= character.getCurrentPrice();
		character.useEffect(option);
	}

	/**
	 * It creates the Assistant deck from the Wizard Team received from parameters
	 * and sets the team as the team received from parameter.
	 * @param team The Wizard Team to which the deck belongs.
	 * @throws InstanceAlreadyExistsException if an instance of the assistantDeck is already present.
	 */
	public void createDeck(WizardTeam team) throws InstanceAlreadyExistsException {
		if(assistantsDeck != null)
			throw new InstanceAlreadyExistsException("Can't create a deck for this Player. A deck already exists");
		this.team = team;
		this.assistantsDeck = new ArrayList<>();
		int movement = 0;
		for(int i = 1; i <= 10; i++) {
			if(i % 2 == 0)
				this.assistantsDeck.add(new Assistant(team, i, movement));
			else {
				movement++;
				this.assistantsDeck.add(new Assistant(team, i, movement));
			}
		}
	}

	/**
	 * Moves Mother Nature on the Island received from parameters.
	 * @param from The Island of departure.
	 * @param to The arrival Island.
	 * @throws IllegalArgumentException If the Islands are NULL or if the Player is trying to move Mother Nature to
	 * the same island she is on.
	 */
	public void moveMotherNature(Island from, Island to) throws  IllegalArgumentException{
		if(from == null || to == null)
			throw new IllegalArgumentException("The Islands should not be NULL");
		if(from.equals(to))
			throw new IllegalArgumentException("You are trying to move Mother Nature to the same island she is on. " +
					"You must move Mother Nature.");

		from.unsetMotherNature();
		to.setMotherNature();
	}

	/**
	 * @return It returns the lastAssistantPlayed.
	 */
	public Assistant getLastAssistantPlayed() {
		return this.lastAssistantPlayed;
	}

	/**
	 * @param board The board controlled by the Player.
	 */
	public void setBoard(Board board){
		this.board = board;
	}

	/**
	 * @return It's the Board that the player owns.
	 */
	public Board getBoard() {
		return this.board;
	}

	/**
	 * @return The deck of Assistants that the Player has.
	 */
	public ArrayList<Assistant> getAssistantsDeck() {
		return assistantsDeck;
	}
}