package ulb.view.windows;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;

/**
 * Window responsible for handling the replacement of an ability when a Bugemon learns a new attack as a reward.
 */
public class AttackReplacementWindow {

	private ViewListener viewListener;
	private BugemonDTO bugemon;

	@FXML
	private Label bugemonLabel;
	@FXML
	private Label newAbilityLabel;
	@FXML
	private ImageView bugemonSprite;
	@FXML
	private VBox abilitiesList;

	public void setViewListener(ViewListener viewListener) {
		this.viewListener = viewListener;
	}

	/**
	 * Initializes the window with the given Bugemon and new ability.
	 *
	 * @param bugemon the Bugemon learning a new ability
	 * @param newAbility the new ability being learned
	 */
	public void initializeReplacement(BugemonDTO bugemon, AbilityDTO newAbility) {
		this.bugemon = bugemon;

        bugemonLabel.setText(bugemon.getName() + " apprend une nouvelle attaque");
        newAbilityLabel.setText(createAbilityText(newAbility));
        bugemonSprite.setImage(new Image(getClass().getResourceAsStream(bugemon.getSpritePath())));
        populateAbilities();
    }

	/**
	 * Creates a formatted string describing an ability.
	 *
	 * @param ability the ability to format
	 * @return a string containing the ability name, type, power and description
	 */
	private String createAbilityText(AbilityDTO ability) {
		return ability.name() + "\n" +
				"Type: " + ability.type() + "\n" +
				"Puissance: " + ability.power() + "\n" +
				ability.description();
	}

	/**
	 * Populates the list with the Bugemon's current abilities, each with a button "to replace"
	 */
	private void populateAbilities() {
		abilitiesList.getChildren().clear();

		List<AbilityDTO> abilitySet = bugemon.getAbilities();
		for (int i = 0; i < abilitySet.size(); i++) {
			AbilityDTO oldAbility = abilitySet.get(i);
			if (oldAbility == null) {
				continue;
			}
			// one row per existing attack with the description + button to replace the ability
			HBox row = new HBox(10);
			row.getStyleClass().add("attack-replacement-row");

			Label abilityLabel = new Label(createAbilityText(oldAbility));
			abilityLabel.setWrapText(true);
			abilityLabel.setMaxWidth(350);

			Button replaceButton = new Button("Remplacer");
			replaceButton.setOnAction(event -> {
				if (viewListener != null) {
					viewListener.onReplaceAbility(oldAbility);
				}
			});

			row.getChildren().addAll(abilityLabel, replaceButton);
			abilitiesList.getChildren().add(row);
			// adds a horizontal line between each ability
			if (i < abilitySet.size() - 1) {
				abilitiesList.getChildren().add(new Separator());
			}
		}
	}

	/**
	 * Returns to the previous window (ChooseBugemonWindow)
	 */
	@FXML
	private void returnToPrevious() {
		if (viewListener != null) {
			viewListener.onReturnToChooseBugemon();
		}
	}

	public interface ViewListener {
		void onReplaceAbility(AbilityDTO oldAbility);
		void onReturnToChooseBugemon();
	}
}
