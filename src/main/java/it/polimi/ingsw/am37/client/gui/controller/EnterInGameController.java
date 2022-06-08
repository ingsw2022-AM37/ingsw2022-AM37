package it.polimi.ingsw.am37.client.gui.controller;

import com.jfoenix.controls.JFXCheckBox;
import it.polimi.ingsw.am37.client.Client;
import it.polimi.ingsw.am37.client.gui.SceneController;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EnterInGameController extends GenericController {
    public TextField nicknameField;
    public TextField numOfPlayersField;
    public JFXCheckBox advancedRulesField;
    public Label errorLabel;

    private String nickname;
    private Client.LobbyParameters lobbyParameters;

    public void enterInGame(ActionEvent actionEvent) {
        nickname = nicknameField.getText();
        lobbyParameters = new Client.LobbyParameters(advancedRulesField.isSelected(),
                Integer.parseInt(numOfPlayersField.getText()));
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
