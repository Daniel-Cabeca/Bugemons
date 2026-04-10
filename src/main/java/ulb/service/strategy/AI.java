package ulb.service.strategy;

import java.util.List;
import java.util.Random;
import java.util.Vector;

import ulb.controller.BattleController;
import ulb.controller.action.Action;
import ulb.controller.action.Run;
import ulb.controller.action.Swap;
import ulb.controller.action.UseAbility;
import ulb.model.Player;
import ulb.model.ability.Ability;
import ulb.model.ability.AbilitySet;
import ulb.model.battle.Battle;

public class AI extends Thread {
    private Battle battle;
    private Strategy strategy;
    private final long SLEEP_TIME = 1000;
    private Battle.ParticipantLabel teamLabel = Battle.ParticipantLabel.TEAM_B;

    public AI(Battle battle, Strategy strategy) {
        this.battle = battle;
        this.strategy = strategy;
    }

    @Override
    public void run(){
        this.play();
    }

    /**
     * Plays the game loop in the automatic battle mode
     */
    public void play(){
        while (!this.battle.isGameFinished()){
            this.playAutoTurn();
            try {
                Thread.sleep(SLEEP_TIME);
            } catch(Exception e) {}
            
        }
    }

    /**
	 * Plays a turn in the automatic battle mode
	 */
	public void playAutoTurn() {
        Action action = strategy.pickAction(battle, teamLabel);
        
        if (action != null){
            battle.chooseAction(action, teamLabel);
        }
	}
    
}
