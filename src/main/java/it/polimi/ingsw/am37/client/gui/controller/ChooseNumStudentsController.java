package it.polimi.ingsw.am37.client.gui.controller;

import com.jfoenix.controls.JFXButton;
import it.polimi.ingsw.am37.model.FactionColor;
import it.polimi.ingsw.am37.model.student_container.StudentsContainer;
import it.polimi.ingsw.am37.model.student_container.UnlimitedStudentsContainer;
import javafx.event.ActionEvent;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

public class ChooseNumStudentsController {

    public Spinner<Integer> spinner_blue;
    public Spinner<Integer> spinner_green;
    public Spinner<Integer> spinner_red;
    public Spinner<Integer> spinner_yellow;
    public Spinner<Integer> spinner_pink;
    public JFXButton confirm_button;

    private StudentsContainer container;

    public void confirmStudents(ActionEvent actionEvent) {
        container = new UnlimitedStudentsContainer();
        container.addStudents(spinner_blue.getValue(), FactionColor.BLUE);
        container.addStudents(spinner_red.getValue(), FactionColor.RED);
        container.addStudents(spinner_green.getValue(), FactionColor.GREEN);
        container.addStudents(spinner_yellow.getValue(), FactionColor.YELLOW);
        container.addStudents(spinner_pink.getValue(), FactionColor.PINK);
        ((Stage) confirm_button.getScene().getWindow()).close();
    }

    public StudentsContainer getStudents() {
        return container;
    }

    public void setSourceContainer(StudentsContainer sourceContainer) {
        try {
            for (FactionColor color : FactionColor.values()) {
                Spinner<Integer> spinner = (Spinner<Integer>) getClass().getField(
                        "spinner_" + color.name().toLowerCase()).get(this);
                SpinnerValueFactory<Integer> spinnerValueFactory =
                        new SpinnerValueFactory.IntegerSpinnerValueFactory(0, sourceContainer.getByColor(color), 0);
                spinnerValueFactory.setWrapAround(true);
                spinner.setValueFactory(spinnerValueFactory);
            }
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
