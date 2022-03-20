package it.polimi.ingsw.am37.Model.student_container;

import it.polimi.ingsw.am37.Model.FactionColor;

/**
 * Container for students tile, only adding students is allowed
 */
public class FixedUnlimitedStudentContainer extends StudentContainer {

    /**
     * Default constructor
     */
    public FixedUnlimitedStudentContainer() {
    }


    /**
     * Adding students to the container
     *
     * @param num   the number of students to add (must be >= 0)
     * @param color the colors of students to add (must not be null)
     * @throws IllegalArgumentException thrown when arguments are null or negative
     */
    @Override
    public void addStudents(int num, FactionColor color) throws IllegalArgumentException {
        if (num > 0) throw new IllegalArgumentException("Num must be an int >= 0 but is " + num);
        if (color == null) throw new IllegalArgumentException("color is null");
        student[color.getIndex()] += num;
    }
}