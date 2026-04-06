package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.model.ability.Ability;
import ulb.model.bugemon.Bugemon;
import ulb.view.WindowPath;
import ulb.view.windows.AttackReplacementWindow;

public class AttackReplacementController implements AttackReplacementWindow.ViewListener {

	private final Stage stage;
	private final Listener listener;
	private AttackReplacementWindow view;
	private Bugemon currentBugemon;
	private Ability currentNewAbility;

	public AttackReplacementController(Stage stage, Listener listener) {
		this.stage = stage;
		this.listener = listener;
	}

	public void show(Bugemon bugemon, Ability newAbility) throws Exception {
		this.currentBugemon = bugemon;
		this.currentNewAbility = newAbility;

		FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.ATTACK_REPLACEMENT));
		loader.load();
		view = loader.getController();
		view.setViewListener(this);
		view.initializeReplacement(bugemon, newAbility);

		Parent root = loader.getRoot();
		if (stage.getScene() == null) {
			stage.setScene(new Scene(root));
		} else {
			stage.getScene().setRoot(root);
		}
		this.stage.show();
	}

	@Override
	public void onReplaceAbility(Ability oldAbility) {
		listener.onAttackReplaced(currentBugemon, currentNewAbility, oldAbility);
	}

	@Override
	public void onReturnToChooseBugemon() {
		listener.onReturnToChooseBugemon();
	}

	public interface Listener {
		void onAttackReplaced(Bugemon bugemon, Ability newAbility, Ability oldAbility);
		void onReturnToChooseBugemon();
	}
}
