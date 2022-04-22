package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.StudentsContainer;

import java.util.UUID;

/**
 * This class represent the Clouds in the game.
 */
public class Cloud {

    /**
     * They represent the number of students that can be placed on a Cloud based on the number of Players.
     */
    private final static int studentsPerCloud2Players = 3, studentsPerCloud3Players = 4;

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
     * Default constructor
     */
    public Cloud(boolean isFor2) {
        this.isFor2 = isFor2;
        if (this.isFor2) studentsOnCloud = new LimitedStudentsContainer(studentsPerCloud2Players);
        else studentsOnCloud = new LimitedStudentsContainer(studentsPerCloud3Players);
        cloudId = UUID.randomUUID().toString();
    }

    /**
     * @param students Students used to fill the Cloud.
     */
    public void addStudents(StudentsContainer students) {
        studentsOnCloud.uniteContainers(students);
    }

    /**
     * @return The students on the Cloud.
     */
    public LimitedStudentsContainer removeStudents() {
        LimitedStudentsContainer temp = studentsOnCloud;
        studentsOnCloud = new LimitedStudentsContainer(isFor2 ? studentsPerCloud2Players : studentsPerCloud3Players);
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
}