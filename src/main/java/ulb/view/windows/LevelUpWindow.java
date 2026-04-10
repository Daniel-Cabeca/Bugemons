package ulb.view.windows;


import java.util.List;
import javafx.fxml.FXML;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;
import ulb.model.reward.Reward;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


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

	private String createRewardsText(Reward r) {
		Stats addedStats = r.getStats();
		return "HP: +" + addedStats.getHp() + "\nAttaque: +" + addedStats.getAttack() + "\nDefense: +" + addedStats.getDefense()
				+ "\nInitiative: +" + addedStats.getInitiative();
	}

	public void initializeView(Bugemon bugemon, List<Reward> rewards) {
		if (bugemon == null || rewards == null) {
			System.err.println("Invalid arguments");
			return;
		}
		setRewards(rewards);
		setBugemonLevel(bugemon);
		setBugemonSprite(bugemon);
	}

	public void setRewards(List<Reward> rewards) {
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

	public void setBugemonLevel(Bugemon bugemon) {
		bugemonLevel.setText("Bugémon a atteint le niveau " + bugemon.getLevel() + "!");
	}

	public void setBugemonSprite(Bugemon bugemon) {
		bugemonSprite.setImage(new Image(getClass().getResourceAsStream(bugemon.getSprite())));
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
        void onRewardChosen(Reward reward, ActionEvent event);
    }
}
