package it.polimi.ingsw.am37.message;


import it.polimi.ingsw.am37.model.student_container.StudentsContainer;

/**
 * This message is used to move students from entrance to an {@link it.polimi.ingsw.am37.model.Island}. The students to
 * move are provided in a container and the destination island is identified by its id. This message may rise exception
 * because badly created container with invalid students are passed, so expect an {@link ErrorMessage} as response when
 * exceptions are raised or an {@link UpdateMessage} when the move is successful.
 *
 * @see it.polimi.ingsw.am37.model.Board;
 */
public class StudentsToIslandMessage extends Message {

    /**
     * the students' container with selected students to move into the selected island
     */
    private final StudentsContainer container;

    private final int islandId;

    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID      the default constructor
     * @param container the container with the students to move
     * @param islandId  the id of the destination island
     */
    public StudentsToIslandMessage(String UUID, StudentsContainer container, int islandId) {
        super(UUID, MessageType.STUDENTS_TO_ISLAND);
        this.container = container;
        this.islandId = islandId;
    }

    /**
     * The default sender side constructor for message preparing. UUID must be set using {@link Message#setUUID(String)}
     * before sending it
     *
     * @param container the container with the students to move
     * @param islandId  the id of the destination island
     */
    public StudentsToIslandMessage(StudentsContainer container, int islandId) {
        super(MessageType.STUDENTS_TO_ISLAND);
        this.container = container;
        this.islandId = islandId;
    }

    /**
     * @return the container with students to move into the dining room
     */
    public StudentsContainer getContainer() {
        return container;
    }

    /**
     * @return the id of the destination island
     */
    public int getIslandId() {
        return islandId;
    }
}