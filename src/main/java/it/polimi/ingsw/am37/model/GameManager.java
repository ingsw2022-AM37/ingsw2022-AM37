package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.character.Character;
import it.polimi.ingsw.am37.model.character.EffectHandler;
import it.polimi.ingsw.am37.model.character.Effect;
import it.polimi.ingsw.am37.model.character.Option;
import it.polimi.ingsw.am37.model.exceptions.AssistantImpossibleToPlay;
import it.polimi.ingsw.am37.model.student_container.StudentsContainer;

import java.util.*;

public class GameManager {
    /**
     * Number of characters available
     */
    private static final int NUMBEROFCHARACTERS = 3;
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
     * Default constructor of game manager class. It's the main access point of the game model
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
        this.characters = new Character[NUMBEROFCHARACTERS];
        this.notUsedTeachers = new boolean[FactionColor.values().length];
        this.bag = new Bag();
    }

    /**
     * @return Array of unused teachers
     */
    public boolean[] getNotUsedTeachers() {
        return notUsedTeachers;
    }

    /**
     * @return The bag
     */
    public Bag getBag() {
        return bag;
    }

    /**
     * @return List of clouds of this game
     */
    public ArrayList<Cloud> getClouds() {
        return clouds;
    }

    /**
     * @return The island manager
     */
    public IslandsManager getIslandsManager() {
        return islandsManager;
    }

    /**
     * @return The turn manager
     */
    public TurnManager getTurnManager() {
        return turnManager;
    }

    /**
     * Set up the game following rules settled in the creation of the game manager: create, organize and fill the
     * islands, the bag and the clouds list; after initialize turn manager and if rules are enabled create the
     * characters.
     */
    public void prepareGame() {
        //constants for set up of the game
        final Map<Integer, Integer> numberCloudsForPlayers = Map.of(2, 2, 3, 3, 4, 4);
        //follow the order of the manual of the game
        islandsManager.setUp();
        for (int i = 0; i < numberCloudsForPlayers.get(playersNumber); i++) {
            clouds.add(new Cloud(playersNumber == 2));
        }
        Arrays.fill(notUsedTeachers, true);
        turnManager.setUp(bag);
        //TODO handle assistants logic

        // advanced logic only
        if (this.advancedMode) {
            List<Effect> temp = new ArrayList<>(Arrays.stream(Effect.values()).toList());
            Collections.shuffle(temp);
            for (int i = 0; i < NUMBEROFCHARACTERS; i++) {
                Effect effect = temp.get(i);
                characters[i] = new Character(effect.getInitialPrice(), effect, bag);
            }
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
        if (container == null) {
            throw new IllegalArgumentException("container of moveStudentsToIsland can't be null");
        }
        final int maxForMovement = 3;
        if (container.size() > maxForMovement) throw new RuntimeException();
        Island island = islandsManager.getIslands().stream().
                filter(island1 -> island1.getIslandId() == islandId)
                .findFirst()
                .orElseThrow();
        turnManager.getCurrentPlayer().getBoard().getEntrance().removeContainer(container);
        island.addStudents(container);
    }

    /**
     * Move the students to the dining room of the current player
     *
     * @param container The students to move inside the dining room
     * @throws IllegalArgumentException When the container is null
     */
    public void moveStudentsToDining(StudentsContainer container) throws IllegalArgumentException {
        if (container == null) {
            throw new IllegalArgumentException("container of moveStudentsToDining can't be null");
        }
        final int maxForMovement = 3;
        if (container.size() > maxForMovement) throw new RuntimeException();
        turnManager.getCurrentPlayer().getBoard().getEntrance().removeContainer(container);
        turnManager.addStudentsToDining(container);
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
        if (assistant == null) {
            throw new IllegalArgumentException("Assistant must not be null");
        }
        try {
            turnManager.nextTurnAssistant(assistant);
        } catch (AssistantImpossibleToPlay exception) {
            throw new AssistantImpossibleToPlay(exception);
        }
    }

    /**
     * Move mother nature island by {@param stepForward} islands and activate the associated logic: checking conqueror
     * of the island and if it is possible to unite islands
     *
     * @param stepsForward The num of forward island movement of mother nature
     */
    public void moveMotherNature(int stepsForward) {
        islandsManager.motherNatureActionMovement(stepsForward, turnManager.getPlayers());
    }

    /**
     * Use a character and activate its effect; if it's the first usage increments its costs
     *
     * @param character Character played
     */
    public void playCharacter(Character character, Option option) {
        Character used = Arrays.stream(characters).filter(character::equals).findFirst().orElseThrow();
        used.useEffect(option);
    }
}
