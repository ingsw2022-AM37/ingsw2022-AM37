package it.polimi.ingsw.am37.client.gui.controller;

import it.polimi.ingsw.am37.model.FactionColor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Controller used for giving a choice for choosing a color
 */
public class ChooseColorController {

    /**
     * color chosen
     */
    private FactionColor color = null;

    /**
     *
     * @param mouseEvent event for notify when a color is clicked
     */
    public void colorClicked(MouseEvent mouseEvent) {
        color = FactionColor.valueOf(((Node) mouseEvent.getSource()).getId().toUpperCase());
        ((Stage) ((Node) mouseEvent.getSource()).getScene().getWindow()).close();
    }

    /**
     *
     * @return chosen color
     */
    public FactionColor getColor() {
        return color;
    }
}
