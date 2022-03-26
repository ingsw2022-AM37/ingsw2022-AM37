package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.student_container.FixedUnlimitedStudentsContainer;

import java.util.ArrayList;

/**
 * At the beginning of the game there are twelve islands, they can have students on them and tower which represents
 * that a player is controlling them. Islands can be united and once this happens can't be reversed
 */
public class Island {

    /**
     * It's used for checkConqueror, it's the index of the current player conqueror
     */
    private int indexCurrentConqueror;

    /**
     * It's the number of students of the last conquest of the island, useful to check who will be the next conqueror
     */
    private int numStudentsControlling = 0;

    /**
     * It represents the students on the island
     *
     */
    private final FixedUnlimitedStudentsContainer studentsOnIsland;
    /**
     * Indicate if there is a tower and eventually its color
     *
     */
    private TowerColor tower;
    /**
     * Indicate if there is MotherNature
     */
    private boolean isMotherNature;
    /**
     * Indicate if there is a NoEntryTile which can prevent the conquest from one player
     */
    private boolean hasNoEntryTile;
    /**
     * It represents the number of islands with which the group of islands is compound
     */
    private int numIslandsUnited;

    /**
     * Default constructor
     */
    public Island(boolean isMotherNature, FixedUnlimitedStudentsContainer studentsOnIsland) {
        this.isMotherNature = isMotherNature;
        this.hasNoEntryTile = false;
        this.numIslandsUnited = 1;
        this.tower = TowerColor.NONE;
        this.studentsOnIsland = studentsOnIsland;
    }

    /**
     * It's used to set a noEntryTile
     */
    public void setNoEntryTile() {
        this.hasNoEntryTile = true;
    }

    /**
     * It's used to remove a noEntryTile
     */
    public void removeNoEntryTile() {
        this.hasNoEntryTile = false;
    }

    /**
     * It's used to know if there is a noEntryTile
     */
    public boolean getNoEntryTile() {
        return this.hasNoEntryTile;
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
     * The method checks if an island has next to it another island (or islands) with the same color of tower, if yes then they will be merged in Island with index islandId and others are eliminated
     *
     * @param islands  It's the ArrayList of all islands used to compare the current island with others in order to find if a union is possible
     * @param islandId It's needed to point to the island where there is MotherNature
     * @throws IllegalArgumentException When islandId doesn't identify an island
     */
    public void uniteIfPossible(ArrayList<Island> islands, int islandId) throws IllegalArgumentException {
        boolean UnitedDx = false;

        if (islandId < 0 || islandId >= islands.size())
            throw new IllegalArgumentException("IslandId should be the index of the island you are looking to unify");

        if (islandId == 0 && islands.size()>1) {
            if ((islands.get(islandId).getCurrentTower()) == islands.get(islands.size() - 1).getCurrentTower()) {
                this.numIslandsUnited = this.numIslandsUnited + islands.get(islands.size() - 1).getNumIslands();
                this.studentsOnIsland.uniteContainers(islands.get(islands.size() - 1).getStudentsOnIsland());

                islands.remove(islands.size() - 1);
            }
        }

        if (islandId + 1 < islands.size() && islands.size()>1) {
            if ((islands.get(islandId).getCurrentTower()) == islands.get(islandId + 1).getCurrentTower()) {
                this.numIslandsUnited = this.numIslandsUnited + islands.get(islandId + 1).getNumIslands();
                this.studentsOnIsland.uniteContainers(islands.get(islandId + 1).getStudentsOnIsland());

                UnitedDx = true;
                islands.remove(islandId + 1);
            }
        }

        if (islandId - 1 >= 0 && islands.size()>1) {
            if ((islands.get(islandId).getCurrentTower()) == islands.get(islandId - 1).getCurrentTower())  {
                this.numIslandsUnited = this.numIslandsUnited + islands.get(islandId - 1).getNumIslands();
                this.studentsOnIsland.uniteContainers(islands.get(islandId - 1).getStudentsOnIsland());

                islands.remove(islandId - 1);
                islandId--;
            }
        }

        if (islandId == islands.size() - 1 && !UnitedDx && islands.size()>1) {
            if ((islands.get(islandId).getCurrentTower()) == islands.get(0).getCurrentTower()) {
                this.numIslandsUnited = this.numIslandsUnited + islands.get(0).getNumIslands();
                this.studentsOnIsland.uniteContainers(islands.get(0).getStudentsOnIsland());

                islands.remove(0);

            }
        }
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
     * Apply the presence of MotherNature
     */
    public void setMotherNature() {
        this.isMotherNature = true;
    }

    /**
     * Remove the presence of MotherNature
     */
    public void unsetMotherNature() {
        this.isMotherNature = false;
    }

    /**
     * @return If MotherNature is on the island or not
     */
    public boolean getMotherNature() {
        return this.isMotherNature;
    }

    /**
     * @param color It's the color of the number of students we want to find
     * @return The number of students on the Island of one color
     */
    public int getByColor(FactionColor color) {
        return this.studentsOnIsland.getByColor(color);
    }

    /**
     * The method check which player has the most students on the island, a player have students of one color when he controls their professor. Each player has a towerColor and the tower means who is controlling it. Switches of conqueror(and of towers) are possible.
     *
     * @param players It's the ArrayList of all players, it gives the access to all boards
     * @return The player who conquered the island
     */
    public Player checkConqueror(ArrayList<Player> players) {
        ArrayList<Integer> playerPower = new ArrayList<>();
        boolean[] controlledProf;

        int indexExConqueror = 0; //in teoria non serve inizializzazione ma da errore
        int boolToInt;
        int tmp;
        boolean switchConqueror = false;

        for (int i = 0; i < players.size(); i++) {
            controlledProf = players.get(i).getBoard().getProfTable();
            tmp = 0;
            for (FactionColor color : FactionColor.values()) {
                boolToInt = controlledProf[color.getIndex()] ? 1 : 0;
                tmp = tmp + boolToInt;
            }
            playerPower.add(i, tmp);
        }

        if(this.tower == TowerColor.NONE) {
            for (int i = 0; i < playerPower.size(); i++)
                if (playerPower.get(i) > this.numStudentsControlling) {
                    indexCurrentConqueror = i;
                    this.numStudentsControlling = playerPower.get(i);

                }
            players.get(indexCurrentConqueror).getBoard().removeTowers(this.numIslandsUnited);
        }
        else{
            indexExConqueror = indexCurrentConqueror;
            for (int i = 0; i < playerPower.size(); i++)
                if (playerPower.get(i) > this.numStudentsControlling) {
                    indexCurrentConqueror = i;
                    this.numStudentsControlling = playerPower.get(i);
                    switchConqueror = true;
                }
        }

        if(switchConqueror){
            players.get(indexExConqueror).getBoard().addTowers(this.numIslandsUnited);
            players.get(indexCurrentConqueror).getBoard().removeTowers(this.numIslandsUnited);
        }

        this.tower = players.get(indexCurrentConqueror).getBoard().getTowers().getCurrentTower();

        return players.get(indexCurrentConqueror);

    }
}