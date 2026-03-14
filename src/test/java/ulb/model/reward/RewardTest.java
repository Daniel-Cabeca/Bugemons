package ulb.model.reward;

import org.junit.jupiter.api.Test;
import ulb.model.bugemon.Bugemon;
import ulb.model.type.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RewardTest {

    @Test
    public void checkGainsHPReward() {
        Bugemon bugemon =  new Bugemon(Type.AQUA, 10, 29, 35, 16);
        Reward reward = new Reward(bugemon);
        reward.gainStats(RewardType.HP);
        assertEquals(30, bugemon.getBaseStats().getHp());
    }

    @Test
    public void checkGainsAttackReward() {
        Bugemon bugemon =  new Bugemon(Type.AQUA, 10, 29, 35, 16);
        Reward reward = new Reward(bugemon);
        reward.gainStats(RewardType.ATTACK);
        assertEquals(39, bugemon.getBaseStats().getAttack());
    }

    @Test
    public void checkGainsDefenseReward() {
        Bugemon bugemon =  new Bugemon(Type.AQUA, 10, 29, 35, 16);
        Reward reward = new Reward(bugemon);
        reward.gainStats(RewardType.DEFENSE);
        assertEquals(45, bugemon.getBaseStats().getDefense());
    }

    @Test
    public void checkGainsInitiativeReward() {
        Bugemon bugemon =  new Bugemon(Type.AQUA, 10, 29, 35, 16);
        Reward reward = new Reward(bugemon);
        reward.gainStats(RewardType.INITIATIVE);
        assertEquals(36, bugemon.getBaseStats().getInitiative());
    }
}
