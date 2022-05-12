package it.polimi.ingsw.am37.model.student_container;

import it.polimi.ingsw.am37.model.FactionColor;

import java.util.Arrays;

/**
 * Container for students tile, only adding students is allowed
 */
public class FixedUnlimitedStudentsContainer extends StudentsContainer {

    /**
     * Default constructor
     */
    public FixedUnlimitedStudentsContainer() {
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
        if (num < 0) throw new IllegalArgumentException("Num must be an int >= 0 but is " + num);
        if (color == null) throw new IllegalArgumentException("color is null");
        student[color.getIndex()] += num;
    }

    /**
     * Return a copy of this container
     *
     * @return a student container that is the exact copy of this
     */
    @Override
    public StudentsContainer copy() {
        FixedUnlimitedStudentsContainer container = new FixedUnlimitedStudentsContainer();
        container.student = Arrays.copyOf(this.student, this.student.length);
        return container;
    }
}