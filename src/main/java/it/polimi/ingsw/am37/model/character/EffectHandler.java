package it.polimi.ingsw.am37.model.character;

import it.polimi.ingsw.am37.model.Bag;
import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;

import java.util.ArrayList;
import java.util.function.BiConsumer;

/**
 * This class represents the interface for using an effect and creating the card status of the Character. The effect are
 * built based on base effects inside the {@link EffectDatabase} following what established in {@link Effect} fields
 */
public class EffectHandler {

    /**
     * It's the set of basic effects that make up the character's effect.
     */
    private final ArrayList<BiConsumer<Option, State>> baseEffects;

    private State state;


    /**
     * Default and unique constructor of the effect handler. May be needed a bag because some effect need to obtain some
     * students and place inside their {@link State} on the container.
     *
     * @param effect the effect type to handle
     * @param bag    the bag from where some effects need to extract students
     */
    public EffectHandler(Effect effect, Bag bag) {
        baseEffects = new ArrayList<>(EffectDatabase.getEffects(effect));
        switch (effect) {
            case MONK, PRINCESS -> new State(new LimitedStudentsContainer(4), 0);
            case GRANDMA -> new State(null, 4);
            case JESTER -> new State(new LimitedStudentsContainer(6), 0);
            default -> new State(null, 0);
        }

    }

    /**
     * @param option It's the parameters needed to run the effect and contains all necessary field for correct
     *               functionality
     */
    public void useEffect(Option option) {
        for (BiConsumer<Option, State> singleEffect : baseEffects) {
            singleEffect.accept(option, state);
        }
    }

}