package it.polimi.ingsw.am37.model.character;

import it.polimi.ingsw.am37.model.Player;
import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.UnlimitedStudentsContainer;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * This class is a collections of all base effects implemented in the game. The base effect are composed to create
 * effects of each {@link Character}. Use the {@link this#getEffects(Effect)} passing the effect type desired to build
 * the effect. Base effect are created as lambda functions and stored in an array list. Are identified by their
 * position, and its manually specified the order.
 */
public final class EffectDatabase {

    /**
     * Represent the base effects to access all the possible method. They are composed to create required characters
     */
    private static final ArrayList<BiConsumer<Option, State>> baseEffects = new ArrayList<>();

    /**
     * Default constructor of the database. Mainly this fill up the array of base effects
     */
    private EffectDatabase() {
    }

    public static void setUp() {

        //0 - remove from card
        baseEffects.add(0,
                (option, state) -> state.getContainer().removeContainer(state.getServiceContainer())
        );
        //1 - service to card
        baseEffects.add(1,
                (option, state) -> state.getContainer().uniteContainers(state.getServiceContainer())
        );
        //2 - remove from bag
        baseEffects.add(2,
                (option, state) -> {
                    state.setServiceContainer(new UnlimitedStudentsContainer());
                    state.getServiceContainer().uniteContainers(option.getBag().extractStudents(option.getIntPar()));
                }
        );
        //3 - add to bag
        baseEffects.add(3,
                (option, state) -> option.getBag().addStudents(state.getServiceContainer())
        );
        //4 - add to island
        baseEffects.add(4,
                (option, state) -> option.getIsland().addStudents(state.getServiceContainer())
        );
        //5 - professors with tie students
        baseEffects.add(5,
                (option, state) -> option.getController().getTurnManager().setProfWithDraw()
        );
        //6 - fake mother nature movement on option.island
        baseEffects.add(6,
                (option, state) -> option.getController().getIslandsManager().motherNatureActionNoMovement(option.getIsland(), option.getController().getTurnManager().getPlayers())
        );
        //7 - increase mother nature movement in assistant
        baseEffects.add(7,
                (option, state) -> option.getPlayer().getLastAssistantPlayed().increaseMNMovement(option.getIntPar())
        );
        //8 - decrease noEntry tile on state
        baseEffects.add(8,
                (option, state) -> state.setNoEntryTiles(state.getNoEntryTiles() - 1)
        );
        //9 - add noEntry tile to island
        baseEffects.add(9,
                (option, state) -> {
                    if (state.getNoEntryTiles() > 0)
                        option.getIsland().addNoEntryTile(1);
                }

        );
        //10 - set no Tower
        baseEffects.add(10,
                (option, state) -> option.getController().getIslandsManager().setNoTowerFlag()
        );
        //11 - add to entrance
        baseEffects.add(11,
                (option, state) -> option.getPlayer().getBoard().addStudentsToEntrance(state.getServiceContainer())
        );
        //12 - remove from entrance
        baseEffects.add(12,
                (option, state) -> option.getPlayer().getBoard().removeStudentsFromEntrance(state.getServiceContainer())
        );
        //13 - add to dining
        baseEffects.add(13,
                (option, state) -> option.getController().getTurnManager().addStudentsToDining(state.getServiceContainer())
        );
        //14 - remove from dining
        baseEffects.add(14,
                (option, state) -> option.getController().getTurnManager().removeStudentsFromDining(state.getServiceContainer())
        );
        //15 - increase influence
        baseEffects.add(15,
                (option, state) -> option.getController().getIslandsManager().setPowerBonusFlag(option.getIntPar())
        );
        //16 - do not count a color
        baseEffects.add(16,
                (option, state) -> option.getController().getIslandsManager().setDisabledColorFlag(option.getColor())
        );
        //17 - for all player remove some student of a color and adds them to the service container
        baseEffects.add(17,
                (option, state) -> {
                    UnlimitedStudentsContainer temp2;
                    int studentsToRemove;
                    int sum = 0;
                    for (Player player : option.getController().getTurnManager().getPlayers()) {
                        LimitedStudentsContainer temp = new LimitedStudentsContainer(option.getIntPar());
                        studentsToRemove = Math.min(player.getBoard().getDiningRoom().getByColor(option.getColor()), option.getIntPar());
                        temp.addStudents(studentsToRemove, option.getColor());
                        player.getBoard().getDiningRoom().removeContainer(temp);
                        sum += studentsToRemove;
                    }
                    temp2 = new UnlimitedStudentsContainer();
                    temp2.addStudents(sum, option.getColor());
                    state.setServiceContainer(temp2);
                }
        );
        //18 - copy primaryContainer into serviceContainer
        baseEffects.add(18,
                (option, state) -> {
                    UnlimitedStudentsContainer temp = new UnlimitedStudentsContainer();
                    temp.uniteContainers(option.getPrimaryContainer());
                    state.setServiceContainer(temp);
                }
        );
        //19 - copy secondaryContainer into serviceContainer
        baseEffects.add(19,
                (option, state) -> {
                    UnlimitedStudentsContainer temp = new UnlimitedStudentsContainer();
                    temp.uniteContainers(option.getSecondaryContainer());
                    state.setServiceContainer(temp);
                }
        );
    }

    /**
     * Get the array of all the base effects as functions for an effect type
     *
     * @param effect The card effect
     */
    public static ArrayList<BiConsumer<Option, State>> getEffects(Effect effect) {
        return effect.getBaseEffects().stream()
                .map(baseEffects::get)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
