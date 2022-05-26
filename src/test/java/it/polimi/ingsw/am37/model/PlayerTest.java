package it.polimi.ingsw.am37.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.security.InvalidParameterException;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test for Player class.
 */
public class PlayerTest {

    /**
     * Tests if the deck is created correctly.
     */
    @Test
    @DisplayName("Tests if the deck is created correctly")
    public void createDeckTest() {
        Player player = new Player();
        try {
            player.createDeck(WizardTeam.TEAM1);
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
        }
        assertEquals(1, player.getAssistantsDeck().get(1).getMNMovement());
        assertEquals(1, player.getAssistantsDeck().get(2).getMNMovement());
        assertEquals(2, player.getAssistantsDeck().get(3).getMNMovement());
        assertEquals(2, player.getAssistantsDeck().get(4).getMNMovement());
        assertEquals(3, player.getAssistantsDeck().get(5).getMNMovement());
        assertEquals(3, player.getAssistantsDeck().get(6).getMNMovement());
        assertEquals(4, player.getAssistantsDeck().get(7).getMNMovement());
        assertEquals(4, player.getAssistantsDeck().get(8).getMNMovement());
        assertEquals(5, player.getAssistantsDeck().get(9).getMNMovement());
        assertEquals(5, player.getAssistantsDeck().get(10).getMNMovement());
    }

    /**
     * Tests if the Player can instance two decks.
     */
    @Test
    @DisplayName("Tests if the Player can instance two decks")
    public void createDuplicateDeck() {
        Player player = new Player();
        try {
            player.createDeck(WizardTeam.TEAM1);
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
        }
        assertThrows(InstanceAlreadyExistsException.class, () -> player.createDeck(WizardTeam.TEAM2));
    }

    /**
     * Tests if the Player can use an Assistant which is in the deck
     */
    @Test
    @DisplayName("Tests if the Player can use an Assistant which is in the deck")
    public void useAssistantInTheDeck() {
        Player player = new Player();
        try {
            player.createDeck(WizardTeam.TEAM1);
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
        }
        player.useAssistant(player.getAssistantsDeck().get(3));
        assertEquals(player.getAssistantsDeck().size(), 9);
    }

    /**
     * Tests if the Player can use an Assistant which isn't in the deck
     */
    @Test
    @DisplayName("Tests if the Player can use an Assistant which isn't in the deck")
    public void useAssistantNotInTheDeck() {
        Player player = new Player();
        try {
            player.createDeck(WizardTeam.TEAM1);
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
        }
        player.useAssistant(player.getAssistantsDeck().get(3));
        assertEquals(9, player.getAssistantsDeck().size());
        System.out.println(player.getLastAssistantPlayed());
        assertThrows(InvalidParameterException.class, () -> player.useAssistant(player.getLastAssistantPlayed()));
        assertEquals(9, player.getAssistantsDeck().size());
    }

    /**
     * Tests if the Player can use an Assistant when the deck is empty
     */
    @Test
    @DisplayName("Tests if the Player can use an Assistant when the deck is empty")
    public void useAssistantEmptyDeckTest() {
        Player player = new Player();
        try {
            player.createDeck(WizardTeam.TEAM1);
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
        }
        Assistant firstAssistant = player.getAssistantsDeck().get(1);
        int size = player.getAssistantsDeck().size();
        IntStream.rangeClosed(0, size - 1).forEach(i -> player.useAssistant(player.getAssistantsDeck().get(i + 1)));


        assertThrows(InvalidParameterException.class, () -> player.useAssistant(firstAssistant));
    }
}
