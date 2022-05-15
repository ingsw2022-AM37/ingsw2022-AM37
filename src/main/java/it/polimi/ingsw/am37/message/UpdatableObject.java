package it.polimi.ingsw.am37.message;

import java.beans.PropertyChangeSupport;

/**
 * This class represents an object of the model that could be modified by an action. Is used to limitate the visibility
 * of the model, because only object that extends this class could be exported in {@link UpdateMessage} thus visible to
 * the client. This class also contains an enumeration that define the possible type of exportable objects.
 */
public class UpdatableObject {

    /**
     * The type of the updatable object
     */
    final UpdatableType type;

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
        ISLAND("islands"),
        CLOUD("clouds"),
        BOARD("boards"),
        PLAYER("players"),
        CHARACTER("characters");

        private final String label;

        UpdatableType(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public PropertyChangeSupport getSupport() {
        return support;
    }
}
