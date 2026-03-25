package ulb.view.windows;

import javafx.fxml.FXML;
import ulb.model.bugemon.Stats;
import ulb.model.reward.Reward;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.Vector;

public class LevelUpWindow extends Window {
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
		String text;
		Stats addedStats = r.getStats();
		text = "HP: +" + addedStats.getHp() + "\nAttaque: +" + addedStats.getAttack() + "\nDefense: +" + addedStats.getDefense()
				+ "\nInitiative: +" + addedStats.getInitiative();
		return text;
	}

	public void setRewards(Vector<Reward> rewards) {
		rA = rewards.get(0);
		rB = rewards.get(1);
		rC = rewards.get(2);

		rewardA.setText(createRewardsText(rA));
		rewardB.setText(createRewardsText(rB));
		rewardC.setText(createRewardsText(rC));
	}

	public void setBugemonLevel() {
		bugemonLevel.setText("Bugémon a atteint le niveau " + rA.getBugemon().getLevel() + "!");
	}

	public void setBugemonSprite() {
		bugemonSprite = new ImageView(rA.getBugemon().getSprite());
	}

	@FXML
	private void chooseRewardA() {rA.applyReward();}
	@FXML
	private void chooseRewardB() {rB.applyReward();}
	@FXML
	private void chooseRewardC() {rC.applyReward();}


}
