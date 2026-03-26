package ulb.view.windows;

import javafx.fxml.FXML;
import ulb.controller.GameController;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;
import ulb.model.reward.Reward;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ulb.view.ViewManager;

import java.util.Vector;

public class LevelUpWindow extends ViewManager {
	@FXML
	private Label bugemonLevel;
	@FXML
	private ImageView bugemonSprite;
	@FXML
	private Label rewardA;
	@FXML
	private Label rewardB;
	@FXML
	private Label rewardC;

	private Reward rA;
	private Reward rB;
	private Reward rC;

	private String createRewardsText(Reward r) {
		Stats addedStats = r.getStats();
		return "HP: +" + addedStats.getHp() + "\nAttaque: +" + addedStats.getAttack() + "\nDefense: +" + addedStats.getDefense()
				+ "\nInitiative: +" + addedStats.getInitiative();
	}

	public void initializeRewardSelection(Bugemon bugemon, Vector<Reward> rewards) {
		setRewards(rewards);
		setBugemonLevel(bugemon);
		setBugemonSprite(bugemon);
	}

	public void setRewards(Vector<Reward> rewards) {
		rA = rewards.get(0);
		rB = rewards.get(1);
		rC = rewards.get(2);

		rewardA.setText(createRewardsText(rA));
		rewardB.setText(createRewardsText(rB));
		rewardC.setText(createRewardsText(rC));
	}

	public void setBugemonLevel(Bugemon bugemon) {
		bugemonLevel.setText("Bugémon a atteint le niveau " + bugemon.getLevel() + "!");
	}

	public void setBugemonSprite(Bugemon bugemon) {
		bugemonSprite.setImage(new Image(getClass().getResourceAsStream(bugemon.getSprite())));
	}

	@FXML
	private void chooseRewardA(ActionEvent event) { getGameController().handleLevelUpRewardChoice(rA, event); }
	@FXML
	private void chooseRewardB(ActionEvent event) { getGameController().handleLevelUpRewardChoice(rB, event); }
	@FXML
	private void chooseRewardC(ActionEvent event) { getGameController().handleLevelUpRewardChoice(rC, event); }
}
