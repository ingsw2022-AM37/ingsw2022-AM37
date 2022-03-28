package it.polimi.ingsw.am37.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javax.management.InstanceAlreadyExistsException;
import java.security.InvalidParameterException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test for Player class.
 */
public class PlayerTest {

    /**
     * Tests if the Player can instance two decks.
     */
    @Test
    @DisplayName("Tests if the Player can instance two decks.")
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
        assertEquals(player.getAssistantsDeck().size(), 9);
        assertThrows(InvalidParameterException.class, () -> player.useAssistant(player.getLastAssistantPlayed()));
        assertEquals(player.getAssistantsDeck().size(), 9);
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
        Assistant firstAssistant = player.getAssistantsDeck().get(0);
        while (player.getAssistantsDeck().size() > 0)
            player.useAssistant(player.getAssistantsDeck().get(0));
        assertThrows(InvalidParameterException.class, () -> player.useAssistant(firstAssistant));
    }
}
