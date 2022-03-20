package it.polimi.ingsw.am37.Model.student_container;

import java.util.Arrays;

/**
 * Container for students tile with the ability to remove students if needed
 */
public class UnlimitedStudentContainer extends StudentContainer {

	/**
	 * Default constructor
	 */
	public UnlimitedStudentContainer() {
	}

	/**
	 * Adding students to the container
	 * @param num
	 * @param color
	 * @throws IllegalArgumentException
	 */
	@Override
	public void addStudents(int num, FactionColor color) throws IllegalArgumentException{
		if (num>0) throw new IllegalArgumentException("Num must be an int >= 0 but is "+num);
		if (color == null) throw new IllegalArgumentException("color is null");
		student[color.getIndex()] += num;
	}

	/**
	 * Remove the specified number of student of the specified colors
	 * @param num	the number of students to remove; must be strictly positive
	 * @param color	the color of students to remove
	 */
	public void removeStudents(int num, FactionColor color) throws IllegalArgumentException, StudentSpaceException{
		if(num<=0) throw new IllegalArgumentException("num parameter must be strictly positive");
		if(color == null) throw new IllegalArgumentException("Colors couldn't be null");
		if(student[color.getIndex()]>= num) student[color.getIndex()] -= num;
		else {
			throw new StudentSpaceException("General space error (curr, num, limit): ("+ Arrays.stream(student).sum()+","+num+","+student[color.getIndex()]+");", false);
		}
	}

}