package it.polimi.ingsw.am37.model;

import it.polimi.ingsw.am37.model.character.Character;
import it.polimi.ingsw.am37.model.character.Option;
import it.polimi.ingsw.am37.model.character.OptionBuilder;
import it.polimi.ingsw.am37.model.exceptions.CharacterImpossibleToPlay;
import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameManagerTest {

    /**
     * Test correct initializing of manager after setup with basic settings
     */
    @Test
    @DisplayName("Test correct initializing of manager after setup with basic settings")
    public void testCorrectCreationAndSetUp() {
        GameManager manager = new GameManager(2, false);
        manager.prepareGame();
        assertNotNull(manager.getTurnManager());
        assertEquals(12, manager.getIslandsManager().getIslands().size());
        assertNotNull(manager.getBag());
        assertEquals(24 * 5 - 7 * 2, manager.getBag().size());
        assertNotNull(manager.getClouds());
        assertEquals(2, manager.getClouds().size());
        assertArrayEquals(new boolean[]{true, true, true, true, true}, manager.getNotUsedTeachers());
        assertNotNull(manager.getTurnManager());
        assertEquals(2, manager.getTurnManager().getPlayers().size());
    }

    /**
     * Test correct initializing of manager after setup with 3 players and base rule
     */
    @Test
    @DisplayName("Test correct initializing of manager after setup with 3 players and base rule")
    public void testCorrectCreationAndSetUp3Players() {
        GameManager manager = new GameManager(3, false);
        manager.prepareGame();
        assertEquals(24 * 5 - 9 * 3, manager.getBag().size());
        assertEquals(3, manager.getClouds().size());
        assertEquals(3, manager.getTurnManager().getPlayers().size());
    }

    /**
     * Test moving students from entrance to island
     */
    @Test
    @DisplayName("Test moving students from entrance to island")
    public void testMoveStudentsToIsland() {
        GameManager manager = new GameManager(2, false);
        Random random = new Random();
        manager.prepareGame();
        LimitedStudentsContainer container = new LimitedStudentsContainer(3);
        container.addStudents(2,
                Arrays.stream(FactionColor.values())
                        .filter(color -> manager.getTurnManager().getCurrentPlayer().getBoard().getEntrance().getByColor(color) >= 2)
                        .findFirst()
                        .orElseThrow());
        int beforeIsland =
                manager.getIslandsManager().getIslands().get(1).getStudentsOnIsland().size();
        int sizeBeforeEntrance = manager.getTurnManager().getCurrentPlayer().getBoard().getEntrance().size();
        manager.moveStudentsToIsland(container, 1);
        assertEquals(beforeIsland + 2, manager.getIslandsManager().getIslands().get(1).getStudentsOnIsland().size());
        assertEquals(sizeBeforeEntrance - 2,
                manager.getTurnManager().getCurrentPlayer().getBoard().getEntrance().size());
    }

    /**
     * Test moving students from entrance to island
     */
    @Test
    @DisplayName("Test moving students from entrance to island")
    public void testMoveStudentsToDining() {
        GameManager manager = new GameManager(2, true);
        Random random = new Random();
        manager.prepareGame();
        LimitedStudentsContainer container = new LimitedStudentsContainer(3);
        container.addStudents(2,
                Arrays.stream(FactionColor.values())
                        .filter(color -> manager.getTurnManager().getCurrentPlayer().getBoard().getEntrance().getByColor(color) >= 2)
                        .findFirst()
                        .orElseThrow());
        manager.moveStudentsToDining(container);
    }

    /**
     * Test assistant logic
     */
    @Test
    @DisplayName("Test assistant logic")
    public void testPlayAssistant() {
        GameManager manager = new GameManager(2, false);
        manager.prepareGame();
        assertNull(manager.getTurnManager().getCurrentPlayer().getLastAssistantPlayed());
        Player oldPlayer = manager.getTurnManager().getCurrentPlayer();
        manager.playAssistant(oldPlayer.getAssistantsDeck().get(4));
        assertNotNull(oldPlayer.getLastAssistantPlayed());
        assertNotEquals(oldPlayer, manager.getTurnManager().getCurrentPlayer());
        assertNull(manager.getTurnManager().getCurrentPlayer().getLastAssistantPlayed());
    }

    /**
     * Test character logic
     */
    @Test
    @DisplayName("Test character logic")
    public void testPlayCharacter() {
        GameManager manager = new GameManager(2, true);
        manager.prepareGame();
        try {
            Field characterArray = GameManager.class.getDeclaredField("characters");
            characterArray.setAccessible(true);
            Character[] character = (Character[]) characterArray.get(manager);
            int oldPrice = character[0].getCurrentPrice();
            character[0] = spy(character[0]);
            doNothing().when(character[0]).useEffect(any(Option.class));
            doReturn(character[0].getCurrentPrice() + 1).when(character[0]).getCurrentPrice();
            for (int i = 0; i < 4; i++) {
                manager.getTurnManager().getCurrentPlayer().receiveCoin();
            }
            manager.playCharacter(character[0], OptionBuilder.newBuilder(manager,
                    manager.getTurnManager().getCurrentPlayer()).build());
            assertNotEquals(character[0].getCurrentPrice(), oldPrice);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test a bad played character logic
     */
    @Test
    @DisplayName("Test a bad played character logic")
    public void testBadPlayedCharacter() {
        GameManager manager = new GameManager(2, true);
        manager.prepareGame();
        try {
            Field characterArray = GameManager.class.getDeclaredField("characters");
            characterArray.setAccessible(true);
            Character[] character = (Character[]) characterArray.get(manager);
            character[0] = spy(character[0]);
            doNothing().when(character[0]).useEffect(any(Option.class));
            doReturn(character[0].getCurrentPrice() + 1).when(character[0]).getCurrentPrice();
            assertThrows(CharacterImpossibleToPlay.class, () ->
                    manager.playCharacter(character[0],
                            OptionBuilder.newBuilder(manager, manager.getTurnManager().getCurrentPlayer()).build()));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}