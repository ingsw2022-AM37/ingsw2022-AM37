package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.model.*;
import it.polimi.ingsw.am37.model.student_container.FixedUnlimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;

class ViewTest {

    /**
     * Test display of islands
     */
    @Test
    @DisplayName("Test display of islands")
    void testShowIslands() {
        FixedUnlimitedStudentsContainer container = new FixedUnlimitedStudentsContainer();
        container.addStudents(2, FactionColor.BLUE);
        Island island = new Island(container, 0);
        FixedUnlimitedStudentsContainer container1 = (FixedUnlimitedStudentsContainer) container.copy();
        container1.addStudents(2, FactionColor.GREEN);
        Island island1 = new Island(container1, 1);
        island1.setMotherNatureHere(true);
        island1.addNoEntryTile(1);
        AbstractView view = new CliView();
        view.getReducedModel().update(List.of(island, island1));
        view.showTable();
    }

    /**
     * Test display of clouds
     */
    @Test
    @DisplayName("Test display of clouds")
    void testShowClouds() {
        FixedUnlimitedStudentsContainer container = new FixedUnlimitedStudentsContainer();
        container.addStudents(3, FactionColor.BLUE);
        Cloud cloud = new Cloud(true, 0);
        cloud.addStudents(container);
        FixedUnlimitedStudentsContainer container1 = new FixedUnlimitedStudentsContainer();
        container1.addStudents(3, FactionColor.RED);
        Cloud cloud1 = new Cloud(true, 1);
        cloud1.addStudents(container);
        AbstractView view = new CliView();
        view.getReducedModel().update(List.of(cloud, cloud1));
        view.showTable();
    }

    /**
     * Test display of board
     */
    @Test
    @DisplayName("Test display of board")
    void testShowBoard() {
        Player player = new Player();
        player.setPlayerId("bramba2000");
        LimitedStudentsContainer container = new LimitedStudentsContainer(5);
        container.addStudents(2, FactionColor.GREEN);
        Board board = new Board(2, TowerColor.GRAY, false, container, player);
        player.setBoard(board);
        AbstractView view = new CliView();
        view.getReducedModel().update(List.of(player));
        view.showPlayerStatus(player, false);
    }

    /**
     * Test display of last assistant
     * @throws InstanceAlreadyExistsException
     */
    @Test
    @DisplayName("Test display of last assistant")
    void testShowLastAssistant() throws InstanceAlreadyExistsException {
        Player player = new Player();
        player.setPlayerId("bramba2000");
        player.createDeck(WizardTeam.TEAM1);
        player.setLastAssistantPlayed(player.getAssistantsDeck().get(1));
        AbstractView view = new CliView();
        view.getReducedModel().update(List.of(player));
        view.showPlayerStatus(player, false);
    }

    /**
     * Test display of deck
     * @throws InstanceAlreadyExistsException
     */
    @Test
    @DisplayName("Test display of deck")
    void testShowDeck() throws InstanceAlreadyExistsException {
        Player player = new Player();
        player.setPlayerId("bramba2000");
        player.createDeck(WizardTeam.TEAM1);
        AbstractView view = new CliView();
        view.getReducedModel().update(List.of(player));
        view.showDeck(player);
        player.useAssistant(player.getAssistantsDeck().get(1));
        view.showDeck(player);
    }
}
