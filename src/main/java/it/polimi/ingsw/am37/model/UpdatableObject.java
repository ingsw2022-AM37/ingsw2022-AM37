package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.message.UpdateMessage;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import static it.polimi.ingsw.am37.controller.UpdateController.Properties.P_CREATION;

/**
 * This class represents an object of the model that could be modified by an action. Is used to limitate the visibility
 * of the model, because only object that extends this class could be exported in {@link UpdateMessage} thus visible to
 * the client. This class also contains an enumeration that define the possible type of exportable objects.
 */
public class UpdatableObject {

    /**
     * The type of the updatable object
     */
    public final UpdatableType type;

    protected final transient PropertyChangeSupport support;

    /**
     * Default constructor for UpdatableObject
     *
     * @param type the type of this updatable object
     */
    public UpdatableObject(UpdatableType type) {
        this.type = type;
        this.support = new PropertyChangeSupport(this);
    }

    /**
     * This defines possible type of exportable objects and their key for hashmaps constructions
     *
     * @see UpdateMessage
     */
    public enum UpdatableType {
        ISLAND("islands", "Island"),
        CLOUD("clouds", "Cloud"),
        PLAYER("players", "Player"),
        CHARACTER("characters", "character.Character");

        private final String label;

        private final String className;

        UpdatableType(String label, String className) {
            this.label = label;
            this.className = className;
        }

        public String getLabel() {
            return label;
        }

        public String getClassName() {
            return className;
        }
    }

    public PropertyChangeSupport getSupport() {
        return support;
    }

    /**
     * This function mainly wrap {@link PropertyChangeSupport#addPropertyChangeListener(PropertyChangeListener)} to
     * register a listener but also firing a property type of P_CREATION to signal the creation of the object.
     *
     * @param listener the listener to register to the updatable object
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
        support.firePropertyChange(P_CREATION.toString(), null, null);
    }
}
