package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.exceptions.StudentSpaceException;
import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for Cloud class
 */
public class CloudTest
{
    /**
     * Tests the addition of students in case of a two-player game.
     */
    @Test
    @DisplayName("Tests the addition of students in case of a two-player game.")
    public void addStudents2Players(){
        Cloud c = new Cloud(true, 0);
        LimitedStudentsContainer studentsToAdd = new LimitedStudentsContainer(3);
        studentsToAdd.addStudents(1, FactionColor.RED);
        studentsToAdd.addStudents(1, FactionColor.GREEN);
        studentsToAdd.addStudents(1, FactionColor.BLUE);

        c.addStudents(studentsToAdd);
        assertEquals(3, c.size());
        assertThrows(StudentSpaceException.class, () -> studentsToAdd.addStudents(5, FactionColor.BLUE));
        assertThrows(IllegalArgumentException.class, () -> studentsToAdd.addStudents(1, null));
        assertEquals(3, c.size());
    }

    /**
     * Tests the addition of students in case of a three-player game.
     */
    @Test
    @DisplayName("Tests the addition of students in case of a three-player game.")
    public void addStudentsTest3Players(){
        Cloud c = new Cloud(false, 0);

        LimitedStudentsContainer studentsToAdd = new LimitedStudentsContainer(4);
        studentsToAdd.addStudents(1, FactionColor.RED);
        studentsToAdd.addStudents(1, FactionColor.GREEN);
        studentsToAdd.addStudents(1, FactionColor.BLUE);
        studentsToAdd.addStudents(1, FactionColor.RED);

        c.addStudents(studentsToAdd);
        assertEquals(4, c.size());
        assertThrows(StudentSpaceException.class, () -> {
            studentsToAdd.addStudents(2, FactionColor.GREEN);
            studentsToAdd.addStudents(2, FactionColor.RED);
        });
        assertThrows(IllegalArgumentException.class, () -> studentsToAdd.addStudents(1, null));
        assertEquals(4, c.size());
    }

    /**
     * Tests the removal of students in case of a two-player game.
     */
    @Test
    @DisplayName("Tests the removal of students in case of a two-player game.")
    public void removeStudents2Players(){
        Cloud c = new Cloud(true, 0);
        LimitedStudentsContainer studentsToAdd = new LimitedStudentsContainer(3);
        studentsToAdd.addStudents(1, FactionColor.RED);
        studentsToAdd.addStudents(1, FactionColor.GREEN);
        studentsToAdd.addStudents(1, FactionColor.BLUE);

        c.addStudents(studentsToAdd);
        assertEquals(3, c.size());

        c.removeStudents();
        assertEquals(0, c.size());
    }

    /**
     * Tests the removal of students in case of a three-player game.
     */
    @Test
    @DisplayName("Tests the removal of students in case of a three-player game.")
    public void removeStudentsTest3Players() {
        Cloud c = new Cloud(false, 0);

        LimitedStudentsContainer studentsToAdd = new LimitedStudentsContainer(4);
        studentsToAdd.addStudents(1, FactionColor.RED);
        studentsToAdd.addStudents(1, FactionColor.GREEN);
        studentsToAdd.addStudents(1, FactionColor.BLUE);
        studentsToAdd.addStudents(1, FactionColor.YELLOW);

        c.addStudents(studentsToAdd);
        assertEquals(4, c.size());

        c.removeStudents();
        assertEquals(0, c.size());

    }

    /**
     * Tests the addition and the removal of students in case of a two-player game.
     */
    @Test
    @DisplayName("Tests the addition and the removal of students in case of a two-player game.")
    public void mixedTest2Players() {
        Cloud c = new Cloud(false, 0);

        LimitedStudentsContainer studentsToAdd = new LimitedStudentsContainer(4);
        studentsToAdd.addStudents(1, FactionColor.RED);
        studentsToAdd.addStudents(2, FactionColor.GREEN);

        c.addStudents(studentsToAdd);
        assertEquals(3, c.size());

        c.removeStudents();
        assertEquals(0, c.size());

        studentsToAdd.addStudents(1, FactionColor.PINK);
        c.addStudents(studentsToAdd);
        assertEquals(4, c.size());

        c.removeStudents();
        assertEquals(0, c.size());
    }

    /**
     * Tests that the isFor2 parameter is equal for each element of the ArrayList
     */
    @Test
    @DisplayName("Tests that isFor2 parameter is equal for each element of the ArrayList")
    public void sameIsFor2() {
        ArrayList<Cloud> c = new ArrayList<>();
        c.add(new Cloud(true, 0));
        c.add(new Cloud(true, 1));
        c.add(new Cloud(true, 2));

        for(Cloud cloud : c) {
            assertTrue(cloud.getIsFor2());
        }
    }
}
