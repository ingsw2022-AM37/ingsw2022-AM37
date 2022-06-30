package it.polimi.ingsw.am37.client;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents all the available actions that a player can take with a short string description that is used by CLI to
 * describe the action. Note that not all actions are performable
 */
public enum ActionType {
    SHOW_MENU("Show this menu again"),
    SHOW_TABLE("Show the table"),
    SHOW_STATUS("Show a player's status"),
    SHOW_DECK("Show your assistant deck"),
    SHOW_CONNECTION("Show info about connection"),
    SHOW_PLAYERS("Show connected player"),
    CLOSE_GAME("Close the game"),
    MOVE_STUDENTS_ISLAND("Move some students to island"),
    MOVE_STUDENTS_DINING("Move some students to dining room of your board"),
    MOVE_STUDENTS_UNDEFINED(""),
    MOVE_MOTHER_NATURE("Move mother nature"),
    CHOOSE_CLOUD("Choose a cloud to pick students from"),
    PLAY_ASSISTANT("Play an assistant"),
    PLAY_CHARACTER("Play a character");

    /**
     * Description of action type
     */
    public final String description;

    /**
     * Default constructor
     */
    ActionType(String description) {
        this.description = description;
    }

    /**
     * List of available actions
     */
    private final static List<ActionType> availableActions = new ArrayList<>();

    /**
     * @return all possible actions
     */
    public static List<ActionType> getActions() {
        return availableActions;
    }

    /**
     * Method used to update available actions based on client status
     * @param status client status
     * @param advancedRules if advanced rules are activated
     */
    public static void updateAvailableAction(ClientStatus status, boolean advancedRules) {
        availableActions.clear();
        availableActions.addAll(getActionByStatus(status, advancedRules));
    }

    /**
     * @param status client status
     * @param advancedRules if advanced rules are activated
     * @return list of possible actions
     */
    private static List<ActionType> getActionByStatus(ClientStatus status, boolean advancedRules) {
        List<ActionType> list = new ArrayList<>(List.of(
                SHOW_MENU,
                SHOW_TABLE,
                SHOW_STATUS,
                SHOW_DECK,
                SHOW_CONNECTION,
                SHOW_PLAYERS,
                CLOSE_GAME
        ));
        if (ClientStatus.activeStatus.contains(status)) {
            if (advancedRules)
                list.add(PLAY_CHARACTER);
            switch (status) {
                case PLAYINGASSISTANT -> list.add(PLAY_ASSISTANT);
                case MOVINGSTUDENTS -> {
                    list.add(MOVE_STUDENTS_ISLAND);
                    list.add(MOVE_STUDENTS_DINING);
                    list.add(MOVE_STUDENTS_UNDEFINED);
                }
                case MOVINGMOTHERNATURE -> list.add(MOVE_MOTHER_NATURE);
                case CHOOSINGCLOUD -> list.add(CHOOSE_CLOUD);
            }
        }
        return list;
    }

}
