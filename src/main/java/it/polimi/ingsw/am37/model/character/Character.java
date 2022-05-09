package it.polimi.ingsw.am37.model.character;

import it.polimi.ingsw.am37.message.UpdatableObject;
import it.polimi.ingsw.am37.model.Bag;

import java.util.Objects;

import static it.polimi.ingsw.am37.message.UpdatableObject.UpdatableType.CHARACTER;

/**
 * This class represents the Character in the game. They have special effects to modify and improve the game. They are
 * activated and usable only if advancedMove is enabled in {@link it.polimi.ingsw.am37.model.GameManager}. Each
 * character has a starting price and a current price. When used each player needs to get the price. Use their effect
 * with the {@link this#useEffect(Option)} and the first usage cause the starting price to increase.
 */
@UpdatableObject(type = CHARACTER)
public class Character {

    /**
     * The effect that distinguishes the character.
     */
    private final EffectHandler effectHandler;
    /**
     * The id of the Effect.
     */
    private final Effect effectType;
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
    public Character(int startPrice, Effect effectType, Bag bag) {
        this.startPrice = startPrice;
        this.currentPrice = startPrice;
        this.effectType = effectType;
        this.effectHandler = new EffectHandler(effectType, bag);
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
        effectHandler.useEffect(option);
        if (startPrice == currentPrice) increasePrice();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Character character)) return false;
        return effectType == character.effectType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(effectType);
    }
}