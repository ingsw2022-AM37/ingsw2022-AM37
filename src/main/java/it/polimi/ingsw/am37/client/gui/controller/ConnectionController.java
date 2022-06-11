package it.polimi.ingsw.am37.client.gui.controller;

import it.polimi.ingsw.am37.client.Client;
import it.polimi.ingsw.am37.client.gui.SceneController;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ConnectionController extends GenericController {
    public TextField ipField;
    public TextField portField;
    public Label errorLabel;

    private Client.ConnectionParameters connectionParameters;

    public void connect(ActionEvent actionEvent) {
        int port;
        if (ipField.getText().trim().replaceAll(" ", "").equals("") || portField.getText().trim().replaceAll(" ", "").equals("")) {
            connectionParameters = new Client.ConnectionParameters("localhost", Integer.parseInt("60000"));
        } else {
            try {
                port = Integer.parseInt(portField.getText());
                if (port > 65535 || port < 1) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid port input", ButtonType.OK);
                alert.show();
                return;
            }
            connectionParameters = new Client.ConnectionParameters(ipField.getText(), port);
        }
        synchronized (SceneController.waitObject) {
            SceneController.waitObject.notifyAll();
        }
    }

    public Client.ConnectionParameters getConnectionParameters() {
        return connectionParameters;
    }

}
