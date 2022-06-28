package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.StudentsContainer;
import it.polimi.ingsw.am37.model.student_container.UnlimitedStudentsContainer;

import java.util.Random;

/**
 * This class represents the Bag which contains the students that are not assigned to an Island, Cloud or Board.
 */
public class Bag {

    /**
     * It represents the number of students in the bag when the game starts.
     */
    private final static int startingStudentsPerColor = 24;

    /**
     * Keeps track if the bag is empty or not.
     */
    private boolean isEmpty;

    /**
     * Default constructor, it fills the Bag with 2 students from each Faction.
     */
    public Bag() {
        studentsAvailable = new UnlimitedStudentsContainer();
        isEmpty = false;
        for (FactionColor color : FactionColor.values())
            studentsAvailable.addStudents(startingStudentsPerColor, color);
    }

    /**
     * It represents the students in the Bag.
     */
    private final UnlimitedStudentsContainer studentsAvailable;

    /**
     * It extracts students from the Bag.
     *
     * @param num The number of students to extracts.
     * @return A LimitedStudentContainer that contains the students extracted by FactionColor.
     * @throws IllegalArgumentException If there are not enough students to extract.
     */
    public LimitedStudentsContainer extractStudents(int num) {
        if (studentsAvailable.size() < num) {
            num = studentsAvailable.size();
            isEmpty = true;
        }
        LimitedStudentsContainer studentsExtracted;
        int[] colorBound = new int[]{0, 0, 0, 0, 0};
        Random colorGenerator = new Random();
        FactionColor colorExtracted;
        for (int i = 0; i < num; i++) {
            colorExtracted = FactionColor.values()[colorGenerator.nextInt(FactionColor.values().length)];
            if (studentsAvailable.getByColor(colorExtracted) > colorBound[colorExtracted.getIndex()])
                colorBound[colorExtracted.getIndex()]++;
            else i--;
        }
        studentsExtracted = new LimitedStudentsContainer(colorBound);
        for (FactionColor color : FactionColor.values()) {
            studentsExtracted.addStudents(colorBound[color.getIndex()], color);
            studentsAvailable.removeStudents(colorBound[color.getIndex()], color);
        }
        return studentsExtracted;
    }

    /**
     * It returns if the bag is empty or not.
     *
     * @return true if the bag is empty, false otherwise.
     */
    public boolean isEmpty() {
        return isEmpty;
    }

    /**
     * @return the number of the students in the Bag.
     */
    public int size() {
        return studentsAvailable.size();
    }

    /**
     * Add students to the bag
     *
     * @param container the student to add to bag
     */
    public void addStudents(StudentsContainer container) {
        studentsAvailable.uniteContainers(container);
    }
}