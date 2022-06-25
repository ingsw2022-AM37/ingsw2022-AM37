package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.exceptions.NoProfChangeException;
import it.polimi.ingsw.am37.model.exceptions.StudentSpaceException;
import it.polimi.ingsw.am37.model.student_container.FixedUnlimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class TurnManagerTest {

    /**
     * Tests adding students to dining rooms and simulate a match
     */
    @Test
    @DisplayName("Tests adding students to dining rooms and simulate a match")
    void addStudentsToDining() {

        TurnManager turnManager = new TurnManager(false, 3);

        for (int i = 0; i < 3; i++)
            turnManager.getPlayers().add(new Player());

        turnManager.getPlayers().get(0).setBoard(new Board(3, TowerColor.BLACK, false, turnManager.getPlayers().get(0)));
        turnManager.getPlayers().get(1).setBoard(new Board(3, TowerColor.WHITE, false, turnManager.getPlayers().get(1)));
        turnManager.getPlayers().get(2).setBoard(new Board(3, TowerColor.GRAY, false, turnManager.getPlayers().get(2)));

        turnManager.setCurrentPlayer(turnManager.getPlayers().get(0));

        LimitedStudentsContainer temp = new LimitedStudentsContainer(7);
        temp.addStudents(2, FactionColor.GREEN);
        temp.addStudents(3, FactionColor.BLUE);

        turnManager.addStudentsToDining(temp);

        assertTrue(turnManager.getPlayers().get(0).getBoard().getProfTable()[FactionColor.GREEN.getIndex()]);
        assertTrue(turnManager.getPlayers().get(0).getBoard().getProfTable()[FactionColor.BLUE.getIndex()]);
        for (FactionColor color : FactionColor.values()) {
            assertFalse(turnManager.getPlayers().get(1).getBoard().getProfTable()[color.getIndex()]);
            assertFalse(turnManager.getPlayers().get(2).getBoard().getProfTable()[color.getIndex()]);
            if (color != FactionColor.GREEN && color != FactionColor.BLUE)
                assertFalse(turnManager.getPlayers().get(0).getBoard().getProfTable()[color.getIndex()]);
        }

        assertEquals(2, turnManager.getPlayers().get(0).getBoard().getDiningRoom().getByColor(FactionColor.GREEN));
        assertEquals(3, turnManager.getPlayers().get(0).getBoard().getDiningRoom().getByColor(FactionColor.BLUE));
        for (FactionColor color : FactionColor.values()) {
            assertEquals(0, turnManager.getPlayers().get(1).getBoard().getDiningRoom().getByColor(color));
            assertEquals(0, turnManager.getPlayers().get(2).getBoard().getDiningRoom().getByColor(color));
            if (color != FactionColor.GREEN && color != FactionColor.BLUE)
                assertEquals(0, turnManager.getPlayers().get(0).getBoard().getDiningRoom().getByColor(color));
        }

        turnManager.setCurrentPlayer(turnManager.getPlayers().get(1));
        temp = new LimitedStudentsContainer(20);
        temp.addStudents(3, FactionColor.GREEN);
        temp.addStudents(6, FactionColor.YELLOW);
        temp.addStudents(3, FactionColor.PINK);
        temp.addStudents(2, FactionColor.BLUE);
        turnManager.addStudentsToDining(temp);

        assertTrue(turnManager.getPlayers().get(0).getBoard().getProfTable()[FactionColor.BLUE.getIndex()]);
        assertEquals(2, turnManager.getPlayers().get(0).getBoard().getDiningRoom().getByColor(FactionColor.GREEN));
        assertEquals(3, turnManager.getPlayers().get(0).getBoard().getDiningRoom().getByColor(FactionColor.BLUE));
        for (FactionColor color : FactionColor.values())
            if (color != FactionColor.BLUE && color != FactionColor.GREEN)
                assertEquals(0, turnManager.getPlayers().get(0).getBoard().getDiningRoom().getByColor(color));
        for (FactionColor color : FactionColor.values())
            if (color != FactionColor.BLUE)
                assertFalse(turnManager.getPlayers().get(0).getBoard().getProfTable()[color.getIndex()]);

        assertEquals(3, turnManager.getPlayers().get(1).getBoard().getDiningRoom().getByColor(FactionColor.GREEN));
        assertEquals(6, turnManager.getPlayers().get(1).getBoard().getDiningRoom().getByColor(FactionColor.YELLOW));
        assertEquals(3, turnManager.getPlayers().get(1).getBoard().getDiningRoom().getByColor(FactionColor.PINK));
        assertEquals(2, turnManager.getPlayers().get(1).getBoard().getDiningRoom().getByColor(FactionColor.BLUE));
        assertEquals(0, turnManager.getPlayers().get(1).getBoard().getDiningRoom().getByColor(FactionColor.RED));
        assertTrue(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.GREEN.getIndex()]);
        assertTrue(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.YELLOW.getIndex()]);
        assertTrue(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.PINK.getIndex()]);
        assertFalse(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.BLUE.getIndex()]);
        assertFalse(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.RED.getIndex()]);

        for (FactionColor color : FactionColor.values()) {
            assertFalse(turnManager.getPlayers().get(2).getBoard().getProfTable()[color.getIndex()]);
            assertEquals(0, turnManager.getPlayers().get(2).getBoard().getDiningRoom().getByColor(color));
        }

        turnManager.setCurrentPlayer(turnManager.getPlayers().get(2));
        temp = new LimitedStudentsContainer(20);

        temp.addStudents(4, FactionColor.BLUE);
        temp.addStudents(1, FactionColor.RED);
        temp.addStudents(2, FactionColor.GREEN);
        temp.addStudents(3, FactionColor.PINK);

        turnManager.addStudentsToDining(temp);

        assertEquals(2, turnManager.getPlayers().get(0).getBoard().getDiningRoom().getByColor(FactionColor.GREEN));
        assertEquals(3, turnManager.getPlayers().get(0).getBoard().getDiningRoom().getByColor(FactionColor.BLUE));
        for (FactionColor color : FactionColor.values())
            if (color != FactionColor.BLUE && color != FactionColor.GREEN)
                assertEquals(0, turnManager.getPlayers().get(0).getBoard().getDiningRoom().getByColor(color));
        for (FactionColor color : FactionColor.values())
            assertFalse(turnManager.getPlayers().get(0).getBoard().getProfTable()[color.getIndex()]);


        assertEquals(3, turnManager.getPlayers().get(1).getBoard().getDiningRoom().getByColor(FactionColor.GREEN));
        assertEquals(6, turnManager.getPlayers().get(1).getBoard().getDiningRoom().getByColor(FactionColor.YELLOW));
        assertEquals(3, turnManager.getPlayers().get(1).getBoard().getDiningRoom().getByColor(FactionColor.PINK));
        assertEquals(2, turnManager.getPlayers().get(1).getBoard().getDiningRoom().getByColor(FactionColor.BLUE));
        assertEquals(0, turnManager.getPlayers().get(1).getBoard().getDiningRoom().getByColor(FactionColor.RED));
        assertTrue(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.GREEN.getIndex()]);
        assertTrue(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.YELLOW.getIndex()]);
        assertTrue(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.PINK.getIndex()]);
        assertFalse(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.BLUE.getIndex()]);
        assertFalse(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.RED.getIndex()]);


        assertEquals(2, turnManager.getPlayers().get(2).getBoard().getDiningRoom().getByColor(FactionColor.GREEN));
        assertEquals(0, turnManager.getPlayers().get(2).getBoard().getDiningRoom().getByColor(FactionColor.YELLOW));
        assertEquals(3, turnManager.getPlayers().get(2).getBoard().getDiningRoom().getByColor(FactionColor.PINK));
        assertEquals(4, turnManager.getPlayers().get(2).getBoard().getDiningRoom().getByColor(FactionColor.BLUE));
        assertEquals(1, turnManager.getPlayers().get(2).getBoard().getDiningRoom().getByColor(FactionColor.RED));
        assertFalse(turnManager.getPlayers().get(2).getBoard().getProfTable()[FactionColor.GREEN.getIndex()]);
        assertFalse(turnManager.getPlayers().get(2).getBoard().getProfTable()[FactionColor.YELLOW.getIndex()]);
        assertFalse(turnManager.getPlayers().get(2).getBoard().getProfTable()[FactionColor.PINK.getIndex()]);
        assertTrue(turnManager.getPlayers().get(2).getBoard().getProfTable()[FactionColor.BLUE.getIndex()]);
        assertTrue(turnManager.getPlayers().get(2).getBoard().getProfTable()[FactionColor.RED.getIndex()]);
    }

    /**
     * Tests removing students from dining rooms, simulate a match and tests draw case
     */
    @Test
    @DisplayName("Tests removing students from dining rooms, simulate a match and tests draw case")
    void removeStudentsFromDining() {


        TurnManager turnManager = new TurnManager(false, 3);

        for (int i = 0; i < 3; i++)
            turnManager.getPlayers().add(new Player());

        turnManager.getPlayers().get(0).setBoard(new Board(3, TowerColor.BLACK, false, turnManager.getPlayers().get(0)));
        turnManager.getPlayers().get(1).setBoard(new Board(3, TowerColor.WHITE, false, turnManager.getPlayers().get(1)));
        turnManager.getPlayers().get(2).setBoard(new Board(3, TowerColor.GRAY, false, turnManager.getPlayers().get(2)));

        turnManager.setCurrentPlayer(turnManager.getPlayers().get(0));
        LimitedStudentsContainer temp = new LimitedStudentsContainer(7);
        temp.addStudents(3, FactionColor.RED);
        temp.addStudents(4, FactionColor.GREEN);

        turnManager.addStudentsToDining(temp);

        turnManager.setCurrentPlayer(turnManager.getPlayers().get(1));
        temp = new LimitedStudentsContainer(11);
        temp.addStudents(2, FactionColor.PINK);
        temp.addStudents(5, FactionColor.GREEN);
        temp.addStudents(3, FactionColor.RED);
        temp.addStudents(1, FactionColor.BLUE);

        turnManager.addStudentsToDining(temp);

        turnManager.setCurrentPlayer(turnManager.getPlayers().get(2));
        temp = new LimitedStudentsContainer(16);
        temp.addStudents(2, FactionColor.PINK);
        temp.addStudents(4, FactionColor.GREEN);
        temp.addStudents(5, FactionColor.BLUE);
        temp.addStudents(3, FactionColor.RED);


        turnManager.addStudentsToDining(temp);

        temp = new LimitedStudentsContainer(10);
        temp.addStudents(5, FactionColor.BLUE);
        temp.addStudents(2, FactionColor.GREEN);

        turnManager.removeStudentsFromDining(temp);

        assertTrue(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.BLUE.getIndex()]);
        assertFalse(turnManager.getPlayers().get(2).getBoard().getProfTable()[FactionColor.BLUE.getIndex()]);
        assertFalse(turnManager.getPlayers().get(0).getBoard().getProfTable()[FactionColor.BLUE.getIndex()]);
        assertFalse(turnManager.getPlayers().get(2).getBoard().getProfTable()[FactionColor.GREEN.getIndex()]);
        assertTrue(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.GREEN.getIndex()]);

        assertEquals(0, turnManager.getPlayers().get(2).getBoard().getDiningRoom().getByColor(FactionColor.BLUE));
        assertEquals(1, turnManager.getPlayers().get(1).getBoard().getDiningRoom().getByColor(FactionColor.BLUE));

        turnManager.setCurrentPlayer(turnManager.getPlayers().get(1));
        temp = new LimitedStudentsContainer(2);
        temp.addStudents(1, FactionColor.BLUE);
        temp.addStudents(1, FactionColor.GREEN);
        turnManager.removeStudentsFromDining(temp);
        assertEquals(0, turnManager.getPlayers().get(1).getBoard().getDiningRoom().getByColor(FactionColor.BLUE));
        assertTrue(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.BLUE.getIndex()]);
        assertTrue(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.GREEN.getIndex()]);
        assertEquals(4, turnManager.getPlayers().get(1).getBoard().getDiningRoom().getByColor(FactionColor.GREEN));

        turnManager.setCurrentPlayer(turnManager.getPlayers().get(0));
        temp = new LimitedStudentsContainer(1);
        temp.addStudents(1, FactionColor.GREEN);
        turnManager.addStudentsToDining(temp);
        assertTrue(turnManager.getPlayers().get(0).getBoard().getProfTable()[FactionColor.GREEN.getIndex()]);

        temp = new LimitedStudentsContainer(3);
        temp.addStudents(3, FactionColor.RED);

        try {
            turnManager.removeStudentsFromDining(temp);

        } catch (NoProfChangeException e) {

            assertTrue(turnManager.getPlayers().get(0).getBoard().getProfTable()[FactionColor.RED.getIndex()]);
            assertFalse(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.RED.getIndex()]);
            assertFalse(turnManager.getPlayers().get(2).getBoard().getProfTable()[FactionColor.RED.getIndex()]);
            assertEquals(0, turnManager.getPlayers().get(0).getBoard().getDiningRoom().getByColor(FactionColor.RED));
            assertEquals(3, turnManager.getPlayers().get(1).getBoard().getDiningRoom().getByColor(FactionColor.RED));
            assertEquals(3, turnManager.getPlayers().get(2).getBoard().getDiningRoom().getByColor(FactionColor.RED));
        }
    }

    /**
     * Tests initialization of players, boards and other parameters
     */
    @Test
    @DisplayName("Tests initialization of players, boards and other parameters")
    void setUp() {

        TurnManager turnManager = new TurnManager(true, 3);
        turnManager.setUp(new Bag());

        int i = 0;
        for (Player player : turnManager.getPlayers()) {
            assertEquals(9, player.getBoard().getEntrance().size());
            assertEquals(6, player.getBoard().getTowers().getCurrentSize());

            i++;
        }

        assertEquals(TowerColor.WHITE, turnManager.getPlayers().get(0).getBoard().getTowers().getCurrentTower());
        assertEquals(TowerColor.BLACK, turnManager.getPlayers().get(1).getBoard().getTowers().getCurrentTower());
        assertEquals(TowerColor.GRAY, turnManager.getPlayers().get(2).getBoard().getTowers().getCurrentTower());

        turnManager = new TurnManager(true, 2);
        turnManager.setUp(new Bag());

        i = 0;
        for (Player player : turnManager.getPlayers()) {
            assertEquals(7, player.getBoard().getEntrance().size());
            assertEquals(8, player.getBoard().getTowers().getCurrentSize());

            i++;
        }

        assertEquals(TowerColor.WHITE, turnManager.getPlayers().get(0).getBoard().getTowers().getCurrentTower());
        assertEquals(TowerColor.BLACK, turnManager.getPlayers().get(1).getBoard().getTowers().getCurrentTower());

    }

    /**
     * Tests removing students from cloud and giving them to a board's entrance
     */
    @Test
    @DisplayName("Tests removing students from cloud and giving them to a board's entrance")
    void studentCloudToEntrance() {

        Cloud cloud = new Cloud(false, 0);
        LimitedStudentsContainer temp = new LimitedStudentsContainer(4);
        temp.addStudents(1, FactionColor.GREEN);
        temp.addStudents(2, FactionColor.RED);
        temp.addStudents(1, FactionColor.PINK);

        LimitedStudentsContainer temp2 = new LimitedStudentsContainer(2);
        temp2.addStudents(1, FactionColor.BLUE);

        cloud.addStudents(temp);

        //assertThrows(StudentSpaceException.class, ()->cloud.addStudents(temp2));

        TurnManager turnManager = new TurnManager(false, 2);

        turnManager.getPlayers().add(new Player());
        turnManager.getPlayers().get(0).setBoard(new Board(2, TowerColor.BLACK, false, turnManager.getPlayers().get(0)));
        turnManager.setCurrentPlayer(turnManager.getPlayers().get(0));

        turnManager.studentCloudToEntrance(cloud);

        for (FactionColor color : FactionColor.values()) {
            assertEquals(0, cloud.getByColor(color));
            assertEquals(temp.getByColor(color), turnManager.getPlayers().get(0).getBoard().getEntrance().getByColor(color));
        }
    }

    /**
     * Tests removing students from entrance and giving them to island
     */
    @Test
    @DisplayName("Tests removing students from entrance and giving them to island")
    void studentsEntranceToIsland() {

        TurnManager turnManager = new TurnManager(false, 2);
        Island island = new Island(new FixedUnlimitedStudentsContainer(), 1);

        turnManager.getPlayers().add(new Player());
        turnManager.getPlayers().add(new Player());
        turnManager.getPlayers().get(0).setBoard(new Board(2, TowerColor.BLACK, false, turnManager.getPlayers().get(0)));
        turnManager.getPlayers().get(1).setBoard(new Board(2, TowerColor.GRAY, false, turnManager.getPlayers().get(1)));

        turnManager.setCurrentPlayer(turnManager.getPlayers().get(0));

        LimitedStudentsContainer temp = new LimitedStudentsContainer(10);
        temp.addStudents(2, FactionColor.GREEN);
        temp.addStudents(1, FactionColor.PINK);
        temp.addStudents(4, FactionColor.BLUE);

        turnManager.getPlayers().get(0).getBoard().addStudentsToEntrance(temp);

        temp = new LimitedStudentsContainer(10);
        temp.addStudents(1, FactionColor.GREEN);
        temp.addStudents(2, FactionColor.BLUE);
        temp.addStudents(0, FactionColor.PINK);

        LimitedStudentsContainer temp2 = new LimitedStudentsContainer(2);
        temp2.addStudents(2, FactionColor.GREEN);
        LimitedStudentsContainer finalTemp = temp;
        assertThrows(StudentSpaceException.class, () -> finalTemp.removeContainer(temp2));

        turnManager.studentsEntranceToIsland(island, temp);

        assertEquals(2, island.getByColor(FactionColor.BLUE));
        assertEquals(1, island.getByColor(FactionColor.GREEN));
        for (FactionColor color : FactionColor.values())
            if (color != FactionColor.GREEN && color != FactionColor.BLUE)
                assertEquals(0, island.getByColor(color));

        assertEquals(2, turnManager.getPlayers().get(0).getBoard().getEntrance().getByColor(FactionColor.BLUE));
        assertEquals(1, turnManager.getPlayers().get(0).getBoard().getEntrance().getByColor(FactionColor.GREEN));
        assertEquals(1, turnManager.getPlayers().get(0).getBoard().getEntrance().getByColor(FactionColor.PINK));
        for (FactionColor color : FactionColor.values())
            if (color != FactionColor.GREEN && color != FactionColor.PINK && color != FactionColor.BLUE)
                assertEquals(0, turnManager.getPlayers().get(0).getBoard().getEntrance().getByColor(color));
    }

    /**
     * Tests adding students to dining with taking control of professors with draw
     */
    @Test
    @DisplayName("Tests adding students to dining with taking control of professors with draw")
    public void addStudentsToDiningWithDraw() {

        TurnManager turnManager = new TurnManager(false, 3);
        Bag bag = new Bag();
        LimitedStudentsContainer temp = new LimitedStudentsContainer(10);
        temp.addStudents(2, FactionColor.RED);
        temp.addStudents(3, FactionColor.GREEN);
        temp.addStudents(2, FactionColor.PINK);

        turnManager.setUp(bag);
        turnManager.setCurrentPlayer(turnManager.getPlayers().get(0));
        turnManager.addStudentsToDining(temp);

        assertFalse(turnManager.getPlayers().get(0).getBoard().getProfTable()[FactionColor.YELLOW.getIndex()]);
        assertFalse(turnManager.getPlayers().get(0).getBoard().getProfTable()[FactionColor.BLUE.getIndex()]);
        assertTrue(turnManager.getPlayers().get(0).getBoard().getProfTable()[FactionColor.GREEN.getIndex()]);
        assertTrue(turnManager.getPlayers().get(0).getBoard().getProfTable()[FactionColor.PINK.getIndex()]);
        assertTrue(turnManager.getPlayers().get(0).getBoard().getProfTable()[FactionColor.RED.getIndex()]);
        for (FactionColor color : FactionColor.values()) {
            assertFalse(turnManager.getPlayers().get(1).getBoard().getProfTable()[color.getIndex()]);
            assertFalse(turnManager.getPlayers().get(2).getBoard().getProfTable()[color.getIndex()]);
        }

        turnManager.setCurrentPlayer(turnManager.getPlayers().get(1));
        temp = new LimitedStudentsContainer(10);
        temp.addStudents(2, FactionColor.PINK);
        temp.addStudents(3, FactionColor.RED);
        temp.addStudents(2, FactionColor.GREEN);
        turnManager.addStudentsToDining(temp);

        assertFalse(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.PINK.getIndex()]);
        assertTrue(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.RED.getIndex()]);

        turnManager.setProfWithDraw();

        for (FactionColor color : FactionColor.values())
            turnManager.checkProfessors(color);

        assertTrue(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.PINK.getIndex()]);
        assertFalse(turnManager.getPlayers().get(0).getBoard().getProfTable()[FactionColor.PINK.getIndex()]);

        assertEquals(2, turnManager.getPlayers().get(0).getBoard().getDiningRoom().getByColor(FactionColor.PINK));
        assertEquals(2, turnManager.getPlayers().get(1).getBoard().getDiningRoom().getByColor(FactionColor.PINK));

        assertTrue(turnManager.getPlayers().get(0).getBoard().getProfTable()[FactionColor.GREEN.getIndex()]);
        assertFalse(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.GREEN.getIndex()]);

        turnManager.resetFlags();

        assertTrue(turnManager.getPlayers().get(0).getBoard().getProfTable()[FactionColor.PINK.getIndex()]);
        assertFalse(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.PINK.getIndex()]);

        temp = new LimitedStudentsContainer(10);
        turnManager.setCurrentPlayer(turnManager.getPlayers().get(2));

        temp.addStudents(2, FactionColor.PINK);
        temp.addStudents(3, FactionColor.GREEN);

        turnManager.setProfWithDraw();

        turnManager.addStudentsToDining(temp);

        assertTrue(turnManager.getPlayers().get(2).getBoard().getProfTable()[FactionColor.PINK.getIndex()]);
        assertTrue(turnManager.getPlayers().get(2).getBoard().getProfTable()[FactionColor.GREEN.getIndex()]);
        assertFalse(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.PINK.getIndex()]);
        assertFalse(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.GREEN.getIndex()]);
        assertFalse(turnManager.getPlayers().get(0).getBoard().getProfTable()[FactionColor.PINK.getIndex()]);
        assertFalse(turnManager.getPlayers().get(0).getBoard().getProfTable()[FactionColor.GREEN.getIndex()]);

        turnManager.resetFlags();

        assertEquals(3, turnManager.getCurrentPlayer().getBoard().getDiningRoom().getByColor(FactionColor.GREEN));
        assertEquals(0, turnManager.getCurrentPlayer().getBoard().getDiningRoom().getByColor(FactionColor.RED));
        assertEquals(2, turnManager.getCurrentPlayer().getBoard().getDiningRoom().getByColor(FactionColor.PINK));

        assertEquals(3, turnManager.getPlayers().get(0).getBoard().getDiningRoom().getByColor(FactionColor.GREEN));
        assertEquals(2, turnManager.getPlayers().get(0).getBoard().getDiningRoom().getByColor(FactionColor.RED));
        assertEquals(2, turnManager.getPlayers().get(0).getBoard().getDiningRoom().getByColor(FactionColor.PINK));

        assertEquals(3, turnManager.getPlayers().get(0).getBoard().getDiningRoom().getByColor(FactionColor.GREEN));
        assertEquals(2, turnManager.getPlayers().get(0).getBoard().getDiningRoom().getByColor(FactionColor.RED));
        assertEquals(2, turnManager.getPlayers().get(0).getBoard().getDiningRoom().getByColor(FactionColor.PINK));
    }


    /**
     * Tests removing students from a player and losing two professors to two different players
     */
    @Test
    @DisplayName("Tests removing students from a player and losing two professors to two different players")
    public void removeFromDiningMultiple() {

        TurnManager turnManager = new TurnManager(false, 3);
        turnManager.setUp(new Bag());

        turnManager.setCurrentPlayer(turnManager.getPlayers().get(0));
        LimitedStudentsContainer temp = new LimitedStudentsContainer(15);
        temp.addStudents(3, FactionColor.GREEN);
        temp.addStudents(3, FactionColor.BLUE);
        turnManager.addStudentsToDining(temp);

        turnManager.setCurrentPlayer(turnManager.getPlayers().get(1));
        temp = new LimitedStudentsContainer(15);
        temp.addStudents(4, FactionColor.GREEN);
        temp.addStudents(4, FactionColor.PINK);
        turnManager.addStudentsToDining(temp);

        turnManager.setCurrentPlayer(turnManager.getPlayers().get(2));
        temp = new LimitedStudentsContainer(15);
        temp.addStudents(3, FactionColor.BLUE);
        temp.addStudents(3, FactionColor.PINK);
        turnManager.addStudentsToDining(temp);

        turnManager.setCurrentPlayer(turnManager.getPlayers().get(1));
        temp = new LimitedStudentsContainer(15);
        temp.addStudents(2, FactionColor.GREEN);
        temp.addStudents(2, FactionColor.PINK);

        turnManager.removeStudentsFromDining(temp);
        assertTrue(turnManager.getPlayers().get(0).getBoard().getProfTable()[FactionColor.GREEN.getIndex()]);
        assertTrue(turnManager.getPlayers().get(2).getBoard().getProfTable()[FactionColor.PINK.getIndex()]);
        assertFalse(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.GREEN.getIndex()]);
        assertFalse(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.PINK.getIndex()]);
        assertTrue(turnManager.getPlayers().get(0).getBoard().getProfTable()[FactionColor.BLUE.getIndex()]);
        assertEquals(3, turnManager.getPlayers().get(0).getBoard().getDiningRoom().getByColor(FactionColor.GREEN));
        assertEquals(3, turnManager.getPlayers().get(2).getBoard().getDiningRoom().getByColor(FactionColor.PINK));
        assertEquals(2, turnManager.getPlayers().get(1).getBoard().getDiningRoom().getByColor(FactionColor.GREEN));
        assertEquals(2, turnManager.getPlayers().get(1).getBoard().getDiningRoom().getByColor(FactionColor.PINK));

    }

    /**
     * Tests adding students and taking control of two professors in one time from different players
     */
    @Test
    @DisplayName("Tests adding students and taking control of two professors in one time from different players")
    public void addDiningMultiple() {

        TurnManager turnManager = new TurnManager(false, 3);
        turnManager.setUp(new Bag());

        turnManager.setCurrentPlayer(turnManager.getPlayers().get(0));
        LimitedStudentsContainer temp = new LimitedStudentsContainer(15);
        temp.addStudents(3, FactionColor.GREEN);
        temp.addStudents(3, FactionColor.BLUE);
        turnManager.addStudentsToDining(temp);

        turnManager.setCurrentPlayer(turnManager.getPlayers().get(1));
        temp = new LimitedStudentsContainer(15);
        temp.addStudents(2, FactionColor.GREEN);
        temp.addStudents(2, FactionColor.PINK);
        turnManager.addStudentsToDining(temp);

        turnManager.setCurrentPlayer(turnManager.getPlayers().get(2));
        temp = new LimitedStudentsContainer(15);
        temp.addStudents(3, FactionColor.BLUE);
        temp.addStudents(3, FactionColor.PINK);
        turnManager.addStudentsToDining(temp);

        turnManager.setProfWithDraw();
        turnManager.setCurrentPlayer(turnManager.getPlayers().get(1));
        temp = new LimitedStudentsContainer(15);
        temp.addStudents(1, FactionColor.GREEN);
        temp.addStudents(1, FactionColor.PINK);
        turnManager.addStudentsToDining(temp);

        assertFalse(turnManager.getPlayers().get(0).getBoard().getProfTable()[FactionColor.GREEN.getIndex()]);
        assertFalse(turnManager.getPlayers().get(2).getBoard().getProfTable()[FactionColor.PINK.getIndex()]);
        assertTrue(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.GREEN.getIndex()]);
        assertTrue(turnManager.getPlayers().get(1).getBoard().getProfTable()[FactionColor.PINK.getIndex()]);
        assertTrue(turnManager.getPlayers().get(0).getBoard().getProfTable()[FactionColor.BLUE.getIndex()]);
        assertEquals(3, turnManager.getPlayers().get(0).getBoard().getDiningRoom().getByColor(FactionColor.GREEN));
        assertEquals(3, turnManager.getPlayers().get(2).getBoard().getDiningRoom().getByColor(FactionColor.PINK));
        assertEquals(3, turnManager.getPlayers().get(1).getBoard().getDiningRoom().getByColor(FactionColor.GREEN));
        assertEquals(3, turnManager.getPlayers().get(1).getBoard().getDiningRoom().getByColor(FactionColor.PINK));


    }

    /**
     * Test CalculateCoin when no coins should be provided
     */
    @Test
    @DisplayName("Test CalculateCoin when no coins should be provided")
    void testCalculateCoinNoCoin() {
        Player player = new Player();
        Board board;
        board = new Board(2, TowerColor.BLACK, true, player);
        TurnManager turnManager = new TurnManager(true, 2);
        player.setBoard(board);
        turnManager.getPlayers().add(player);
        turnManager.setCurrentPlayer(player);
        LimitedStudentsContainer container = new LimitedStudentsContainer(2);
        container.addStudents(2, FactionColor.BLUE);
        turnManager.addStudentsToDining(container);
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

    }

    /**
     * Test CalculateCoin when just one coin should be provided
     */
    @Test
    @DisplayName("Test CalculateCoin when just one coin should be provided")
    void testCalculateCoinWithCoin() {
        Player player = new Player();
        Board board;
        board = new Board(2, TowerColor.BLACK, true, player);
        TurnManager turnManager = new TurnManager(true, 2);
        player.setBoard(board);
        turnManager.getPlayers().add(player);
        turnManager.setCurrentPlayer(player);
        LimitedStudentsContainer container = new LimitedStudentsContainer(6);
        container.addStudents(5, FactionColor.BLUE);
        turnManager.addStudentsToDining(container);
        Field privateField;
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


    /**
     * Test CalculateCoin when there are exactly three students
     */
    @Test
    @DisplayName("Test CalculateCoin when there are exactly three students")
    void testCalculateCoin3Students() {
        Player player = new Player();
        Board board;
        board = new Board(2, TowerColor.BLACK, true, player);
        TurnManager turnManager = new TurnManager(true, 2);
        player.setBoard(board);
        turnManager.getPlayers().add(player);
        turnManager.setCurrentPlayer(player);
        LimitedStudentsContainer container = new LimitedStudentsContainer(3);
        container.addStudents(3, FactionColor.BLUE);
        turnManager.addStudentsToDining(container);
        Field privateField;
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

    /**
     * Test CalculateCoin when students are removed and then re-added
     */
    @Test
    @DisplayName("Test CalculateCoin when students are removed and then re-added")
    void testCalculateCoinsAfterRemoving() {
        Player player = new Player();
        Board board;
        board = new Board(2, TowerColor.BLACK, true, player);
        TurnManager turnManager = new TurnManager(true, 2);
        player.setBoard(board);
        turnManager.getPlayers().add(player);
        turnManager.setCurrentPlayer(player);
        LimitedStudentsContainer container = new LimitedStudentsContainer(3);
        container.addStudents(3, FactionColor.BLUE);
        turnManager.addStudentsToDining(container);
        LimitedStudentsContainer temp = new LimitedStudentsContainer(2);
        temp.addStudents(2, FactionColor.BLUE);
        turnManager.removeStudentsFromDining(temp);
        Field privateField;
        try {
            privateField = Player.class.getDeclaredField("numberOfCoins");
            privateField.setAccessible(true);
            assertEquals(2, privateField.get(player));
            privateField = Board.class.getDeclaredField("coinsArray");
            privateField.setAccessible(true);
            assertArrayEquals(new boolean[]{true, true, true}, ((boolean[][]) privateField.get(board))[FactionColor.BLUE.getIndex()]);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        turnManager.addStudentsToDining(container);
        try {
            privateField = Player.class.getDeclaredField("numberOfCoins");
            privateField.setAccessible(true);
            assertEquals(3, privateField.get(player));
            privateField = Board.class.getDeclaredField("coinsArray");
            privateField.setAccessible(true);
            assertArrayEquals(new boolean[]{false, true, true}, ((boolean[][]) privateField.get(board))[FactionColor.BLUE.getIndex()]);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test remove students from dining
     */
    @Test
    @DisplayName("Test remove students from dining")
    void testRemoveStudentFromDining() {
        Player player = new Player();
        Board board;
        board = new Board(2, TowerColor.BLACK, false, player);
        TurnManager turnManager = new TurnManager(false, 2);
        player.setBoard(board);
        turnManager.getPlayers().add(player);
        turnManager.setCurrentPlayer(player);
        LimitedStudentsContainer container = new LimitedStudentsContainer(5);
        container.addStudents(5, FactionColor.GREEN);
        turnManager.addStudentsToDining(container);
        LimitedStudentsContainer temp = new LimitedStudentsContainer(2);
        temp.addStudents(2, FactionColor.GREEN);
        turnManager.removeStudentsFromDining(temp);
        assertEquals(3, board.getDiningRoom().size());
        assertEquals(3, board.getDiningRoom().getByColor(FactionColor.GREEN));
    }

    /**
     * Test remove from dining more students than are presents
     */
    @Test
    @DisplayName("Test remove from dining more students than are presents ")
    void testRemoveStudentsFromDiningWhenTooFew() {
        Player player = new Player();
        Board board;
        board = new Board(2, TowerColor.BLACK, false, player);
        TurnManager turnManager = new TurnManager(false, 2);
        player.setBoard(board);
        turnManager.getPlayers().add(player);
        turnManager.setCurrentPlayer(player);
        LimitedStudentsContainer container = new LimitedStudentsContainer(5);
        container.addStudents(1, FactionColor.GREEN);
        turnManager.addStudentsToDining(container);
        LimitedStudentsContainer temp = new LimitedStudentsContainer(2);
        temp.addStudents(2, FactionColor.GREEN);
        assertThrows(StudentSpaceException.class, () -> turnManager.removeStudentsFromDining(temp));
    }

}