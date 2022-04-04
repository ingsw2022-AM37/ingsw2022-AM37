package it.polimi.ingsw.am37.model.character;

import it.polimi.ingsw.am37.model.*;
import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;

/**
 * Represent the option that a character may need. It's created before each useEffect method and passed to character and
 * each effects. Consider build this with the special class OptionBuilder for setting only some parameters; remember that
 * controller and player must always be not null
 *
 * @see OptionBuilder
 */
public class Option {

    /**
     * The island used by characters effect
     */
    private final Island island;

    /**
     * The player that has called the character
     */
    private final Player player;
    /**
     * The color some characters activate effect on
     */
    private final FactionColor color;
    /**
     * An int parameter used by some characters
     */
    private final int intPar;
    /**
     * the bag some
     */
    private final Bag bag;
    /**
     * The game controller that has all game references
     */
    private final GameManager controller;
    /**
     * The student container with selected students to remove
     */
    private final LimitedStudentsContainer removeContainer;
    /**
     * The student container with selected students to add
     */
    private final LimitedStudentsContainer addContainer;

    /**
     * Default constructor
     */
    public Option(Island island, Player player, FactionColor color, int intPar, Bag bag, GameManager controller, LimitedStudentsContainer removeContainer, LimitedStudentsContainer addContainer) {
        this.island = island;
        this.player = player;
        this.color = color;
        this.intPar = intPar;
        this.bag = bag;
        this.controller = controller;
        this.removeContainer = removeContainer;
        this.addContainer = addContainer;
    }

    public Island getIsland() {
        return island;
    }

    public Player getPlayer() {
        return player;
    }

    public FactionColor getColor() {
        return color;
    }

    public int getIntPar() {
        return intPar;
    }

    public Bag getBag() {
        return bag;
    }

    public GameManager getController() {
        return controller;
    }

    public LimitedStudentsContainer getRemoveContainer() {
        return removeContainer;
    }

    public LimitedStudentsContainer getAddContainer() {
        return addContainer;
    }
}