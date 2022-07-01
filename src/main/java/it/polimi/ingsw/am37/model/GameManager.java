package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.character.Character;
import it.polimi.ingsw.am37.model.character.Effect;
import it.polimi.ingsw.am37.model.character.EffectDatabase;
import it.polimi.ingsw.am37.model.character.Option;
import it.polimi.ingsw.am37.model.exceptions.*;
import it.polimi.ingsw.am37.model.student_container.StudentsContainer;

import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * External class of model used to control everything and make every possible action
 */
public class GameManager {
    /**
     * A lock to ensure synchronized methods.
     */
    private final Object lock;

    /**
     * Number of characters available
     */
    public static final int NUMBER_OF_CHARACTERS = 3;
    /**
     *
     */
    public static final int[] MAX_FOR_MOVEMENTS = new int[]{3, 4};
    /**
     * Number of player in the game handled by this manager
     */
    private final int playersNumber;
    /**
     * Flag for game mode used: FALSE represent basic rules, TRUE represent advanced rules
     */
    private final boolean advancedMode;
    /**
     * Array of professors (represented as boolean if they are present or not) not assigned in any boards
     *
     * @see FactionColor for color definition of each prof
     */
    private final boolean[] notUsedTeachers;

    /**
     * Model of the game bag from where students are extracted by players
     */
    private final Bag bag;

    /**
     * List of all cloud available in a game.
     */
    private final ArrayList<Cloud> clouds;

    /**
     * Array of characters available in this game
     */
    final private Character[] characters;

    /**
     * Reference of the manager of islands logic. Used for accessing islands and their functionality
     */
    private final IslandsManager islandsManager;

    /**
     * Reference of the manager of turns logic and player interconnections
     */
    private final TurnManager turnManager;

    /**
     * Default constructor of Game Manager class. It's the main access point of the game model.
     *
     * @param playersNumber Number of player of this instance of game
     * @param advancedMode  Enable advanced mode or disable it
     */
    public GameManager(int playersNumber, boolean advancedMode) {
        this.playersNumber = playersNumber;
        this.advancedMode = advancedMode;
        this.clouds = new ArrayList<>();
        this.turnManager = new TurnManager(advancedMode, playersNumber);
        this.islandsManager = new IslandsManager();
        this.characters = new Character[NUMBER_OF_CHARACTERS];
        this.notUsedTeachers = new boolean[FactionColor.values().length];
        this.bag = new Bag();
        this.lock = new Object();
    }

    /**
     * @return Array of unused Teachers
     */
    public boolean[] getNotUsedTeachers() {
        synchronized (lock) {
            return notUsedTeachers;
        }
    }

    /**
     * @return The bag
     */
    public Bag getBag() {
        synchronized (lock) {
            return bag;
        }
    }

    /**
     * @return List of Clouds of this game.
     */
    public ArrayList<Cloud> getClouds() {
        synchronized (lock) {
            return clouds;
        }
    }

    /**
     * @return The Island Manager
     */
    public IslandsManager getIslandsManager() {
        synchronized (lock) {
            return islandsManager;
        }
    }

    /**
     * @return The Turn Manager
     */
    public TurnManager getTurnManager() {
        synchronized (lock) {
            return turnManager;
        }
    }

    /**
     * @return the Characters that can be played.
     */
    public Character[] getCharacters() {
        synchronized (lock) {
            return characters;
        }
    }

    /**
     * Set up the game following rules settled in the creation of the game manager: create, organize and fill the
     * islands, the bag and the clouds list; after initialize turn manager and if rules are enabled create the
     * characters.
     */
    public void prepareGame() {
        synchronized (lock) {
            //constants for set up of the game
            final Map<Integer, Integer> numberCloudsForPlayers = Map.of(2, 2, 3, 3, 4, 4);
            //follow the order of the manual of the game
            islandsManager.setUp();
            for (int i = 0; i < numberCloudsForPlayers.get(playersNumber); i++) {
                clouds.add(new Cloud(playersNumber == 2, i));
            }
            Arrays.fill(notUsedTeachers, true);
            turnManager.setUp(bag);

            // advanced logic only
            if (this.advancedMode) {
                EffectDatabase.setUp();
                List<Effect> temp = new ArrayList<>(Arrays.stream(Effect.values()).toList());
                Collections.shuffle(temp);
                /*                           Character Testing
                 * -----CHARACTER--------------TEST DIFFICULTY---------------HINT------------------
                 * -    Monk:                      EASY                      DONE        //No hints
                 * -    Magic Postman:             EASY                      DONE        //In order to function correctly it must be played before moving mother nature
                 * -    Knight:                    EASY                      DONE        //No hints
                 * -    Mushroom Man:              EASY                      DONE        //In order to function correctly it must be played before moving mother nature
                 * -    Princess:                  EASY                      DONE        //No hints
                 * -    Farmer:                    MEDIUM                    DONE        //In order to function correctly it must be played before moving students in the dining
                 * -    Centaur:                   MEDIUM                    DONE        //In order to function correctly it must be played before moving mother nature
                 * -    Jester:                    MEDIUM                    DONE        //No hints
                 * -    Minstrel:                  MEDIUM                    DONE        //In order to function correctly you must have at least two students in the dining
                 * -    Herald:                    MEDIUM                    DONE
                 * -    Grandma:                   MEDIUM                    DONE        //In order to function correctly it must be played before moving mother nature
                 * -    Thief:                     HARD                      DONE        //No hints
                 */
                for (int i = 0; i < NUMBER_OF_CHARACTERS; i++) {
                    Effect effect = temp.get(i);
                    characters[i] = new Character(effect.getInitialPrice(), effect, bag);
                    if (effect == Effect.GRANDMA) islandsManager.setStateCharacterNoEntryTile(characters[i].getState());
                }
            }
        }
    }

