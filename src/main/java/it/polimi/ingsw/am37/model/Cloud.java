package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.StudentsContainer;

import java.util.Objects;

import static it.polimi.ingsw.am37.model.UpdatableObject.UpdatableType.CLOUD;
import static it.polimi.ingsw.am37.controller.UpdateController.Properties.P_CLOUD;

/**
 * This class represent the Clouds in the game.
 */
public class Cloud extends UpdatableObject {

    /**
     * They represent the number of students that can be placed on a Cloud based on the number of Players.
     */
    private final int studentsPerCloud2Players = 3, studentsPerCloud3Players = 4;

    /**
     * the unique ID of a cloud
     */
    private final String cloudId;
    /**
     * It is needed to know if the game is played by two or three Players.
     */
    private final boolean isFor2;
    /**
     * It represents the students that are on the Cloud at the start of every turn.
     */
    private LimitedStudentsContainer studentsOnCloud;

    /**
     * Counter for increase cloud id
     */
    private static int idCounter = 0;


    /**
     * Default constructor
     */
    public Cloud(boolean isFor2, int id) {
        super(CLOUD);
        this.isFor2 = isFor2;
        if (this.isFor2) studentsOnCloud = new LimitedStudentsContainer(studentsPerCloud2Players);
        else studentsOnCloud = new LimitedStudentsContainer(studentsPerCloud3Players);
        cloudId = String.valueOf(id);
        idCounter = idCounter + 1;
    }

    /**
     * @param students Students used to fill the Cloud.
     */
    public void addStudents(StudentsContainer students) {
        StudentsContainer oldContainer = studentsOnCloud.copy();
        studentsOnCloud.uniteContainers(students);
        support.firePropertyChange(P_CLOUD.toString(), oldContainer, studentsOnCloud);
    }

    /**
     * @return The students on the Cloud.
     */
    public LimitedStudentsContainer removeStudents() {
        LimitedStudentsContainer temp = studentsOnCloud;
        studentsOnCloud = new LimitedStudentsContainer(isFor2 ? studentsPerCloud2Players : studentsPerCloud3Players);
        support.firePropertyChange(P_CLOUD.toString(), temp, studentsOnCloud);
        return temp;
    }

    /**
     * @param color The color of the students whose number is needed.
     * @return The number of students in the Cloud with specified color
     */
    public int getByColor(FactionColor color) {
        return studentsOnCloud.getByColor(color);
    }

    /**
     * @return The flag that indicates whether the game is for two or three player.
     */
    public boolean getIsFor2() {
        return isFor2;
    }

    /**
     * @return the students allowed on a Cloud in a 2 Player match.
     */
    public int getStudentsPerCloud2Players() {
        return studentsPerCloud2Players;
    }

    /**
     * @return the students allowed on a Cloud in a 3 Player match.
     */
    public int getStudentsPerCloud3Players() {
        return studentsPerCloud3Players;
    }

    /**
     * @return The number of students on the Cloud.
     */
    public int size() {
        return studentsOnCloud.size();
    }

    /**
     * @return the cloud id
     */
    public String getCloudId() {
        return cloudId;
    }

    /**
     * Wrap of {@link StudentsContainer#getStudentsAsString()} to be accessible without exposing the container object
     * @return a string representation of students on this cloud
     */
    public String getStudentsAsString() {
        return studentsOnCloud.getStudentsAsString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cloud cloud)) return false;
        return getCloudId().equals(cloud.getCloudId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCloudId());
    }
}