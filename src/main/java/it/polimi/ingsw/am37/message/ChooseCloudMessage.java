package it.polimi.ingsw.am37.message;


/**
 * This message is used at the end of turn to select from which clouds get the students to insert into the new
 * entranceRoom
 *
 * @see it.polimi.ingsw.am37.model.Cloud for reference
 */
public class ChooseCloudMessage extends Message {

    /**
     * Represent the id of the selected {@link it.polimi.ingsw.am37.model.Cloud}
     */
    private final String cloudId;

    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID    the default constructor
     * @param cloudId the cloud id
     */
    public ChooseCloudMessage(String UUID, String cloudId) {
        super(UUID, MessageType.CHOOSE_CLOUD);
        this.cloudId = cloudId;
    }

    /**
     * The default sender side constructor for message preparing. UUID must be set using {@link Message#setUUID(String)}
     * before sending it
     */
    public ChooseCloudMessage(String cloudId) {
        super(MessageType.CHOOSE_CLOUD);
        this.cloudId = cloudId;
    }

    /**
     * @return the if of the selected client
     */
    public String getCloudId() {
        return cloudId;
    }
}
