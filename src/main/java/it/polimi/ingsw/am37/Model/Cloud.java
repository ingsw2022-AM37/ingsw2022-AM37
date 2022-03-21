package it.polimi.ingsw.am37.Model;

import it.polimi.ingsw.am37.Model.student_container.LimitedStudentContainer;

/**
 * This class represent the Clouds in the game.
 */
public class Cloud {

	/**
	 * Default constructor
	 */
	public Cloud(boolean isFor2) {
		this.isFor2 = isFor2;
		if(this.isFor2)
			studentsOnCloud = new LimitedStudentContainer(3);
		else
			studentsOnCloud = new LimitedStudentContainer(4);
	}

	/**
	 * It represents the students that are on the Cloud at the start of every turn.
	 */
	private final LimitedStudentContainer studentsOnCloud;

	/**
	 * It is needed to know if the game is played by two or three Players.
	 */
	private final boolean isFor2;



	/**
	 * @param num The number of students that you want to add.
	 * @param color The color of students that you want to add.
	 */
	public void addStudents(int num, FactionColor color) {
		studentsOnCloud.addStudents(num, color);
	}

	/**
	 * @param num The number of students that you want to remove.
	 * @param color The color of students that you want to remove.
	 */
	public void removeStudents(int num, FactionColor color) {
		studentsOnCloud.removeStudents(num, color);
	}

	/**
	 * @param color The color of the students whose number is needed.
	 * @return The number of students in the Cloud with specified color
	 */
	public int getByColor(FactionColor color) {
		return studentsOnCloud.getByColor(color);
	}

	/**
	 * @return The flag that indicates whether the game is for two or three player.
	 */
	public boolean getIsFor2() {
		return isFor2;
	}

}