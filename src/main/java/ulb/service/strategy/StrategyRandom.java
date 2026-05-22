package ulb.service.strategy;

import ulb.model.ability.Ability;
import ulb.model.ability.AbilitySet;
import ulb.model.action.Action;
import ulb.model.action.SwapAction;
import ulb.model.action.UseAbilityAction;
import ulb.model.battle.Battle;
import ulb.model.bugemon.Bugemon;

import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * Strategy implementations picking a random ability of the active Bugemon nto use at each turn.
 */
public class StrategyRandom implements Strategy {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Action pickAction(Battle battle, Battle.ParticipantLabel teamLabel) {
		Vector<Action> actions = battle.getAvailableActions(teamLabel);
		Action chosenAction = null;

		if (actions.stream().anyMatch(a -> a instanceof UseAbilityAction)) { // if the available action is UseAbilityAction
			Ability randomAbility = getRandomAbility(battle, teamLabel);
			chosenAction = new UseAbilityAction(randomAbility);

			// if the Bugemon is KO, it needs to be swapped (the only available action is SWAP)
		} else if (actions.stream().anyMatch(a -> a instanceof SwapAction)) {
			Bugemon chosenBugemon = pickRandomBugemon(battle, teamLabel);
			if (chosenBugemon != null) {
				chosenAction = new SwapAction(chosenBugemon);
			}
		}
		return chosenAction;
	}

	/**
	 * Returns a random ability from the active Bugemon on the player's side.
	 *
	 * @return a randomly selected Ability of the self (Team A) active Bugemon
	 */
	private Ability getRandomAbility(Battle battle, Battle.ParticipantLabel teamLabel) {
		Bugemon bugemon = battle.getActiveBugemon(teamLabel);
		AbilitySet AbilitiesA = bugemon.getAbilities();
		Random rand = new Random();
		int i = rand.nextInt(AbilitiesA.size());

		return AbilitiesA.getAbility(i);
	}

	/**
	 * Picks a random non-KO Bugemon from the team, or null if they are all KO
	 *
	 * @return the randomly chosen bugemon
	 */
	private Bugemon pickRandomBugemon(Battle battle, Battle.ParticipantLabel teamLabel) {
		List<Bugemon> availableBugemons = battle.getAvailableBugemons(teamLabel);
		if (!availableBugemons.isEmpty()) {
			Random rand = new Random();
			int i = rand.nextInt(availableBugemons.size());

			return availableBugemons.get(i);
		}
		return null;
	}
}
