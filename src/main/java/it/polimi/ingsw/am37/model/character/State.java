package it.polimi.ingsw.am37.model.character;

import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.UnlimitedStudentsContainer;

public class State {
    private LimitedStudentsContainer container;
    private int noEntryTiles;
    private UnlimitedStudentsContainer serviceContainer;

    public State(LimitedStudentsContainer container, int noEntryTiles) {
        this.container = container;
        this.noEntryTiles = noEntryTiles;
        this.serviceContainer = new UnlimitedStudentsContainer();
    }

    public LimitedStudentsContainer getContainer() {
        return container;
    }

    public void setContainer(LimitedStudentsContainer container) {
        this.container = container;
    }

    public int getNoEntryTiles() {
        return noEntryTiles;
    }

    public void setNoEntryTiles(int noEntryTiles) {
        this.noEntryTiles = noEntryTiles;
    }

    public UnlimitedStudentsContainer getServiceContainer() {
        return serviceContainer;
    }

    public void setServiceContainer(UnlimitedStudentsContainer serviceContainer) {
        this.serviceContainer = serviceContainer;
    }
}
