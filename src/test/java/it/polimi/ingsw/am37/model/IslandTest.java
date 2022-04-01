package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.exceptions.NoIslandConquerorException;
import it.polimi.ingsw.am37.model.student_container.FixedUnlimitedStudentsContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {

    @Test
    @DisplayName("Tests uniteIfPossible with the second of four islands, the middle two are compatible ")

    void uniteIfPossible1() {

        ArrayList<Island> islands = new ArrayList<>();
        FixedUnlimitedStudentsContainer students1 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students2 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students3 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students4 = new FixedUnlimitedStudentsContainer();

        for(FactionColor color: FactionColor.values()) {
            students1.addStudents(10, color);
            students2.addStudents(10, color);
            students3.addStudents(10, color);
            students4.addStudents(10, color);
        }

        islands.add(new Island(false, students1));
        islands.add(new Island(false, students2));
        islands.add(new Island(false, students3));
        islands.add(new Island(false, students4));

        islands.get(0).setTower(TowerColor.BLACK);
        islands.get(1).setTower(TowerColor.GRAY);
        islands.get(2).setTower(TowerColor.GRAY);
        islands.get(3).setTower(TowerColor.BLACK);

        assertThrows(IllegalArgumentException.class, ()-> islands.get(0).uniteIfPossible(islands, -1));
        assertThrows(IllegalArgumentException.class, ()-> islands.get(0).uniteIfPossible(islands, 4));

        islands.get(2).uniteIfPossible(islands, 2);

        assertEquals(3, islands.size());
        assertEquals(1, islands.get(0).getNumIslands());
        assertEquals(2, islands.get(1).getNumIslands());
        assertEquals(1, islands.get(2).getNumIslands());

        for(FactionColor color : FactionColor.values())
            assertEquals(10, islands.get(0).getStudentsOnIsland().getByColor(color));
        for(FactionColor color : FactionColor.values())
            assertEquals(20, islands.get(1).getStudentsOnIsland().getByColor(color));
        for(FactionColor color : FactionColor.values())
            assertEquals(10, islands.get(2).getStudentsOnIsland().getByColor(color));
    }

    @Test
    @DisplayName("Tests uniteIfPossible with the third of four islands all compatible ")

    void uniteIfPossible2() {

        ArrayList<Island> islands = new ArrayList<>();
        FixedUnlimitedStudentsContainer students1 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students2 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students3 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students4 = new FixedUnlimitedStudentsContainer();

        for(FactionColor color: FactionColor.values()) {
            students1.addStudents(10, color);
            students2.addStudents(10, color);
            students3.addStudents(10, color);
            students4.addStudents(10, color);
        }

        islands.add(new Island(false, students1));
        islands.add(new Island(false, students2));
        islands.add(new Island(false, students3));
        islands.add(new Island(false, students4));

        for(int i=0; i<4; i++)
            islands.get(i).setTower(TowerColor.BLACK);

        islands.get(3).uniteIfPossible(islands, 3);

        assertEquals(2, islands.size());
        assertEquals(1, islands.get(0).getNumIslands());
        assertEquals(3, islands.get(1).getNumIslands());

        for(FactionColor color: FactionColor.values())
            assertEquals(10, islands.get(0).getByColor(color));
        for(FactionColor color: FactionColor.values())
            assertEquals(30, islands.get(1).getByColor(color));

    }


    @Test
    @DisplayName("Tests uniteIfPossible with the first of three islands all compatible ")
    void uniteIfPossible3() {

        ArrayList<Island> islands = new ArrayList<>();
        FixedUnlimitedStudentsContainer students1 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students2 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students3 = new FixedUnlimitedStudentsContainer();

        for(FactionColor color: FactionColor.values()) {
            students1.addStudents(10, color);
            students2.addStudents(10, color);
            students3.addStudents(10, color);
        }

        islands.add(new Island(false, students1));
        islands.add(new Island(false, students2));
        islands.add(new Island(false, students3));

        for(int i=0; i<3; i++)
            islands.get(i).setTower(TowerColor.BLACK);

        islands.get(0).uniteIfPossible(islands, 0);

        assertEquals(1, islands.size());
        assertEquals(3, islands.get(0).getNumIslands());

        for(FactionColor color: FactionColor.values())
            assertEquals(30, islands.get(0).getByColor(color));



    }


    @Test
    @DisplayName("Tests uniteIfPossible with the second of four islands all compatible, then with the first two times ")
    void uniteIfPossible4() {

        ArrayList<Island> islands = new ArrayList<>();
        FixedUnlimitedStudentsContainer students1 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students2 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students3 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students4 = new FixedUnlimitedStudentsContainer();

        for(FactionColor color: FactionColor.values()) {
            students1.addStudents(10, color);
            students2.addStudents(10, color);
            students3.addStudents(10, color);
            students4.addStudents(10, color);
        }

        islands.add(new Island(false, students1));
        islands.add(new Island(false, students2));
        islands.add(new Island(false, students3));
        islands.add(new Island(false, students4));

        for(int i=0; i<4; i++)
            islands.get(i).setTower(TowerColor.BLACK);

        islands.get(2).uniteIfPossible(islands, 2);

        assertEquals(2, islands.size());
        assertEquals(1, islands.get(0).getNumIslands());
        assertEquals(3, islands.get(1).getNumIslands());

        for(FactionColor color: FactionColor.values())
            assertEquals(10, islands.get(0).getByColor(color));
        for(FactionColor color: FactionColor.values())
            assertEquals(30, islands.get(1).getByColor(color));

        islands.get(0).uniteIfPossible(islands, 0);

        assertEquals(1, islands.size());
        assertEquals(4, islands.get(0).getNumIslands());

        for(FactionColor color: FactionColor.values())
            assertEquals(40, islands.get(0).getByColor(color));

        islands.get(0).uniteIfPossible(islands, 0);

        assertEquals(1, islands.size());
        assertEquals(4, islands.get(0).getNumIslands());

        for(FactionColor color: FactionColor.values())
            assertEquals(40, islands.get(0).getByColor(color));

    }


    @Test
    @DisplayName("Tests the conqueror of the island after changes or after no changes")
    void checkConquerorForTwo() {
        FixedUnlimitedStudentsContainer students = new FixedUnlimitedStudentsContainer();
        ArrayList<Player> players = new ArrayList<>();
        Player player;

        for(int i=0; i<2; i++)
            players.add(new Player());

        players.get(0).setBoard(new Board(2,TowerColor.BLACK ,true ,players.get(0)  ));
        players.get(1).setBoard(new Board(2,TowerColor.GRAY  ,true ,players.get(1) ));

        players.get(0).getBoard().addProf(FactionColor.GREEN);
        players.get(1).getBoard().addProf(FactionColor.PINK);
        players.get(1).getBoard().addProf(FactionColor.YELLOW);

        for(FactionColor color : FactionColor.values())
            students.addStudents(1, color);

        Island island = new Island(false, students);

        player = island.checkConqueror(players);

        assertEquals(players.get(1), player);
        assertEquals(TowerColor.GRAY, island.getCurrentTower());

        player = island.checkConqueror(players);

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

        player = island.checkConqueror(players);

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
        ArrayList<Player> players = new ArrayList<>();
        Player player;

        for(int i=0; i<3; i++)
            players.add(new Player());

        players.get(0).setBoard(new Board(3,TowerColor.BLACK ,true ,players.get(0)  ));
        players.get(1).setBoard(new Board(3,TowerColor.GRAY ,true ,players.get(1)  ));
        players.get(2).setBoard(new Board(3,TowerColor.WHITE ,true  ,players.get(2) ));

        players.get(0).getBoard().addProf(FactionColor.GREEN);
        players.get(1).getBoard().addProf(FactionColor.PINK);
        players.get(2).getBoard().addProf(FactionColor.YELLOW);

        for(FactionColor color : FactionColor.values())
            students.addStudents(1, color);

        Island island = new Island(false, students);

        assertThrows(NoIslandConquerorException.class, ()-> island.checkConqueror(players));

        players.get(0).getBoard().addProf(FactionColor.BLUE);
        players.get(0).getBoard().addProf(FactionColor.RED);

        int n = players.get(0).getBoard().getTowers().getCurrentSize();
        assertEquals(6, n);
        int m = players.get(1).getBoard().getTowers().getCurrentSize();
        assertEquals(6, m);
        int h = players.get(2).getBoard().getTowers().getCurrentSize();
        assertEquals(6, h);

        player = island.checkConqueror(players);

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

        player = island.checkConqueror(players);

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

        player = island.checkConqueror(players);

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
    void mixedTest(){

        ArrayList<Island> islands = new ArrayList<>();
        FixedUnlimitedStudentsContainer students1 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students2 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students3 = new FixedUnlimitedStudentsContainer();
        FixedUnlimitedStudentsContainer students4 = new FixedUnlimitedStudentsContainer();
        ArrayList<Player> players = new ArrayList<>();
        Player player;

        students1.addStudents(3, FactionColor.RED);
        students1.addStudents(5, FactionColor.GREEN);
        students1.addStudents(2, FactionColor.PINK);
        students1.addStudents(6, FactionColor.YELLOW);
        students1.addStudents(2, FactionColor.BLUE);

        for(FactionColor color : FactionColor.values()) {
            students2.addStudents(3, color);
            students4.addStudents(4, color);
        }

        students3.addStudents(3, FactionColor.RED);
        students3.addStudents(5, FactionColor.GREEN);
        students3.addStudents(2, FactionColor.PINK);
        students3.addStudents(6, FactionColor.YELLOW);
        students3.addStudents(2, FactionColor.BLUE);

        islands.add(new Island(false, students1));
        islands.add(new Island(false, students2));
        islands.add(new Island(false, students3));
        islands.add(new Island(false, students4));

        islands.get(3).uniteIfPossible(islands, 3);

        assertEquals(4, islands.size());
        assertEquals(1, islands.get(0).getNumIslands());
        assertEquals(1, islands.get(1).getNumIslands());
        assertEquals(1, islands.get(2).getNumIslands());
        assertEquals(1, islands.get(3).getNumIslands());

        for(FactionColor color : FactionColor.values()) {
            assertEquals(3, islands.get(1).getStudentsOnIsland().getByColor(color));
            assertEquals(4, islands.get(3).getStudentsOnIsland().getByColor(color));
        }

        for(int i=0; i<3; i++)
            players.add(new Player());

        players.get(0).setBoard(new Board(3, TowerColor.BLACK,true ,players.get(0)  ));
        players.get(1).setBoard(new Board(3,TowerColor.GRAY ,true  ,players.get(1) ));
        players.get(2).setBoard(new Board(3, TowerColor.WHITE,true ,players.get(2)  ));

        players.get(0).getBoard().addProf(FactionColor.GREEN);
        players.get(1).getBoard().addProf(FactionColor.PINK);
        players.get(2).getBoard().addProf(FactionColor.YELLOW);

        player = islands.get(2).checkConqueror(players);

        assertEquals(players.get(2), player);
        assertEquals(TowerColor.WHITE, islands.get(2).getCurrentTower());

        int n = players.get(0).getBoard().getTowers().getCurrentSize();
        assertEquals(6, n);
        int m = players.get(1).getBoard().getTowers().getCurrentSize();
        assertEquals(6, m);
        int h = players.get(2).getBoard().getTowers().getCurrentSize();
        assertEquals(5, h);

        assertThrows(NoIslandConquerorException.class, ()->islands.get(1).checkConqueror(players));
        assertThrows(NoIslandConquerorException.class, ()->islands.get(3).checkConqueror(players));

        players.get(1).getBoard().addProf(FactionColor.BLUE);
        player = islands.get(3).checkConqueror(players);
        assertEquals(players.get(1), player);

        islands.get(3).uniteIfPossible(islands, 3);
        assertEquals(1, islands.get(3).getNumIslands());

        players.get(1).getBoard().addProf(FactionColor.RED);
        player = islands.get(2).checkConqueror(players);
        assertEquals(players.get(2), player);

        players.get(2).getBoard().removeProf(FactionColor.YELLOW);
        players.get(1).getBoard().removeProf(FactionColor.BLUE);
        assertThrows(NoIslandConquerorException.class, ()->islands.get(2).checkConqueror(players));
        assertEquals(TowerColor.WHITE, islands.get(2).getCurrentTower());

        players.get(1).getBoard().addProf(FactionColor.BLUE);

        player = islands.get(2).checkConqueror(players);
        assertEquals(players.get(1), player);
        assertEquals(TowerColor.GRAY, islands.get(2).getCurrentTower());

        player = islands.get(0).checkConqueror(players);
        assertEquals(players.get(1), player);
        assertEquals(TowerColor.GRAY, islands.get(0).getCurrentTower());

        islands.get(2).uniteIfPossible(islands, 2);

        players.get(1).getBoard().removeProf(FactionColor.BLUE);
        players.get(0).getBoard().addProf(FactionColor.BLUE);

        player = islands.get(2).checkConqueror(players);
        assertEquals(players.get(1), player);

        players.get(0).getBoard().removeProf(FactionColor.GREEN);
        players.get(0).getBoard().addProf(FactionColor.YELLOW);

        player = islands.get(2).checkConqueror(players);
        assertEquals(players.get(0), player);

        assertEquals(TowerColor.GRAY, islands.get(0).getCurrentTower());
        assertEquals(TowerColor.NONE, islands.get(1).getCurrentTower());
        assertEquals(TowerColor.BLACK, islands.get(2).getCurrentTower());

        for(FactionColor color : FactionColor.values())
            assertEquals(3, islands.get(1).getByColor(color));

        assertEquals(2, islands.get(0).getByColor(FactionColor.BLUE));
        assertEquals(2, islands.get(0).getByColor(FactionColor.PINK));
        assertEquals(3, islands.get(0).getByColor(FactionColor.RED));
        assertEquals(5, islands.get(0).getByColor(FactionColor.GREEN));
        assertEquals(6, islands.get(0).getByColor(FactionColor.YELLOW));

        assertEquals(6, islands.get(2).getByColor(FactionColor.BLUE));
        assertEquals(6, islands.get(2).getByColor(FactionColor.PINK));
        assertEquals(7, islands.get(2).getByColor(FactionColor.RED));
        assertEquals(9, islands.get(2).getByColor(FactionColor.GREEN));
        assertEquals(10, islands.get(2).getByColor(FactionColor.YELLOW));

        n = players.get(0).getBoard().getTowers().getCurrentSize();
        assertEquals(4, n);
        m = players.get(1).getBoard().getTowers().getCurrentSize();
        assertEquals(5, m);
        h = players.get(2).getBoard().getTowers().getCurrentSize();
        assertEquals(6, h);

        assertEquals(3, islands.size());
        assertEquals(1, islands.get(0).getNumIslands());
        assertEquals(1, islands.get(1).getNumIslands());
        assertEquals(2, islands.get(2).getNumIslands());
    }

}

