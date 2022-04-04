package it.polimi.ingsw.am37.model.character;

import it.polimi.ingsw.am37.model.*;
import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;

/**
 * Builder class to create the class Option with optional parameters.
 * The default not initialized values are null for objects and -1 for intPar; controller and player must be provided
 *
 * @see Option
 */
public class OptionBuilder {

    private Island island;
    private Player player;
    private FactionColor color;
    private int intPar;
    private Bag bag;
    private GameManager controller;
    private LimitedStudentsContainer removeContainer;
    private LimitedStudentsContainer addContainer;

    private OptionBuilder(GameManager controller, Player player) {
        this.controller = controller;
        this.player = player;
        this.intPar = -1;
    }

    public static OptionBuilder newBuilder(GameManager controller, Player player) {
        return new OptionBuilder(controller, player);
    }

    /**
     * @param island the parameter to set.
     * @return The updated builder.
     */
    public OptionBuilder island(Island island) {
        this.island = island;
        return this;
    }

    /**
     * @param color the parameter to set.
     * @return The updated builder.
     */
    public OptionBuilder color(FactionColor color) {
        this.color = color;
        return this;
    }

    /**
     * @param intPar the parameter to set.
     * @return The updated builder.
     */
    public OptionBuilder intPar(int intPar) {
        this.intPar = intPar;
        return this;
    }

    /**
     * @param bag the parameter to set.
     * @return The updated builder.
     */
    public OptionBuilder bag(Bag bag) {
        this.bag = bag;
        return this;
    }

    /**
     * @param container the parameter to set.
     * @return The updated builder.
     */
    public OptionBuilder removeContainer(LimitedStudentsContainer container) {
        this.removeContainer = container;
        return this;
    }

    /**
     * @param container the parameter to set.
     * @return The updated builder.
     */
    public OptionBuilder addContainer(LimitedStudentsContainer container) {
        this.addContainer = container;
        return this;
    }

    /**
     * Builds the Option with the given parameters
     *
     * @return The constructed Option
     */
    public Option build() {
        return new Option(island, player, color, intPar, bag, controller, removeContainer, addContainer);
    }

    /**
     * Resets every single parameter for a new use of Option.
     */
    public void clear() {
        island = null;
        player = null;
        color = null;
        intPar = -1;
        bag = null;
        controller = null;
        removeContainer = null;
        addContainer = null;
    }
}
