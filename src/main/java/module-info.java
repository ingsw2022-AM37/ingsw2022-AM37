module it.polimi.ingsw.am37 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.management;


    opens it.polimi.ingsw.am37 to javafx.fxml;
    exports it.polimi.ingsw.am37;
}