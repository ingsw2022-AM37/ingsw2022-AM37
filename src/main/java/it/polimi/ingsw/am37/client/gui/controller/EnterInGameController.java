package it.polimi.ingsw.am37.client.gui.controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXToggleButton;
import it.polimi.ingsw.am37.client.Client;
import it.polimi.ingsw.am37.client.gui.SceneController;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
    Controller of the login controller: the associated page provide the user 3 field to select their nickname, the desired number of players of each game and if enable the advanced rules or not

 */
public class EnterInGameController extends GenericController {
    /**The text field for nickname */
    public TextField nicknameField;
    /**The toggle button to switch between 2 or 3 players mode */
    public JFXToggleButton numOfPlayersField;
    /**The checkbox to enable advanced rules */
    public JFXCheckBox advancedRulesField;
    /**An error label only shown when some error occured */
    public Label errorLabel;

    /** The nickname provided by the user; could be null or not valid */
    private String nickname;
    /** The lobby parameters provided by the user; always valid */
    private Client.LobbyParameters lobbyParameters;

    /**
        This functions is the function connected to SUBMIT button
         and when called store in this object variables the data of the fields; also notify the
         on scene controller to unlock the game that is waiting on it.

        @param actionEvent the click on the button event
     */
    public void enterInGame(ActionEvent actionEvent) {
        nickname = nicknameField.getText().trim().replaceAll(" ", "");
        lobbyParameters = new Client.LobbyParameters(advancedRulesField.isSelected(),
                numOfPlayersField.isSelected() ? 3 : 2);
        synchronized (SceneController.waitObject) {
            SceneController.waitObject.notifyAll();
        }
    }

    /**
    * @return the user desired lobby parameters
     */
    public Client.LobbyParameters getLobbyParameters() {
        return lobbyParameters;
    }

    /**
    * @return the user provided username
     */
    public String getNickname() {
        return nickname;
    }
}
