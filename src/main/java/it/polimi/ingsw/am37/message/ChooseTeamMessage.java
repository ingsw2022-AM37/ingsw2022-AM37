package it.polimi.ingsw.am37.message;

import it.polimi.ingsw.am37.model.WizardTeam;

/**
 * This message is used to select a desired wizardTeam in a lobby before the starting of a match. The selected wizard
 * team must be unique, so a confirmation/error message is expected to ensure the availability of the desired team.
 *
 * @see WizardTeam for teams available
 */
public class ChooseTeamMessage extends Message {

    /**
     * This represents the player's desired wizard team
     */
    private final WizardTeam desiredTeam;

    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID the client identifier
     * @param team the desired team
     */
    public ChooseTeamMessage(String UUID, WizardTeam team) {
        super(UUID, MessageType.CHOOSE_TEAM);
        this.desiredTeam = team;
    }

    /**
     * The default sender side constructor for message preparing. UUID must be set using {@link Message#setUUID(String)}
     * before sending it
     *
     * @param team the team desired
     */
    public ChooseTeamMessage(WizardTeam team) {
        super(MessageType.CHOOSE_TEAM);
        this.desiredTeam = team;
    }

    /**
     * @return the desiredTeam
     */
    public WizardTeam getDesiredTeam() {
        return desiredTeam;
    }
}