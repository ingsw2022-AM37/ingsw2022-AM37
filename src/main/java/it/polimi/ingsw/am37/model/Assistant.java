package it.polimi.ingsw.am37.model;

/**
 * There are ten assistant cards for each wizard, if possible equal cards can't be used in the same turn
 */
public class Assistant {

    /**
     * Represents the team to which the assistant belongs.
     */
    private final WizardTeam team;

    /**
     * Each assistant has a cardValue which is useful to establish the order of players'
     * turns, a lower value gives more priority when deciding the order
     */
    private final int cardValue;

    /**
     * Each assistant has a moveMNMovement which specific the maximum number of islands on which you
     * can move MotherNature in clockwise
     */
    private int moveMNMovement;

    /**
     * Default constructor
     */
    public Assistant(WizardTeam team, int cardValue, int moveMNMovement) {
        this.team = team;
        this.cardValue = cardValue;
        this.moveMNMovement = moveMNMovement;
    }

    /**
     * @return the value of the card
     */
    public int getCardValue() {
        return this.cardValue;
    }

    /**
     * @return the possible movement for MotherNature
     */
    public int getMNMovement() {
        return this.moveMNMovement;
    }

    public void increaseMNMovement(int i) {
        this.moveMNMovement += i;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assistant assistant)) return false;

        if (getCardValue() != assistant.getCardValue()) return false;
        if (moveMNMovement != assistant.moveMNMovement) return false;
        return team == assistant.team;
    }

    @Override
    public int hashCode() {
        int result = team.hashCode();
        result = 31 * result + getCardValue();
        result = 31 * result + moveMNMovement;
        return result;
    }
}