package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.message.MessageType;
import it.polimi.ingsw.am37.message.UpdateMessage;
import it.polimi.ingsw.am37.model.Island;
import it.polimi.ingsw.am37.model.UpdatableObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class GUIViewTest {
    private static GuiView view;

    @BeforeAll
    static void beforeAll() {
        view = new GuiView();
        view.gameStarted();
    }

    /**
     * Test draw islands
     */
    @Test
    void testIslandDraw() {
        ArrayList<UpdatableObject> islands = new ArrayList<>();
        for (
                int i = 0;
                i < 6; i++) {
            islands.add(new Island(null, i));
        }

        UpdateMessage message = new UpdateMessage(islands.stream().toList(), MessageType.ERROR, "");
        view.updateView(message, null);
    }
}
