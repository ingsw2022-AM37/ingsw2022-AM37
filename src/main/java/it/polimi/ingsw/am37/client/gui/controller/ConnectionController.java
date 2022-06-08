package it.polimi.ingsw.am37.client.gui.controller;

import it.polimi.ingsw.am37.client.Client;
import it.polimi.ingsw.am37.client.gui.SceneController;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ConnectionController extends GenericController {
    public TextField ipField;
    public TextField portField;
    public Label errorLabel;

    private Client.ConnectionParameters connectionParameters;

    public void connect(ActionEvent actionEvent) {
        connectionParameters = new Client.ConnectionParameters(ipField.getText(),
                Integer.parseInt(portField.getText()));
        synchronized (SceneController.waitObject) {
            SceneController.waitObject.notifyAll();
        }
    }

    public Client.ConnectionParameters getConnectionParameters() {
        return connectionParameters;
    }

}
