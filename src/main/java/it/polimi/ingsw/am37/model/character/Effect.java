package it.polimi.ingsw.am37.model.character;

import java.util.ArrayList;
import java.util.Arrays;

public enum Effect {
    MONK(new int[]{0, 4, 2, 1}),
    FARMER(new int[]{5}),
    HERALD(new int[]{6}),
    MAGIC_POSTMAN(new int[]{7}),
    GRANDMA(new int[]{8, 9}),
    CENTAUR(new int[]{10}),
    JESTER(new int[]{0, 1, 12, 11}),
    KNIGHT(new int[]{15}),
    MUSHROOM_MAN(new int[]{16}),
    MINSTREL(new int[]{12, 11, 14, 13}),
    PRINCESS(new int[]{0, 11, 2, 1}),
    THIEF(new int[]{14, 3});

    private final ArrayList<Integer> baseEffects;

    Effect(int[] array) {
        baseEffects = new ArrayList<>(Arrays.stream(array).boxed().toList());
    }

    public ArrayList<Integer> getBaseEffects() {
        return baseEffects;
    }
}
