package ulb.model.reward;

import org.junit.jupiter.api.Test;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;
import ulb.model.type.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RewardTest {

    private int getGainedPoint(Stats previous, Stats actual){
		Stats difference = new Stats(actual);
		Stats opposite = new Stats(-previous.hp, -previous.attack, -previous.defense, -previous.initiative);
		difference.change(opposite);
		return difference.hp / 2 + difference.initiative / 2 + difference.attack + difference.defense;
	}

    @Test
    public void checkGainsHPReward() {
        Bugemon bugemon =  new Bugemon(Type.AQUA, 10, 29, 35, 16);
        Reward reward = new Reward(bugemon);
        reward.choseType(RewardType.HP);
        reward.applyReward();
        assertEquals(30, bugemon.getBaseStats().getHp());
    }

    @Test
    public void checkGainsAttackReward() {
        Bugemon bugemon =  new Bugemon(Type.AQUA, 10, 29, 35, 16);
        Reward reward = new Reward(bugemon);
        reward.choseType(RewardType.ATTACK);
        reward.applyReward();
        assertEquals(39, bugemon.getBaseStats().getAttack());
    }

    @Test
    public void checkGainsDefenseReward() {
        Bugemon bugemon =  new Bugemon(Type.AQUA, 10, 29, 35, 16);
        Reward reward = new Reward(bugemon);
        reward.choseType(RewardType.DEFENSE);
        reward.applyReward();
        assertEquals(45, bugemon.getBaseStats().getDefense());
    }

    @Test
    public void checkGainsInitiativeReward() {
        Bugemon bugemon =  new Bugemon(Type.AQUA, 10, 29, 35, 16);
        Reward reward = new Reward(bugemon);
        reward.choseType(RewardType.INITIATIVE);
        reward.applyReward();
        assertEquals(36, bugemon.getBaseStats().getInitiative());
    }

    @Test
    public void checkGainsCombinationReward(){
        Bugemon bugemon =  new Bugemon(Type.AQUA, 10, 29, 35, 16);
        Reward reward = new Reward(bugemon);
        reward.choseType(RewardType.COMBINATION);
        Stats previousBugemonStats = new Stats(bugemon.getBaseStats());
        reward.applyReward();
        assertEquals(10, getGainedPoint(previousBugemonStats, bugemon.getBaseStats()));
    }
}
