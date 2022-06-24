package it.polimi.ingsw.am37.model.student_container;

import it.polimi.ingsw.am37.model.FactionColor;
import it.polimi.ingsw.am37.model.exceptions.StudentSpaceException;

/**
 * Abstract class to manage students tile in the model.
 */
public abstract class StudentsContainer {

    /**
     * General array of 5 integers, for the index of each student colors is used ad enumeration
     *
     * @see FactionColor
     */
    protected int[] student;

    public StudentsContainer() {
        student = new int[]{0, 0, 0, 0, 0};
    }

    /**
     * ABSTRACT - Add students to the collection by color
     */
    abstract public void addStudents(int num, FactionColor color);

    /**
     * Return the number of students with specified color in this
     *
     * @param color the color needed
     * @return The number of students in this with specified color, as <code>int</code>
     */
    public int getByColor(FactionColor color) throws IllegalArgumentException {
        if (color == null) throw new IllegalArgumentException("color must not be null");
        return student[color.getIndex()];
    }

    /**
     * Add all the students of the provided container to this
     *
     * @param other the source container to merge in this
     */
    public void uniteContainers(StudentsContainer other) throws IllegalArgumentException, StudentSpaceException {
        if (other == null) throw new IllegalArgumentException("other container must not be null");
        for (FactionColor color :
                FactionColor.values()) {
            try {
                addStudents(other.getByColor(color), color);
            } catch (StudentSpaceException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Return the number of all students in this
     *
     * @return the number of students in the containers
     */
    public int size() {
        int sum = 0;
        for (FactionColor color :
                FactionColor.values()) {
            sum += getByColor(color);
        }
        return sum;

    }

    /**
     * Return a copy of this container
     *
     * @return a student container that is the exact copy of this
     */
    public abstract StudentsContainer copy();

    /**
     * Utility method to check if an external container is a subset or equal of this container; more formally, return
     * {@code false} if the provided container is bigger or has more students of at leas one color, otherwise return
     * {@code true}
     *
     * @param container the student container to check if is a subset or equal
     * @return true of false if the provided container is a subset or equal of this container
     */
    public boolean contains(StudentsContainer container) {
        if (this.size() < container.size()) return false;
        for (FactionColor color :
                FactionColor.values()) {
            if (this.student[color.getIndex()] < container.student[color.getIndex()]) return false;
        }
        return true;
    }

    /**
     * This method prepare and return a string with a visual representation of the this. {@link FactionColor} are
     * identified by their first letter of color name in english
     *
     * @return a string with simple representation of the students inside this
     */
    public String getStudentsAsString() {
        StringBuilder stringBuilder = new StringBuilder("[");
        String[] studentsString = new String[FactionColor.values().length];
        for (FactionColor color :
                FactionColor.values()) {
            studentsString[color.getIndex()] =
                    "@|" + color.color + " " + student[color.getIndex()] + color.name().charAt(0) + "|@";
        }
        stringBuilder.append(String.join(", ", studentsString));
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}