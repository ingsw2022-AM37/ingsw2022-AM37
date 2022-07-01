package it.polimi.ingsw.am37.message;

/**
 * Enumeration of possible messages between server and clients
 */
public enum MessageType {
    LOGIN("LoginMessage"),
    LOBBY_REQUEST("LobbyRequestMessage"),
    UPDATE("UpdateMessage"),
    ACTIVE_LOBBIES("ActiveLobbiesMessage"),
    ERROR("ErrorMessage"),
    PLAY_ASSISTANT("PlayAssistantMessage"),
    CHOOSE_TEAM("ChooseTeamMessage"),
    STUDENTS_TO_DINING("StudentsToDiningMessage"),
    MOVE_MOTHER_NATURE("MoveMotherNatureMessage"),
    PLAY_CHARACTER("PlayCharacterMessage"),
    NEXT_TURN("NextTurnMessage"),
    STUDENTS_TO_ISLAND("StudentsToIslandMessage"),
    CHOOSE_CLOUD("ChooseCloudMessage"),
    CONFIRM("ConfirmMessage"),
    PING("PingMessage"),
    PLANNING_PHASE("PlanningPhaseMessage"),
    RESILIENCE("ResilienceMessage"),
    START_GAME("StartGameMessage"),
    END_GAME("EndGameMessage");

    private final String className;

    MessageType(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}