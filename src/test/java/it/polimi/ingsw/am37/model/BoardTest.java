package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    static Board board;
    static LimitedStudentsContainer container;

    @BeforeAll
    static void setUp() {
        container = new LimitedStudentsContainer(7);
        container.addStudents(2, FactionColor.BLUE);
        container.addStudents(3, FactionColor.GREEN);
    }

    /**
     * Test base constructor
     */
    @Test
    @DisplayName("Test base constructor")
    void testConstructor() {
        board = new Board(2, TowerColor.BLACK, false, new Player());
        assertNotNull(board);
        assertNotNull(board.getEntrance());
        assertNotNull(board.getTowers());
        assertNotNull(board.getDiningRoom());
        assertNotNull(board.getPlayer());
        assertEquals(0, board.getDiningRoom().size());
        assertEquals(0, board.getEntrance().size());
        assertEquals(8, board.getTowers().getCurrentSize());
        assertArrayEquals(new boolean[]{false, false, false, false, false}, board.getProfTable());
        // Enable accessing of private field
        try {
            Field coinsArray = Board.class.getDeclaredField("coinsArray");
            coinsArray.setAccessible(true);
            assertNull(coinsArray.get(board));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test coins logic initialization
     */
    @Test
    @DisplayName("Test coins logic initialization")
    void testConstructorWithCoinLogic() {
        board = new Board(2, TowerColor.BLACK, true, new Player());
        // Enable accessing of private field
        try {
            Field privateField = Board.class.getDeclaredField("coinsArray");
            privateField.setAccessible(true);
            assertNotNull(privateField.get(board));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test constructor against all possibility of num of player
     */
    @Test
    @DisplayName("Test constructor against all possibility of num of player")
    void testConstructorWithAllPlayers() {
        board = new Board(2, TowerColor.BLACK, false, new Player());
        assertEquals(8, board.getTowers().getCurrentSize());
        board = new Board(3, TowerColor.BLACK, false, new Player());
        assertEquals(6, board.getTowers().getCurrentSize());
        assertThrows(IllegalArgumentException.class, () -> new Board(5, TowerColor.BLACK, false, new Player()));
    }

    /**
     * Test advanced constructor with provided container
     */
    @Test
    @DisplayName("Test advanced constructor with provided container")
    void testAdvancedConstructor() {
        board = new Board(2, TowerColor.BLACK, false, container, new Player());
        assertTrue(board.getEntrance().size() > 0);
    }
}