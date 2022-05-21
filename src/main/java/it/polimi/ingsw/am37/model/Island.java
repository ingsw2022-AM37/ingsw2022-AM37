package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.student_container.FixedUnlimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.StudentsContainer;

import java.util.Objects;

import static it.polimi.ingsw.am37.model.UpdatableObject.UpdatableType.ISLAND;
import static it.polimi.ingsw.am37.controller.UpdateController.Properties.*;

/**
 * At the beginning of the game there are twelve islands, they can have students on them and tower which represents that
 * a player is controlling them. Islands can be united and once this happens can't be reversed
 */
public class Island extends UpdatableObject {


    /**
     * If there is Mother Nature
     */
    private boolean motherNatureHere;

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
    private final StudentsContainer studentsOnIsland;
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
        super(ISLAND);
        this.NoEntryTile = 0;
        this.numIslandsUnited = 1;
        this.tower = TowerColor.NONE;
        this.studentsOnIsland = studentsOnIsland;
        this.islandId = islandId;
        this.motherNatureHere = false;
    }

    /**
     * It's used to set a noEntryTile
     *
     * @param num Number of entry Tile to add
     */
    public void addNoEntryTile(int num) {
        int oldValue = this.NoEntryTile;
        this.NoEntryTile = this.NoEntryTile + num;
        support.firePropertyChange(P_ISLAND_NOENTRYTILE.toString(), oldValue, this.NoEntryTile);
    }

    /**
     * It's used to remove a noEntryTile
     */
    public void removeNoEntryTile() {
        int oldValue = this.NoEntryTile;
        this.NoEntryTile = this.NoEntryTile - 1;
        support.firePropertyChange(P_ISLAND_NOENTRYTILE.toString(), oldValue, this.NoEntryTile);
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
        TowerColor oldColor = tower;
        this.tower = color;
        support.firePropertyChange(P_ISLAND_TOWER.toString(), oldColor, this.tower);
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
    public void addStudents(StudentsContainer cont) {
        FixedUnlimitedStudentsContainer oldContainer = (FixedUnlimitedStudentsContainer) this.studentsOnIsland.copy();
        this.studentsOnIsland.uniteContainers(cont);
        this.support.firePropertyChange(P_ISLAND_STUDENTS.toString(), oldContainer, this.studentsOnIsland);
    }

    /**
     * @return The students on the island
     */
    public StudentsContainer getStudentsOnIsland() {
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
        int oldValue = this.numIslandsUnited;
        this.numIslandsUnited = num;
        support.firePropertyChange(P_ISLAND_DIMENSION.toString(), oldValue, this.numIslandsUnited);
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
     * @return if there is Mother Nature
     */
    public boolean getMotherNatureHere() {
        return motherNatureHere;
    }

    /**
     * @param bool used to set Mother Nature
     */
    public void setMotherNatureHere(boolean bool) {
        motherNatureHere = bool;
    }

    /**
     * @param islandId Id to be setted
     */
    public void setIslandId(int islandId) {
        this.islandId = islandId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Island island)) return false;
        return getIslandId() == island.getIslandId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIslandId());
    }
}