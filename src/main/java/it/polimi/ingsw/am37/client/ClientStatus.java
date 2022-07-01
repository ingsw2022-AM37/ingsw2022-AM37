package it.polimi.ingsw.am37.client;

import java.util.List;

/**
 * Used to keep track of client possible actions
 */
public enum ClientStatus {
    LOGIN,
    CHOOSINGNAME,
    CHOOSINGLOBBY,
    PLAYINGASSISTANT,
    MOVINGSTUDENTS,
    MOVINGMOTHERNATURE,
    CHOOSINGCLOUD,
    WAITINGFORTURN,
    ENDGAME;

    /**
     * List of status during game in lobby
     */
    public final static List<ClientStatus> activeStatus = List.of(PLAYINGASSISTANT, MOVINGSTUDENTS,
            MOVINGMOTHERNATURE, CHOOSINGCLOUD);
}
