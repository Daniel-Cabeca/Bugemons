package ulb.model.reward;

import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;

import java.util.Random;

public class Reward {

    private Bugemon bugemon;

    public Reward(Bugemon bugemon) {
        this.bugemon = bugemon;
    }

    /**
     * Gains chosen reward by adding to the base stats of the bugemon
     *
     * @param chosenReward the chosen reward type
     */
    public void gainStats(RewardType chosenReward) {
        Stats reward  = new Stats();
        switch (chosenReward) {
            case HP:
                reward.hp = 20;
                break;
            case ATTACK:
                reward.attack = 10;
                break;
            case DEFENSE:
                reward.defense = 10;
                break;
            case INITIATIVE:
                reward.initiative = 20;
                break;
            // TO DO: add random combination reward
            default:
                break;
        }
        bugemon.changeBaseStats(reward);
    }

    /**
     * @return the randomly generated reward type
     */
    public RewardType generateRandomReward() {
        Random rand = new Random();
        int i = rand.nextInt(RewardType.values().length);
        return RewardType.values()[i];
    }

}
