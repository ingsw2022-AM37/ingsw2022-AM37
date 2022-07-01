package it.polimi.ingsw.am37.controller;

import it.polimi.ingsw.am37.model.*;
import it.polimi.ingsw.am37.model.character.Character;
import it.polimi.ingsw.am37.model.student_container.StudentsContainer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Class used to check what is changed and needed to be updated
 */
public class UpdateController implements PropertyChangeListener {

    private List<String> updateList = new ArrayList<>();
    private HashSet<UpdatableObject> updatedObjects = new HashSet<>();

    public enum Properties {
        P_CREATION("creation"),
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
        P_BOARD_PROF("board-professor"),
        P_CHARACTER_PLAYED("character-played");

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
                if ((int) evt.getNewValue() == 0)
                    updateList.add("Island " + island.getIslandId() + ": deleted");
                else updateList.add(
                        "Island " + island.getIslandId() + ": update dimension from" + evt.getOldValue() + " to " +
                                evt.getNewValue());
            }
            case P_PLAYER_LASTASSISTANT -> {
                Player player = (Player) updatedObject;
                updateList.add("Player " + player.getPlayerId() + ": played assistant " + evt.getNewValue());
            }
            case P_PLAYER_CHARACTERUSED -> {
                Player player = (Player) updatedObject;
                updateList.add("Player " + player.getPlayerId() + ": played character " +
                        ((Character) evt.getNewValue()).getEffectType());
            }
            case P_BOARD_ENTRANCE -> {
                Board board = ((Player) updatedObject).getBoard();
                updateList.add("Player " + board.getPlayer().getPlayerId() + ": update in entrance room from " +
                        (((StudentsContainer) evt.getOldValue()).getStudentsAsString() + " to " +
                                ((StudentsContainer) evt.getNewValue()).getStudentsAsString()));
            }
            case P_BOARD_DINING -> {
                Board board = ((Player) updatedObject).getBoard();
                updateList.add("Player " + board.getPlayer().getPlayerId() + ": update in dining room from " +
                        (((StudentsContainer) evt.getOldValue()).getStudentsAsString() + " to " +
                                ((StudentsContainer) evt.getNewValue()).getStudentsAsString()));
            }
            case P_BOARD_PROF -> {
                Board board = ((Player) updatedObject).getBoard();
                StringBuilder string = new StringBuilder("Player " + board.getPlayer().getPlayerId());
                if (evt.getOldValue() == null)
                    string.append(" acquired prof of color ").append(((FactionColor) evt.getNewValue()).name());
                else string.append(" lost prof of color ").append(((FactionColor) evt.getOldValue()).name());
                updateList.add(string.toString());
            }
            case P_PLAYER_COINS, P_BOARD_TOWER, P_CHARACTER_PLAYED, P_CREATION -> {
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

    /**
     * This function clears the updated objects and modification list before the start of a new turn. The objects aren't
     * cleared but instead a new instance of the container is created so any reference to the old objects will be
     * preserved
     */
    public void clear() {
        updateList = new ArrayList<>();
        updatedObjects = new HashSet<>();
    }
}
