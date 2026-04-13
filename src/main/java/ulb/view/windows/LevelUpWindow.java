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

public class LevelUpWindow extends Window {
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

	private String createRewardsText(RewardDTO r) {
		StatsDTO addedStats = r.getStats();
		return "HP: +" + addedStats.getHp() + "\nAttaque: +" + addedStats.getAttack() + "\nDefense: +" + addedStats.getDefense()
				+ "\nInitiative: +" + addedStats.getInitiative();
	}

	public void initializeView(BugemonDTO bugemon, List<RewardDTO> rewards) {
		if (bugemon == null || rewards == null) {
			System.err.println("Invalid arguments");
			return;
		}
		setRewards(rewards);
		setBugemonLevel(bugemon);
		setBugemonSprite(bugemon);
	}

	public void setRewards(List<RewardDTO> rewards) {
		if (rewards == null || rewards.size() < 3) {
			System.err.println("Invalid reward list");
			return;
		}
		rewardA = rewards.get(0);
		rewardB = rewards.get(1);
		rewardC = rewards.get(2);

		rewardALabel.setText(createRewardsText(rewardA));
		rewardBLabel.setText(createRewardsText(rewardB));
		rewardCLabel.setText(createRewardsText(rewardC));
	}

	public void setBugemonLevel(BugemonDTO bugemon) {
		bugemonLevel.setText("Bugémon a atteint le niveau " + bugemon.getLevel() + "!");
	}

	public void setBugemonSprite(BugemonDTO bugemon) {
		bugemonSprite.setImage(new Image(getClass().getResourceAsStream(bugemon.getSpritePath())));
	}

	@FXML
	private void chooseRewardA(ActionEvent event) {
		viewListener.onRewardChosen(rewardA, event);
	}

	@FXML
	private void chooseRewardB(ActionEvent event) {
		viewListener.onRewardChosen(rewardB, event);
	}

	@FXML
	private void chooseRewardC(ActionEvent event) {
		viewListener.onRewardChosen(rewardC, event);
	}

	public interface ViewListener {
        void onRewardChosen(RewardDTO reward, ActionEvent event);
    }
}
