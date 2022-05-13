package it.polimi.ingsw.am37.message;

import it.polimi.ingsw.am37.model.Cloud;
import it.polimi.ingsw.am37.model.Island;
import it.polimi.ingsw.am37.model.TowerColor;
import it.polimi.ingsw.am37.model.student_container.StudentsContainer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class UpdateController implements PropertyChangeListener {

    private final List<String> updateList = new ArrayList<>();
    private final HashSet<UpdatableObject> updatedObjects = new HashSet<>();

    public enum Properties {
        P_CLOUD("cloud"),
        P_ISLAND_STUDENTS("island-students"),
        P_ISLAND_TOWER("island-tower"),
        P_ISLAND_NOENTRYTILE("island-noEntryTiles"),
        P_ISLAND_DIMENSION("island-dimension");

        Properties(String label) {
            this.label = label;
        }

        final String label;

        @Override
        public String toString() {
            return label;
        }

        public static Properties findByLabel(String label) {
            for (Properties p : Properties.values()) {
                if (Objects.equals(p.label, label)) return p;
            }
            throw new IllegalArgumentException("No properties with label " + label);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        UpdatableObject updatedObject = (UpdatableObject) evt.getSource();
        updatedObjects.add(updatedObject);
        switch (Properties.findByLabel(evt.getPropertyName())) {
            case P_CLOUD -> {
                Cloud cloud = (Cloud) updatedObject;
                updateList.add("Cloud " + cloud.getCloudId() + " update students from " +
                        ((StudentsContainer) evt.getOldValue()).getStudentsAsString() + " to " +
                        ((StudentsContainer) evt.getNewValue()).getStudentsAsString());
            }
            case P_ISLAND_STUDENTS -> {
                Island island = (Island) updatedObject;
                updateList.add("Island " + island.getIslandId() + ": update students from " +
                        ((StudentsContainer) evt.getOldValue()).getStudentsAsString() + " to " +
                        ((StudentsContainer) evt.getNewValue()).getStudentsAsString());
            }
            case P_ISLAND_TOWER -> {
                Island island = (Island) updatedObject;
                updateList.add("Island " + island.getIslandId() + ": update towers from " +
                        evt.getOldValue() + " to " +
                       evt.getNewValue());
            }
            case P_ISLAND_NOENTRYTILE -> {
                Island island = (Island) updatedObject;
                updateList.add("Island " + island.getIslandId() + ": update no entry tiles from" +
                        evt.getOldValue() + " to " +
                        evt.getNewValue());
            }
            case P_ISLAND_DIMENSION -> {
                Island island = (Island) updatedObject;
                updateList.add("Island " + island.getIslandId() + ": update dimension from" +
                        evt.getOldValue() + " to " +
                        evt.getNewValue());
            }
        }
    }

    public List<String> getUpdateList() {
        return updateList;
    }

    public List<UpdatableObject> getUpdatedObjects() {
        return updatedObjects.stream().toList();
    }
}
