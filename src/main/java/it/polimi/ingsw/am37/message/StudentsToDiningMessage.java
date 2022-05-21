package it.polimi.ingsw.am37.message;

import it.polimi.ingsw.am37.model.student_container.StudentsContainer;

/**
 * This message is used to move students from entrance to the dining room of a player. This message may rise exception
 * because badly created container with invalid students are passed, so expect an {@link ErrorMessage} as response when
 * exceptions are raised or an {@link UpdateMessage} when the move is successful.
 *
 * @see it.polimi.ingsw.am37.model.Board;
 */
public class StudentsToDiningMessage extends Message {

    /**
     * the students' container with selected students to move into the dining room
     */
    private final StudentsContainer container;

    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID      the default constructor
     * @param container the container with the students to move
     */
    public StudentsToDiningMessage(String UUID, StudentsContainer container) {
        super(UUID, MessageType.STUDENTS_TO_DINING);
        this.container = container;
    }

    /**
     * The default sender side constructor for message preparing. UUID must be set using {@link Message#setUUID(String)}
     * before sending it
     *
     * @param container the container with the students to move
     */
    public StudentsToDiningMessage(StudentsContainer container) {
        super(MessageType.STUDENTS_TO_DINING);
        this.container = container;
    }

    /**
     * @return the container with students to move into the dining room
     */
    public StudentsContainer getContainer() {
        return container;
    }
}