package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.exceptions.MNmovementWrongException;
import it.polimi.ingsw.am37.model.student_container.FixedUnlimitedStudentsContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IslandsManagerTest {

    /**
     * Tests uniteIfPossible with the second of four islands, the middle two are compatible
     */
    @Test
    @DisplayName("Tests uniteIfPossible with the second of four islands, the middle two are compatible ")
    void uniteIfPossible1() {

        IslandsManager islandsManager = new IslandsManager();


        FixedUnlimitedStudentsContainer students1 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students2 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students3 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students4 = new FixedUnlimitedStudentsContainer();

        for (FactionColor color : FactionColor.values()) {
            students1.addStudents(10, color);
            students2.addStudents(10, color);
            students3.addStudents(10, color);
            students4.addStudents(10, color);
        }

        islandsManager.getIslands().add(new Island(students1, 0));
        islandsManager.getIslands().add(new Island(students2, 1));
        islandsManager.getIslands().add(new Island(students3, 2));
        islandsManager.getIslands().add(new Island(students4, 3));

        islandsManager.getIslands().get(0).setTower(TowerColor.BLACK);
        islandsManager.getIslands().get(1).setTower(TowerColor.GRAY);
        islandsManager.getIslands().get(2).setTower(TowerColor.GRAY);
        islandsManager.getIslands().get(3).setTower(TowerColor.BLACK);

        assertThrows(IndexOutOfBoundsException.class, () -> islandsManager.uniteIfPossible(islandsManager.getIslands().get(-1)));
        assertThrows(IndexOutOfBoundsException.class, () -> islandsManager.uniteIfPossible(islandsManager.getIslands().get(4)));

        islandsManager.uniteIfPossible(islandsManager.getIslands().get(2));

        assertEquals(3, islandsManager.getIslands().size());
        assertEquals(1, islandsManager.getIslands().get(0).getNumIslands());
        assertEquals(2, islandsManager.getIslands().get(1).getNumIslands());
        assertEquals(1, islandsManager.getIslands().get(2).getNumIslands());

        for (FactionColor color : FactionColor.values())
            assertEquals(10, islandsManager.getIslands().get(0).getStudentsOnIsland().getByColor(color));
        for (FactionColor color : FactionColor.values())
            assertEquals(20, islandsManager.getIslands().get(1).getStudentsOnIsland().getByColor(color));
        for (FactionColor color : FactionColor.values())
            assertEquals(10, islandsManager.getIslands().get(2).getStudentsOnIsland().getByColor(color));
    }

    /**
     * Tests uniteIfPossible with the third of four islands all compatible
     */
    @Test
    @DisplayName("Tests uniteIfPossible with the third of four islands all compatible ")
    void uniteIfPossible2() {

        IslandsManager islandsManager = new IslandsManager();
        FixedUnlimitedStudentsContainer students1 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students2 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students3 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students4 = new FixedUnlimitedStudentsContainer();

        for (FactionColor color : FactionColor.values()) {
            students1.addStudents(10, color);
            students2.addStudents(10, color);
            students3.addStudents(10, color);
            students4.addStudents(10, color);
        }

        islandsManager.getIslands().add(new Island(students1, 0));
        islandsManager.getIslands().add(new Island(students2, 1));
        islandsManager.getIslands().add(new Island(students3, 2));
        islandsManager.getIslands().add(new Island(students4, 3));

        for (int i = 0; i < 4; i++)
            islandsManager.getIslands().get(i).setTower(TowerColor.BLACK);

        islandsManager.uniteIfPossible(islandsManager.getIslands().get(3));

        assertEquals(2, islandsManager.getIslands().size());
        assertEquals(1, islandsManager.getIslands().get(0).getNumIslands());
        assertEquals(3, islandsManager.getIslands().get(1).getNumIslands());

        for (FactionColor color : FactionColor.values())
            assertEquals(10, islandsManager.getIslands().get(0).getByColor(color));
        for (FactionColor color : FactionColor.values())
            assertEquals(30, islandsManager.getIslands().get(1).getByColor(color));

    }

    /**
     * Tests uniteIfPossible with the first of three islands all compatible
     */
    @Test
    @DisplayName("Tests uniteIfPossible with the first of three islands all compatible ")
    void uniteIfPossible3() {

        IslandsManager islandsManager = new IslandsManager();
        FixedUnlimitedStudentsContainer students1 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students2 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students3 = new FixedUnlimitedStudentsContainer();

        for (FactionColor color : FactionColor.values()) {
            students1.addStudents(10, color);
            students2.addStudents(10, color);
            students3.addStudents(10, color);
        }

        islandsManager.getIslands().add(new Island(students1, 0));
        islandsManager.getIslands().add(new Island(students2, 1));
        islandsManager.getIslands().add(new Island(students3, 2));

        for (int i = 0; i < 3; i++)
            islandsManager.getIslands().get(i).setTower(TowerColor.BLACK);

        islandsManager.uniteIfPossible(islandsManager.getIslands().get(0));

        assertEquals(1, islandsManager.getIslands().size());
        assertEquals(3, islandsManager.getIslands().get(0).getNumIslands());

        for (FactionColor color : FactionColor.values())
            assertEquals(30, islandsManager.getIslands().get(0).getByColor(color));

    }

    /**
     * Tests uniteIfPossible with the second of four islands all compatible, then with the first two times
     */
    @Test
    @DisplayName("Tests uniteIfPossible with the second of four islands all compatible, then with the first two times ")
    void uniteIfPossible4() {

        IslandsManager islandsManager = new IslandsManager();
        FixedUnlimitedStudentsContainer students1 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students2 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students3 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students4 = new FixedUnlimitedStudentsContainer();

        for (FactionColor color : FactionColor.values()) {
            students1.addStudents(10, color);
            students2.addStudents(10, color);
            students3.addStudents(10, color);
            students4.addStudents(10, color);
        }

        islandsManager.getIslands().add(new Island(students1, 0));
        islandsManager.getIslands().add(new Island(students2, 1));
        islandsManager.getIslands().add(new Island(students3, 2));
        islandsManager.getIslands().add(new Island(students4, 3));

        for (int i = 0; i < 4; i++)
            islandsManager.getIslands().get(i).setTower(TowerColor.BLACK);

        islandsManager.uniteIfPossible(islandsManager.getIslands().get(2));

        assertEquals(2, islandsManager.getIslands().size());
        assertEquals(1, islandsManager.getIslands().get(0).getNumIslands());
        assertEquals(3, islandsManager.getIslands().get(1).getNumIslands());

        for (FactionColor color : FactionColor.values())
            assertEquals(10, islandsManager.getIslands().get(0).getByColor(color));
        for (FactionColor color : FactionColor.values())
            assertEquals(30, islandsManager.getIslands().get(1).getByColor(color));

        islandsManager.uniteIfPossible(islandsManager.getIslands().get(0));

        assertEquals(1, islandsManager.getIslands().size());
        assertEquals(4, islandsManager.getIslands().get(0).getNumIslands());

        for (FactionColor color : FactionColor.values())
            assertEquals(40, islandsManager.getIslands().get(0).getByColor(color));

        islandsManager.uniteIfPossible(islandsManager.getIslands().get(0));

        assertEquals(1, islandsManager.getIslands().size());
        assertEquals(4, islandsManager.getIslands().get(0).getNumIslands());

        for (FactionColor color : FactionColor.values())
            assertEquals(40, islandsManager.getIslands().get(0).getByColor(color));

    }

    /**
     * Tests the conqueror of the island after changes or after no changes
     */
    @Test
    @DisplayName("Tests the conqueror of the island after changes or after no changes")
    void checkConquerorForTwo() {

        FixedUnlimitedStudentsContainer students = new FixedUnlimitedStudentsContainer();
        ArrayList<Player> players = new ArrayList<>();
        Player player;

        for (int i = 0; i < 2; i++)
            players.add(new Player());

        players.get(0).setBoard(new Board(2, TowerColor.BLACK, true, players.get(0)));
        players.get(1).setBoard(new Board(2, TowerColor.GRAY, true, players.get(1)));


        players.get(0).getBoard().addProf(FactionColor.GREEN);
        players.get(1).getBoard().addProf(FactionColor.PINK);
        players.get(1).getBoard().addProf(FactionColor.YELLOW);

        for (FactionColor color : FactionColor.values())
            students.addStudents(1, color);

        Island island = new Island(students, 0);
        IslandsManager islandsManager = new IslandsManager();
        islandsManager.getIslands().add(0, island);

        islandsManager.setCurrentPlayer(players.get(0));


        islandsManager.checkConqueror(islandsManager.getIslands().get(0), players);

        assertEquals(TowerColor.GRAY, island.getCurrentTower());

        islandsManager.checkConqueror(islandsManager.getIslands().get(0), players);


        assertEquals(TowerColor.GRAY, island.getCurrentTower());
        int q = players.get(0).getBoard().getTowers().getCurrentSize();
        assertEquals(8, q);
        int v = players.get(1).getBoard().getTowers().getCurrentSize();
        assertEquals(7, v);

        players.get(0).getBoard().addProf(FactionColor.RED);
        players.get(1).getBoard().removeProf(FactionColor.PINK);
        players.get(0).getBoard().addProf(FactionColor.PINK);

        q = players.get(0).getBoard().getTowers().getCurrentSize();
        assertEquals(8, q);
        v = players.get(1).getBoard().getTowers().getCurrentSize();
        assertEquals(7, v);

        islandsManager.checkConqueror(islandsManager.getIslands().get(0), players);

        assertEquals(TowerColor.BLACK, island.getCurrentTower());
        q = players.get(0).getBoard().getTowers().getCurrentSize();
        assertEquals(7, q);
        v = players.get(1).getBoard().getTowers().getCurrentSize();
        assertEquals(8, v);
    }

    /**
     * Tests the conqueror of the island after changes or after no changes or when there isn't a winner
     */
    @Test
    @DisplayName("Tests the conqueror of the island after changes or after no changes or when there isn't a winner")
    void checkConquerorForThree() {
        FixedUnlimitedStudentsContainer students = new FixedUnlimitedStudentsContainer();
        IslandsManager islandsManager = new IslandsManager();
        ArrayList<Player> players = new ArrayList<>();
        Player player;

        for (int i = 0; i < 3; i++)
            players.add(new Player());

        players.get(0).setBoard(new Board(3, TowerColor.BLACK, true, players.get(0)));
        players.get(1).setBoard(new Board(3, TowerColor.GRAY, true, players.get(1)));
        players.get(2).setBoard(new Board(3, TowerColor.WHITE, true, players.get(2)));

        islandsManager.setCurrentPlayer(players.get(0));

        players.get(0).getBoard().addProf(FactionColor.GREEN);
        players.get(1).getBoard().addProf(FactionColor.PINK);
        players.get(2).getBoard().addProf(FactionColor.YELLOW);

        for (FactionColor color : FactionColor.values())
            students.addStudents(1, color);

        Island island = new Island(students, 0);
        islandsManager.getIslands().add(0, island);

        islandsManager.checkConqueror(islandsManager.getIslands().get(0), players);
        assertEquals(TowerColor.NONE, islandsManager.getIslands().get(0).getCurrentTower());

        players.get(0).getBoard().addProf(FactionColor.BLUE);
        players.get(0).getBoard().addProf(FactionColor.RED);

        int n = players.get(0).getBoard().getTowers().getCurrentSize();
        assertEquals(6, n);
        int m = players.get(1).getBoard().getTowers().getCurrentSize();
        assertEquals(6, m);
        int h = players.get(2).getBoard().getTowers().getCurrentSize();
        assertEquals(6, h);

        islandsManager.checkConqueror(islandsManager.getIslands().get(0), players);

        assertEquals(TowerColor.BLACK, island.getCurrentTower());

        n = players.get(0).getBoard().getTowers().getCurrentSize();
        assertEquals(5, n);
        m = players.get(1).getBoard().getTowers().getCurrentSize();
        assertEquals(6, m);
        h = players.get(2).getBoard().getTowers().getCurrentSize();
        assertEquals(6, h);

        players.get(0).getBoard().removeProf(FactionColor.BLUE);
        players.get(1).getBoard().addProf(FactionColor.BLUE);
        players.get(0).getBoard().removeProf(FactionColor.RED);
        players.get(2).getBoard().addProf(FactionColor.RED);

        islandsManager.checkConqueror(islandsManager.getIslands().get(0), players);

        assertEquals(TowerColor.BLACK, island.getCurrentTower());

        n = players.get(0).getBoard().getTowers().getCurrentSize();
        assertEquals(5, n);
        m = players.get(1).getBoard().getTowers().getCurrentSize();
        assertEquals(6, m);
        h = players.get(2).getBoard().getTowers().getCurrentSize();
        assertEquals(6, h);

        players.get(0).getBoard().removeProf(FactionColor.GREEN);
        players.get(2).getBoard().addProf(FactionColor.GREEN);

        islandsManager.checkConqueror(islandsManager.getIslands().get(0), players);

        assertEquals(TowerColor.WHITE, island.getCurrentTower());

        n = players.get(0).getBoard().getTowers().getCurrentSize();
        assertEquals(6, n);
        m = players.get(1).getBoard().getTowers().getCurrentSize();
        assertEquals(6, m);
        h = players.get(2).getBoard().getTowers().getCurrentSize();
        assertEquals(5, h);
    }

    /**
     * Tests uniteIfPossible and checkConqueror united, this is a full match emulation with difficult situations, only used add and remove prof instead of addStudents and removeStudents
     */
    @Test
    @DisplayName("Tests uniteIfPossible and checkConqueror united, this is a full match emulation with difficult situations, only used add and remove prof instead of addStudents and removeStudents ")
    void mixedTest() {

        FixedUnlimitedStudentsContainer students1 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students2 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students3 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students4 = new FixedUnlimitedStudentsContainer();
        ArrayList<Player> players = new ArrayList<>();
        IslandsManager islandsManager = new IslandsManager();
        Player player;

        students1.addStudents(3, FactionColor.RED);
        students1.addStudents(5, FactionColor.GREEN);
        students1.addStudents(2, FactionColor.PINK);
        students1.addStudents(6, FactionColor.YELLOW);
        students1.addStudents(2, FactionColor.BLUE);

        for (FactionColor color : FactionColor.values()) {
            students2.addStudents(3, color);
            students4.addStudents(4, color);
        }

        students3.addStudents(3, FactionColor.RED);
        students3.addStudents(5, FactionColor.GREEN);
        students3.addStudents(2, FactionColor.PINK);
        students3.addStudents(6, FactionColor.YELLOW);
        students3.addStudents(2, FactionColor.BLUE);

        islandsManager.getIslands().add(new Island(students1, 0));
        islandsManager.getIslands().add(new Island(students2, 1));
        islandsManager.getIslands().add(new Island(students3, 2));
        islandsManager.getIslands().add(new Island(students4, 3));

        islandsManager.uniteIfPossible(islandsManager.getIslands().get(3));

        assertEquals(4, islandsManager.getIslands().size());
        assertEquals(1, islandsManager.getIslands().get(0).getNumIslands());
        assertEquals(1, islandsManager.getIslands().get(1).getNumIslands());
        assertEquals(1, islandsManager.getIslands().get(2).getNumIslands());
        assertEquals(1, islandsManager.getIslands().get(3).getNumIslands());

        for (FactionColor color : FactionColor.values()) {
            assertEquals(3, islandsManager.getIslands().get(1).getStudentsOnIsland().getByColor(color));
            assertEquals(4, islandsManager.getIslands().get(3).getStudentsOnIsland().getByColor(color));
        }

        for (int i = 0; i < 3; i++)
            players.add(new Player());

        players.get(0).setBoard(new Board(3, TowerColor.BLACK, true, players.get(0)));
        players.get(1).setBoard(new Board(3, TowerColor.GRAY, true, players.get(1)));
        players.get(2).setBoard(new Board(3, TowerColor.WHITE, true, players.get(2)));

        islandsManager.setCurrentPlayer(players.get(0));

        players.get(0).getBoard().addProf(FactionColor.GREEN);
        players.get(1).getBoard().addProf(FactionColor.PINK);
        players.get(2).getBoard().addProf(FactionColor.YELLOW);

        islandsManager.checkConqueror(islandsManager.getIslands().get(2), players);

        assertEquals(TowerColor.WHITE, islandsManager.getIslands().get(2).getCurrentTower());

        int n = players.get(0).getBoard().getTowers().getCurrentSize();
        assertEquals(6, n);
        int m = players.get(1).getBoard().getTowers().getCurrentSize();
        assertEquals(6, m);
        int h = players.get(2).getBoard().getTowers().getCurrentSize();
        assertEquals(5, h);

        islandsManager.checkConqueror(islandsManager.getIslands().get(1), players);
        assertEquals(TowerColor.NONE, islandsManager.getIslands().get(1).getCurrentTower());
        islandsManager.checkConqueror(islandsManager.getIslands().get(3), players);
        assertEquals(TowerColor.NONE, islandsManager.getIslands().get(3).getCurrentTower());

        players.get(1).getBoard().addProf(FactionColor.BLUE);
        islandsManager.checkConqueror(islandsManager.getIslands().get(3), players);
        assertEquals(TowerColor.GRAY, islandsManager.getIslands().get(3).getCurrentTower());

        islandsManager.uniteIfPossible(islandsManager.getIslands().get(3));
        assertEquals(1, islandsManager.getIslands().get(3).getNumIslands());

        players.get(1).getBoard().addProf(FactionColor.RED);
        islandsManager.checkConqueror(islandsManager.getIslands().get(2), players);
        assertEquals(TowerColor.GRAY, islandsManager.getIslands().get(3).getCurrentTower());

        players.get(2).getBoard().removeProf(FactionColor.YELLOW);
        players.get(1).getBoard().removeProf(FactionColor.BLUE);
        islandsManager.checkConqueror(islandsManager.getIslands().get(2), players);
        assertEquals(TowerColor.WHITE, islandsManager.getIslands().get(2).getCurrentTower());

        players.get(1).getBoard().addProf(FactionColor.BLUE);

        islandsManager.checkConqueror(islandsManager.getIslands().get(2), players);
        assertEquals(TowerColor.GRAY, islandsManager.getIslands().get(2).getCurrentTower());

        islandsManager.checkConqueror(islandsManager.getIslands().get(0), players);
        assertEquals(TowerColor.GRAY, islandsManager.getIslands().get(0).getCurrentTower());

        islandsManager.uniteIfPossible(islandsManager.getIslands().get(2));

        players.get(1).getBoard().removeProf(FactionColor.BLUE);
        players.get(0).getBoard().addProf(FactionColor.BLUE);

        islandsManager.checkConqueror(islandsManager.getIslands().get(2), players);
        assertEquals(TowerColor.GRAY, islandsManager.getIslands().get(0).getCurrentTower());
        players.get(0).getBoard().removeProf(FactionColor.GREEN);
        players.get(0).getBoard().addProf(FactionColor.YELLOW);

        islandsManager.checkConqueror(islandsManager.getIslands().get(2), players);
        assertEquals(TowerColor.BLACK, islandsManager.getIslands().get(2).getCurrentTower());

        assertEquals(TowerColor.GRAY, islandsManager.getIslands().get(0).getCurrentTower());
        assertEquals(TowerColor.NONE, islandsManager.getIslands().get(1).getCurrentTower());
        assertEquals(TowerColor.BLACK, islandsManager.getIslands().get(2).getCurrentTower());

        for (FactionColor color : FactionColor.values())
            assertEquals(3, islandsManager.getIslands().get(1).getByColor(color));

        assertEquals(2, islandsManager.getIslands().get(0).getByColor(FactionColor.BLUE));
        assertEquals(2, islandsManager.getIslands().get(0).getByColor(FactionColor.PINK));
        assertEquals(3, islandsManager.getIslands().get(0).getByColor(FactionColor.RED));
        assertEquals(5, islandsManager.getIslands().get(0).getByColor(FactionColor.GREEN));
        assertEquals(6, islandsManager.getIslands().get(0).getByColor(FactionColor.YELLOW));

        assertEquals(6, islandsManager.getIslands().get(2).getByColor(FactionColor.BLUE));
        assertEquals(6, islandsManager.getIslands().get(2).getByColor(FactionColor.PINK));
        assertEquals(7, islandsManager.getIslands().get(2).getByColor(FactionColor.RED));
        assertEquals(9, islandsManager.getIslands().get(2).getByColor(FactionColor.GREEN));
        assertEquals(10, islandsManager.getIslands().get(2).getByColor(FactionColor.YELLOW));

        n = players.get(0).getBoard().getTowers().getCurrentSize();
        assertEquals(4, n);
        m = players.get(1).getBoard().getTowers().getCurrentSize();
        assertEquals(5, m);
        h = players.get(2).getBoard().getTowers().getCurrentSize();
        assertEquals(6, h);

        assertEquals(3, islandsManager.getIslands().size());
        assertEquals(1, islandsManager.getIslands().get(0).getNumIslands());
        assertEquals(1, islandsManager.getIslands().get(1).getNumIslands());
        assertEquals(2, islandsManager.getIslands().get(2).getNumIslands());
    }

    /**
     * This test checks the initialization of islands
     */
    @Test
    @DisplayName("This test checks the initialization of islands")

    void setUp() {

        int[] cont = new int[FactionColor.values().length];
        for (FactionColor color : FactionColor.values())
            cont[color.getIndex()] = 0;

        IslandsManager islandsManager = new IslandsManager();
        islandsManager.setUp();

        assertEquals(12, islandsManager.getIslands().size());

        for (Island island : islandsManager.getIslands()) {
            assertEquals(TowerColor.NONE, island.getCurrentTower());
            assertEquals(1, island.getNumIslands());
            for (FactionColor color : FactionColor.values()) {
                cont[color.getIndex()] = island.getStudentsOnIsland().getByColor(color) + cont[color.getIndex()];
            }
        }

        for (FactionColor color : FactionColor.values())
            assertEquals(2, cont[color.getIndex()]);

        Island islandTemp = islandsManager.getMotherNaturePosition();

        for (FactionColor color : FactionColor.values())
            assertEquals(0, islandTemp.getByColor(color));

        int index = islandsManager.getIslands().indexOf(islandTemp);
        for (int i = 0; i < 6; i++) {
            index = index + 1;
            if (index == 12)
                index = 0;
        }

        for (FactionColor color : FactionColor.values())
            assertEquals(0, islandsManager.getIslands().get(index).getByColor(color));
    }

    /**
     * Tests the possible movement of Mother Nature starting from random position
     */
    @Test

    @DisplayName("Tests the possible movement of Mother Nature starting from random position")
    void moveMotherNature() throws InstanceAlreadyExistsException {
        int movement = 5;

        IslandsManager islandsManager = new IslandsManager();
        islandsManager.setAdditionalMNFlag(0);
        islandsManager.setUp();

        Player player = new Player();

        player.setBoard(new Board(3, TowerColor.BLACK, false, player));

        islandsManager.setCurrentPlayer(player);
        player.createDeck(WizardTeam.TEAM1);
        player.useAssistant(player.getAssistantsDeck().get(7)); //4 max movement without flag

        Island islandTemp = islandsManager.getMotherNaturePosition();

        int index = islandsManager.getIslands().indexOf(islandTemp);
        for (int i = 0; i < movement; i++) {
            index = index + 1;
            if (index == 12)
                index = 0;
        }

        int finalIndex = index;
        assertThrows(MNmovementWrongException.class, () -> islandsManager.moveMotherNature(islandsManager.getIslands().get(finalIndex)));

        islandsManager.setAdditionalMNFlag(3);

        index = islandsManager.getIslands().indexOf(islandTemp);
        for (int i = 0; i < movement; i++) {
            index = index + 1;
            if (index == 12)
                index = 0;
        }
        islandsManager.moveMotherNature(islandsManager.getIslands().get(index));

        assertEquals(islandsManager.getIslands().get(index).getIslandId(), islandsManager.getMotherNaturePosition().getIslandId());

    }

    /**
     * Use characters in checkConqueror
     */
    @Test
    @DisplayName("Use characters in checkConqueror")
    void testCharacter() {

        FixedUnlimitedStudentsContainer students = new FixedUnlimitedStudentsContainer();
        Player winner;
        students.addStudents(3, FactionColor.YELLOW);
        students.addStudents(2, FactionColor.BLUE);
        students.addStudents(5, FactionColor.GREEN);
        students.addStudents(2, FactionColor.PINK);
        students.addStudents(7, FactionColor.RED);

        IslandsManager islandsManager = new IslandsManager();
        Island island = new Island(students, 0);
        islandsManager.getIslands().add(island);

        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            players.add(new Player());

        players.get(0).setBoard(new Board(3, TowerColor.BLACK, false, players.get(0)));
        players.get(1).setBoard(new Board(3, TowerColor.WHITE, false, players.get(1)));
        players.get(2).setBoard(new Board(3, TowerColor.GRAY, false, players.get(2)));

        islandsManager.setCurrentPlayer(players.get(0));

        players.get(0).getBoard().addProf(FactionColor.BLUE);
        players.get(1).getBoard().addProf(FactionColor.PINK);

        islandsManager.checkConqueror(island, players);
        assertEquals(TowerColor.NONE, islandsManager.getIslands().get(0).getCurrentTower());

        islandsManager.setDisabledColorFlag(FactionColor.BLUE);
        islandsManager.checkConqueror(island, players);
        assertEquals(players.get(1).getBoard().getTowers().getCurrentTower(), islandsManager.getIslands().get(0).getCurrentTower());

        islandsManager.resetFlags();
        islandsManager.checkConqueror(island, players);
        assertEquals(players.get(1).getBoard().getTowers().getCurrentTower(), islandsManager.getIslands().get(0).getCurrentTower());
        assertEquals(TowerColor.WHITE, island.getCurrentTower());

        islandsManager.setPowerBonusFlag(1);
        islandsManager.checkConqueror(island, players);
        assertEquals(players.get(1).getBoard().getTowers().getCurrentTower(), islandsManager.getIslands().get(0).getCurrentTower());
        assertEquals(TowerColor.WHITE, island.getCurrentTower());

        islandsManager.resetFlags();
        islandsManager.setPowerBonusFlag(2);
        islandsManager.checkConqueror(island, players);
        assertEquals(players.get(0).getBoard().getTowers().getCurrentTower(), islandsManager.getIslands().get(0).getCurrentTower());
        assertEquals(TowerColor.BLACK, island.getCurrentTower());

        players.get(2).getBoard().addProf(FactionColor.GREEN);

        islandsManager.setNoTowerFlag();
        islandsManager.checkConqueror(island, players);
        assertEquals(players.get(2).getBoard().getTowers().getCurrentTower(), islandsManager.getIslands().get(0).getCurrentTower());
        assertEquals(TowerColor.GRAY, island.getCurrentTower());
        assertEquals(5, players.get(2).getBoard().getTowers().getCurrentSize());

    }
}