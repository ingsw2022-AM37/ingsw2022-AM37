package it.polimi.ingsw.am37.client.gui.controller;

import it.polimi.ingsw.am37.client.Client;
import it.polimi.ingsw.am37.client.gui.SceneController;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

public class EnterInGameController extends GenericController {
    public TextField nicknameField;
    public Spinner<Integer> numOfPlayersField;
    public CheckBox advancedRulesField;
    public Label errorLabel;

    private String nickname;
    private Client.LobbyParameters lobbyParameters;

    public void enterInGame(ActionEvent actionEvent) {
        nickname = nicknameField.getText();
        lobbyParameters = new Client.LobbyParameters(advancedRulesField.isSelected(),
                numOfPlayersField.getValue());
        synchronized (SceneController.waitObject) {
            SceneController.waitObject.notifyAll();
        }
    }

    public Client.LobbyParameters getLobbyParameters() {
        return lobbyParameters;
    }

    public String getNickname() {
        return nickname;
    }
}
