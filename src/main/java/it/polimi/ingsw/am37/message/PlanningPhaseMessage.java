package it.polimi.ingsw.am37.message;

/**
 * This message is sent by server when a new player have to play. This message is send as broadcast to all the users to
 * notify the change. The player whose current turn was need to suspend everything.
 */
public class PlanningPhaseMessage extends Message {

    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID the sender identifier
     */
    public PlanningPhaseMessage(String UUID) {
        super(UUID, MessageType.PLANNING_PHASE);
    }

    /**
     * The default sender side constructor for message preparing. UUID must be set using {@link Message#setUUID(String)}
     * before sending it
     */
    public PlanningPhaseMessage() {
        super(MessageType.PLANNING_PHASE);
    }
}
