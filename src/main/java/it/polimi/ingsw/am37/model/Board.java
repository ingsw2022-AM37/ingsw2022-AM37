package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;


/**
 * The class represent the player board in the game. This hold the tower and students container,
 * provide access for adding and removing elements.
 */
public class Board {

    /**
     * It's the number of coins, if they are disabled it's useless. It's used to check if the number of coins is constant between board and its player
     */
    private int numberOfCoins = 0;

    /**
     * Container for the towers
     */
    final LimitedTowerContainer towerArea;

    /**
     * Container for the students in the entrance zone
     */
    final LimitedStudentsContainer entranceArea;

    /**
     * Container for the students in the dining room
     */
    final LimitedStudentsContainer diningRoom;

    /**
     * Represent the table of professors using an array of boolean; each element its true if professor is in
     */
    final boolean[] profTable;

    /**
     * Reference to the player who this board belongs to
     */
    final Player player;

    /**
     * The number of player in this match, use it to hold different logic switching on it
     */
    final int numOfPlayer;

    /**
     * Flag for show if the coin logic should be enabled
     */
    boolean coinsEnabled;

    /**
     * The default constructor create empty entrance and dining rooms and fill the tower containers
     * @param numOfPlayer the number of player in this match, use it to use different limits on the containers
     * @param color the color of tower of the player
     * @param coinsEnabled the flag to activate coins logic
     * @param player It's the owner of the board
     */
    public Board(int numOfPlayer, TowerColor color, boolean coinsEnabled, Player player) {
        this.numOfPlayer = numOfPlayer;
        this.coinsEnabled = coinsEnabled;
        this.player = player;
        switch (numOfPlayer) {
            case 2 -> towerArea = new LimitedTowerContainer(8, 8, color);
            case 3 -> towerArea = new LimitedTowerContainer(6, 8, color);
            case 4 -> towerArea = new LimitedTowerContainer(8, 0, color);
            default -> throw new IllegalArgumentException("number of player must be between 2 and 4");

        }
        entranceArea = new LimitedStudentsContainer(numOfPlayer == 3 ? 9 : 7);
        diningRoom = new LimitedStudentsContainer(new int[]{10, 10, 10, 10, 10});
        profTable = new boolean[]{false, false, false, false, false};
        if(coinsEnabled)
            this.numberOfCoins = 15;
    }

    /**
     * The advanced constructor create empty dining room and fill the tower container, also fill
     * the entrance with provided container to speed up the process
     * @param numOfPlayer  the number of player in this match, use it to use different limits on the containers
     * @param color        the color of tower of the player
     * @param coinsEnabled the flag to activate coins logic
     * @param entrance     the container of the students extracted from the bag at the beginning
     * @param player It's the owner of the board
     */
    public Board(int numOfPlayer, TowerColor color, boolean coinsEnabled, LimitedStudentsContainer entrance, Player player) {
        this.numOfPlayer = numOfPlayer;
        this.coinsEnabled = coinsEnabled;
        this.player = player;
        switch (numOfPlayer) {
            case 2 -> towerArea = new LimitedTowerContainer(8, 8, color);
            case 3 -> towerArea = new LimitedTowerContainer(6, 8, color);
            case 4 -> towerArea = new LimitedTowerContainer(8, 0, color);
            default -> throw new IllegalArgumentException("number of player must be between 2 and 4");
        }
        this.entranceArea = entrance;
        diningRoom = new LimitedStudentsContainer(new int[]{10, 10, 10, 10, 10});
        profTable = new boolean[]{false, false, false, false, false};
        if(coinsEnabled)
            this.numberOfCoins = 15;
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
        player.receiveCoin();
    }

    /**
     * Remove the students from dining rooms. This functions must be used only by the theft
     * character to remove 3 students from the dining room; if there are fewer than 3 students
     * of the specified color, all of them are removed.
     *
     * @param color the color of students to remove
     * @return a container with the removed students
     */
    public LimitedStudentsContainer removeStudentFromDining(FactionColor color) {
        diningRoom.removeStudents(Math.min(3, diningRoom.getByColor(color)), color);
        LimitedStudentsContainer temp = new LimitedStudentsContainer(3);
        temp.addStudents(Math.min(3, diningRoom.getByColor(color)), color);
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
     * @param previous the state of the dining room before the insert
     * @param current  the state of the dining room after the insert
     * @return the number of coins taken
     */
    private int calculateCoin(LimitedStudentsContainer previous, LimitedStudentsContainer current) {
        int coins = 0;
        for (FactionColor color :
                FactionColor.values()) {
            if (previous.getByColor(color) % 3 == 0)
                coins += (current.getByColor(color) - previous.getByColor(color)) / 3;
            else for (int i = previous.getByColor(color); i <= current.getByColor(color); i++) {
                if (i % 3 == 0) coins++;
            }
        }
        return coins;
    }

    /**
     * @return the player who owns this board
     */
    public Player getPlayer() {
        return player;
    }

}