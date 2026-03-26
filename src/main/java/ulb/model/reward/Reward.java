package ulb.model.reward;

import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;

import java.util.Random;

public class Reward {

    private Bugemon bugemon;
    private Stats stats;

    public Reward(Bugemon bugemon) {
        this.bugemon = bugemon;
        this.stats = new Stats();
    }

    public Reward(Reward otherReward){
        this.bugemon = otherReward.bugemon;
        this.stats = new Stats(otherReward.stats);
    }

    /**
     * Generate random stats for the combinaison reward type
     * @return the stats generated
     */
    private Stats generateRandomStats(){
        Stats reward = new Stats();
        for (int i = 0; i < 10; i++) {
            int chosenStat = (int) (Math.random() * 4); // Number in [0, 3]

            switch (chosenStat) {
                case 0:
                    reward.hp += 2;
                    break;

                case 1:
                    reward.initiative += 2;
                    break;

                case 2:
                    reward.attack += 1;
                    break;

                case 3:
                    reward.defense += 1;
                    break;

                default:
                    break;
            }
        }
		return reward;
    }

    public void applyReward(){
        bugemon.consumeLevelReward(this.stats);
    }

    /**
     * Initializes the reward stats based on the given reward type
     *
     * @param chosenReward the type of reward to initialize stats for
     */
    public void configureReward(RewardType chosenReward) {
        switch (chosenReward) {
            case HP:
                this.stats.hp = 20;
                break;

            case ATTACK:
                this.stats.attack = 10;
                break;

            case DEFENSE:
                this.stats.defense = 10;
                break;

            case INITIATIVE:
                this.stats.initiative = 20;
                break;
            
            case COMBINATION:
                this.stats = this.generateRandomStats();
                break;

            default:
                break;
        }
    }

    /**
     * @return the randomly generated reward type
     */
    public RewardType generateRandomReward() {
        Random rand = new Random();
        int i = rand.nextInt(RewardType.values().length);
        return RewardType.values()[i];
    }

    /**
     * @return a copy of the stats reward
     */
    public Stats getStats(){ return new Stats(this.stats); }   
    public Bugemon getBugemon() { return this.bugemon; }

}
