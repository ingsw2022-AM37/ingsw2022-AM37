package it.polimi.ingsw.am37.model.student_container;

import it.polimi.ingsw.am37.model.FactionColor;
import it.polimi.ingsw.am37.model.exceptions.StudentSpaceException;

import java.util.Arrays;

/**
 * Container for students tile with the ability to remove students if needed
 */
public class UnlimitedStudentsContainer extends StudentsContainer {

    /**
     * Default constructor
     */
    public UnlimitedStudentsContainer() {
        super();
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
     * Remove the specified number of student of the specified colors
     *
     * @param num   the number of students to remove; must be strictly positive
     * @param color the color of students to remove
     */
    public void removeStudents(int num, FactionColor color) throws IllegalArgumentException, StudentSpaceException {
        if (num < 0) throw new IllegalArgumentException("num parameter must be strictly positive");
        if (color == null) throw new IllegalArgumentException("Colors couldn't be null");
        if (student[color.getIndex()] >= num) student[color.getIndex()] -= num;
        else {
            throw new StudentSpaceException("General space error (curr, num, limit): (" + Arrays.stream(student).sum() + "," + num + "," + student[color.getIndex()] + ");", false);
        }
    }

    public void removeContainer(StudentsContainer container) throws StudentSpaceException {
        for (FactionColor color :
                FactionColor.values()) {
            if (this.getByColor(color) < container.getByColor(color)) throw new StudentSpaceException(false);
            this.removeStudents(container.getByColor(color), color);
        }
    }

    /**
     * Return a copy of this container
     *
     * @return a student container that is the exact copy of this
     */
    @Override
    public StudentsContainer copy() {
        UnlimitedStudentsContainer container = new UnlimitedStudentsContainer();
        container.student = Arrays.copyOf(this.student, this.student.length);
        return container;
    }
}