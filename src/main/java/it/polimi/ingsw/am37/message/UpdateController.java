package it.polimi.ingsw.am37.message;

import it.polimi.ingsw.am37.model.*;
import it.polimi.ingsw.am37.model.character.Character;
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
        P_ISLAND_DIMENSION("island-dimension"),
        P_PLAYER_LASTASSISTANT("player-lastAssistant"),
        P_PLAYER_CHARACTERUSED("player-characterUsed"),
        P_PLAYER_COINS("player-coins"),
        P_BOARD_ENTRANCE("board-entrance"),
        P_BOARD_DINING("board-dining"),
        P_BOARD_TOWER("board-tower"),
        P_BOARD_PROF("board-professor");

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
                updateList.add("Island " + island.getIslandId() + ": update towers from " + evt.getOldValue() + " to " +
                        evt.getNewValue());
            }
            case P_ISLAND_NOENTRYTILE -> {
                Island island = (Island) updatedObject;
                updateList.add(
                        "Island " + island.getIslandId() + ": update no entry tiles from" + evt.getOldValue() + " to " +
                                evt.getNewValue());
            }
            case P_ISLAND_DIMENSION -> {
                Island island = (Island) updatedObject;
                updateList.add(
                        "Island " + island.getIslandId() + ": update dimension from" + evt.getOldValue() + " to " +
                                evt.getNewValue());
            }
            case P_PLAYER_LASTASSISTANT -> {
                Player player = (Player) updatedObject;
                updateList.add("Player " + player.getPlayerId() + ": play assistant " + evt.getNewValue());
            }
            case P_PLAYER_CHARACTERUSED -> {
                Player player = (Player) updatedObject;
                updateList.add("Player " + player.getPlayerId() + ": play character " +
                        ((Character) evt.getNewValue()).getEffectType());
            }
            case P_BOARD_ENTRANCE -> {
                Board board = (Board) updatedObject;
                updateList.add("Player " + board.getPlayer().getPlayerId() + ": update in entrance room from " +
                        (((StudentsContainer) evt.getOldValue()).getStudentsAsString() + " to " +
                                ((StudentsContainer) evt.getNewValue()).getStudentsAsString()));
            }
            case P_BOARD_DINING -> {
                Board board = (Board) updatedObject;
                updateList.add("Player " + board.getPlayer().getPlayerId() + ": update in dining room from " +
                        (((StudentsContainer) evt.getOldValue()).getStudentsAsString() + " to " +
                                ((StudentsContainer) evt.getNewValue()).getStudentsAsString()));
            }
            case P_BOARD_PROF -> {
                Board board = (Board) updatedObject;
                StringBuilder string = new StringBuilder("Player " + board.getPlayer().getPlayerId());
                if (evt.getOldValue() == null)
                    string.append(" acquired prof of color ").append(((FactionColor) evt.getNewValue()).name());
                else string.append(" lost prof of color ").append(((FactionColor) evt.getOldValue()).name());
                updateList.add(string.toString());
            }
            case P_PLAYER_COINS, P_BOARD_TOWER -> {
            }
            default -> System.err.println("Property change unexpected: " + evt.getPropertyName());
        }
    }

    public List<String> getUpdateList() {
        return updateList;
    }

    public List<UpdatableObject> getUpdatedObjects() {
        return updatedObjects.stream().toList();
    }
}
