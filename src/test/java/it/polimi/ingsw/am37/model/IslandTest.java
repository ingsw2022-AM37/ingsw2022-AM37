package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.student_container.FixedUnlimitedStudentsContainer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {

    @Test

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

        islands.get(2).uniteIfPossible(islands, 3);

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
    void checkConqueror() {
        FixedUnlimitedStudentsContainer students = new FixedUnlimitedStudentsContainer();
        ArrayList<Player> players = new ArrayList<>();
        Player player;

        for(int i=0; i<2; i++)
            players.add(new Player());

        players.get(0).setBoard(new Board(2, TowerColor.BLACK,true , players.get(0) ));
        players.get(1).setBoard(new Board(2,  TowerColor.GRAY,true, players.get(1) ));

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

        players.get(0).getBoard().addProf(FactionColor.BLUE);
        players.get(0).getBoard().addProf(FactionColor.RED);

        int n = players.get(0).getBoard().getTowers().getCurrentSize();
        assertEquals(8, n);
        int m = players.get(1).getBoard().getTowers().getCurrentSize();
        assertEquals(7, m);

        player = island.checkConqueror(players);

        assertEquals(players.get(0), player);
        assertEquals(TowerColor.BLACK, island.getCurrentTower());
        int z = players.get(0).getBoard().getTowers().getCurrentSize();
        assertEquals(7, z);
        int k = players.get(1).getBoard().getTowers().getCurrentSize();
        assertEquals(8, k);
    }

}

