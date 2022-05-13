package it.polimi.ingsw.am37.message;

import it.polimi.ingsw.am37.model.Cloud;
import it.polimi.ingsw.am37.model.FactionColor;
import it.polimi.ingsw.am37.model.Island;
import it.polimi.ingsw.am37.model.TowerColor;
import it.polimi.ingsw.am37.model.student_container.FixedUnlimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.UnlimitedStudentsContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdatableChangeTest {

    @Test
    @DisplayName("test cloud firing property")
    void testCloudFireProperty() {
        Cloud cloud = new Cloud(true);
        UpdateController controller = new UpdateController();
        cloud.getSupport().addPropertyChangeListener("cloud", controller);
        UnlimitedStudentsContainer container = new UnlimitedStudentsContainer();
        container.addStudents(3, FactionColor.BLUE);
        cloud.addStudents(container);
        cloud.removeStudents();
        assertEquals(1, controller.getUpdatedObjects().size());
        assertEquals(2, controller.getUpdateList().size());
    }

    @Test
    @DisplayName("test island firing property")
    void testIslandFireProperty() {
        FixedUnlimitedStudentsContainer container = new FixedUnlimitedStudentsContainer();
        container.addStudents(3, FactionColor.BLUE);
        Island island = new Island(container, 1);
        UpdateController controller = new UpdateController();
        island.getSupport().addPropertyChangeListener(controller);
        island.addStudents(container);
        island.addNoEntryTile(2);
        island.removeNoEntryTile();
        island.setNumIslands(2);
        island.setTower(TowerColor.BLACK);
        assertEquals(5, controller.getUpdateList().size());
        assertEquals(1, controller.getUpdatedObjects().size());
    }


}
