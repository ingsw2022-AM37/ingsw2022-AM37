package it.polimi.ingsw.am37.client.gui.observer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Observer used between graphics controllers and guiView
 */
public class GuiObserver implements PropertyChangeListener {
    /**
     * A blocking FIFO queue that contains the list of clicked objects not yet processed by the game controller
     */
    private final BlockingQueue<ClickableObjectType> clickedObjectsQueue;
    /**
     * A regular queue that contains the id of
     */
    private final Queue<String> idsQueue;

    private ClickableObjectType lastClickedObject;

    /**
     * Default builder of gui observer initializing the queues with needed values
     */
    public GuiObserver() {
        clickedObjectsQueue = new LinkedBlockingQueue<>();
        idsQueue = new LinkedList<>();
        idsQueue.add(null);
        lastClickedObject = null;
    }

    /**
     * this function provides the id of the last retrieved object, could be null if the object doesn't have a valid id
     *
     * @return the id of the last retrieved object
     */
    public String getLastRetrievedObjectId() {
        return idsQueue.peek();
    }

    /**
     * @return the last clicked object if is present, or null when none has been clicked
     */
    public ClickableObjectType getLastClickedObjectType() {
        return lastClickedObject;
    }

    /**
     * This method gets called when a bound property is changed; if the object that fire this property has an id, that
     * is inserted into {@link GuiObserver#idsQueue} to be later retrieved.
     *
     * @param evt A PropertyChangeEvent object describing the event source and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ClickableObjectType clickedObject = ClickableObjectType.valueOf(evt.getPropertyName());
        switch (clickedObject) {
            case CO_ASSISTANT, CO_CHARACTER, CO_CLOUD, CO_ISLAND -> idsQueue.add((String) evt.getNewValue());
            case CO_DINING, CO_ENTRANCE, CO_MOTHER_NATURE -> idsQueue.add(null);
        }
        clickedObjectsQueue.add(clickedObject);
    }

    /**
     * This function return the next in line clicked objects enumeration; if none is present wait until one available
     * object is clicked
     *
     * @return the first clicked object that needs to be processed
     * @throws InterruptedException if interrupted while waiting
     */
    public ClickableObjectType nextClickedObjectType() throws InterruptedException {
        lastClickedObject = clickedObjectsQueue.take();
        idsQueue.remove();
        return lastClickedObject;
    }

    /**
     * This enumeration contains all the available clickable objects that must be observed
     */
    public enum ClickableObjectType {
        CO_MOTHER_NATURE,
        CO_ISLAND,
        CO_ENTRANCE,
        CO_DINING,
        CO_CLOUD,
        CO_ASSISTANT,
        CO_CHARACTER
    }
}
