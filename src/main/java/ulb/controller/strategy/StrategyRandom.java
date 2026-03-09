package ulb.controller.strategy;

import java.util.Random;
import java.util.Vector;
import java.lang.Thread;

import ulb.controller.BattleController;
import ulb.controller.action.*;
import ulb.model.Player;
import ulb.model.ability.Ability;
import ulb.model.ability.AbilitySet;
import ulb.model.bugemon.Bugemon;
import ulb.model.battle.Battle;
import ulb.model.battle.BattleState;

public class StrategyRandom implements Strategy, Runnable {
    private BattleController battleController;
    private final long SLEEP_TIME = 1000;

    public StrategyRandom(Battle battle) {
        this(new BattleController(new Player(), battle, false));
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

    public void play(){
        while (!battleController.isGameFinished()){
            this.playAutoTurn();
            try{
                Thread.sleep(SLEEP_TIME);
            }catch(Exception e){}
            
        }
    }

    /**
	 * Plays a turn in the automatic battle mode
	 */
	public BattleState playAutoTurn() {

        Vector<Action> actions = battleController.getAvailableAction();
        UseAbility useAbility = new UseAbility();
        Swap swap = new Swap();
        if (actions.stream().anyMatch(a -> a instanceof UseAbility)){
            useRandomAbility();
        } else if (actions.stream().anyMatch(a -> a instanceof Swap)){
            Bugemon chosenBugemon = pickRandomBugemon();
            if (chosenBugemon != null){
                swap.setToSwap(chosenBugemon);
                battleController.useAction(swap);
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
	    int i = rand.nextInt(AbilitiesA.SIZE);

	    return AbilitiesA.getAbility(i);
	}

    public Bugemon pickRandomBugemon(){
        Vector<Bugemon> availableBugemons = battleController.getAvailableBugemons();
        if (availableBugemons.size() != 0){
            Random rand = new Random();
            int i = rand.nextInt(availableBugemons.size());

            return availableBugemons.get(i);
        }
        return null;
    }

}
