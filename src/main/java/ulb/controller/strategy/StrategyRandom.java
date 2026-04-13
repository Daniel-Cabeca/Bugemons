package ulb.controller.strategy;

import java.util.Random;
import java.util.List;
import java.lang.Thread;

import ulb.controller.BattleController;
import ulb.controller.action.*;
import ulb.model.Player;
import ulb.model.ability.Ability;
import ulb.model.ability.AbilitySet;
import ulb.model.bugemon.Bugemon;
import ulb.model.battle.Battle;
import ulb.model.battle.BattleState;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.service.ItemService;

public class StrategyRandom implements Strategy, Runnable {
    private BattleController battleController;
    private final long SLEEP_TIME = 1000;

    public StrategyRandom(Battle battle, ItemService itemService) {
        this(new BattleController(new Player(itemService), battle, ParticipantLabel.TEAM_B, itemService));
    }
    public StrategyRandom(BattleController battleController){
        this.battleController = battleController;
    }

    @Override
    public void run(){
        this.play();
    }

    public Action pickAction(){
        return new Run();
    }

    /**
     * Plays the game loop in the automatic battle mode
     */
    public void play(){
        while (!battleController.isGameFinished()){
            this.playAutoTurn();
            try {
                Thread.sleep(SLEEP_TIME);
            } catch(Exception e) {}
            
        }
    }

    /**
	 * Plays a turn in the automatic battle mode
	 */
	public BattleState playAutoTurn() {
		BattleState state = battleController.getState();

		if (state == BattleState.INGAME) {
			useRandomAbility();
		} else if (state == BattleState.SWAPPING) {
			Bugemon chosenBugemon = pickRandomBugemon();
			if (chosenBugemon != null) {
				battleController.useAction(new Swap(chosenBugemon));
			}
		}
		return this.battleController.getState();
	}

    /**
	 * Uses a random ability for the current active Bugemon of the specified team.
	 */
	public void useRandomAbility() {

        UseAbility useAbility = new UseAbility(this.getRandomAbility());
		battleController.useAction(useAbility);
	}

    /**
	 * Returns a random ability from the active Bugemon on the player's side.
	 *
	 * @return a randomly selected Ability of the self (Team A) active Bugemon
	 */
	public Ability getRandomAbility() {
        Bugemon bugemon = this.battleController.getActiveBugemonSelf();
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
    public Bugemon pickRandomBugemon(){
        List<Bugemon> availableBugemons = battleController.getAvailableBugemons();
        if (!availableBugemons.isEmpty()){
            Random rand = new Random();
            int i = rand.nextInt(availableBugemons.size());

            return availableBugemons.get(i);
        }
        return null;
    }

}
