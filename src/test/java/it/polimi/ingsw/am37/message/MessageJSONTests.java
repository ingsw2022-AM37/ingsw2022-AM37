package it.polimi.ingsw.am37.message;

import com.google.gson.Gson;
import it.polimi.ingsw.am37.model.FactionColor;
import it.polimi.ingsw.am37.model.WizardTeam;
import it.polimi.ingsw.am37.model.character.Effect;
import it.polimi.ingsw.am37.model.character.Option;
import it.polimi.ingsw.am37.model.student_container.UnlimitedStudentsContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class MessageJSONTests {
    static Gson gson;

    @BeforeAll
    static void beforeAll() {
        gson = new MessageGsonBuilder().registerMessageAdapter()
                .registerStudentContainerAdapter()
                .getGsonBuilder()
                .create();
    }

    @Test
    @DisplayName("Serialization and deserialization of LoginMessage")
    void loginJSONTest() {
        LoginMessage loginMessage = new LoginMessage("110011", "bramba2000");
        String json = gson.toJson(loginMessage);
        assertNotNull(json);
        Message newMessage = gson.fromJson(json, Message.class);
        LoginMessage newLoginMessage = (LoginMessage) newMessage;
        assertEquals(loginMessage.UUID, loginMessage.UUID);
        assertEquals(loginMessage.getNickname(), loginMessage.getNickname());
    }

    @Test
    @DisplayName("Serialization and deserialization of ConfirmMessage")
    void confirmJSONTest() {
        ConfirmMessage confirmMessage = new ConfirmMessage("110011");
        String json = gson.toJson(confirmMessage);
        assertNotNull(json);
        Message newMessage = gson.fromJson(json, Message.class);
        ConfirmMessage newConfirmMessage = (ConfirmMessage) newMessage;
        assertEquals(confirmMessage.UUID, newConfirmMessage.UUID);
    }

    @Test
    @DisplayName("Serialization and deserialization of ErrorMessage")
    void errorMessageJSONTest() {
        ErrorMessage errorMessage = new ErrorMessage("110011", "error message");
        String json = gson.toJson(errorMessage);
        assertNotNull(json);
        Message newMessage = gson.fromJson(json, Message.class);
        ErrorMessage newErrorMessage = (ErrorMessage) newMessage;
        assertEquals(errorMessage.UUID, newErrorMessage.UUID);
        assertEquals(errorMessage.getMessage(), newErrorMessage.getMessage());
    }

    @Test
    @DisplayName("Serialization and deserialization of ChooseCloudMessage")
    void chooseCloudMessageJSONTest() {
        ChooseCloudMessage chooseCloudMessage = new ChooseCloudMessage("110011", "405040320");
        String json = gson.toJson(chooseCloudMessage);
        assertNotNull(json);
        Message newMessage = gson.fromJson(json, Message.class);
        ChooseCloudMessage newChooseCloudMessage = (ChooseCloudMessage) newMessage;
        assertEquals(chooseCloudMessage.UUID, newChooseCloudMessage.UUID);
        assertEquals(chooseCloudMessage.getCloudId(), newChooseCloudMessage.getCloudId());
    }

    @Test
    @DisplayName("Serialization and deserialization of ChooseTeamMessage")
    void chooseTeamMessageJSONTest() {
        ChooseTeamMessage chooseTeamMessage = new ChooseTeamMessage("110011", WizardTeam.TEAM1);
        String json = gson.toJson(chooseTeamMessage);
        assertNotNull(json);
        Message newMessage = gson.fromJson(json, Message.class);
        ChooseTeamMessage newChooseTeamMessage = (ChooseTeamMessage) newMessage;
        assertEquals(chooseTeamMessage.UUID, newChooseTeamMessage.UUID);
        assertEquals(WizardTeam.TEAM1, newChooseTeamMessage.getDesiredTeam());
    }

    @Test
    @DisplayName("Serialization and deserialization of LobbyRequestMessage")
    void lobbyRequestMessageJSONTest() {
        LobbyRequestMessage lobbyRequestMessage = new LobbyRequestMessage("110011", 2, true);
        String json = gson.toJson(lobbyRequestMessage);
        assertNotNull(json);
        Message newMessage = gson.fromJson(json, Message.class);
        LobbyRequestMessage newLobbyRequestMessage = (LobbyRequestMessage) newMessage;
        assertEquals(lobbyRequestMessage.UUID, newLobbyRequestMessage.UUID);
        assertTrue(newLobbyRequestMessage.isDesiredAdvanceMode());
        assertEquals(2, newLobbyRequestMessage.getDesiredSize());
    }

    @Test
    @DisplayName("Serialization and deserialization of MoveMotherNatureMessage")
    void moveMotherNatureMessageJSONTest() {
        MoveMotherNatureMessage moveMotherNatureMessage = new MoveMotherNatureMessage("110011", 2);
        String json = gson.toJson(moveMotherNatureMessage);
        assertNotNull(json);
        Message newMessage = gson.fromJson(json, Message.class);
        MoveMotherNatureMessage newMoveMotherNatureMessage = (MoveMotherNatureMessage) newMessage;
        assertEquals(moveMotherNatureMessage.UUID, newMoveMotherNatureMessage.UUID);
        assertEquals(2, newMoveMotherNatureMessage.getIslandId());
    }

    @Test
    @DisplayName("Serialization and deserialization of NextTurnMessage")
    void nextTurnMessageJSONTest() {
        NextTurnMessage nextTurnMessage = new NextTurnMessage("110011", "110012");
        String json = gson.toJson(nextTurnMessage);
        assertNotNull(json);
        Message newMessage = gson.fromJson(json, Message.class);
        NextTurnMessage newNextTurnMessage = (NextTurnMessage) newMessage;
        assertEquals(nextTurnMessage.UUID, newNextTurnMessage.UUID);
        assertEquals("110012", newNextTurnMessage.getNextPlayerUUID());
    }

    @Test
    @DisplayName("Serialization and deserialization of PingMessage")
    void pingMessageJSONTest() {
        PingMessage pingMessage = new PingMessage("110011");
        String json = gson.toJson(pingMessage);
        assertNotNull(json);
        Message newMessage = gson.fromJson(json, Message.class);
        PingMessage newPingMessage = (PingMessage) newMessage;
        assertEquals(pingMessage.UUID, newPingMessage.UUID);
    }

    @Test
    @DisplayName("Serialization and deserialization of PlayAssistantMessage")
    void playAssistantMessageJSONTest() {
        PlayAssistantMessage playAssistantMessage = new PlayAssistantMessage("110011", 40);
        String json = gson.toJson(playAssistantMessage);
        assertNotNull(json);
        Message newMessage = gson.fromJson(json, Message.class);
        PlayAssistantMessage newPlayAssistantMessage = (PlayAssistantMessage) newMessage;
        assertEquals(playAssistantMessage.UUID, newPlayAssistantMessage.UUID);
        assertEquals(40, newPlayAssistantMessage.getCardValue());
    }

    @Test
    @DisplayName("Serialization and deserialization of PlayCharacterMessage")
    void playCharacterMessageJSONTest() {
        PlayCharacterMessage playCharacterMessage = new PlayCharacterMessage("110011", Effect.GRANDMA,
                mock(Option.class));
        String json = gson.toJson(playCharacterMessage);
        assertNotNull(json);
        Message newMessage = gson.fromJson(json, Message.class);
        PlayCharacterMessage newPlayCharacterMessage = (PlayCharacterMessage) newMessage;
        assertEquals(playCharacterMessage.UUID, newPlayCharacterMessage.UUID);
        assertEquals(Effect.GRANDMA, newPlayCharacterMessage.getChosenCharacter());
    }

    @Test
    @DisplayName("Serialization and deserialization of StudentsToDiningMessage")
    void studentsToDiningMessageJSONTest() {
        UnlimitedStudentsContainer container = new UnlimitedStudentsContainer();
        container.addStudents(3, FactionColor.BLUE);
        StudentsToDiningMessage studentsToDiningMessage = new StudentsToDiningMessage("110011", container);
        String json = gson.toJson(studentsToDiningMessage);
        assertNotNull(json);
        Message newMessage = gson.fromJson(json, Message.class);
        StudentsToDiningMessage newStudentsToDiningMessage = (StudentsToDiningMessage) newMessage;
        assertEquals(studentsToDiningMessage.UUID, newStudentsToDiningMessage.UUID);
        assertEquals(3, newStudentsToDiningMessage.getContainer().getByColor(FactionColor.BLUE));
    }

    @Test
    @DisplayName("Serialization and deserialization of StudentsToIslandMessage")
    void studentsToIslandMessageJSONTest() {
        UnlimitedStudentsContainer container = new UnlimitedStudentsContainer();
        container.addStudents(3, FactionColor.BLUE);
        StudentsToIslandMessage studentsToIslandMessage = new StudentsToIslandMessage("110011", container, 4);
        String json = gson.toJson(studentsToIslandMessage);
        System.out.println(json);
        assertNotNull(json);
        Message newMessage = gson.fromJson(json, Message.class);
        StudentsToIslandMessage newStudentsToIslandMessage = (StudentsToIslandMessage) newMessage;
        assertEquals(studentsToIslandMessage.UUID, newStudentsToIslandMessage.UUID);
        assertEquals(3, newStudentsToIslandMessage.getContainer().getByColor(FactionColor.BLUE));
        assertEquals(4, newStudentsToIslandMessage.getIslandId());
    }

    @Test
    @DisplayName("Serialization and deserialization of UpdateMessage")
    void updateMessageJSONTest() {
        UpdateMessage updateMessage = new UpdateMessage("110011", new ArrayList<>(), MessageType.PING,
                "Last action " + "description");
        String json = gson.toJson(updateMessage);
        assertNotNull(json);
        Message newMessage = gson.fromJson(json, Message.class);
        UpdateMessage newUpdateMessage = (UpdateMessage) newMessage;
        assertEquals(updateMessage.UUID, newUpdateMessage.UUID);
    }
}
