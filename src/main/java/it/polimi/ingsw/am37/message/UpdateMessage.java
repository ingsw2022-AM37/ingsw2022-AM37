package it.polimi.ingsw.am37.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class UpdateMessage extends Message {


    final HashMap<String, List<it.polimi.ingsw.am37.message.UpdatableObject>> updatedObjects;

    final MessageType lastAction;

    final String lastActionDescription;

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
            updatedObjects.computeIfAbsent(object.type().getLabel(), k -> new ArrayList<>());
            updatedObjects.get(object.type().getLabel()).add(object);
        }
        return updatedObjects;
    }
}