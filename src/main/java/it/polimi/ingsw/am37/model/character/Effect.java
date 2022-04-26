package it.polimi.ingsw.am37.model.character;

import java.util.ArrayList;
import java.util.Arrays;

public enum Effect {
    MONK(new int[]{0, 4, 2, 1}, 1),
    FARMER(new int[]{5}, 2),
    HERALD(new int[]{6}, 3),
    MAGIC_POSTMAN(new int[]{7}, 1),
    GRANDMA(new int[]{8, 9}, 2),
    CENTAUR(new int[]{10}, 3),
    JESTER(new int[]{0, 1, 12, 11}, 1),
    KNIGHT(new int[]{15}, 2),
    MUSHROOM_MAN(new int[]{16}, 3),
    MINSTREL(new int[]{12, 11, 14, 13}, 1),
    PRINCESS(new int[]{0, 11, 2, 1}, 2),
    THIEF(new int[]{14, 3}, 3);

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
    Effect(int[] array, int initialPrice) {
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
}
