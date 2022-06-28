package it.polimi.ingsw.am37.client.gui.controller;

import it.polimi.ingsw.am37.model.FactionColor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ChooseColorController {
    private FactionColor color = null;

    public void colorClicked(MouseEvent mouseEvent) {
        color = FactionColor.valueOf(((Node) mouseEvent.getSource()).getId().toUpperCase());
        ((Stage) ((Node) mouseEvent.getSource()).getScene().getWindow()).close();
    }

    public FactionColor getColor() {
        return color;
    }
}
