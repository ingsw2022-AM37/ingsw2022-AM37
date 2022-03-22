package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;

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
			studentsOnCloud = new LimitedStudentsContainer(3);
		else
			studentsOnCloud = new LimitedStudentsContainer(4);
	}

	/**
	 * It represents the students that are on the Cloud at the start of every turn.
	 */
	private LimitedStudentContainer studentsOnCloud;

	/**
	 * It is needed to know if the game is played by two or three Players.
	 */
	private final boolean isFor2;



	/**
	 * @param students Students used to fill the Cloud.
	 */
	public void addStudents(StudentContainer students) {
		studentsOnCloud.uniteContainers(students);
	}

	/**
	 * @return The students on the Cloud.
	 */
	public LimitedStudentContainer removeStudents() {

		LimitedStudentContainer temp = studentsOnCloud;
		studentsOnCloud = new LimitedStudentContainer(isFor2 ? 3 : 4);
		return studentsOnCloud;
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