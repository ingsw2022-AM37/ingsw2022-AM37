package it.polimi.ingsw.am37.model.character;

import java.util.ArrayList;
import java.util.function.BiConsumer;

/**
 * This class represents the effect of the Character
 */
public class CharacterEffect {

	/**
	 * It's the set of basic effects that make up the character's effect.
	 */
	private ArrayList<BiConsumer<Option, State>> baseEffects;

	public CharacterEffect() {

	}

	/**
	 * @param option It's the parameters needed to run the effect
	 */
	public void useEffect(Option option) {
		for (BiConsumer<Option, State> singleEffect : baseEffects) {
			//FIXME add a proper state
			singleEffect.accept(option, new State());
		}
	}

}