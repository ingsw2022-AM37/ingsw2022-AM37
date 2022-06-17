package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.model.*;
import it.polimi.ingsw.am37.model.character.Character;

import java.util.*;

/**
 * This class is a reduced version of the game model. This contains all objects that could be displayed or accessed by
 * the client. The information to populate and keeps updated with the server complete model are provided after each
 * action with the {@link it.polimi.ingsw.am37.message.UpdateMessage#getUpdatedObjects()} field. Most of the fields are
 * stored hash hashmap to improve and speed find them to update.
 */
public class ReducedModel {

    /**
     * All the islands visible, stored as sorted map with their {@link Island#getIslandId()} as key
     */
    private final TreeMap<Integer, Island> islands;

    /**
     * All the characters enable in this match, stored as set
     */
    private final Set<Character> characters;

    /**
     * All the players, stored as hashmap with their {@link Player#getPlayerId()} as key
     */
    private final HashMap<String, Player> players;

    /**
     * All the boards, stored as hashmap with their player {@link Player#getPlayerId()} as key
     */
    private final HashMap<String, Board> boards;

    /**
     * All the clouds, stored as hashmap with their {@link Cloud#getCloudId()} as key
     */
    private final HashMap<String, Cloud> clouds;

    /**
     * Default constructor of the reduced model. All fields are created empty, use {@link ReducedModel#update(List)}
     * with a list of object create to fill them
     */
    public ReducedModel() {
        islands = new TreeMap<>();
        characters = new HashSet<>();
        players = new HashMap<>();
        boards = new HashMap<>();
        clouds = new HashMap<>();
    }


    /**
     * Fill the reduced model with updated (or created) objects. This function needs only a list of
     * {@link UpdatableObject} and insert them in the right map/set.
     *
     * @param updatedObjects the list of updatedObjects
     */
    public void update(List<? extends UpdatableObject> updatedObjects) {
        islands.clear();
        for (UpdatableObject obj : updatedObjects) {
            switch (obj.type) {
                case ISLAND -> {
                    Island island = (Island) obj;
                    if (island.getNumIslands() != 0) islands.put(island.getIslandId(), island);
                }
                case CLOUD -> {
                    Cloud cloud = (Cloud) obj;
                    clouds.put(cloud.getCloudId(), cloud);
                }
                case PLAYER -> {
                    Player player = (Player) obj;
                    players.put(player.getPlayerId(), player);
                    boards.put(player.getPlayerId(), player.getBoard());
                }
                case CHARACTER -> {
                    Character character = (Character) obj;
                    if (!characters.add(character)) {
                        characters.remove(character);
                        characters.add(character);
                    }
                }
            }
        }
    }

    /**
     * @return the islands of this model as list
     */
    public List<Island> getIslands() {
        return islands.values().stream().toList();
    }

    /**
     * @return the characters
     */
    public Set<Character> getCharacters() {
        return characters;
    }

    /**
     * @return the players of this match
     */
    public HashMap<String, Player> getPlayers() {
        return players;
    }

    /**
     * @return the boards to show
     */
    public HashMap<String, Board> getBoards() {
        return boards;
    }

    /**
     * @return the clouds of this match
     */
    public HashMap<String, Cloud> getClouds() {
        return clouds;
    }
}