package ulb.model.reward;

import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;

public class Reward {

	private final Bugemon bugemon;
	private Stats stats;

	public Reward(Bugemon bugemon) {
		this.bugemon = bugemon;
		this.stats = new Stats();
	}

	public void applyReward() {
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
				this.stats.setHp(20);
				break;

			case ATTACK:
				this.stats.setAttack(10);
				break;

			case DEFENSE:
				this.stats.setDefense(10);
				break;

			case INITIATIVE:
				this.stats.setInitiative(20);
				break;

			case COMBINATION:
				this.stats = this.generateRandomStats();
				break;

			default:
				break;
		}
	}

	/**
	 * Generate random stats for the combinaison reward type
	 *
	 * @return the stats generated
	 */
	private Stats generateRandomStats() {
		Stats reward = new Stats();
		for (int i = 0; i < 10; i++) {
			int chosenStat = (int) (Math.random() * 4); // Number in [0, 3]

			switch (chosenStat) {
				case 0:
					reward.setHp(reward.getHp() + 2);
					break;
				case 1:
					reward.setInitiative(reward.getInitiative() + 2);
					break;
				case 2:
					reward.setAttack(reward.getAttack() + 1);
					break;
				case 3:
					reward.setDefense(reward.getDefense() + 1);
					break;

				default:
					break;
			}
		}
		return reward;
	}

	/**
	 * @return a copy of the stats reward
	 */
	public Stats getStats() { return new Stats(this.stats); }

	public void setStats(Stats stats) { this.stats = stats; }

	public Bugemon getBugemon() { return this.bugemon; }

}
