package it.polimi.ingsw.am37.model.character;

import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.UnlimitedStudentsContainer;

/**
 * This class represent the state of a {@link Character}: some of them need to have a container for students or no entry
 * tiles. Also, some service attributes are here for make base effects exchange info and attributes.
 */
public class State {
    /**
     * Containers for holding students that some characters have "on their card"; set as null if not needed
     */
    private LimitedStudentsContainer container;

    /**
     * Represented a counter of no entry tiles available to this effect; set as 0 if not needed
     */
    private int noEntryTiles;

    /**
     * A very special containers, it's used by most of the operation of lots of effects related to students as memory
     * between different base effects
     */
    private UnlimitedStudentsContainer serviceContainer;

    /**
     * Default constructor
     *
     * @param container    the initial container of students (could be null if not required)
     * @param noEntryTiles the initial count of no entry tiles (could be null if not required)
     */
    public State(LimitedStudentsContainer container, int noEntryTiles) {
        this.container = container;
        this.noEntryTiles = noEntryTiles;
        this.serviceContainer = new UnlimitedStudentsContainer();
    }

    /**
     * @return The card specific container of students
     */
    public LimitedStudentsContainer getContainer() {
        return container;
    }

    /**
     * @param container the containers that set the object value
     */
    public void setContainer(LimitedStudentsContainer container) {
        this.container = container;
    }

    /**
     * @return the number of no entry tiles available to this effect
     */
    public int getNoEntryTiles() {
        return noEntryTiles;
    }

    /**
     * @param noEntryTiles the new numbers of no entry tiles
     */
    public void setNoEntryTiles(int noEntryTiles) {
        this.noEntryTiles = noEntryTiles;
    }

    /**
     * @return the service containers
     */
    public UnlimitedStudentsContainer getServiceContainer() {
        return serviceContainer;
    }

    /**
     * @param serviceContainer the new service containers
     */
    public void setServiceContainer(UnlimitedStudentsContainer serviceContainer) {
        this.serviceContainer = serviceContainer;
    }
}
