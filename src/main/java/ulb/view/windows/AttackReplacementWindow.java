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

public class AttackReplacementWindow extends Window {

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

	public void initializeReplacement(BugemonDTO bugemon, AbilityDTO newAbility) {
		this.bugemon = bugemon;

        bugemonLabel.setText(bugemon.getName() + " apprend une nouvelle attaque");
        newAbilityLabel.setText(createAbilityText(newAbility));
        bugemonSprite.setImage(new Image(getClass().getResourceAsStream(bugemon.getSpritePath())));
        populateAbilities();
    }

	private String createAbilityText(AbilityDTO ability) {
		return ability.getName() + "\n" +
				"Type: " + ability.getType() + "\n" +
				"Puissance: " + ability.getPower() + "\n" +
				ability.getDescription();
	}

	private void populateAbilities() {
		abilitiesList.getChildren().clear();

		List<AbilityDTO> abilitySet = bugemon.getAbilities();
		for (int i = 0; i < abilitySet.size(); i++) {
			AbilityDTO oldAbility = abilitySet.get(i);
			if (oldAbility == null) {
				continue;
			}

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
