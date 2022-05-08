package it.polimi.ingsw.am37.message;

import it.polimi.ingsw.am37.model.character.Character;
import it.polimi.ingsw.am37.model.character.Option;

/**
 * This message is used to comunicate the intent to play a character. The desired character is passed by object
 * reference, also an option instance is created with user's selection to play the characters. A character isn't always
 * playable by a user, so after this message should expect a {@link UpdateMessage} when the character is playable or
 * {@link ErrorMessage} when it's not; check the {@link ErrorMessage#getMessage()} to better understand the cause of the
 * error.
 *
 * @see Character
 * @see Option
 */
public class PlayCharacterMessage extends Message {

    /**
     * the character to play
     */
    private final Character chosenCharacter;

    /**
     * the user's selected option to play this character
     */
    private final Option option;

    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID            the default constructor
     * @param chosenCharacter the character to play
     * @param option          the option to play the character with
     */
    public PlayCharacterMessage(String UUID, Character chosenCharacter, Option option) {
        super(UUID, MessageType.PLAY_CHARACTER);
        this.chosenCharacter = chosenCharacter;
        this.option = option;
    }

    /**
     * The default sender side constructor for message preparing. UUID must be set using {@link Message#setUUID(String)}
     * before sending it
     *
     * @param chosenCharacter the character to play
     * @param option          the option to play the character with
     */
    public PlayCharacterMessage(Character chosenCharacter, Option option) {
        super(MessageType.PLAY_CHARACTER);
        this.chosenCharacter = chosenCharacter;
        this.option = option;
    }

    /**
     * @return the character chosen to play
     */
    public Character getChosenCharacter() {
        return chosenCharacter;
    }

    /**
     * @return the option associated to the character and the action
     */
    public Option getOption() {
        return option;
    }
}