package it.polimi.ingsw.am37.model.character;

import it.polimi.ingsw.am37.model.*;
import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;

/**
 * Builder class to create the class {@link Option} with optional parameters. The default not initialized values are
 * null for objects and -1 for intPar; controller and player must be provided. Each possible field option could be set
 * using the method in this class that as the same name of the field in {@link Option} class
 */
public class OptionBuilder {

    private Island island;
    private Player player;
    private FactionColor color;
    private int intPar;
    private Bag bag;
    private GameManager controller;
    private LimitedStudentsContainer primaryContainer;
    private LimitedStudentsContainer secondaryContainer;

    private OptionBuilder(GameManager controller, Player player) {
        this.controller = controller;
        this.player = player;
        this.intPar = -1;
    }

    public static OptionBuilder newBuilder(GameManager controller, Player player) {
        return new OptionBuilder(controller, player);
    }

    public OptionBuilder island(Island island) {
        this.island = island;
        return this;
    }

    public OptionBuilder color(FactionColor color) {
        this.color = color;
        return this;
    }

    public OptionBuilder intPar(int intPar) {
        this.intPar = intPar;
        return this;
    }

    public OptionBuilder bag(Bag bag) {
        this.bag = bag;
        return this;
    }

    public OptionBuilder primaryContainer(LimitedStudentsContainer container) {
        this.primaryContainer = container;
        return this;
    }

    public OptionBuilder secondaryContainer(LimitedStudentsContainer container) {
        this.secondaryContainer = container;
        return this;
    }

    public OptionBuilder player(Player player) {
        this.player = player;
        return this;
    }

    public Option build() {
        return new Option(island, player, color, intPar, bag, controller, primaryContainer, secondaryContainer);
    }

    public void clear() {
        island = null;
        player = null;
        color = null;
        intPar = -1;
        bag = null;
        controller = null;
        primaryContainer = null;
        secondaryContainer = null;
    }
}
