package it.polimi.ingsw.am37.client.gui.controller;

import it.polimi.ingsw.am37.model.FactionColor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class ChooseColorController {
    private FactionColor color;

    public void colorClicked(MouseEvent mouseEvent) {
        color = FactionColor.valueOf(((Node) mouseEvent.getSource()).getId());
    }

    public FactionColor getColor() {
        return color;
    }
}
