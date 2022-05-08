package it.polimi.ingsw.am37.message;

/**
 * \ This message is used to comunicate the mother nature destination as regular movements; for movements associated to
 * special characters please use {@link PlayCharacterMessage}. This message only contains the id of the destination
 * islands. Check are performed server side to ensure movements is correct and within assistant (and bonuses) values.
 */
public class MoveMotherNatureMessage extends Message {

    /**
     * This is the id of the mother nature's destination island
     */
    private final int islandId;


    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID     the default constructor
     * @param islandId the id of the destination island
     */
    protected MoveMotherNatureMessage(String UUID, int islandId) {
        super(UUID, MessageType.MOVE_MOTHER_NATURE);
        this.islandId = islandId;
    }

    /**
     * The default sender side constructor for message preparing. UUID must be set using {@link Message#setUUID(String)}
     * before sending it
     *
     * @param islandId the id of the destination island
     */
    protected MoveMotherNatureMessage(int islandId) {
        super(MessageType.MOVE_MOTHER_NATURE);
        this.islandId = islandId;
    }
}