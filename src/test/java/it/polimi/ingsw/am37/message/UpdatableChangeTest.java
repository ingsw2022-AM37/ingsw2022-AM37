package it.polimi.ingsw.am37.message;

import it.polimi.ingsw.am37.model.*;
import it.polimi.ingsw.am37.model.character.Character;
import it.polimi.ingsw.am37.model.character.Effect;
import it.polimi.ingsw.am37.model.character.EffectHandler;
import it.polimi.ingsw.am37.model.character.Option;
import it.polimi.ingsw.am37.model.student_container.FixedUnlimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.StudentsContainer;
import it.polimi.ingsw.am37.model.student_container.UnlimitedStudentsContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UpdatableChangeTest {

    @Test
    @DisplayName("test cloud firing property")
    void testCloudFireProperty() {
        Cloud cloud = new Cloud(true);
        UpdateController controller = new UpdateController();
        cloud.getSupport().addPropertyChangeListener("cloud", controller);
        UnlimitedStudentsContainer container = new UnlimitedStudentsContainer();
        container.addStudents(3, FactionColor.BLUE);
        cloud.addStudents(container);
        cloud.removeStudents();
        assertEquals(1, controller.getUpdatedObjects().size());
        assertEquals(2, controller.getUpdateList().size());
    }

    @Test
    @DisplayName("test island firing property")
    void testIslandFireProperty() {
        FixedUnlimitedStudentsContainer container = new FixedUnlimitedStudentsContainer();
        container.addStudents(3, FactionColor.BLUE);
        Island island = new Island(container, 1);
        UpdateController controller = new UpdateController();
        island.getSupport().addPropertyChangeListener(controller);
        island.addStudents(container);
        island.addNoEntryTile(2);
        island.removeNoEntryTile();
        island.setNumIslands(2);
        island.setTower(TowerColor.BLACK);
        assertEquals(5, controller.getUpdateList().size());
        assertEquals(1, controller.getUpdatedObjects().size());
    }

    @Test
    @DisplayName("test player firing property")
    void testPlayerFireProperty() throws InstanceAlreadyExistsException {
        Player player = new Player();
        UpdateController controller = new UpdateController();
        player.getSupport().addPropertyChangeListener(controller);
        player.receiveCoin();
        assertEquals(1, controller.getUpdatedObjects().size());
        player.createDeck(WizardTeam.TEAM1);
        player.useAssistant(player.getAssistantsDeck().get(0));
        Character mockedCharacter = mock(Character.class);
        when(mockedCharacter.getEffectType()).thenReturn(Effect.MONK);
        player.useCharacter(mockedCharacter, mock(Option.class));
        assertEquals(2, controller.getUpdateList().size());
        assertEquals(1, controller.getUpdatedObjects().size());
    }

    @Test
    @DisplayName("test board firing property")
    void testBoardFireProperty () {
        Player player = spy(Player.class);
        Board board = new Board(2, TowerColor.BLACK, false, player);
        player.setBoard(board);
        UpdateController controller = new UpdateController();
        board.getSupport().addPropertyChangeListener(controller);
        board.addProf(FactionColor.BLUE);
        board.removeProf(FactionColor.BLUE);
        board.removeTowers(1);
        board.addTowers(1);
        StudentsContainer container = new UnlimitedStudentsContainer();
        container.addStudents(2, FactionColor.BLUE);
        board.addStudentsToEntrance(container);
        board.removeStudentsFromEntrance(container);
        assertEquals(4, controller.getUpdateList().size());
        assertEquals(1, controller.getUpdatedObjects().size());
    }

    @Test
    @DisplayName("Test character firing property")
    void testCharacterFireProperty () throws NoSuchFieldException, IllegalAccessException {
        UpdateController controller = new UpdateController();
        Character character = new Character(Effect.MONK.getInitialPrice(), Effect.MONK, new Bag());
        character.getSupport().addPropertyChangeListener(controller);
        Field field = character.getClass().getDeclaredField("effectHandler");
        field.setAccessible(true);
        field.set(character, mock(EffectHandler.class));
        character.useEffect(mock(Option.class));
        assertEquals(0, controller.getUpdateList().size());
        Player player = new Player();
        player.getSupport().addPropertyChangeListener(controller);
        player.receiveCoin();
        player.receiveCoin();
        player.useCharacter(character, mock(Option.class));
        assertEquals(1, controller.getUpdateList().size());
        assertEquals(2, controller.getUpdatedObjects().size());
    }
}
