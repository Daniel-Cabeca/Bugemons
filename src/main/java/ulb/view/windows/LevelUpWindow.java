package ulb.view.windows;


import java.util.List;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.stats.StatsDTO;
import ulb.DTO.reward.RewardDTO;

public class LevelUpWindow {
	@FXML
	private Label bugemonLevel;
	@FXML
	private ImageView bugemonSprite;
	@FXML
	private Label rewardALabel;
	@FXML
	private Label rewardBLabel;
	@FXML
	private Label rewardCLabel;

	private ViewListener viewListener;
	private RewardDTO rewardA;
	private RewardDTO rewardB;
	private RewardDTO rewardC;

	public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }
	/**
	 * Generates a short text describing the given reward.
	 * @param r the reward that needs to be described. 
	 * @return the generated text.
	 */
	private String createRewardsText(RewardDTO r) {
		StatsDTO addedStats = r.stats();
		return "HP: +" + addedStats.hp() + "\nAttaque: +" + addedStats.attack() + "\nDefense: +" + addedStats.defense()
				+ "\nInitiative: +" + addedStats.initiative();
	}

	/**
	 * Initialize the LevelUpWindow with the given bugemon and rewards.
	 * @param bugemon the bugemon that levels up.
	 * @param rewards the rewards generated.
	 */
	public void initializeView(BugemonDTO bugemon, List<RewardDTO> rewards) {
		if (bugemon == null || rewards == null) {
			throw new IllegalArgumentException("'bugemon' and 'rewards' cannot be null.");
		}
		setRewards(rewards);
		setBugemonLevel(bugemon);
		setBugemonSprite(bugemon);
	}

	/**
	 * Sets the rewards list on the window.
	 * @param rewards the list of rewards.
	 */
	public void setRewards(List<RewardDTO> rewards) {
		if (rewards == null || rewards.size() < 3) {
			throw new IllegalArgumentException("Rewards list cannot be null and contain at least 3 rewards.");
		}
		rewardA = rewards.get(0);
		rewardB = rewards.get(1);
		rewardC = rewards.get(2);
		rewardALabel.setText(createRewardsText(rewardA));
		rewardBLabel.setText(createRewardsText(rewardB));
		rewardCLabel.setText(createRewardsText(rewardC));
	}

	/**
	 * Sets the level of the given bugemon.
	 * @param bugemon the bugemon that needs it's level set.
	 */
	public void setBugemonLevel(BugemonDTO bugemon) {
		bugemonLevel.setText("Bugémon a atteint le niveau " + bugemon.level() + "!");
	}

	/**
	 * Sets the sprite of the given bugemon.
	 * @param bugemon the bugemon that needs it's sprite set.
	 */
	public void setBugemonSprite(BugemonDTO bugemon) {
		bugemonSprite.setImage(new Image(getClass().getResourceAsStream(bugemon.getSpritePath())));
	}

	/**
	 * Handles the button that chooses Reward A.
	 * @param event the next event.
	 */
	@FXML
	private void chooseRewardA(ActionEvent event) {
		viewListener.onRewardChosen(rewardA, event);
	}

	/**
	 * Handles the button that chooses Reward B.
	 * @param event the next event.
	 */
	@FXML
	private void chooseRewardB(ActionEvent event) {
		viewListener.onRewardChosen(rewardB, event);
	}

	/**
	 * Handles the button that chooses Reward C.
	 * @param event the next event.
	 */
	@FXML
	private void chooseRewardC(ActionEvent event) {
		viewListener.onRewardChosen(rewardC, event);
	}

	public interface ViewListener {
        void onRewardChosen(RewardDTO reward, ActionEvent event);
    }
}
