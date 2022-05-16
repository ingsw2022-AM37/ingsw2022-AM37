package it.polimi.ingsw.am37.message;

public class PlanningPhaseMessage extends Message {

    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID the default constructor
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
