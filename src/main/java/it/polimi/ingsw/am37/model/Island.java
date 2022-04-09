package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.exceptions.NoEntryTileException;
import it.polimi.ingsw.am37.model.exceptions.NoIslandConquerorException;
import it.polimi.ingsw.am37.model.student_container.FixedUnlimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;

import java.util.ArrayList;

/**
 * At the beginning of the game there are twelve islands, they can have students on them and tower which represents
 * that a player is controlling them. Islands can be united and once this happens can't be reversed
 */
public class Island {

    /**
     * It's a number to identify island
     */
    private int islandId;

    /**
     * It's used for checkConqueror, it's the index of the current player conqueror
     */
    private Player currentConqueror;

    /**
     * It represents the students on the island
     */
    private final FixedUnlimitedStudentsContainer studentsOnIsland;
    /**
     * Indicate if there is a tower and eventually its color
     */
    private TowerColor tower;

    /**
     * Indicate if there is a NoEntryTile (or more than one) which can prevent the conquest from one player
     */
    private int NoEntryTile;
    /**
     * It represents the number of islands with which the group of islands is compound
     */
    private int numIslandsUnited;

    /**
     * Default constructor
     */
    public Island(FixedUnlimitedStudentsContainer studentsOnIsland, int islandId) {
        this.NoEntryTile = 0;
        this.numIslandsUnited = 1;
        this.tower = TowerColor.NONE;
        this.studentsOnIsland = studentsOnIsland;
        this.islandId = islandId;
    }

    /**
     * It's used to set a noEntryTile
     *
     * @param num Number of entry Tile to add
     */
    public void addNoEntryTile(int num) {
        this.NoEntryTile = this.NoEntryTile + num;
    }

    /**
     * It's used to remove a noEntryTile
     */
    public void removeNoEntryTile() {
        this.NoEntryTile = this.NoEntryTile - 1;
    }

    /**
     * It's used to know if there is a noEntryTile
     */
    public int getNoEntryTile() {
        return this.NoEntryTile;
    }

    /**
     * @param color It's the color of tower needed to be imposed to the island
     */
    public void setTower(TowerColor color) {
        this.tower = color;
    }

    /**
     * @return The current color of tower in the island
     */
    public TowerColor getCurrentTower() {
        return this.tower;
    }

    /**
     * @param cont It's the array of students which will be added to the island's one
     */
    public void addStudents(FixedUnlimitedStudentsContainer cont) {
        this.studentsOnIsland.uniteContainers(cont);
    }

    /**
     * @return The students on the island
     */
    public FixedUnlimitedStudentsContainer getStudentsOnIsland() {
        return this.studentsOnIsland;
    }

    /**
     * @return the number of the islands which possibly formed a united group
     */
    public int getNumIslands() {
        return this.numIslandsUnited;
    }

    /**
     * @param num The number of islands which form the bigger one
     */
    public void setNumIslands(int num) {
        this.numIslandsUnited = num;
    }

    /**
     * @param color It's the color of the number of students we want to find
     * @return The number of students on the Island of one color
     */
    public int getByColor(FactionColor color) {
        return this.studentsOnIsland.getByColor(color);
    }

    /**
     * @return The current conqueror of the island
     */
    public Player getCurrentConqueror() {
        return this.currentConqueror;
    }

    /**
     * @param currentConqueror The current conqueror of the island
     */
    public void setCurrentConqueror(Player currentConqueror) {
        this.currentConqueror = currentConqueror;
    }

    /**
     * @return Return the id of island
     */
    public int getIslandId() {
        return this.islandId;
    }

    /**
     * @param islandId Id to be setted
     */
    public void setIslandId(int islandId) {
        this.islandId = islandId;
    }

}