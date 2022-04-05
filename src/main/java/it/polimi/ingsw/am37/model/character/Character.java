package it.polimi.ingsw.am37.model.character;

/**
 * This class represents the Character in the game.
 */
public class Character {

    /**
     * The effect that distinguishes the character.
     */
    private final CharacterEffect effect;
    /**
     * The id of the Effect.
     */
    private final Effect effectId;
    /**
     * The starting price of the Character, it can change during the game.
     */
    private final int startPrice;
    /**
     * The price of the character in the current game state.
     */
    private int currentPrice;

    /**
     * Default constructor
     */
    public Character(int startPrice, Effect character, CharacterEffect effect) {
        this.startPrice = startPrice;
        this.currentPrice = startPrice;
        this.effectId = character;
        this.effect = effect;
    }

    /**
     * it updates the cost of the Character after being used.
     */
    private void increasePrice() {
        currentPrice++;
    }

    /**
     * @return current price of the character.
     */
    public int getCurrentPrice() {
        return currentPrice;
    }

    /**
     * @param option The option parameters used to use the Effect.
     */
    public void useEffect(Option option) {
        effect.useEffect(option);
        increasePrice();
    }

}