package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.character.State;
import it.polimi.ingsw.am37.model.exceptions.MNmovementWrongException;
import it.polimi.ingsw.am37.model.exceptions.NoIslandConquerorException;
import it.polimi.ingsw.am37.model.student_container.FixedUnlimitedStudentsContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class IslandsManager {

    private final static int numOfInitialIslands = 12;
    private Player currentPlayer;
    private ArrayList<Island> islands;
    private Island motherNaturePosition;
    private boolean noTowerFlag;
    private FactionColor disabledColorFlag;
    private int powerBonusFlag;
    private int additionalMNFlag;
    private State stateCharacterNoEntryTile = null;

    /**
     * Default constructor
     */
    public IslandsManager(ArrayList<Island> islands) {
        this.islands = islands;
        this.noTowerFlag = false;
        this.powerBonusFlag = 0;
        currentPlayer = null;
        disabledColorFlag = null;
        this.additionalMNFlag = 0;
    }

    /**
     * @return The island on which there is Mother Nature
     */
    public Island getMotherNaturePosition() {
        return this.motherNaturePosition;
    }

    /**
     * This is a one-time method, it creates islands, set Mother Nature on a random one and put a student on each island, excluding the one with Mother Nature and the opposite one
     */
    public void setUp() {
        Random random = new Random();
        int motherNatureHere = random.nextInt(0, 12);

        ArrayList<FactionColor> initialFalseBag = new ArrayList<>();
        for (FactionColor color : FactionColor.values())
            initialFalseBag.add(color);
        for (FactionColor color : FactionColor.values())
            initialFalseBag.add(color);
        Collections.shuffle(initialFalseBag);

        for (int i = 0; i < numOfInitialIslands; i++) {
            islands.add(i, new Island(new FixedUnlimitedStudentsContainer()));
            if (i == motherNatureHere) {
                this.motherNaturePosition = islands.get(i);
            }
        }

        int motherNatureDx = motherNatureHere;

        for (int cont = 0; cont < numOfInitialIslands; cont++) {
            int tmp = motherNatureDx - motherNatureHere;
            if (tmp < 0)
                tmp = -tmp;
            if (tmp != 6 && tmp != 0) {
                islands.get(motherNatureDx).getStudentsOnIsland().addStudents(1, initialFalseBag.get(0));
                initialFalseBag.remove(0);
            }
            motherNatureDx = (motherNatureDx < numOfInitialIslands - 1) ? motherNatureDx + 1 : 0;
        }
    }

    /**
     * @return Islands in the game in a certain time
     */
    public ArrayList<Island> getIslands() {
        return islands;
    }

    /**
     * Set the flag to disable tower effect in checkConqueror
     */
    public void setNoTowerFlag() {
        this.noTowerFlag = true;
    }

    /**
     * The method checks if an island has next to it another island (or islands) with the same color of tower, if yes then they will be merged in Island with index islandId and others are eliminated
     *
     * @param island It's needed to point to the island where there is MotherNature
     * @throws IllegalArgumentException When islandId doesn't identify an island
     */
    public void uniteIfPossible(Island island) throws IllegalArgumentException {

        int islandId = islands.indexOf(island);
        boolean UnitedDx = false;

        if (islands.get(islandId).getCurrentTower() != TowerColor.NONE) {

            if (islandId == 0 && islands.size() > 1) {
                if ((islands.get(islandId).getCurrentTower()) == islands.get(islands.size() - 1).getCurrentTower()) {
                    islands.get(islandId).setNumIslands(islands.get(islandId).getNumIslands() + islands.get(islands.size() - 1).getNumIslands());
                    islands.get(islandId).getStudentsOnIsland().uniteContainers(islands.get(islands.size() - 1).getStudentsOnIsland());

                    islands.remove(islands.size() - 1);
                }
            }

            if (islandId + 1 < islands.size() && islands.size() > 1) {
                if ((islands.get(islandId).getCurrentTower()) == islands.get(islandId + 1).getCurrentTower()) {
                    islands.get(islandId).setNumIslands(islands.get(islandId).getNumIslands() + islands.get(islandId + 1).getNumIslands());
                    ;
                    islands.get(islandId).getStudentsOnIsland().uniteContainers(islands.get(islandId + 1).getStudentsOnIsland());

                    UnitedDx = true;
                    islands.remove(islandId + 1);
                }
            }

            if (islandId - 1 >= 0 && islands.size() > 1) {
                if ((islands.get(islandId).getCurrentTower()) == islands.get(islandId - 1).getCurrentTower()) {
                    islands.get(islandId).setNumIslands(islands.get(islandId).getNumIslands() + islands.get(islandId - 1).getNumIslands());
                    ;
                    islands.get(islandId).getStudentsOnIsland().uniteContainers(islands.get(islandId - 1).getStudentsOnIsland());

                    islands.remove(islandId - 1);
                    islandId--;
                }
            }

            if (islandId == islands.size() - 1 && !UnitedDx && islands.size() > 1) {
                if ((islands.get(islandId).getCurrentTower()) == islands.get(0).getCurrentTower()) {
                    islands.get(islandId).setNumIslands(islands.get(islandId).getNumIslands() + islands.get(0).getNumIslands());
                    ;
                    islands.get(islandId).getStudentsOnIsland().uniteContainers(islands.get(0).getStudentsOnIsland());

                    islands.remove(0);

                }
            }
        }

    }

    /**
     * The method check which player has the most students on the island, a player have students of one color when he controls their professor. Each player has a towerColor and the tower means who is controlling it. Switches of conqueror(and of towers) are possible.
     *
     * @param island  The island in the array we are looking to
     * @param players It's the ArrayList of all players, it gives the access to all boards
     * @return The player who conquered the island
     */
    public Player checkConqueror(Island island, ArrayList<Player> players) {

        HashMap<Player, Integer> playerPower = new HashMap<>();
        boolean[] controlledProf;

        Player exConqueror = null;
        int boolToInt;
        int tmp;
        boolean switchConqueror = false;
        int numStudentsControlling = 0;
        int max1 = 0;
        int max2 = 0;
        Player playerMax1 = null;
        Player playerMax2 = null;

        if (island.getNoEntryTile()) {
            island.removeNoEntryTile();
            //tolgo noEntryTile da personaggio
            return null;
        }

        for (Player player : players) {
            controlledProf = player.getBoard().getProfTable();
            tmp = 0;
            for (FactionColor color : FactionColor.values()) {
                if (color != this.disabledColorFlag) {
                    boolToInt = controlledProf[color.getIndex()] ? island.getStudentsOnIsland().getByColor(color) : 0;
                    tmp = tmp + boolToInt;
                }
            }
            playerPower.put(player, tmp);
        }

        for (Player player : players)
            if (player.getBoard().getTowers().getCurrentTower() == currentPlayer.getBoard().getTowers().getCurrentTower())
                playerPower.put(player, powerBonusFlag + playerPower.get(player));

        if (island.getCurrentTower() == TowerColor.NONE) {

            for (Player player : players) {
                if (playerPower.get(player) > max1) {
                    max2 = max1;
                    max1 = playerPower.get(player);
                } else if (playerPower.get(player) > max2)
                    max2 = playerPower.get(player);
            }
            if (max1 == max2)
                throw new NoIslandConquerorException();

            for (Player player : players)
                if (playerPower.get(player) > numStudentsControlling) {
                    island.setCurrentConqueror(player);
                    numStudentsControlling = playerPower.get(player);
                }
            island.getCurrentConqueror().getBoard().removeTowers(island.getNumIslands());
        } else {

            for (Player player : players) {
                if (playerPower.get(player) > max1) {
                    max2 = max1;
                    playerMax2 = playerMax1;
                    max1 = playerPower.get(player);
                    playerMax1 = player;
                } else if (playerPower.get(player) > max2) {
                    max2 = playerPower.get(player);
                    playerMax2 = player;
                }
            }
            if (max1 == max2 && max1 > playerPower.get(island.getCurrentConqueror()) + (this.noTowerFlag ? 0 : island.getNumIslands()) && playerMax1.getBoard().getTowers().getCurrentTower() != island.getCurrentTower() && playerMax2.getBoard().getTowers().getCurrentTower() != island.getCurrentTower())
                throw new NoIslandConquerorException();

            numStudentsControlling = playerPower.get(island.getCurrentConqueror());
            exConqueror = island.getCurrentConqueror();
            for (Player player : players)
                if (playerPower.get(player) > numStudentsControlling + ((switchConqueror || this.noTowerFlag) ? 0 : island.getNumIslands()) && player.getBoard().getTowers().getCurrentTower() != island.getCurrentTower()) {
                    island.setCurrentConqueror(player);
                    numStudentsControlling = playerPower.get(player);
                    if (exConqueror.getBoard().getTowers().getCurrentTower() != island.getCurrentConqueror().getBoard().getTowers().getCurrentTower())
                        switchConqueror = true;
                }
        }

        if (switchConqueror) {
            exConqueror.getBoard().addTowers(island.getNumIslands());
            island.getCurrentConqueror().getBoard().removeTowers(island.getNumIslands());
        }

        island.setTower(island.getCurrentConqueror().getBoard().getTowers().getCurrentTower());

        return island.getCurrentConqueror();

    }

    /**
     * It's a method that call checkConqueror and uniteIfPossible on a certain island (where there isn't Mother Nature)
     *
     * @param island  The island where Mother Nature is
     * @param players The players in the game
     */
    public void motherNatureActionNoMovement(Island island, ArrayList<Player> players) {
        this.checkConqueror(island, players);
        this.uniteIfPossible(island);
    }

    /**
     * This method is used for moving Mother Nature and use checkConqueror and uniteIfPossible
     *
     * @param island  It's the island where you want to move Mother Nature
     * @param players The list of all players
     * @throws MNmovementWrongException
     */
    public void motherNatureActionMovement(Island island, ArrayList<Player> players) throws MNmovementWrongException {
        moveMotherNature(island);
        motherNatureActionNoMovement(island, players);
    }

    /**
     * This method is used for moving Mother Nature
     *
     * @param island It's the island where you want to move Mother Nature
     * @throws MNmovementWrongException
     */
    public void moveMotherNature(Island island) throws MNmovementWrongException {
        int moveForward;

        if (islands.indexOf(island) >= islands.indexOf(getMotherNaturePosition()))
            moveForward = islands.indexOf(island) - islands.indexOf(getMotherNaturePosition());
        else
            moveForward = islands.size() - islands.indexOf(getMotherNaturePosition()) + islands.indexOf(island);

        int indexIslandMotherNature = islands.indexOf(this.motherNaturePosition);
        int destinationMotherNature = indexIslandMotherNature;

        if (moveForward > currentPlayer.getLastAssistantPlayed().getMNMovement() + this.additionalMNFlag || moveForward == 0)
            throw new MNmovementWrongException();

        for (int contMovement = 0; contMovement < moveForward; contMovement++) {
            destinationMotherNature = destinationMotherNature + 1;
            if (destinationMotherNature == islands.size())
                destinationMotherNature = 0;
        }

        this.motherNaturePosition = this.islands.get(destinationMotherNature);
    }

    /**
     * @param color It's the color to be disabled in a island's conquer, it's basically a flag
     */
    public void setDisabledColorFlag(FactionColor color) {
        this.disabledColorFlag = color;
    }

    /**
     * @param power It's the "bonus" for affinity calculation for the current player
     */
    public void setPowerBonusFlag(int power) {
        this.powerBonusFlag = power;
    }

    /**
     * It deletes all effects of flags
     */
    public void resetFlag() {
        this.noTowerFlag = false;
        this.powerBonusFlag = 0;
        this.disabledColorFlag = null;
        this.additionalMNFlag = 0;
    }

    /**
     * @param player It's the player who is currently playing
     */
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    /**
     * @param num It's an increase for Mother Nature movement, based on the assistant played
     */
    public void setAdditionalMNFlag(int num) {
        this.additionalMNFlag = num;
    }

}