    /**
     * Move the students to the dining room of the current player
     *
     * @param container The students to move inside the dining room
     * @throws IllegalArgumentException When the container is null
     */
    public void moveStudentsToDining(StudentsContainer container) throws IllegalArgumentException {
        synchronized (lock) {
            if (container == null) {
                throw new IllegalArgumentException("container of moveStudentsToDining can't be null");
            }
            if (container.size() > MAX_FOR_MOVEMENTS[playersNumber % 2]) throw new RuntimeException();
            turnManager.getCurrentPlayer().getBoard().getEntrance().removeContainer(container);
            turnManager.addStudentsToDining(container);
        }
    }

    /**
     * Move the students inside the container into an island provided
     *
     * @param container The students to move
     * @param islandId  The id of destination island where students are moved
     * @throws IllegalArgumentException When the container is null
     */
    public void moveStudentsToIsland(StudentsContainer container, int islandId) throws IllegalArgumentException {
        synchronized (lock) {
            if (container == null) {
                throw new IllegalArgumentException("container of moveStudentsToIsland can't be null");
            }
            if (container.size() > MAX_FOR_MOVEMENTS[playersNumber % 2]) throw new RuntimeException();
            Island island = islandsManager.getIslands()
                    .stream()
                    .filter(island1 -> island1.getIslandId() == islandId)
                    .findFirst()
                    .orElseThrow();
            turnManager.getCurrentPlayer().getBoard().getEntrance().removeContainer(container);
            island.addStudents(container);
        }
    }

    /**
     * Register the used assistant inside the turn manager
     *
     * @param assistant The assistant played
     * @throws AssistantImpossibleToPlay When the assistant is impossible to play because it's not in the desk of the
     *                                   current player
     * @throws IllegalArgumentException  When the assistant is null
     */
    public void playAssistant(Assistant assistant) throws AssistantImpossibleToPlay, IllegalArgumentException {
        synchronized (lock) {
            if (assistant == null) {
                throw new IllegalArgumentException("Assistant must not be null");
            }
            try {
                turnManager.playAssistant(assistant);
            } catch (AssistantImpossibleToPlay exception) {
                throw new AssistantImpossibleToPlay(exception);
            }
        }
    }

    /**
     * Move mother nature island by {@param stepForward} islands and activate the associated logic: checking conqueror
     * of the island and if it is possible to unite islands
     *
     * @param islandId The num of forward island movement of mother nature
     */
    public void moveMotherNature(int islandId) throws MNmovementWrongException, WinningException {
        synchronized (lock) {
            islandsManager.setCurrentPlayer(turnManager.getCurrentPlayer());
            islandsManager.motherNatureActionMovement(islandId, turnManager.getPlayers());
            if (islandsManager.getIslands().size() <= 3) {
                throw new WinningException(calculateWinningPlayer());
            }
        }
    }

    /**
     * returns the player which has fewer towers in the board
     */
    public Player calculateWinningPlayer() {
        Player winnerPlayer = turnManager.getCurrentPlayer();
        for (Player player : turnManager.getPlayers()) {
            if (player.getBoard().getTowers().getCurrentSize() < winnerPlayer.getBoard().getTowers().getCurrentSize())
                if (player.getBoard().getPossessedProf() > player.getBoard().getPossessedProf())
                    winnerPlayer = player;
        }
        return winnerPlayer;
    }

    /**
     * Use a character and activate its effect; if it's the first usage increments its costs
     *
     * @param character Character played
     */
    public void playCharacter(Character character, Option option) throws CharacterImpossibleToPlay {
        synchronized (lock) {
            if (turnManager.getCurrentPlayer().getNumberOfCoins() >= character.getCurrentPrice()) {
                if (character.getEffectType() == Effect.MONK || character.getEffectType() == Effect.PRINCESS)
                    if (bag.isEmpty()) {
                        turnManager.setLastRound(true);
                        throw new CharacterImpossibleToPlay("Bag is empty, you can't play this character");
                    }
                turnManager.getCurrentPlayer().useCharacter(character, option);
            } else throw new CharacterImpossibleToPlay("You can't play Character, not enough coins");
        }
    }

    /**
     * Choose a cloud.
     */
    public void chooseCloud(String cloudId) throws IllegalArgumentException, StudentSpaceException {
        synchronized (lock) {
            Cloud currentCloud = clouds.stream()
                    .filter(cloud -> cloud.getCloudId().equals(cloudId))
                    .findFirst()
                    .orElseThrow();
            turnManager.getCurrentPlayer().getBoard().getEntrance().uniteContainers(currentCloud.removeStudents());
        }
    }

    /**
     * Next turn method.
     */
    public void nextTurn() {
        synchronized (lock) {
            islandsManager.resetFlags();
            turnManager.resetFlags();
            turnManager.nextTurn();
            turnManager.setCurrentPlayer(turnManager.getOrderPlayed().get(0));
            islandsManager.setCurrentPlayer(turnManager.getCurrentPlayer());
        }
    }

    /**
     * This method register the given listeners to all the updatable objects in the model
     *
     * @param listener the listener to register inside the model
     */
    public void registerListener(PropertyChangeListener listener) {
        for (Island island : islandsManager.getIslands()) {
            island.addPropertyChangeListener(listener);
        }
        for (Cloud cloud : clouds) {
            cloud.addPropertyChangeListener(listener);
        }
        for (Player player :
                turnManager.getPlayers()) {
            player.addPropertyChangeListener(listener);
        }
        if (advancedMode) {
            for (Character character :
                    characters) {
                character.addPropertyChangeListener(listener);
            }
        }
    }
}
