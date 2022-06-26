package it.polimi.ingsw.am37.model.student_container;

import it.polimi.ingsw.am37.model.FactionColor;
import it.polimi.ingsw.am37.model.exceptions.StudentSpaceException;

import java.util.Arrays;

/**
 * Container for students tile with limited amount set generally or by color and ability to remose students if needed
 */
public class LimitedStudentsContainer extends StudentsContainer {

    /**
     * The maximum amount of students in this container
     */
    int maxSize;
    /**
     * The maximum amount of students for each color in this container
     */
    int[] maxSizeForColor;


    /**
     * Create a student container and set its maximum amounts of students
     *
     * @param max the maximum amounts of student desired
     * @throws IllegalArgumentException throw when argument is negative
     */
    public LimitedStudentsContainer(int max) throws IllegalArgumentException {
        if (max >= 0) maxSize = max;
        else throw new IllegalArgumentException("max(" + max + " must be >=0 ");
    }

    /**
     * Create a student container and set its maximum amount of students by color
     *
     * @param maxByColor array of maximum amount for each color, all elements must be positive and at least one strictly
     *                   positive
     * @throws IllegalArgumentException thrown when parameter is badly formatted
     */
    public LimitedStudentsContainer(int[] maxByColor) throws IllegalArgumentException {
        maxSizeForColor = new int[5];
        maxSize = -1;
        if (Arrays.stream(maxByColor).parallel().allMatch(c -> c >= 0) && Arrays.stream(maxByColor)
                .anyMatch(c -> c > 0))
            maxSizeForColor = Arrays.copyOf(maxByColor, maxByColor.length);
        else
            throw new IllegalArgumentException("Parameter array" + Arrays.toString(maxByColor) + " is badly " +
                    "formatted: all elements must be positive and at least one bigger than 0");
    }

    /**
     * Add students checking with the maximum number set
     *
     * @param num   the number of students to add (must be >= 0)
     * @param color the colors of students to add (must not be null)
     * @throws IllegalArgumentException thrown when arguments are null or negative
     */
    @Override
    public void addStudents(int num, FactionColor color) throws IllegalArgumentException, StudentSpaceException {
        if (num < 0) throw new IllegalArgumentException("Num must be an int >= 0 but is " + num);
        if (color == null) throw new IllegalArgumentException("color is null");
        if (maxSize == -1 && num + student[color.getIndex()] > maxSizeForColor[color.getIndex()]) {
            throw new StudentSpaceException("Space error for color " + color + " (curr, num, limit): (" + student[color.getIndex()] + "," + num + "," + maxSizeForColor[color.getIndex()] + ");", true);
        } else if (maxSize != -1 && Arrays.stream(student).sum() + num > maxSize) {
            throw new StudentSpaceException("General space error (curr, num, limit): (" + Arrays.stream(student)
                    .sum() + "," + num + "," + maxSize + ");", true);
        } else {
            student[color.getIndex()] += num;
        }
    }

    /**
     * Remove the specified number of student of the specified colors
     *
     * @param num   the number of students to remove; must be strictly positive
     * @param color the color of students to remove
     */
    public void removeStudents(int num, FactionColor color) throws IllegalArgumentException, StudentSpaceException {
        if (num < 0) throw new IllegalArgumentException("num parameter must be strictly positive");
        if (color == null) throw new IllegalArgumentException("Colors can't be null");
        if (student[color.getIndex()] >= num) student[color.getIndex()] -= num;
        else {
            throw new StudentSpaceException("General space error (curr, num, limit): (" + Arrays.stream(student)
                    .sum() + "," + num + "," + student[color.getIndex()] + ");", false);
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
        LimitedStudentsContainer container = maxSize >= 0 ? new LimitedStudentsContainer(maxSize) : new LimitedStudentsContainer(maxSizeForColor);
        container.student = Arrays.copyOf(this.student, this.student.length);
        return container;
    }
}