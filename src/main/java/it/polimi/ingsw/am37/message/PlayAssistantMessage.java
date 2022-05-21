package it.polimi.ingsw.am37.message;

/**
 * This message is used to represent the intents to play an assistant. The assistant is identified by its card value
 * (that's unique for each team so for each player). The assistant played has to check if this assistant is playable by
 * the player so this message should expect a {@link ConfirmMessage} or {@link ErrorMessage} to be sure the assistant is
 * valid; in case it isn't a new one should be played.
 */
public class PlayAssistantMessage extends Message {

    /**
     * Represent the card value of the desired assistant
     */
    private final int cardValue;

    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID           the default constructor
     * @param assistantValue the card value of the desired assistant
     */
    public PlayAssistantMessage(String UUID, int assistantValue) {
        super(UUID, MessageType.PLAY_ASSISTANT);
        this.cardValue = assistantValue;
    }

    /**
     * The default sender side constructor for message preparing. UUID must be set using {@link Message#setUUID(String)}
     * before sending it
     *
     * @param assistantValue the card value of the desired assistant
     */
    public PlayAssistantMessage(int assistantValue) {
        super(MessageType.PLAY_ASSISTANT);
        this.cardValue = assistantValue;
    }

    /**
     * @return the card value
     */
    public int getCardValue() {
        return cardValue;
    }

}