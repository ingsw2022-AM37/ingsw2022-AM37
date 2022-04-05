package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.exceptions.MNmovementWrongException;
import it.polimi.ingsw.am37.model.exceptions.NoIslandConquerorException;
import it.polimi.ingsw.am37.model.student_container.FixedUnlimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.UnlimitedStudentsContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class IslandsManagerTest {

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

        islandsManager.getIslands().add(new Island(students1));
        islandsManager.getIslands().add(new Island(students2));
        islandsManager.getIslands().add(new Island(students3));
        islandsManager.getIslands().add(new Island(students4));

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

        islandsManager.getIslands().add(new Island(students1));
        islandsManager.getIslands().add(new Island(students2));
        islandsManager.getIslands().add(new Island(students3));
        islandsManager.getIslands().add(new Island(students4));

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

        islandsManager.getIslands().add(new Island(students1));
        islandsManager.getIslands().add(new Island(students2));
        islandsManager.getIslands().add(new Island(students3));

        for (int i = 0; i < 3; i++)
            islandsManager.getIslands().get(i).setTower(TowerColor.BLACK);

        islandsManager.uniteIfPossible(islandsManager.getIslands().get(0));

        assertEquals(1, islandsManager.getIslands().size());
        assertEquals(3, islandsManager.getIslands().get(0).getNumIslands());

        for (FactionColor color : FactionColor.values())
            assertEquals(30, islandsManager.getIslands().get(0).getByColor(color));

    }

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

        islandsManager.getIslands().add(new Island(students1));
        islandsManager.getIslands().add(new Island(students2));
        islandsManager.getIslands().add(new Island(students3));
        islandsManager.getIslands().add(new Island(students4));

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

        Island island = new Island(students);
        IslandsManager islandsManager = new IslandsManager();
        islandsManager.getIslands().add(0, island);

        islandsManager.setCurrentPlayer(players.get(0));


        player = islandsManager.checkConqueror(islandsManager.getIslands().get(0), players);

        assertEquals(players.get(1), player);
        assertEquals(TowerColor.GRAY, island.getCurrentTower());

        player = islandsManager.checkConqueror(islandsManager.getIslands().get(0), players);

        assertEquals(players.get(1), player);
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

        player = islandsManager.checkConqueror(islandsManager.getIslands().get(0), players);

        assertEquals(players.get(0), player);
        assertEquals(TowerColor.BLACK, island.getCurrentTower());
        q = players.get(0).getBoard().getTowers().getCurrentSize();
        assertEquals(7, q);
        v = players.get(1).getBoard().getTowers().getCurrentSize();
        assertEquals(8, v);
    }


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

        Island island = new Island(students);
        islandsManager.getIslands().add(0, island);

        assertThrows(NoIslandConquerorException.class, () -> islandsManager.checkConqueror(islandsManager.getIslands().get(0), players));

        players.get(0).getBoard().addProf(FactionColor.BLUE);
        players.get(0).getBoard().addProf(FactionColor.RED);

        int n = players.get(0).getBoard().getTowers().getCurrentSize();
        assertEquals(6, n);
        int m = players.get(1).getBoard().getTowers().getCurrentSize();
        assertEquals(6, m);
        int h = players.get(2).getBoard().getTowers().getCurrentSize();
        assertEquals(6, h);

        player = islandsManager.checkConqueror(islandsManager.getIslands().get(0), players);

        assertEquals(players.get(0), player);
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

        player = islandsManager.checkConqueror(islandsManager.getIslands().get(0), players);

        assertEquals(players.get(0), player);
        assertEquals(TowerColor.BLACK, island.getCurrentTower());

        n = players.get(0).getBoard().getTowers().getCurrentSize();
        assertEquals(5, n);
        m = players.get(1).getBoard().getTowers().getCurrentSize();
        assertEquals(6, m);
        h = players.get(2).getBoard().getTowers().getCurrentSize();
        assertEquals(6, h);

        players.get(0).getBoard().removeProf(FactionColor.GREEN);
        players.get(2).getBoard().addProf(FactionColor.GREEN);

        player = islandsManager.checkConqueror(islandsManager.getIslands().get(0), players);

        assertEquals(players.get(2), player);
        assertEquals(TowerColor.WHITE, island.getCurrentTower());

        n = players.get(0).getBoard().getTowers().getCurrentSize();
        assertEquals(6, n);
        m = players.get(1).getBoard().getTowers().getCurrentSize();
        assertEquals(6, m);
        h = players.get(2).getBoard().getTowers().getCurrentSize();
        assertEquals(5, h);
    }


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

        islandsManager.getIslands().add(new Island(students1));
        islandsManager.getIslands().add(new Island(students2));
        islandsManager.getIslands().add(new Island(students3));
        islandsManager.getIslands().add(new Island(students4));

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

        player = islandsManager.checkConqueror(islandsManager.getIslands().get(2), players);

        assertEquals(players.get(2), player);
        assertEquals(TowerColor.WHITE, islandsManager.getIslands().get(2).getCurrentTower());

        int n = players.get(0).getBoard().getTowers().getCurrentSize();
        assertEquals(6, n);
        int m = players.get(1).getBoard().getTowers().getCurrentSize();
        assertEquals(6, m);
        int h = players.get(2).getBoard().getTowers().getCurrentSize();
        assertEquals(5, h);

        assertThrows(NoIslandConquerorException.class, () -> islandsManager.checkConqueror(islandsManager.getIslands().get(1), players));
        assertThrows(NoIslandConquerorException.class, () -> islandsManager.checkConqueror(islandsManager.getIslands().get(3), players));

        players.get(1).getBoard().addProf(FactionColor.BLUE);
        player = islandsManager.checkConqueror(islandsManager.getIslands().get(3), players);
        assertEquals(players.get(1), player);

        islandsManager.uniteIfPossible(islandsManager.getIslands().get(3));
        assertEquals(1, islandsManager.getIslands().get(3).getNumIslands());

        players.get(1).getBoard().addProf(FactionColor.RED);
        player = islandsManager.checkConqueror(islandsManager.getIslands().get(2), players);
        assertEquals(players.get(2), player);

        players.get(2).getBoard().removeProf(FactionColor.YELLOW);
        players.get(1).getBoard().removeProf(FactionColor.BLUE);
        assertThrows(NoIslandConquerorException.class, () -> islandsManager.checkConqueror(islandsManager.getIslands().get(2), players));
        assertEquals(TowerColor.WHITE, islandsManager.getIslands().get(2).getCurrentTower());

        players.get(1).getBoard().addProf(FactionColor.BLUE);

        player = islandsManager.checkConqueror(islandsManager.getIslands().get(2), players);
        assertEquals(players.get(1), player);
        assertEquals(TowerColor.GRAY, islandsManager.getIslands().get(2).getCurrentTower());

        player = islandsManager.checkConqueror(islandsManager.getIslands().get(0), players);
        assertEquals(players.get(1), player);
        assertEquals(TowerColor.GRAY, islandsManager.getIslands().get(0).getCurrentTower());

        islandsManager.uniteIfPossible(islandsManager.getIslands().get(2));

        players.get(1).getBoard().removeProf(FactionColor.BLUE);
        players.get(0).getBoard().addProf(FactionColor.BLUE);

        player = islandsManager.checkConqueror(islandsManager.getIslands().get(2), players);
        assertEquals(players.get(1), player);

        players.get(0).getBoard().removeProf(FactionColor.GREEN);
        players.get(0).getBoard().addProf(FactionColor.YELLOW);

        player = islandsManager.checkConqueror(islandsManager.getIslands().get(2), players);
        assertEquals(players.get(0), player);

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

    @Test
    @DisplayName("This test checks the initialization of islands")
        //THIS METHOD IS RANDOM, SO I USE PRINT AND NOT ASSERT
    void setUp() {
        IslandsManager islandsManager = new IslandsManager();
        islandsManager.setUp();
        int i = 0;
        System.out.println("Islands' size: " + islandsManager.getIslands().size());
        System.out.println("I have Mother Nature " + islandsManager.getIslands().indexOf(islandsManager.getMotherNaturePosition()));
        System.out.println("-----------------------------------------------");
        for (Island island : islandsManager.getIslands()) {
            System.out.println("I am the island number " + i);
            System.out.println("I have " + island.getNumIslands() + " islands");
            System.out.println("I have a tower of color: " + island.getCurrentTower());
            System.out.println("I have " + island.getStudentsOnIsland().size() + " students");
            i++;
            System.out.println("-----------------------------------------------");
        }

        int cont = 0;
        for (Island island : islandsManager.getIslands()) {
            i = 0;
            for (FactionColor color : FactionColor.values()) {
                System.out.println("I am " + i + " and I have " + island.getByColor(color) + " students of " + color);
                cont = cont + island.getByColor(color);
                i++;
            }
        }
        System.out.println("Total students on islands: " + cont);
    }

    @Test
    //THIS TEST CAN FAIL BECAUSE AN EXCEPTION CAN BE LAUNCHED, BUT IT'S BASED ON MOTHER NATURE INITIAL POSITION WHICH IS RANDOM
    //SO THIS ISN'T WRONG. FOR THIS REASON I USE PRINT AND NOT ASSERT.
    //EXAMPLE: MOTHER NATURE IS ON ISLAND 1 (BUT I CAN'T KNOW), IF I WANT TO MOVE ON ISLAND 1 IS A EXCEPTION
    @DisplayName("THIS CAN FAIL, IT'S NORMAL. Tests the possible movement of Mother Nature starting from random position")
    void moveMotherNature() throws InstanceAlreadyExistsException {

        IslandsManager islandsManager = new IslandsManager();
        islandsManager.setAdditionalMNFlag(15);
        islandsManager.setUp();
        Player player = new Player();

        player.setBoard(new Board(3, TowerColor.BLACK, false, player));

        islandsManager.setCurrentPlayer(player);
        player.createDeck(WizardTeam.TEAM1);
        player.useAssistant(player.getAssistantsDeck().get(7)); //4 max movement without flag

        int i = 0;
        System.out.println("Islands' size: " + islandsManager.getIslands().size());
        System.out.println("I have Mother Nature " + islandsManager.getIslands().indexOf(islandsManager.getMotherNaturePosition()));
        System.out.println("-----------------------------------------------");
        for (Island island : islandsManager.getIslands()) {
            System.out.println("I am the island number " + i);
            System.out.println("I have " + island.getNumIslands() + " islands");
            System.out.println("I have a tower of color: " + island.getCurrentTower());
            System.out.println("I have " + island.getStudentsOnIsland().size() + " students");
            i++;
            System.out.println("-----------------------------------------------");
        }

        islandsManager.moveMotherNature(islandsManager.getIslands().get(5));

        i = 0;
        System.out.println("Islands' size: " + islandsManager.getIslands().size());
        System.out.println("I have Mother Nature " + islandsManager.getIslands().indexOf(islandsManager.getMotherNaturePosition()));
        System.out.println("-----------------------------------------------");
        for (Island island : islandsManager.getIslands()) {
            System.out.println("I am the island number " + i);
            System.out.println("I have " + island.getNumIslands() + " islands");
            System.out.println("I have a tower of color: " + island.getCurrentTower());
            System.out.println("I have " + island.getStudentsOnIsland().size() + " students");
            i++;
            System.out.println("-----------------------------------------------");
        }


    }

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
        Island island = new Island(students);
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

        assertThrows(NoIslandConquerorException.class, () -> islandsManager.checkConqueror(island, players));

        islandsManager.setDisabledColorFlag(FactionColor.BLUE);
        winner = islandsManager.checkConqueror(island, players);
        assertEquals(players.get(1), winner);

        islandsManager.resetFlag();
        winner = islandsManager.checkConqueror(island, players);
        assertEquals(players.get(1), winner);
        assertEquals(TowerColor.WHITE, island.getCurrentTower());

        islandsManager.setPowerBonusFlag(1);
        winner = islandsManager.checkConqueror(island, players);
        assertEquals(players.get(1), winner);
        assertEquals(TowerColor.WHITE, island.getCurrentTower());

        islandsManager.resetFlag();
        islandsManager.setPowerBonusFlag(2);
        winner = islandsManager.checkConqueror(island, players);
        assertEquals(players.get(0), winner);
        assertEquals(TowerColor.BLACK, island.getCurrentTower());

        players.get(2).getBoard().addProf(FactionColor.GREEN);

        islandsManager.setNoTowerFlag();
        winner = islandsManager.checkConqueror(island, players);
        assertEquals(players.get(2), winner);
        assertEquals(TowerColor.GRAY, island.getCurrentTower());
        assertEquals(5, players.get(2).getBoard().getTowers().getCurrentSize());

    }
}