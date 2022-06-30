package it.polimi.ingsw.am37.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class BagTest
{
    /**
     * Tests the case where too many students are extracted.
     */
    @Test
    @DisplayName("Tests the case where too many students are extracted.")
    public void extractTooManyStudents() {
        Bag bag = new Bag();
        bag.extractStudents(121);
        assertEquals(0, bag.size());
        assertTrue(bag.isEmpty());
    }

    /**
     * Tests the case where an okay amount of students is extracted.
     */
    @Test
    @DisplayName("Tests the case where an okay amount of students is extracted.")
    public void extractOkayAmountOfStudents(){
        Bag bag = new Bag();
        bag.extractStudents(50);
        assertEquals(70, bag.size());
        assertFalse(bag.isEmpty());
    }

    /**
     * Tests the case where the exact amount of students is extracted.
     */
    @Test
    @DisplayName("Tests the case where the exact amount of students is extracted.")
    public void extractExactStudents(){
        Bag bag = new Bag();
        bag.extractStudents(120);
        assertEquals(0, bag.size());
        assertFalse(bag.isEmpty());
    }
}
