package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.exceptions.WinningException;
import it.polimi.ingsw.am37.model.student_container.UnlimitedStudentsContainer;

import java.util.ArrayList;

public class GameManager {
    /**
     * Represent the number of the players of the game this manage
     */
    private final int numOfPlayer;

    /**
     * Flag for enabling expert mode rules
     */
    private final boolean expertMode;

    /**
     * A simple array of not used professors
     */
    private boolean[] notUsedTeachers = new boolean[FactionColor.values().length];

    /**
     * The bag from where students tiles are extracted
     */
    private Bag bag;

    /**
     * A list of the clouds active in the game
     */
    private ArrayList<Cloud> clouds;

    /**
     * A list of active characters
     */
    private Character[] characters = new Character[3];

    /**
     * The island manager to handle islands and mother nature action
     */
    private IslandsManager islandsManager;

    /**
     * The turn manager to handle players, professors and turn handling
     */
    private TurnManager turnManager;

    /**
     * Construct a game manager
     *
     * @param numOfPlayer the number of players in the match
     * @param expertMode  if the game is in expert mode
     */
    public GameManager(int numOfPlayer, boolean expertMode) {
        this.numOfPlayer = numOfPlayer;
        this.expertMode = expertMode;
    }

    /**
     * Prepare a model for hosting a game: set up the managers and call their sets up methods, reset to default variables state.
     * Should be used after the constructor before starting the game
     *
     * @see IslandsManager
     * @see TurnManager
     */
    public void prepareGame(Character[] characters) {
        islandsManager = new IslandsManager();
        islandsManager.setUp();
        turnManager = new TurnManager();
        turnManager.setUp();
        fillBag();
        //num of players is the same as number of clouds
        clouds = new ArrayList<>();
        for (int i = 0; i < numOfPlayer; i++) {
            clouds.add(new Cloud(numOfPlayer != 3));
        }
        if (expertMode)
            this.characters = characters;
        //TODO add logic for characters setup
        for (FactionColor color :
                FactionColor.values()) {
            notUsedTeachers[color.getIndex()] = true;
        }
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
     * Fill the bag with all the students lefts
     */
    private void fillBag() {
        final int initialStudentsForEachColor = 24;
        bag = new Bag();
        UnlimitedStudentsContainer container = new UnlimitedStudentsContainer();
        for (FactionColor color :
                FactionColor.values()) {
            container.addStudents(initialStudentsForEachColor, color);
        }
        bag.addStudents(container);
    }

    /**
     * Reset the turn for the next player
     */
    public void resetTurn() {
        islandsManager.resetFlag();
        turnManager.resetTurn();
    }

    /**
     * Check the end of the game.
     *
     * @throws WinningException thrown one of the winning conditions is matched
     */
    public void checkWinCondition(boolean endTurn) throws WinningException {
        boolean victory = false;
        for (Player player :
                turnManager.getPlayers()) {
            if (player.getBoard().getTowers().getCurrentSize() == 0 && numOfPlayer != 4)
                throw new WinningException(player);
            if (islandsManager.getIslands().size() <= 3 || (endTurn && (player.getAssistantsDeck().size() == 0 || bag.size() == 0)))
                victory = true;
        }
        if (victory) {
            Player winner = turnManager.getPlayers().stream().min((player1, player2) -> {
                if (player1.getBoard().getTowers().getCurrentSize() == player2.getBoard().getTowers().getCurrentSize()) {
                    int prof1 = 0;
                    for (int i = 0; i < FactionColor.values().length; i++) {
                        if (player1.getBoard().getProfTable()[i]) prof1++;
                    }
                    int prof2 = 0;
                    for (int i = 0; i < FactionColor.values().length; i++) {
                        if (player2.getBoard().getProfTable()[i]) prof1++;
                    }
                    return Integer.compare(prof1, prof2);
                } else {
                    return Integer.compare(player1.getBoard().getTowers().getCurrentSize(), player2.getBoard().getTowers().getCurrentSize());
                }
            }).orElse(null);
            throw new WinningException(winner);
        }
    }

    /**
     *
     */
    public void extractCharacter() {

    }
}
