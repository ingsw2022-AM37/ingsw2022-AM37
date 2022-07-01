package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.character.State;
import it.polimi.ingsw.am37.model.exceptions.MNmovementWrongException;
import it.polimi.ingsw.am37.model.exceptions.WinningException;
import it.polimi.ingsw.am37.model.student_container.FixedUnlimitedStudentsContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

@SuppressWarnings("ConstantConditions")
/**
 * Class used to control all actions on islands
 */
public class IslandsManager {

    /**
     * Who is playing
     */
    private Player currentPlayer;

    /**
     * Total islands
     */
    private final ArrayList<Island> islands;

    /**
     * Position of Mother Nature
     */
    private Island motherNaturePosition;

    /**
     * If towers aren't used in affinity check
     */
    private boolean noTowerFlag;

    /**
     * If a certain color is disabled in affinity check
     */
    private FactionColor disabledColorFlag;

    /**
     * If current player has a bonus in affinity check
     */
    private int powerBonusFlag;

    /**
     * If you can move Mother Nature longer
     */
    private int additionalMNFlag;

    /**
     * State of the character with no entry tiles
     */
    private State stateCharacterNoEntryTile = null;

    /**
     * Default constructor
     */
    public IslandsManager() {
        this.islands = new ArrayList<>();
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
     * This is a one-time method, it creates islands, set Mother Nature on a random one and put a student on each
     * island, excluding the one with Mother Nature and the opposite one
     */
    public void setUp() {
        final int numOfInitialIslands = 12;

        Random random = new Random();
        int motherNatureHere = random.nextInt(0, 12);

        ArrayList<FactionColor> initialFalseBag = new ArrayList<>();
        Collections.addAll(initialFalseBag, FactionColor.values());
        Collections.addAll(initialFalseBag, FactionColor.values());
        Collections.shuffle(initialFalseBag);

        for (int i = 0; i < numOfInitialIslands; i++) {
            islands.add(i, new Island(new FixedUnlimitedStudentsContainer(), i));
            if (i == motherNatureHere) {
                this.motherNaturePosition = islands.get(i);
                islands.get(i).setMotherNatureHere(true);
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
     * The method checks if an island has next to it another island (or islands) with the same color of tower, if yes
     * then they will be merged in Island with index islandId and others are eliminated
     *
     * @param island It's needed to point to the island where there is MotherNature
     * @throws IllegalArgumentException When islandId doesn't identify an island
     */
    public void uniteIfPossible(Island island) {

        int islandId = islands.indexOf(island);
        boolean UnitedDx = false;

        if (island.getCurrentTower() != TowerColor.NONE) {

            if (islandId == 0 && islands.size() > 1) {
                if (island.getCurrentTower() == islands.get(islands.size() - 1).getCurrentTower()) {
                    island.setNumIslands(island.getNumIslands() + islands.get(islands.size() - 1).getNumIslands());
                    island.getStudentsOnIsland().uniteContainers(islands.get(islands.size() - 1).getStudentsOnIsland());
                    int temp = islands.get(islands.size() - 1).getNoEntryTile();
                    islands.get(islands.size() - 1).setNumIslands(0);
                    islands.remove(islands.size() - 1);
                    island.addNoEntryTile(temp);
                }
            }

            if (islandId + 1 < islands.size() && islands.size() > 1) {
                if (island.getCurrentTower() == islands.get(islandId + 1).getCurrentTower()) {
                    island.setNumIslands(island.getNumIslands() + islands.get(islandId + 1).getNumIslands());
                    island.getStudentsOnIsland().uniteContainers(islands.get(islandId + 1).getStudentsOnIsland());

                    UnitedDx = true;
                    int temp = islands.get(islandId + 1).getNoEntryTile();
                    islands.get(islandId + 1).setNumIslands(0);
                    islands.remove(islandId + 1);
                    island.addNoEntryTile(temp);
                    for (int i = islandId + 1; i < islands.size(); i++)
                        islands.get(i).setIslandId(islands.get(i).getIslandId() - 1);
                }
            }

            if (islandId - 1 >= 0 && islands.size() > 1) {
                if (island.getCurrentTower() == islands.get(islandId - 1).getCurrentTower()) {
                    island.setNumIslands(island.getNumIslands() + islands.get(islandId - 1).getNumIslands());
                    island.getStudentsOnIsland().uniteContainers(islands.get(islandId - 1).getStudentsOnIsland());
                    int temp = islands.get(islandId - 1).getNoEntryTile();
                    islands.get(islandId - 1).setNumIslands(0);
                    islands.remove(islandId - 1);
                    island.addNoEntryTile(temp);
                    for (int i = islandId - 1; i < islands.size(); i++)
                        islands.get(i).setIslandId(islands.get(i).getIslandId() - 1);
                    islandId--;
                }
            }

            if (islandId == islands.size() - 1 && !UnitedDx && islands.size() > 1) {
                if (island.getCurrentTower() == islands.get(0).getCurrentTower()) {
                    island.setNumIslands(island.getNumIslands() + islands.get(0).getNumIslands());
                    island.getStudentsOnIsland().uniteContainers(islands.get(0).getStudentsOnIsland());
                    int temp = islands.get(0).getNoEntryTile();
                    islands.get(0).setNumIslands(0);
                    islands.remove(0);
                    island.addNoEntryTile(temp);
                    for (Island value : islands) value.setIslandId(value.getIslandId() - 1);

                }
            }
        }
    }

    /**
     * The method check which player has the most students on the island, a player have students of one color when he
     * controls their professor. Each player has a towerColor and the tower means who is controlling it. Switches of
     * conqueror(and of towers) are possible.
     *
     * @param island  The island in the array we are looking to
     * @param players It's the ArrayList of all players, it gives the access to all boards
     */
    public void checkConqueror(Island island, ArrayList<Player> players) throws WinningException {
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

        if (island.getNoEntryTile() > 0) {
            island.removeNoEntryTile();
            this.stateCharacterNoEntryTile.setNoEntryTiles(this.stateCharacterNoEntryTile.getNoEntryTiles() + 1);
            return;
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

        playerPower.put(currentPlayer, powerBonusFlag + playerPower.get(currentPlayer));

        if (island.getCurrentTower() == TowerColor.NONE) {
            for (Player player : players) {
                if (playerPower.get(player) > max1) {
                    max2 = max1;
                    max1 = playerPower.get(player);
                } else if (playerPower.get(player) > max2)
                    max2 = playerPower.get(player);
            }
            if (max1 == max2)
                return;

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
            if (max1 == max2 && max1 > playerPower.get(island.getCurrentConqueror()) + (this.noTowerFlag ? 0 :
                    island.getNumIslands()) && playerMax1.getBoard().getTowers().getCurrentTower() != island.getCurrentTower() && playerMax2.getBoard().getTowers().getCurrentTower() != island.getCurrentTower())
                return;

            numStudentsControlling = playerPower.get(island.getCurrentConqueror());
            exConqueror = island.getCurrentConqueror();
            for (Player player : players)
                if (playerPower.get(player) > numStudentsControlling + ((switchConqueror || this.noTowerFlag) ? 0 :
                        island.getNumIslands()) && player.getBoard().getTowers().getCurrentTower() != island.getCurrentTower()) {
                    island.setCurrentConqueror(player);
                    numStudentsControlling = playerPower.get(player);
                    switchConqueror = true;
                }
        }
        if (switchConqueror) {
            exConqueror.getBoard().addTowers(island.getNumIslands());
            island.getCurrentConqueror().getBoard().removeTowers(island.getNumIslands());
        }

        island.setTower(island.getCurrentConqueror().getBoard().getTowers().getCurrentTower());
    }

    /**
     * It's a method that call checkConqueror and uniteIfPossible on a certain island (where there isn't Mother Nature)
     *
     * @param island  The island where Mother Nature is
     * @param players The players in the game
     */
    public void motherNatureActionNoMovement(Island island, ArrayList<Player> players) throws WinningException {
        this.checkConqueror(island, players);
        this.uniteIfPossible(island);
    }

    /**
     * This method is used for moving Mother Nature and use checkConqueror and uniteIfPossible
     *
     * @param destinationIslandId It's the island where you want to move Mother Nature
     * @param players             The list of all players
     * @throws MNmovementWrongException If the movement can't be performed.
     */
    public void motherNatureActionMovement(int destinationIslandId, ArrayList<Player> players) throws MNmovementWrongException, WinningException {
        int stepsForward = islands.indexOf(getIslands().get(destinationIslandId)) - islands.indexOf(getMotherNaturePosition());

        int temp = islands.indexOf(getMotherNaturePosition()) + stepsForward;
        Island island = islands.get(temp > islands.size() ? temp - islands.size() : temp);
        moveMotherNature(island);
        motherNatureActionNoMovement(island, players);
    }

    /**
     * This method is used for moving Mother Nature
     *
     * @param island It's the island where you want to move Mother Nature
     * @throws MNmovementWrongException If the movement can't be performed.
     */
    public void moveMotherNature(Island island) throws MNmovementWrongException {
        int moveForward;

        if (islands.indexOf(island) == islands.indexOf(getMotherNaturePosition()))
            moveForward = islands.size();

        else if (islands.indexOf(island) > islands.indexOf(getMotherNaturePosition()))
            moveForward = islands.indexOf(island) - islands.indexOf(getMotherNaturePosition());
        else
            moveForward = islands.size() - islands.indexOf(getMotherNaturePosition()) + islands.indexOf(island);

        int destinationMotherNature = islands.indexOf(this.motherNaturePosition);

        if (moveForward > currentPlayer.getLastAssistantPlayed().getMNMovement() + this.additionalMNFlag || moveForward == 0)
            throw new MNmovementWrongException("You can't move Mother Nature here");

        for (int contMovement = 0; contMovement < moveForward; contMovement++) {
            destinationMotherNature = destinationMotherNature + 1;
            if (destinationMotherNature == islands.size())
                destinationMotherNature = 0;
        }

        islands.get(islands.indexOf(motherNaturePosition)).setMotherNatureHere(false);
        this.motherNaturePosition = this.islands.get(destinationMotherNature);
        islands.get(destinationMotherNature).setMotherNatureHere(true);
    }

    /**
     * @param color It's the color to be disabled on an island's conquer, it's basically a flag
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
    public void resetFlags() {
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

    /**
     * @param state A character's state linked to all islands
     */
    public void setStateCharacterNoEntryTile(State state) {
        this.stateCharacterNoEntryTile = state;
    }

}
