package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.exceptions.StudentSpaceException;
import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;

import java.util.Arrays;


/**
 * The class represent the player board in the game. This hold the tower and students container,
 * provide access for adding and removing elements.
 */
public class Board {

    /**
     * Container for the towers
     */
    private final LimitedTowerContainer towerArea;
    /**
     * Container for the students in the entrance zone
     */
    private final LimitedStudentsContainer entranceArea;
    /**
     * Container for the students in the dining room
     */
    private final LimitedStudentsContainer diningRoom;
    /**
     * Represent the table of professors using an array of boolean; each element its true if professor is in
     */
    private final boolean[] profTable;
    /**
     * Reference to the player who this board belongs to
     */
    private final Player player;
    /**
     * Flag for show if the coin logic should be enabled
     */
    private final boolean coinsEnabled;
    /**
     * It's the array of coins over the board.Represented as a matrix where first dimensions is color index and second is
     * the three coins on a color table; if coinsEnable is false is useless, else it's used to track given and not give
     * coins on the board
     */
    private boolean[][] coinsArray;

    /**
     * Represent the space between coins
     */
    static final int spaceBetweenCoins = 3;

    /**
     * The default constructor create empty entrance and dining rooms and fill the tower containers
     *
     * @param numOfPlayer  the number of player in this match, use it to use different limits on the containers
     * @param color        the color of tower of the player
     * @param coinsEnabled the flag to activate coins logic
     * @param player       It's the owner of the board
     */
    public Board(int numOfPlayer, TowerColor color, boolean coinsEnabled, Player player) {
        //default variables for settings
        final int
                maxTowerSizeFor2 = 8,
                maxTowerSizeFor3 = 6,
                maxEntranceSizeFor2 = 7,
                maxEntranceSizeFor3 = 9,
                maxTableSize = 10,
                coinsOnStudentTable = 3;
        // Begin of real method
        this.coinsEnabled = coinsEnabled;
        this.player = player;
        switch (numOfPlayer) {
            case 2 -> towerArea = new LimitedTowerContainer(maxTowerSizeFor2, maxTowerSizeFor2, color);
            case 3 -> towerArea = new LimitedTowerContainer(maxTowerSizeFor3, maxTowerSizeFor3, color);
            case 4 -> towerArea = new LimitedTowerContainer(maxTowerSizeFor2, 0, color);
            default -> throw new IllegalArgumentException("number of player must be between 2 and 4");
        }
        entranceArea = new LimitedStudentsContainer(numOfPlayer == 3 ? maxEntranceSizeFor3 : maxEntranceSizeFor2);
        int[] temp = new int[FactionColor.values().length];
        Arrays.fill(temp, maxTableSize);
        diningRoom = new LimitedStudentsContainer(temp) {
            @Override
            public void addStudents(int num, FactionColor color) throws IllegalArgumentException, StudentSpaceException {
                super.addStudents(num, color);

            }
        };
        profTable = new boolean[FactionColor.values().length];
        Arrays.fill(profTable, false);
        if (coinsEnabled) {
            coinsArray = new boolean[FactionColor.values().length][coinsOnStudentTable];
            for (FactionColor col :
                    FactionColor.values()) {
                Arrays.fill(coinsArray[col.getIndex()], true);
            }
        }

    }

    /**
     * The advanced constructor create empty dining room and fill the tower container, also fill
     * the entrance with provided container to speed up the process
     *
     * @param numOfPlayer  the number of player in this match, use it to use different limits on the containers
     * @param color        the color of tower of the player
     * @param coinsEnabled the flag to activate coins logic
     * @param entrance     the container of the students extracted from the bag at the beginning
     * @param player       It's the owner of the board
     */
    public Board(int numOfPlayer, TowerColor color, boolean coinsEnabled, LimitedStudentsContainer entrance, Player player) {
        this(numOfPlayer, color, coinsEnabled, player);
        entranceArea.uniteContainers(entrance);
    }

