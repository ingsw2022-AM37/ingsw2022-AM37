package it.polimi.ingsw.am37.model.character;

/**
 *
 */
public class Character implements CharacterEffect {

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
    private Effect effectId;

    /**
     * Default constructor
     */
    public Character() {
    }

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
     * @param option the option parameters
     */
    public void useEffect(Option option) {
        // TODO implement here
    }

}