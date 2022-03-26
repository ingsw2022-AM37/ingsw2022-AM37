package it.polimi.ingsw.am37.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LimitedTowerContainerTest {

    @Test
    void addTowers() {

        LimitedTowerContainer towerTest = new LimitedTowerContainer(8,0,TowerColor.BLACK);

        towerTest.addTowers(5);

        assertEquals(5,towerTest.getCurrentSize());
        assertEquals(TowerColor.BLACK, towerTest.getCurrentTower());

        towerTest.addTowers(3);

        assertEquals(8 , towerTest.getCurrentSize());
        assertEquals(TowerColor.BLACK, towerTest.getCurrentTower());

        assertThrows(IllegalArgumentException.class, ()->towerTest.addTowers(-2) );
        assertThrows(IllegalArgumentException.class, ()->towerTest.addTowers(0) );
        assertThrows(IllegalArgumentException.class, ()->towerTest.addTowers(1) );

    }

    @Test
    void removeTowers() {

        LimitedTowerContainer towerTest = new LimitedTowerContainer(7,7,TowerColor.GRAY);

        towerTest.removeTowers(6);

        assertEquals(1,towerTest.getCurrentSize());
        assertEquals(TowerColor.GRAY, towerTest.getCurrentTower());

        towerTest.removeTowers(1);

        assertEquals(0, towerTest.getCurrentSize());
        assertEquals(TowerColor.GRAY, towerTest.getCurrentTower());

        assertThrows(IllegalArgumentException.class, ()->towerTest.removeTowers(-2) );
        assertThrows(IllegalArgumentException.class, ()->towerTest.removeTowers(0) );
        assertThrows(IllegalArgumentException.class, ()->towerTest.removeTowers(1) );
    }

    @Test
    void mixTowers(){

        LimitedTowerContainer towerTest = new LimitedTowerContainer(7,7,TowerColor.WHITE);

        towerTest.removeTowers(5);
        towerTest.addTowers(3);
        towerTest.removeTowers(5);
        towerTest.addTowers(7);

        assertEquals(7, towerTest.getCurrentSize());
    }
}