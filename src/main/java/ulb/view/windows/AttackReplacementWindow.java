package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ulb.model.ability.Ability;
import ulb.model.ability.AbilitySet;
import ulb.model.bugemon.Bugemon;

public class AttackReplacementWindow extends Window {

    private ViewListener viewListener;
    private Bugemon bugemon;

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

    public void initializeReplacement(Bugemon bugemon, Ability newAbility) {
        this.bugemon = bugemon;

        bugemonLabel.setText(bugemon.getName() + " apprend une nouvelle attaque");
        newAbilityLabel.setText(createAbilityText(newAbility));
        bugemonSprite.setImage(new Image(getClass().getResourceAsStream(bugemon.getSprite())));
        populateAbilities();
    }

    private String createAbilityText(Ability ability) {
        return ability.getName() + "\n"
                + "Type: " + ability.getType() + "\n"
                + "Puissance: " + ability.getPower() + "\n"
                + ability.getDescription();
    }

    private void populateAbilities() {
        abilitiesList.getChildren().clear();

        AbilitySet abilitySet = bugemon.getAbilities();
        for (int i = 0; i < abilitySet.size(); i++) {
            Ability oldAbility = abilitySet.getAbility(i);
            if (oldAbility == null) {
                continue;
            }

            HBox row = new HBox(10);

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
        }
    }

    @FXML
    private void returnToPrevious() {
        if (viewListener != null) {
            viewListener.onReturnToChooseBugemon();
        }
    }

    public interface ViewListener {
        void onReplaceAbility(Ability oldAbility);
        void onReturnToChooseBugemon();
    }
}
