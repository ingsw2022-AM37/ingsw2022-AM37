package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.student_container.FixedUnlimitedStudentsContainer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {

    @Test
    void uniteIfPossible() {
    }

    @Test
    void checkConqueror() {

        ArrayList<Player> players = new ArrayList<>();
        Player player;

        for(int i=0; i<2; i++)
            players.add(new Player());

        players.get(0).setBoard(new Board(2, TowerColor.BLACK, true, players.get(0) ));
        players.get(1).setBoard(new Board(2, TowerColor.GRAY, true, players.get(1) ));

        players.get(0).getBoard().addProf(FactionColor.GREEN);
        players.get(1).getBoard().addProf(FactionColor.PINK);
        players.get(1).getBoard().addProf(FactionColor.YELLOW);

        FixedUnlimitedStudentsContainer students = new FixedUnlimitedStudentsContainer();
        for(FactionColor color : FactionColor.values())
            students.addStudents(1, color);

        Island island = new Island(false, students);

        player = island.checkConqueror(players);

        assertEquals(players.get(1), player);

        assertEquals(TowerColor.GRAY, island.getCurrentTower());

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
        assertEquals(7, n);
        int k = players.get(1).getBoard().getTowers().getCurrentSize();
        assertEquals(8, m);

    }

}