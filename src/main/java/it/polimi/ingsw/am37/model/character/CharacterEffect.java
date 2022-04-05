package it.polimi.ingsw.am37.model.character;

import it.polimi.ingsw.am37.model.Bag;
import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;

import java.util.ArrayList;
import java.util.function.BiConsumer;

/**
 * This class represents the effect of the Character
 */
public class CharacterEffect {

    /**
     * It's the set of basic effects that make up the character's effect.
     */
    private final ArrayList<BiConsumer<Option, State>> baseEffects;

    private State state;


    public CharacterEffect(Effect effect, Bag bag) {
        baseEffects = new ArrayList<>(EffectDatabase.getEffects(effect));
        switch (effect) {
            case MONK, PRINCESS -> new State(new LimitedStudentsContainer(4), 0);
            case GRANDMA -> new State(null, 4);
            case JESTER -> new State(new LimitedStudentsContainer(6), 0);
            default -> new State(null, 0);
        }

    }

    /**
     * @param option It's the parameters needed to run the effect
     */
    public void useEffect(Option option) {
        for (BiConsumer<Option, State> singleEffect : baseEffects) {
            singleEffect.accept(option, state);
        }
    }

}