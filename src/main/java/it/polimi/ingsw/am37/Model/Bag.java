package it.polimi.ingsw.am37.Model;

import it.polimi.ingsw.am37.Model.student_container.LimitedStudentContainer;
import it.polimi.ingsw.am37.Model.student_container.UnlimitedStudentContainer;
import java.util.Random;

/**
 * This class represents the Bag which contains the students that are not assigned to an Island, Cloud or Board.
 */
public class Bag {

	/**
	 * Default constructor, it fills the Bag with 2 students from each Faction.
	 * @see FactionColor
	 * @see UnlimitedStudentContainer
	 */
	public Bag() {
		studentsAvailable = new UnlimitedStudentContainer();
		for(FactionColor color : FactionColor.values())
			studentsAvailable.addStudents(2, color);
	}

	/**
	 * It represents the students in the Bag.
	 */
	private final UnlimitedStudentContainer studentsAvailable;


	/**
	 * It extracts students from the Bag.
	 * @param num The number of students to extracts.
	 * @return A LimitedStudentContainer that contains the students extracted by FactionColor.
	 * @throws IllegalArgumentException If there are not enough students to extract.
	 * @see LimitedStudentContainer
	 * @see FactionColor
	 */
	public LimitedStudentContainer extractStudents(int num) {
		if(studentsAvailable.size() < num)
			throw new IllegalArgumentException("there are not enough students to extract");
		LimitedStudentContainer studentsExtracted;
		int[] colorBound = new int[]{0,0,0,0,0};
		Random colorGenerator = new Random();
		FactionColor colorExtracted;
		for(int i = 0; i < num; i++) {
			colorExtracted = FactionColor.values()[colorGenerator.nextInt(FactionColor.values().length)];
			if(studentsAvailable.getByColor(colorExtracted) > colorBound[colorExtracted.getIndex()])
				colorBound[colorExtracted.getIndex()]++;
			else i--;
		}
		studentsExtracted = new LimitedStudentContainer(colorBound);
		for(FactionColor color : FactionColor.values()) {
			studentsExtracted.addStudents(colorBound[color.getIndex()], color);
			studentsAvailable.removeStudents(colorBound[color.getIndex()], color);
		}
		return studentsExtracted;
	}
}