package it.polimi.ingsw.am37.Model.student_container;
import it.polimi.ingsw.am37.Model.FactionColor;

/**
 * Abstract class to manage students tile in the model.
 */
public abstract class StudentContainer {

	/**
	 * General array of 5 integers, for the index of each student colors is used ad enumeration
	 * @see FactionColor
	 */
	protected int[] student;

	public StudentContainer(){
		student = new int[]{0,0,0,0,0};
	}

	/**
	 * ABSTRACT - Add students to the collection by color
	 */
	abstract public void addStudents( int num, FactionColor color);

	/**
	 * Return the number of students with specified color in this
	 * @param color	the color needed
	 * @return The number of students in this with specified color, as <code>int</code>
	 */
	public int getByColor(FactionColor color) throws IllegalArgumentException{
		if(color == null) throw new IllegalArgumentException("color must not be null");
		return student[color.getIndex()];
	}

	/**
	 * Add all the students of the provided container to this
	 * @param other	the source container to merge in this
	 */
	public void uniteContainers(StudentContainer other) throws IllegalArgumentException, StudentSpaceException {
		if(other == null) throw new IllegalArgumentException("other container must not be null");
		for (FactionColor color :
				FactionColor.values()) {
			addStudents(other.getByColor(color), color);
		}
	}

}