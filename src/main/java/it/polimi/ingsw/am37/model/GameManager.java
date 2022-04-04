package it.polimi.ingsw.am37.model;

import java.util.ArrayList;

public class GameManager {
    /**
     *
     */
    private boolean[] notUsedTeachers = new boolean[FactionColor.values().length];
    /**
     *
     */
    private Bag bag;
    /**
     *
     */
    private ArrayList<Cloud> clouds;
    /**
     *
     */
    private Character[] characters = new Character[3];
    /**
     *
     */
    private IslandsManager manager;
    /**
     *
     */
    private TurnManager turnManager;

    /**
     *
     */
    public void prepareGame() {

    }

    /**
     * @return
     */
    public IslandsManager getManager() {
        return manager;
    }

    /**
     * @return
     */
    public TurnManager getTurnManager() {
        return turnManager;
    }

    /**
     *
     */
    private void fillBag() {

    }

    /**
     *
     */
    private void resetTurn() {

    }

    /**
     * @return
     */
    private String checkWinCondition() {
        return null;
    }

    /**
     *
     */
    private void extractCharacter() {

    }
}
