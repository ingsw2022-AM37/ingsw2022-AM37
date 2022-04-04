package it.polimi.ingsw.am37.model.character;

import it.polimi.ingsw.am37.model.FactionColor;
import it.polimi.ingsw.am37.model.Player;
import it.polimi.ingsw.am37.model.student_container.FixedUnlimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.UnlimitedStudentsContainer;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public final class EffectDatabase {

    /**
     * Represents the base effects to access all the possible method. They are composed
     * to create required characters
     */
    private static final ArrayList<BiConsumer<Option, State>> baseEffects = new ArrayList<>();

    /**
     * Default Constructor. It creates every single base effect possible in the game.
     */
    public EffectDatabase() {

        //0 - remove from card
        baseEffects.add(0,
                (option, state) -> {
                    for (FactionColor color :
                            FactionColor.values()) {
                        state.container.removeStudents(option.getRemoveContainer().getByColor(color), color);
                    }
                }
        );
        //1 - service to card
        baseEffects.add(1,
                (option, state) -> state.container.uniteContainers(state.serviceContainer)
        );
        //2 - remove from bag
        baseEffects.add(2,
                (option, state) -> {
                    state.serviceContainer = new UnlimitedStudentsContainer();
                    state.serviceContainer.uniteContainers(option.getBag().extractStudents(option.getIntPar()));
                }
        );
        //3 - add to bag
        baseEffects.add(3,
                (option, state) -> {
                    // FIXME need a bag.addStudents
                    // option.getBag().
                }
        );
        //4 - add to island
        baseEffects.add(4,
                (option, state) -> {
                    FixedUnlimitedStudentsContainer temp = new FixedUnlimitedStudentsContainer();
                    temp.addStudents(1, option.getColor());
                    option.getIsland().addStudents(temp);
                }
        );
        //5 - professors with tie students
        baseEffects.add(5,
                (option, state) -> {
                    //FIXME need the right flag
                }
        );
        //6 - use check conqueror on option.island
        baseEffects.add(6,
                (option, state) -> {
                    //FIXME need the right flag
                }
        );
        //7 - increase mother nature movement in assistant
        baseEffects.add(7,
                (option, state) -> option.getPlayer().getLastAssistantPlayed().increaseMNMovement(2)
        );
        //8 - remove noEntry tile
        baseEffects.add(8,
                (option, state) -> state.noEntryTiles--
        );
        //9 - add noEntry tile to island
        baseEffects.add(9,
                (option, state) -> option.getIsland().setNoEntryTile()
        );
        //10 - set no Tower
        baseEffects.add(10,
                (option, state) -> {
                    //FIXME need the right flag
                }
        );
        //11 - add to entrance
        baseEffects.add(11,
                (option, state) -> option.getPlayer().getBoard().addStudentsToEntrance(option.getAddContainer())
        );
        //12 - remove from entrance
        baseEffects.add(12,
                (option, state) -> option.getPlayer().getBoard().removeStudentsFromEntrance(option.getRemoveContainer())
        );
        //13 - add to dining
        baseEffects.add(13,
                (option, state) -> option.getPlayer().getBoard().addStudentToDining(option.getAddContainer())
        );
        //14 - remove from dining
        baseEffects.add(14,
                (option, state) -> {
                    for (FactionColor color :
                            FactionColor.values()) {
                        option.getPlayer().getBoard().removeStudentFromDining(option.getRemoveContainer().getByColor(color), color);
                    }
                }
        );
        //15 - increase influence
        baseEffects.add(15,
                (option, state) -> {
                    // FIXME need the right flag
                }
        );
        //16 - do not count a color
        baseEffects.add(16,
                (option, state) -> {
                    // FIXME need the right flag
                }
        );
        //17 - remove from all player some student of a color
        baseEffects.add(17,
                (option, state) -> {
                    //FIXME need method getPlayers in controller
                    //option.getController().getPlayers.foreach(player -> player.getBoard().removeStudentFromDining(option.getIntPar(), option.getColor()));
                }
        );
    }

    /**
     * Get the array of all the effects as functions for the specified characters
     *
     * @param effect The card effect
     */
    public static ArrayList<BiConsumer<Option, State>> getEffects(Effect effect) {
        return baseEffects.stream()
                .filter(
                        obj -> effect.getBaseEffects().contains(baseEffects.indexOf(obj))
                )
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
