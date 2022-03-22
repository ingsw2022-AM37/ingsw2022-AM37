package it.polimi.ingsw.am37.model;

/**
 * Each Wizard is identified by a team and after being chosen it gives access to ten assistant cards of the same team
 */
public class Wizard {

	/**
	 * Default constructor
	 */
	public Wizard(WizardTeam team) {
		this.team = team;
	}

	/**
	 * Represents wizard's team
	 * @see WizardTeam
	 */
	private final WizardTeam team;

	/**
	 * @return the team of the wizard
	 * @see WizardTeam
	 */
	public WizardTeam getTeam() {
		return this.team;
	}

}