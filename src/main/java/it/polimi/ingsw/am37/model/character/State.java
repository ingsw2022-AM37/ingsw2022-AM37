package it.polimi.ingsw.am37.model.character;

import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.UnlimitedStudentsContainer;

public class State {
    protected LimitedStudentsContainer container;
    protected int noEntryTiles;
    protected UnlimitedStudentsContainer serviceContainer;
}
