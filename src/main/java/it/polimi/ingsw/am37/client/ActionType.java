package it.polimi.ingsw.am37.client;

import java.util.ArrayList;
import java.util.List;

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
    MOVE_MOTHER_NATURE("Move mother nature"),
    CHOOSE_CLOUD("Choose a cloud to pick students from"),
    PLAY_ASSISTANT("Play an assistant"),
    PLAY_CHARACTER("Play a character");

    public final String description;

    ActionType(String description) {
        this.description = description;
    }

    public static List<ActionType> getActionByStatus(ClientStatus status) {
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
            list.add(PLAY_CHARACTER);
            switch (status) {
                case PLAYINGASSISTANT -> list.add(PLAY_ASSISTANT);
                case MOVINGSTUDENTS -> {
                    list.add(MOVE_STUDENTS_ISLAND);
                    list.add(MOVE_STUDENTS_DINING);
                }
                case MOVINGMOTHERNATURE -> list.add(MOVE_MOTHER_NATURE);
                case CHOOSINGCLOUD -> list.add(CHOOSE_CLOUD);
            }
        }
        return list;
    }

}
