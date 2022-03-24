package it.polimi.ingsw.am37.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LimitedTowerContainerTest {

    @Test
    void addTowers() {

        LimitedTowerContainer towerTest = new LimitedTowerContainer(7,0,TowerColor.BLACK);
        towerTest.addTowers(4);
        assertEquals(4,towerTest.getCurrentSize());
        assertEquals(TowerColor.BLACK, towerTest.getcurrentTower());
        towerTest.addTowers(3);
        assertEquals(7 , towerTest.getCurrentSize());
        assertEquals(TowerColor.BLACK, towerTest.getcurrentTower());

    }

    @Test
    void removeTowers() {

        LimitedTowerContainer towerTest = new LimitedTowerContainer(7,7,TowerColor.GRAY);
        towerTest.removeTowers(6);
        assertEquals(1,towerTest.getCurrentSize());
        assertEquals(TowerColor.GRAY, towerTest.getcurrentTower());
        towerTest.removeTowers(1);
        assertEquals(0, towerTest.getCurrentSize());
        assertEquals(TowerColor.GRAY, towerTest.getcurrentTower());
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