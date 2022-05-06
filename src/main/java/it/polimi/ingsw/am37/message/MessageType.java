package it.polimi.ingsw.am37.message;

/**
 *
 */
public enum MessageType {
    LOGIN("LoginMessage"),
    LOBBY_REQUEST("LobbyRequestMessage"),
    START_GAME("StartGameMessage"),
    UPDATE("UpdateMessage"),
    ERROR("ErrorMessage"),
    PLAY_ASSISTANT("PlayAssistanMessage"),
    CHOOSE_TEAM("ChoseTeamMessage"),
    STUDENTS_TO_DINING("StudentsToDiningMessage"),
    MOVE_MOTHER_NATURE("StudentsToDiningMessage"),
    PLAY_CHARACTER("PlayCharacterMessage"),
    NEXT_TURN("NextTurnMessage"),
    STUDENTS_TO_ISLAND("StudentsToIslandMessage"),
    CHOOSE_CLOUD("ChooseCloudMessage"),
    CONFIRM("ConfirmMessage");

    private final String className;

    MessageType(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}