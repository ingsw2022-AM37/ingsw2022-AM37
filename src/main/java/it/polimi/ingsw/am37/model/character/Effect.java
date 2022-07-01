package it.polimi.ingsw.am37.model.character;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * All characters in the game with theirs price and information
 */
public enum Effect {
    MONK("Monk", new int[]{18, 0, 4, 2, 1}, 1),
    FARMER("Farmer", new int[]{5}, 2),
    HERALD("Herald", new int[]{6}, 3),
    MAGIC_POSTMAN("Magic Postman", new int[]{7}, 1),
    GRANDMA("Grandma", new int[]{8, 9}, 2),
    CENTAUR("Centaur", new int[]{10}, 3),
    JESTER("Jester", new int[]{18, 0, 19, 12, 18, 11, 19, 1}, 1),
    KNIGHT("Knight", new int[]{15}, 2),
    MUSHROOM_MAN("Mushroom Man", new int[]{16}, 3),
    MINSTREL("Minstrel", new int[]{18, 12, 19, 14, 18, 13, 19, 11}, 1),
    PRINCESS("Princess", new int[]{18, 0, 13, 2, 1}, 2),
    THIEF("Thief", new int[]{17, 3}, 3);

    /**
     * Character name
     */
    private final String characterName;
    /**
     * the array of baseEffect needed to create the effect
     */
    private final ArrayList<Integer> baseEffects;

    /**
     * The initial price of each effect
     */
    private final int initialPrice;

    /**
     * Default constructor of an effect, used only by the enumeration
     *
     * @param array        the array of base effect that compose this effect
     * @param initialPrice the initial price of this effect
     */
    Effect(String characterName, int[] array, int initialPrice) {
        this.characterName = characterName;
        this.baseEffects = new ArrayList<>(Arrays.stream(array).boxed().toList());
        this.initialPrice = initialPrice;
    }

    /**
     * @return the base effect array of needed effects
     */
    public ArrayList<Integer> getBaseEffects() {
        return baseEffects;
    }

    /**
     * @return the initial price of this effect
     */
    public int getInitialPrice() {
        return initialPrice;
    }

    /**
     * @return the name of the character
     */
    public String getCharacterName() {
        return characterName;
    }
}
