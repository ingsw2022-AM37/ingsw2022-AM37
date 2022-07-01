package it.polimi.ingsw.am37.client.gui.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;

/**
 * External controller used by everyone else
 */
public class GenericController {

    private Alert currentAlert = null;
    private Alert.AlertType currentAlertType = null;

    private void closeRequestHandler(DialogEvent event) {
        currentAlert = null;
        currentAlertType = null;
    }

    private void pushAlert(MyAlert alert) {
        if (currentAlert != null && alert.getAlertType().ordinal() >= currentAlertType.ordinal()) {
            currentAlert.close();
        }
        currentAlert = alert;
        currentAlertType = alert.getAlertType();
        currentAlert.setOnCloseRequest(this::closeRequestHandler);
        currentAlert.show();
    }

    public void showError(String message) {
        MyAlert alert = new MyAlert(MyAlert.AlertType.ERROR, message, ButtonType.OK);
        pushAlert(alert);
    }

    public void showImportant(String message) {
        MyAlert alert = new MyAlert(MyAlert.AlertType.WARNING, message, ButtonType.OK);
        pushAlert(alert);
    }

    public void showInfo(String message) {
        MyAlert alert = new MyAlert(MyAlert.AlertType.INFORMATION, message, ButtonType.OK);
        pushAlert(alert);
    }

    private static class MyAlert extends Alert {
        public MyAlert(AlertType alertType, String contentText, ButtonType... buttons) {
            super(alertType, contentText, buttons);
            this.setHeaderText(null);
        }
    }
}
