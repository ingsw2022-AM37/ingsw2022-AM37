package it.polimi.ingsw.am37.Model.student_container;

import java.util.*;

/**
 * Container for students tile, only adding students is allowed
 */
public class FixedUnlimitedStudentContainer extends StudentContainer {

	/**
	 * Default constructor
	 */
	public FixedUnlimitedStudentContainer() {
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
}