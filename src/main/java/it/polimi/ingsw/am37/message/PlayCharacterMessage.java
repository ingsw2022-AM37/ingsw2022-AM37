package it.polimi.ingsw.am37.message;

import it.polimi.ingsw.am37.model.FactionColor;
import it.polimi.ingsw.am37.model.GameManager;
import it.polimi.ingsw.am37.model.character.Character;
import it.polimi.ingsw.am37.model.character.Effect;
import it.polimi.ingsw.am37.model.character.Option;
import it.polimi.ingsw.am37.model.character.OptionBuilder;
import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;

import java.util.NoSuchElementException;

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
    private final Effect chosenCharacter;

    /**
     * The if of the island used by characters effect
     */
    private final int islandId;

    /**
     * The nickname of the player that has called the character
     */
    private final String playerNickname;

    /**
     * The color some characters activate effect on
     */
    private final FactionColor color;

    /**
     * An int parameter used by some characters
     */
    private final int intPar;

    /**
     * The student container with selected students to remove
     */
    private final LimitedStudentsContainer primaryContainer;

    /**
     * The student container with selected students to add
     */
    private final LimitedStudentsContainer secondaryContainer;

    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID            the default constructor
     * @param chosenCharacter the character to play
     * @param option          the option to play the character with
     */
    public PlayCharacterMessage(String UUID, Effect chosenCharacter, Option option) {
        super(UUID, MessageType.PLAY_CHARACTER);
        this.chosenCharacter = chosenCharacter;
        this.islandId = option.getIsland() == null ? -1 : option.getIsland().getIslandId();
        this.playerNickname = option.getPlayer().getPlayerId();
        this.color = option.getColor();
        this.intPar = option.getIntPar();
        this.primaryContainer = option.getPrimaryContainer();
        this.secondaryContainer = option.getSecondaryContainer();
    }

    /**
     * The default sender side constructor for message preparing. UUID must be set using {@link Message#setUUID(String)}
     * before sending it
     *
     * @param chosenCharacter the character to play
     * @param option          the option to play the character with
     */
    public PlayCharacterMessage(Effect chosenCharacter, Option option) {
        super(MessageType.PLAY_CHARACTER);
        this.chosenCharacter = chosenCharacter;
        this.islandId = option.getIsland().getIslandId();
        this.playerNickname = option.getPlayer().getPlayerId();
        this.color = option.getColor();
        this.intPar = option.getIntPar();
        this.primaryContainer = option.getPrimaryContainer();
        this.secondaryContainer = option.getSecondaryContainer();
    }

    /**
     * @return the character chosen to play
     */
    public Effect getChosenCharacter() {
        return chosenCharacter;
    }

    /**
     * This function create a correct option to play a character with
     *
     * @param manager the game manager this option needs to refer to
     * @return the option associated to the character and the action
     */
    public Option getOption(GameManager manager) {
        try {
            OptionBuilder optionBuilder = OptionBuilder.newBuilder(manager, manager.getTurnManager()
                    .getPlayers()
                    .stream()
                    .filter(p -> p.getPlayerId().equals(playerNickname))
                    .findFirst()
                    .orElseThrow());
            optionBuilder.island(manager.getIslandsManager()
                    .getIslands()
                    .stream()
                    .filter(i -> i.getIslandId() == islandId)
                    .findFirst()
                    .orElse(null));
            optionBuilder.color(color);
            optionBuilder.intPar(intPar);
            optionBuilder.primaryContainer(primaryContainer);
            optionBuilder.secondaryContainer(secondaryContainer);
            return optionBuilder.build();
        } catch (NoSuchElementException e) {
            return null;
        }

    }
}