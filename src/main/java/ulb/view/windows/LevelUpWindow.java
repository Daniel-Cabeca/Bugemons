package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import ulb.model.bugemon.Stats;
import ulb.model.reward.Reward;
import ulb.view.handler.Window;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

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
	private String rewardsText;


	private String createRewardsText(Reward r) {
		String text;
		Stats addedStats = r.getStats();
		text = "HP: +" + addedStats.getHp() + "\nAttaque: +" + addedStats.getAttack() + "\nDefense: +" + addedStats.getDefense()
				+ "\nInitiative: +" + addedStats.getInitiative();
		return text;
	}

	public void setRewards(Reward A, Reward B, Reward C) {
		rA = A;
		rB = B;
		rC = C;

		rewardA.setText(createRewardsText(A));
		rewardB.setText(createRewardsText(B));
		rewardC.setText(createRewardsText(C));
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
