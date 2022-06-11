package it.polimi.ingsw.am37.client.gui.controller;

import com.jfoenix.controls.JFXToggleButton;
import it.polimi.ingsw.am37.client.Client;
import it.polimi.ingsw.am37.client.gui.SceneController;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EnterInGameController extends GenericController {
    public TextField nicknameField;
    public JFXToggleButton numOfPlayersField;
    public CheckBox advancedRulesField;
    public Label errorLabel;

    private String nickname;
    private Client.LobbyParameters lobbyParameters;

    public void enterInGame(ActionEvent actionEvent) {
        nickname = nicknameField.getText().trim().replaceAll(" ", "");
        lobbyParameters = new Client.LobbyParameters(advancedRulesField.isSelected(),
                numOfPlayersField.isSelected() ? 3 : 2);
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
