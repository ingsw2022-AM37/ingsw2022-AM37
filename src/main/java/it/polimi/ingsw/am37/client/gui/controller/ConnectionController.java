package it.polimi.ingsw.am37.client.gui.controller;

import it.polimi.ingsw.am37.client.Client;
import it.polimi.ingsw.am37.client.gui.SceneController;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller used at the beginning for the first connection screen
 */
public class ConnectionController extends GenericController {

    /**
     * field used to save IP of server
     */
    public TextField ipField;

    /**
     * field used to save server's port
     */
    public TextField portField;

    /**
     * label used to show some errors
     */
    public Label errorLabel;

    /**
     * saved client connection parameters
     */
    private Client.ConnectionParameters connectionParameters;

    /**
     * method used to connect to server when the button is clicked, if a field is missing will be used default setting
     * @param actionEvent event used to notify when button for connection is clicked
     */
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

    /**
     *
     * @return client connection parameters
     */
    public Client.ConnectionParameters getConnectionParameters() {
        return connectionParameters;
    }

}
