package it.polimi.ingsw.am37.model.character;

import it.polimi.ingsw.am37.model.*;
import it.polimi.ingsw.am37.model.student_container.FixedUnlimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.UnlimitedStudentsContainer;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EffectDatabaseTest {

    private static ArrayList<BiConsumer<Option, State>> baseEffects;
    private OptionBuilder optionBuilder;
    private GameManager manager;

    @BeforeAll
    static void beforeAll() {
        try {
            EffectDatabase.setUp();
            Field baseEffectField = EffectDatabase.class.getDeclaredField("baseEffects");
            baseEffectField.setAccessible(true);
            baseEffects = (ArrayList<BiConsumer<Option, State>>) baseEffectField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setUp() {
        this.manager = new GameManager(2, false);
        Player player = new Player();
        optionBuilder = OptionBuilder.newBuilder(manager, player);
    }

    @Test
    @DisplayName("Test base effect 0 - Move students from Card")
    public void testBaseEffect0() {
        BiConsumer<Option, State> singleEffect = baseEffects.get(0);
        LimitedStudentsContainer stateContainer = new LimitedStudentsContainer(10);
        UnlimitedStudentsContainer remContainer = new UnlimitedStudentsContainer();
        remContainer.addStudents(2, FactionColor.BLUE);
        stateContainer.addStudents(4, FactionColor.BLUE);
        Option option = optionBuilder.build();
        State state = new State(stateContainer, 0);
        state.setServiceContainer(remContainer);
        singleEffect.accept(option, state);
        assertEquals(2, state.getContainer().size());
        assertEquals(2, state.getServiceContainer().size());
    }

    @Test
    @DisplayName("Test base effect 1 - Move students to Card")
    public void testBaseEffect1() {
        BiConsumer<Option, State> singleEffect = baseEffects.get(1);
        LimitedStudentsContainer addContainer = new LimitedStudentsContainer(10), stateContainer =
                new LimitedStudentsContainer(10);
        addContainer.addStudents(2, FactionColor.BLUE);
        stateContainer.addStudents(4, FactionColor.BLUE);
        Option option = optionBuilder.build();
        State state = new State(stateContainer, 0);
        state.getServiceContainer().uniteContainers(addContainer);
        singleEffect.accept(option, state);
        assertEquals(6, state.getContainer().size());
        assertEquals(2, state.getServiceContainer().size());
    }

    @Test
    @DisplayName("Test base effect 2 - Remove from bag")
    public void testBaseEffect2() {
        BiConsumer<Option, State> singleEffect = baseEffects.get(2);
        Option option = optionBuilder.intPar(3).build();
        State state = new State(null, 0);
        int oldBag = manager.getBag().size();
        singleEffect.accept(option, state);
        assertEquals(3, state.getServiceContainer().size());
        assertEquals(oldBag - 3, option.getBag().size());
    }

    @Test
    @DisplayName("Test base effect 3 - Add to bag")
    public void testBaseEffect3() {
        BiConsumer<Option, State> singleEffect = baseEffects.get(3);
        LimitedStudentsContainer addContainer = new LimitedStudentsContainer(10);
        addContainer.addStudents(4, FactionColor.BLUE);
        Option option = optionBuilder.build();
        State state = new State(null, 0);
        state.getServiceContainer().uniteContainers(addContainer);
        int oldBag = manager.getBag().size();
        singleEffect.accept(option, state);
        assertEquals(oldBag + 4, manager.getBag().size());
    }

    @Test
    @DisplayName("Test base effect 4 - Add to island")
    void testBaseEffect4() {
        BiConsumer<Option, State> singleEffect = baseEffects.get(4);
        LimitedStudentsContainer addContainer = new LimitedStudentsContainer(10);
        addContainer.addStudents(5, FactionColor.BLUE);
        Option option = optionBuilder.island(new Island(new FixedUnlimitedStudentsContainer(), 0)).build();
        State state = new State(null, 0);
        state.getServiceContainer().uniteContainers(addContainer);
        singleEffect.accept(option, state);
        assertEquals(5, option.getIsland().getStudentsOnIsland().size());
    }

    @Test
    @DisplayName("Test base effect 5 - Get prof with draw")
    void testBaseEffect5() {
        BiConsumer<Option, State> singleEffect = baseEffects.get(5);
        Option option = optionBuilder.build();

        try {
            Field field = manager.getTurnManager().getClass().getDeclaredField("getProfWithDraw");
            field.setAccessible(true);
            assertFalse((boolean) field.get(manager.getTurnManager()));
            singleEffect.accept(option, null);
            assertTrue((boolean) field.get(manager.getTurnManager()));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Test
    @DisplayName("Test base effect 6 - Simulate call of the method")
    void testBaseEffect6() throws IllegalAccessException, NoSuchFieldException {
        BiConsumer<Option, State> singleEffect = baseEffects.get(6);
        manager.prepareGame();
        Option option = optionBuilder.island(manager.getIslandsManager().getIslands().get(0)).build();
        Field field = GameManager.class.getDeclaredField("islandsManager");
        field.setAccessible(true);
        IslandsManager islandsManager = mock(((IslandsManager) field.get(manager)).getClass());
        field.set(manager, islandsManager);
        islandsManager.setCurrentPlayer(manager.getTurnManager().getPlayers().get(0));
        doNothing().when(islandsManager).motherNatureActionNoMovement(any(), any());
        singleEffect.accept(option, null);
        verify(islandsManager).motherNatureActionNoMovement(any(), any());
    }

    @Test
    @DisplayName("Test base effect 7 - Increase mother nature movement in assistant")
    void testBaseEffect7() {
        BiConsumer<Option, State> singleEffect = baseEffects.get(7);
        manager.prepareGame();
        Player currentPlayer = mock(Player.class);
        int intPar = new Random().nextInt(5);
        Assistant assistant = new Assistant(WizardTeam.TEAM1, 2, 3);
        assistant = spy(assistant);
        when(currentPlayer.getLastAssistantPlayed()).thenReturn(assistant);
        Option option = optionBuilder.player(currentPlayer).intPar(intPar).build();
        singleEffect.accept(option, null);
        verify(currentPlayer.getLastAssistantPlayed()).increaseMNMovement(intPar);
        assertEquals(3 + intPar, currentPlayer.getLastAssistantPlayed().getMNMovement());
    }

    @Test
    @DisplayName("Test base effect 8 - Decrease no entry tiles")
    void testBaseEffect8() {
        BiConsumer<Option, State> singleEffect = baseEffects.get(8);
        Option option = optionBuilder.build();
        int initialStack = 4;
        State state = new State(null, initialStack);
        singleEffect.accept(option, state);
        assertEquals(initialStack - 1, state.getNoEntryTiles());
    }

    @Test
    @DisplayName("Test base effect 9 - Add no entry tiles on island")
    void testBaseEffect9() {
        BiConsumer<Option, State> singleEffect = baseEffects.get(9);
        Island island = spy(new Island(null, 44));
        State state = new State(null, 2);
        Option option = optionBuilder.island(island).build();
        singleEffect.accept(option, state);
        assertEquals(1, island.getNoEntryTile());
    }

    @Test
    @DisplayName("Test base effect 9 - No call the add entry tiles when none are available in state")
    void testBaseEffect9EmptyState() {
        BiConsumer<Option, State> singleEffect = baseEffects.get(9);
        Island island = spy(new Island(null, 44));
        State state = new State(null, 0);
        Option option = optionBuilder.island(island).build();
        singleEffect.accept(option, state);
        verify(island, times(0)).addNoEntryTile(anyInt());
        assertEquals(0, island.getNoEntryTile());
    }

    @Test
    @DisplayName("Test base effect 10 - Set no Tower")
    void testBaseEffect10() throws NoSuchFieldException, IllegalAccessException {
        BiConsumer<Option, State> singleEffect = baseEffects.get(10);
        GameManager spyGame = spy(manager);
        IslandsManager spyManager = spy(new IslandsManager());
        when(spyGame.getIslandsManager()).thenReturn(spyManager);
        Option option = OptionBuilder.newBuilder(spyGame, new Player()).build();
        singleEffect.accept(option, null);
        Field field = IslandsManager.class.getDeclaredField("noTowerFlag");
        field.setAccessible(true);
        verify(spyManager).setNoTowerFlag();
        assertTrue((boolean) field.get(spyManager));
    }

    @Test
    @DisplayName("Test base effect 11 - Add to entrance")
    void testBaseEffect11() {
        BiConsumer<Option, State> singleEffect = baseEffects.get(11);
        manager.prepareGame();
        UnlimitedStudentsContainer addContainer = new UnlimitedStudentsContainer();
        addContainer.uniteContainers(manager.getBag().extractStudents(2));
        LimitedStudentsContainer startContainer = new LimitedStudentsContainer(10);
        for (FactionColor color :
                FactionColor.values()) {
            startContainer.addStudents(1, color);
        }
        Player mockPlayer = spy(Player.class);
        Board spyBoard = spy(new Board(2, TowerColor.BLACK, false, startContainer, mockPlayer));
        when(mockPlayer.getBoard()).thenReturn(spyBoard);
        Option option = optionBuilder.player(mockPlayer).build();
        State state = new State(null, 0);
        state.setServiceContainer(addContainer);
        singleEffect.accept(option, state);
        int[] expectedArray = new int[FactionColor.values().length], currentArray =
                new int[FactionColor.values().length];
        for (FactionColor color :
                FactionColor.values()) {
            expectedArray[color.getIndex()] = 1 + addContainer.getByColor(color);
            currentArray[color.getIndex()] = spyBoard.getEntrance().getByColor(color);
        }
        assertArrayEquals(expectedArray, currentArray);
    }

    @Test
    @DisplayName("Test base effect 12 - Remove from entrance")
    void testBaseEffect12() {
        BiConsumer<Option, State> singleEffect = baseEffects.get(12);
        manager.prepareGame();
        LimitedStudentsContainer startContainer = manager.getBag().extractStudents(7);
        UnlimitedStudentsContainer removeContainer = new UnlimitedStudentsContainer();
        removeContainer.addStudents(2,
                Arrays.stream(FactionColor.values()).filter(color -> startContainer.getByColor(color) >= 2).findAny().orElseThrow());
        Player mockPlayer = spy(Player.class);
        Board spyBoard = spy(new Board(2, TowerColor.BLACK, false, startContainer, mockPlayer));
        when(mockPlayer.getBoard()).thenReturn(spyBoard);
        Option option = optionBuilder.player(mockPlayer).build();
        State state = new State(null, 0);
        state.setServiceContainer(removeContainer);
        int[] old = new int[FactionColor.values().length];
        for (FactionColor color :
                FactionColor.values()) {
            old[color.getIndex()] = startContainer.getByColor(color);
        }
        singleEffect.accept(option, state);
        for (FactionColor color :
                FactionColor.values()) {
            if (removeContainer.getByColor(color) > 0)
                assertEquals(old[color.getIndex()] - 2, spyBoard.getEntrance().getByColor(color));
            else
                assertEquals(old[color.getIndex()], spyBoard.getEntrance().getByColor(color));
        }
    }


    @Test
    @DisplayName("Test base effect 13 - Add to dining")
    void testBaseEffect13() {
        BiConsumer<Option, State> singleEffect = baseEffects.get(13);
        manager.prepareGame();
        UnlimitedStudentsContainer addContainer = new UnlimitedStudentsContainer();
        addContainer.uniteContainers(manager.getBag().extractStudents(2));
        GameManager mockGameManager = mock(GameManager.class);
        TurnManager mockTurnManager = mock(TurnManager.class);
        when(mockGameManager.getTurnManager()).thenReturn(mockTurnManager);
        Option option = OptionBuilder.newBuilder(mockGameManager, new Player()).build();
        State state = new State(null, 0);
        state.setServiceContainer(addContainer);
        singleEffect.accept(option, state);
        verify(mockTurnManager).addStudentsToDining(addContainer);
    }

    @Test
    @DisplayName("Test base effect 14 - Add to dining")
    void testBaseEffect14() {
        BiConsumer<Option, State> singleEffect = baseEffects.get(14);
        manager.prepareGame();
        UnlimitedStudentsContainer addContainer = new UnlimitedStudentsContainer();
        addContainer.uniteContainers(manager.getBag().extractStudents(2));
        GameManager mockGameManager = mock(GameManager.class);
        TurnManager mockTurnManager = mock(TurnManager.class);
        when(mockGameManager.getTurnManager()).thenReturn(mockTurnManager);
        Option option = OptionBuilder.newBuilder(mockGameManager, new Player()).build();
        State state = new State(null, 0);
        state.setServiceContainer(addContainer);
        singleEffect.accept(option, state);
        verify(mockTurnManager).removeStudentsFromDining(addContainer);
    }

    @Test
    @DisplayName("Test base effect 15 - Increase influence")
    void testBaseEffect15() throws NoSuchFieldException, IllegalAccessException {
        BiConsumer<Option, State> singleEffect = baseEffects.get(15);
        manager.prepareGame();
        Option option = optionBuilder.intPar(2).build();
        singleEffect.accept(option, null);
        Field field = IslandsManager.class.getDeclaredField("powerBonusFlag");
        field.setAccessible(true);
        assertEquals(2, (Integer) field.get(manager.getIslandsManager()));
    }

    @Test
    @DisplayName("Test base effect 16 - Do not count a color")
    void testBaseEffect16() throws NoSuchFieldException, IllegalAccessException {
        BiConsumer<Option, State> singleEffect = baseEffects.get(16);
        manager.prepareGame();
        Option option = optionBuilder.color(FactionColor.BLUE).build();
        singleEffect.accept(option, null);
        Field field = IslandsManager.class.getDeclaredField("disabledColorFlag");
        field.setAccessible(true);
        assertEquals(FactionColor.BLUE, field.get(manager.getIslandsManager()));
    }

    @Test
    @DisplayName("Test base effect 17 - Remove from all player's dining")
    void testBaseEffect17() {
        BiConsumer<Option, State> singleEffect = baseEffects.get(17);
        manager.prepareGame();
        Option option = optionBuilder.intPar(2).color(FactionColor.BLUE).build();
        Board board = mock(Board.class);
        LimitedStudentsContainer mockContainer = mock(LimitedStudentsContainer.class);
        when(board.getDiningRoom()).thenReturn(mockContainer);
        manager.getTurnManager().getPlayers().replaceAll(Mockito::spy);
        manager.getTurnManager().getPlayers().forEach(player -> when(player.getBoard()).thenReturn(board));
        singleEffect.accept(option, new State(mockContainer, 0));
        verify(mockContainer, times(manager.getTurnManager().getPlayers().size())).removeContainer(isNotNull());

    }

    @Test
    @DisplayName("Test base effect 18 - Copy primaryContainer to serviceContainer")
    void testBaseEffect18() {
        BiConsumer<Option, State> singleEffect = baseEffects.get(18);
        Random random = new Random();
        int[] expectedArray = new int[FactionColor.values().length];
        LimitedStudentsContainer container = new LimitedStudentsContainer(new int[]{10, 10, 10, 10, 10});
        for (FactionColor color :
                FactionColor.values()) {
            expectedArray[color.getIndex()] = random.nextInt(10);
            container.addStudents(expectedArray[color.getIndex()], color);
        }
        Option option = optionBuilder.primaryContainer(container).build();
        State state = new State(null, 0);
        singleEffect.accept(option, state);
        int[] currentArray = new int[FactionColor.values().length];
        for (FactionColor color :
                FactionColor.values()) {
            currentArray[color.getIndex()] = state.getServiceContainer().getByColor(color);
        }
        assertArrayEquals(expectedArray, currentArray);
    }

    @Test
    @DisplayName("Test base effect 19 - Copy secondaryContainer to serviceContainer")
    void testBaseEffect19() {
        BiConsumer<Option, State> singleEffect = baseEffects.get(19);
        Random random = new Random();
        int[] expectedArray = new int[FactionColor.values().length];
        LimitedStudentsContainer container = new LimitedStudentsContainer(new int[]{10, 10, 10, 10, 10});
        for (FactionColor color :
                FactionColor.values()) {
            expectedArray[color.getIndex()] = random.nextInt(10);
            container.addStudents(expectedArray[color.getIndex()], color);
        }
        Option option = optionBuilder.secondaryContainer(container).build();
        State state = new State(null, 0);
        singleEffect.accept(option, state);
        int[] currentArray = new int[FactionColor.values().length];
        for (FactionColor color :
                FactionColor.values()) {
            currentArray[color.getIndex()] = state.getServiceContainer().getByColor(color);
        }
        assertArrayEquals(expectedArray, currentArray);
    }

    @AfterEach
    void tearDown() {
        optionBuilder.clear();
    }
}