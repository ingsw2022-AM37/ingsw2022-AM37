package it.polimi.ingsw.am37.client.gui.controller;

import com.jfoenix.controls.JFXButton;
import it.polimi.ingsw.am37.model.FactionColor;
import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.StudentsContainer;
import it.polimi.ingsw.am37.model.student_container.UnlimitedStudentsContainer;
import javafx.event.ActionEvent;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Controller of a dialog window that allow user to select some students from a source container. Set the source
 * container with {@link ChooseNumStudentsController#setSourceContainer(StudentsContainer, int)}  before show and then
 * wait until closure of the modal. If the user insert a valid number of students and press the confirm button the
 * selected container is not {@code null} and valid, otherwise the container is {@code null}
 */
public class ChooseNumStudentsController {

    /**
     * spinner for blue students
     */
    public Spinner<Integer> spinner_blue;

    /**
     * spinner for green students
     */
    public Spinner<Integer> spinner_green;

    /**
     * spinner for red students
     */
    public Spinner<Integer> spinner_red;

    /**
     * spinner for yellow students
     */
    public Spinner<Integer> spinner_yellow;

    /**
     * spinner for pink students
     */
    public Spinner<Integer> spinner_pink;

    /**
     * confirm button
     */
    public JFXButton confirm_button;

    /**
     * The label used to comunicate information and remaining students in the students dialog
     */
    public Text label;

    /**
     * The container of students, could be null if an error occurred or user close the dialog without the confirm
     * button
     */
    private StudentsContainer container;

    /**
     * Create a student container with the value in the spinner box if it's correct, otherwise the container is null.
     * Internal FXML callback on when the confirm button of the dialog is pressed
     *
     * @param actionEvent the button pressing event
     */
    public void confirmStudents(ActionEvent actionEvent) {
        container = new LimitedStudentsContainer(120);
        container.addStudents(spinner_blue.getValue(), FactionColor.BLUE);
        container.addStudents(spinner_red.getValue(), FactionColor.RED);
        container.addStudents(spinner_green.getValue(), FactionColor.GREEN);
        container.addStudents(spinner_yellow.getValue(), FactionColor.YELLOW);
        container.addStudents(spinner_pink.getValue(), FactionColor.PINK);
        ((Stage) confirm_button.getScene().getWindow()).close();
    }

    /**
     * @return the container associated to this dialog; could be {@code null} if an error occurred or user closed the
     * dialog without the confirm button
     */
    public StudentsContainer getStudents() {
        return container;
    }

    /**
     * Initializing spinner with {@code initialValue=0}, enabling wrapping and set the max value based on the current
     * container. If no students of a color are present the spinner is disabled
     *
     * @param sourceContainer the student container with students to choose from
     * @param num             the max number of movable students (no check is performed, only display it to user)
     */
    public void setSourceContainer(StudentsContainer sourceContainer, int num) {
        label.setText(label.getText() + " (Remaining students movements are " + num + "!)");
        try {
            for (FactionColor color : FactionColor.values()) {
                Spinner<Integer> spinner = (Spinner<Integer>) getClass().getField(
                        "spinner_" + color.name().toLowerCase()).get(this);
                if (sourceContainer.getByColor(color) == 0) spinner.setDisable(true);
                else {
                    SpinnerValueFactory<Integer> spinnerValueFactory =
                            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, sourceContainer.getByColor(color), 0);
                    spinnerValueFactory.setWrapAround(true);
                    spinner.setValueFactory(spinnerValueFactory);
                    spinner.getEditor().textProperty().addListener(((observable, oldValue, newValue) -> {
                        if (newValue == null || !newValue.matches("\\d*")) {
                            spinner.getEditor().setText(oldValue);
                        }
                    }));
                }
            }
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