    /**
     * @return the tower container
     */
    public LimitedTowerContainer getTowers() {
        return towerArea;
    }

    /**
     * @return the size of tower container
     */
    public int getTowersSize() {
        return towerArea.getCurrentSize();
    }

    /**
     * @param num the number of towers to add
     */
    public void addTowers(int num) {
        towerArea.addTowers(num);
    }

    /**
     * @param num the number of towers to remove
     */
    public void removeTowers(int num) {
        towerArea.removeTowers(num);
    }

    /**
     * @return the array which represents the prof table
     */
    public boolean[] getProfTable() {
        return profTable;
    }

    /**
     * @param color the color of the professor to add
     */
    public void addProf(FactionColor color) {
        profTable[color.getIndex()] = true;
    }

    /**
     * @param color the color of the professor to remove
     */
    public void removeProf(FactionColor color) {
        profTable[color.getIndex()] = false;
    }

    /**
     * @return the dining room students container
     */
    public LimitedStudentsContainer getDiningRoom() {
        return diningRoom;
    }

    /**
     * Unite the container provided with the dining room
     *
     * @param container the input container
     */
    public void addStudentToDining(LimitedStudentsContainer container) {
        diningRoom.uniteContainers(container);
        if (coinsEnabled) {
            for (int i = 0; i < calculateCoin(diningRoom); i++) {
                player.receiveCoin();
            }
        }
    }

    /**
     * Remove the students from dining rooms. If there are less than num students, all of them are removed without raising exception
     *
     * @param num   the max number of students to remove
     * @param color the color of students to remove
     * @return a container with the removed students
     */
    public LimitedStudentsContainer removeStudentFromDining(int num, FactionColor color) {
        diningRoom.removeStudents(Math.min(num, diningRoom.getByColor(color)), color);
        LimitedStudentsContainer temp = new LimitedStudentsContainer(num);
        temp.addStudents(Math.min(num, diningRoom.getByColor(color)), color);
        if (coinsEnabled) checkCoins(diningRoom);
        return temp;
    }

    /**
     * @return the entrance student container
     */
    public LimitedStudentsContainer getEntrance() {
        return entranceArea;
    }

    /**
     * @param container the input container
     */
    public void addStudentsToEntrance(LimitedStudentsContainer container) {
        entranceArea.uniteContainers(container);
    }

    /**
     * @param container the students who want to remove
     */
    public void removeStudentsFromEntrance(LimitedStudentsContainer container) {
        for (FactionColor color :
                FactionColor.values()) {
            entranceArea.removeStudents(container.getByColor(color), color);
        }
    }

    /**
     * Return the number of coins taken this turn for each color
     *
     * @param current the state of the dining room after the insert
     * @return the number of coins taken
     */
    private int calculateCoin(LimitedStudentsContainer current) {
        int coins = 0;
        for (FactionColor color :
                FactionColor.values()) {
            int lastTakenCoinIndex = current.getByColor(color) / spaceBetweenCoins - 1;
            if (lastTakenCoinIndex >= 0 && coinsArray[color.getIndex()][lastTakenCoinIndex]) {
                coinsArray[color.getIndex()][lastTakenCoinIndex] = false;
                coins++;
            }
        }
        return coins;
    }

    /**
     * Check and reassess the state of given coins when some students are removed
     *
     * @param current the state of the dining room after the removal
     */
    private void checkCoins(LimitedStudentsContainer current) {
        for (FactionColor color :
                FactionColor.values()) {
            int firstNotTakenCoinIndex = current.getByColor(color) / spaceBetweenCoins;
            for (int i = firstNotTakenCoinIndex; i < coinsArray[color.getIndex()].length; i++) {
                coinsArray[color.getIndex()][i] = true;
            }
        }
    }

    /**
     * @return the player who owns this board
     */
    public Player getPlayer() {
        return player;
    }

}