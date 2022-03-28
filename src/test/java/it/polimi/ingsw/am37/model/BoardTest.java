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

    @Test
    @DisplayName("Test coins logic initialization")
    void testConstructorWithCoinLogic() {
        board = new Board(2, TowerColor.BLACK, true, new Player());
        // Enable accessing of private field
        try {
            Field privateField = Board.class.getDeclaredField("coinsArray");
            privateField.setAccessible(true);
            assertNotNull(privateField.get(board));
            privateField = Board.class.getDeclaredField("coinsEnabled");
            privateField.setAccessible(true);
            assertTrue((boolean) privateField.get(board));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Test constructor against all possibility of num of player")
    void testConstructorWithAllPlayers() {
        board = new Board(2, TowerColor.BLACK, false, new Player());
        assertEquals(8, board.getTowers().getCurrentSize());
        board = new Board(3, TowerColor.BLACK, false, new Player());
        assertEquals(6, board.getTowers().getCurrentSize());
        board = new Board(4, TowerColor.BLACK, false, new Player());
        assertEquals(0, board.getTowers().getCurrentSize());
        assertThrows(IllegalArgumentException.class, () -> new Board(5, TowerColor.BLACK, false, new Player()));
    }

    @Test
    @DisplayName("Test advanced constructor with provided container")
    void testAdvancedConstructor() {
        board = new Board(2, TowerColor.BLACK, false, container, new Player());
        assertTrue(board.getEntrance().size() > 0);
    }

    @Test
    @DisplayName("Test CalculateCoin when no coins should be provided")
    void testCalculateCoinNoCoin() {
        Player player = new Player();
        board = new Board(2, TowerColor.BLACK, true, player);
        LimitedStudentsContainer container = new LimitedStudentsContainer(2);
        container.addStudents(2, FactionColor.BLUE);
        board.addStudentToDining(container);
        Field privateField;
        try {
            privateField = Player.class.getDeclaredField("numberOfCoins");
            privateField.setAccessible(true);
            assertEquals(0, privateField.get(player));
            privateField = Board.class.getDeclaredField("coinsArray");
            privateField.setAccessible(true);
            assertArrayEquals(new boolean[]{true, true, true}, ((boolean[][]) privateField.get(board))[FactionColor.BLUE.getIndex()]);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Test
    @DisplayName("Test CalculateCoin when just one coin should be provided")
    void testCalculateCoinWithCoin() {
        Player player = new Player();
        board = new Board(2, TowerColor.BLACK, true, player);
        LimitedStudentsContainer container = new LimitedStudentsContainer(6);
        container.addStudents(5, FactionColor.BLUE);
        board.addStudentToDining(container);
        Field privateField;
        try {
            privateField = Player.class.getDeclaredField("numberOfCoins");
            privateField.setAccessible(true);
            assertEquals(1, privateField.get(player));
            privateField = Board.class.getDeclaredField("coinsArray");
            privateField.setAccessible(true);
            assertArrayEquals(new boolean[]{false, true, true}, ((boolean[][]) privateField.get(board))[FactionColor.BLUE.getIndex()]);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Test CalculateCoin when there are exactly three students")
    void testCalculateCoin3Students() {
        Player player = new Player();
        board = new Board(2, TowerColor.BLACK, true, player);
        LimitedStudentsContainer container = new LimitedStudentsContainer(3);
        container.addStudents(3, FactionColor.BLUE);
        board.addStudentToDining(container);
        Field privateField;
        try {
            privateField = Player.class.getDeclaredField("numberOfCoins");
            privateField.setAccessible(true);
            assertEquals(1, privateField.get(player));
            privateField = Board.class.getDeclaredField("coinsArray");
            privateField.setAccessible(true);
            assertArrayEquals(new boolean[]{false, true, true}, ((boolean[][]) privateField.get(board))[FactionColor.BLUE.getIndex()]);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Test CalculateCoin when students are removed and then re-added")
    void testCalculateCoinsAfterRemoving() {
        Player player = new Player();
        board = new Board(2, TowerColor.BLACK, true, player);
        LimitedStudentsContainer container = new LimitedStudentsContainer(3);
        container.addStudents(3, FactionColor.BLUE);
        board.addStudentToDining(container);
        board.removeStudentFromDining(2, FactionColor.BLUE);
        Field privateField;
        try {
            privateField = Player.class.getDeclaredField("numberOfCoins");
            privateField.setAccessible(true);
            assertEquals(1, privateField.get(player));
            privateField = Board.class.getDeclaredField("coinsArray");
            privateField.setAccessible(true);
            assertArrayEquals(new boolean[]{true, true, true}, ((boolean[][]) privateField.get(board))[FactionColor.BLUE.getIndex()]);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        board.addStudentToDining(container);
        try {
            privateField = Player.class.getDeclaredField("numberOfCoins");
            privateField.setAccessible(true);
            assertEquals(2, privateField.get(player));
            privateField = Board.class.getDeclaredField("coinsArray");
            privateField.setAccessible(true);
            assertArrayEquals(new boolean[]{false, true, true}, ((boolean[][]) privateField.get(board))[FactionColor.BLUE.getIndex()]);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Test remove students from dining")
    void testRemoveStudentFromDining() {
        board = new Board(2, TowerColor.BLACK, false, new Player());
        LimitedStudentsContainer container = new LimitedStudentsContainer(5);
        container.addStudents(5, FactionColor.GREEN);
        board.addStudentToDining(container);
        board.removeStudentFromDining(2, FactionColor.GREEN);
        assertEquals(3, board.getDiningRoom().size());
        assertEquals(3, board.getDiningRoom().getByColor(FactionColor.GREEN));
    }

    @Test
    @DisplayName("Test remove from dining more students than are presents ")
    void testRemoveStudentsFromDiningWhenTooFew() {
        board = new Board(2, TowerColor.BLACK, false, new Player());
        LimitedStudentsContainer container = new LimitedStudentsContainer(5);
        container.addStudents(1, FactionColor.GREEN);
        board.addStudentToDining(container);
        assertDoesNotThrow(() -> board.removeStudentFromDining(2, FactionColor.GREEN));
        assertEquals(0, board.getDiningRoom().size());
    }
}