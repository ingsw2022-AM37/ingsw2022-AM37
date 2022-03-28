package it.polimi.ingsw.am37.model.character;

import it.polimi.ingsw.am37.model.student_container.FixedUnlimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.UnlimitedStudentsContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class EffectDatabase {

    /**
     *
     */
    private ArrayList<BiConsumer<Option, State>> baseEffects;

    /**
     *
     */
    private HashMap<Effect, ArrayList<Integer>> database;

    /**
     *
     */
    public EffectDatabase() {
        baseEffects = new ArrayList<>();
        database = new HashMap<>();

        //remove from card
        baseEffects.add(
                (option, state) -> {
                    state.container.removeStudents(1, option.color);
                }
        );
        //add to island
        baseEffects.add(
                (option, state) -> {
                    FixedUnlimitedStudentsContainer temp = new FixedUnlimitedStudentsContainer();
                    temp.addStudents(1, option.color);
                    option.island.addStudents(temp);
                }
        );
        //remove from bag
        baseEffects.add(
                (option, state) -> {
                    state.serviceContainer = new UnlimitedStudentsContainer();
                    state.serviceContainer.uniteContainers(option.bag.extractStudents(option.intPar));
                }
        );
        //add to bag
        baseEffects.add(
                (option, state) -> {

                }
        );
        //service to card
        baseEffects.add(
                (option, state) -> {
                    state.container.uniteContainers(state.serviceContainer);
                }
        );
        //professors with tie students
        baseEffects.add(
                (option, state) -> {

                }
        );
        //use check conqueror on option.island
        baseEffects.add(
                (option, state) -> {

                }
        );
        //increase mother nature movement in assistant
        baseEffects.add(
                (option, state) -> {
                    option.player.getLastAssistantPlayed().increaseMNMovement(2);
                }
        );
        //remove noEntry tile
        baseEffects.add(
                (option, state) -> {
                    state.noEntryTiles--;
                }
        );
        //add noEntry tile to island
        baseEffects.add(
                (option, state) -> {
                    option.island.setNoEntryTile();
                }
        );
        //set no Tower
        baseEffects.add(
                (option, state) -> {

                }
        );
        //add to entrance
        baseEffects.add(
                (option, state) -> {

                }
        );
        //remove from entrance
        baseEffects.add(
                (option, state) -> {

                }
        );
        //add to dining
        baseEffects.add(
                (option, state) -> {

                }
        );
        //remove from dining
        baseEffects.add(
                (option, state) -> {

                }
        );
        //increase influence
        baseEffects.add(
                (option, state) -> {

                }
        );
        //do not count a color
        baseEffects.add(
                (option, state) -> {

                }
        );
        //remove from all player some student of a color
        baseEffects.add(
                (option, state) -> {

                }
        );
    }

    /**
     *
     */
    public ArrayList<BiConsumer<Option, State>> getEffects(Effect effect) {
        ArrayList<Integer> indexes = database.get(effect);
        ArrayList<BiConsumer<Option, State>> consumers = new ArrayList<>();
        for (Integer index : indexes) {
            consumers.add(baseEffects.get(index));
        }
        return consumers;
    }
}
