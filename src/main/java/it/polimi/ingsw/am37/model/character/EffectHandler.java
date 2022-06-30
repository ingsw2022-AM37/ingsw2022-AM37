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
    public final static int DEFAULT_NOENTRYTILES = -1;
    public final static int GRANDMA_NOENTRYTILES = 4;
    public final static int MONK_PRINCESS_CONTAINER_DIM = 4;
    public final static int JESTER_CONTAINERD_DIM = 6;

    /**
     * It's the set of basic effects that make up the character's effect.
     */
    private final transient ArrayList<BiConsumer<Option, State>> baseEffects;

    private final State state;


    /**
     * Default and unique constructor of the effect handler. May be needed a bag because some effect need to obtain some
     * students and place inside their {@link State} on the container.
     *
     * @param effect the effect type to handle
     */
    public EffectHandler(Effect effect, Bag bag) {
        baseEffects = EffectDatabase.getEffects(effect);
        switch (effect) {
            case MONK, PRINCESS -> {
                state = new State(new LimitedStudentsContainer(MONK_PRINCESS_CONTAINER_DIM), DEFAULT_NOENTRYTILES);
                state.getContainer().uniteContainers(bag.extractStudents(MONK_PRINCESS_CONTAINER_DIM));
            }
            case GRANDMA -> state = new State(null, GRANDMA_NOENTRYTILES);
            case JESTER -> {
                state = new State(new LimitedStudentsContainer(JESTER_CONTAINERD_DIM), DEFAULT_NOENTRYTILES);
                state.getContainer().uniteContainers(bag.extractStudents(JESTER_CONTAINERD_DIM));
            }
            default -> state = new State(null, DEFAULT_NOENTRYTILES);
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

    /**
     * @return the state associated to this character
     */
    public State getState() {
        return state;
    }
}