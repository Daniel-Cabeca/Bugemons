package ulb.view.windows;


import java.util.List;
import javafx.fxml.FXML;
import ulb.communication.Message;
import ulb.communication.types.GetInfoMessage;
import ulb.communication.types.InfoType;
import ulb.communication.types.LevelUpMessage;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;
import ulb.model.reward.Reward;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Vector;

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
	private Reward rewardA;
	private Reward rewardB;
	private Reward rewardC;

	public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

	// @Override
	// public void onLoad() {
	// 	Message m = sendMessage(new GetInfoMessage(InfoType.LEVEL_UP));
	// 	if (m instanceof LevelUpMessage levelUpMessage) {
	// 		initializeRewardSelection(levelUpMessage.getBugemon(), levelUpMessage.getRewards());
	// 	}
	// }

	private String createRewardsText(Reward r) {
		Stats addedStats = r.getStats();
		return "HP: +" + addedStats.getHp() + "\nAttaque: +" + addedStats.getAttack() + "\nDefense: +" + addedStats.getDefense()
				+ "\nInitiative: +" + addedStats.getInitiative();
	}

	// public void initializeRewardSelection(Bugemon bugemon, Vector<Reward> rewards) {
	// 	setRewards(rewards);
	// 	setBugemonLevel(bugemon);
	// 	setBugemonSprite(bugemon);
	// }

	public void initializeView(Bugemon bugemon, List<Reward> rewards) {
		setRewards(rewards);
		setBugemonLevel(bugemon);
		setBugemonSprite(bugemon);
	}

	public void setRewards(List<Reward> rewards) {
		rewardA = rewards.get(0);
		rewardB = rewards.get(1);
		rewardC = rewards.get(2);

		rewardALabel.setText(createRewardsText(rewardA));
		rewardBLabel.setText(createRewardsText(rewardB));
		rewardCLabel.setText(createRewardsText(rewardC));
	}

	public void setBugemonLevel(Bugemon bugemon) {
		bugemonLevel.setText("Bugémon a atteint le niveau " + bugemon.getLevel() + "!");
	}

	public void setBugemonSprite(Bugemon bugemon) {
		bugemonSprite.setImage(new Image(getClass().getResourceAsStream(bugemon.getSprite())));
	}

	@FXML
	private void chooseRewardA(ActionEvent event) {
		// LevelUpMessage levelUpMessage = new LevelUpMessage(rewardA, event);
		// sendMessage(levelUpMessage);
		viewListener.onRewardChosen(rewardA, event);
	}

	@FXML
	private void chooseRewardB(ActionEvent event) {
		// LevelUpMessage levelUpMessage = new LevelUpMessage(rewardB, event);
		// sendMessage(levelUpMessage);
		viewListener.onRewardChosen(rewardB, event);
	}

	@FXML
	private void chooseRewardC(ActionEvent event) {
		// LevelUpMessage levelUpMessage = new LevelUpMessage(rewardC, event);
		// sendMessage(levelUpMessage);
		viewListener.onRewardChosen(rewardC, event);
	}

	public interface ViewListener {
        void onRewardChosen(Reward reward, ActionEvent event);
    }
}
