package it.polimi.ingsw.am37.message;

import it.polimi.ingsw.am37.model.UpdatableObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This message is used to share relevant information about the updated objects after each action. Should be used as a
 * confirmation that previous action have been successful. Only the updated objects are provided by this message. This
 * message provides also useful information about the last action performed like is type
 * {@link UpdateMessage#lastAction} and a shor descriptive message {@link UpdateMessage#lastActionDescription}
 */
public class UpdateMessage extends Message {


    /**
     * The type of the action that caused an update
     */
    private final MessageType lastAction;
    /**
     * A descriptive string about the last action
     */
    private final String lastActionDescription;
    /**
     * A bucketed map of all the updated objects sorted by the
     * {@link UpdatableObject.UpdatableType}. Buckets are simple list, so use lists method
     * to retrieve object
     */
    private final HashMap<String, List<UpdatableObject>> updatedObjects;

    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID                  the default constructor
     * @param updatedObjects        the updated objects of the model
     * @param lastAction            the type of last action performed
     * @param lastActionDescription a text description of last performed action
     */
    public UpdateMessage(String UUID, List<UpdatableObject> updatedObjects, MessageType lastAction,
                         String lastActionDescription) {
        super(UUID, MessageType.UPDATE);
        this.updatedObjects = createUpdatedObjectMap(updatedObjects);
        this.lastAction = lastAction;
        this.lastActionDescription = lastActionDescription;
    }

    /**
     * The default sender side constructor for message preparing. UUID must be set using {@link Message#setUUID(String)}
     * before sending it
     *
     * @param updatedObjects        the updated objects of the model
     * @param lastAction            the type of last action performed
     * @param lastActionDescription a text description of last performed action
     */
    public UpdateMessage(List<UpdatableObject> updatedObjects, MessageType lastAction, String lastActionDescription) {
        super(MessageType.UPDATE);
        this.updatedObjects = createUpdatedObjectMap(updatedObjects);
        this.lastAction = lastAction;
        this.lastActionDescription = lastActionDescription;
    }

    /**
     * This is a utility function to create the hashmap of the updated objects. A list of updated objects is converted
     * into a bucketed hashmap with the type provided by the annotation {@link UpdatableObject}. List are created only
     * when necessary as {@link ArrayList}. This method should only be used by the constructors to not repeat twice the
     * creation code.
     *
     * @param updatableObjects the list of updatable objects without any organizing
     * @return a bucketed hashmap with as key the type of each objects
     */
    private HashMap<String, List<UpdatableObject>> createUpdatedObjectMap(List<UpdatableObject> updatableObjects) {
        final HashMap<String, List<UpdatableObject>> updatedObjects;
        updatedObjects = new HashMap<>();
        for (var object :
                updatableObjects) {
            updatedObjects.computeIfAbsent(object.type.getLabel(), k -> new ArrayList<>());
            updatedObjects.get(object.type.getLabel()).add(object);
        }
        return updatedObjects;
    }

    /**
     * @return the hashmap of updated object
     */
    public HashMap<String, List<UpdatableObject>> getUpdatedObjects() {
        return updatedObjects;
    }

    /**
     * Retrieve the list of updated objects for a specific type
     *
     * @param type the type of updatable object to retrieve a list
     */
    public List<? extends UpdatableObject> getUpdatedObjects(UpdatableObject.UpdatableType type) {
        return updatedObjects.get(type.getLabel());
    }

    /**
     * @return The type of the triggering event.
     */
    public MessageType getLastAction() {
        return lastAction;
    }

    /**
     * @return the description of the triggering event.
     */
    public String getLastActionDescription() {
        return lastActionDescription;
    }
}