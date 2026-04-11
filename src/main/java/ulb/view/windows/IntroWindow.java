package ulb.view.windows;

import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ulb.repository.LoadException;
import ulb.service.ServiceLoader;
import ulb.view.WindowPath;

import java.util.List;

public class IntroWindow extends Window {

    @FXML private Label messageLabel;

    private List<String> messages;
    private int index = 0;

    @Override
    public void onLoad() {
        index = 0;
        int phase = gameController.getIntroPhase();
        String name = gameController.getPendingPlayerName();
        messages = switch (phase) {
            case 0 -> List.of(
                "Bienvenue à la Tour NO !",
                "Je suis le Professeur Bugon, et je vais t'accompagner dans cette aventure.",
                "La Tour NO est un endroit mystérieux que peu d'aventuriers ont osé explorer.",
                "Tu devras gravir ses étages, affronter ses gardiens, et atteindre le sommet.",
                "Mais avant de commencer... quel est ton nom ?"
            );
            case 1 -> List.of(
                name + " ! Quel beau prénom !",
                "Et dis-moi... tu es un garçon ou une fille ?"
            );
            case 2 -> List.of(
                "Parfait !",
                "Alors " + name + ", la Tour NO t'attend. Bonne chance !"
            );
            default -> List.of();
        };
        messageLabel.setText(messages.get(0));
    }

    @FXML
    private void handleNext(MouseEvent event) {
        event.consume();
        index++;
        if (index < messages.size()) {
            messageLabel.setText(messages.get(index));
            return;
        }
        int phase = gameController.getIntroPhase();
        gameController.nextIntroPhase();
        if (phase == 0) {
            switchWindow(WindowPath.NAME_CHOICE);
        } else if (phase == 1) {
            switchWindow(WindowPath.GENDER_CHOICE);
        } else {
            try {
                ServiceLoader.getAccountService().savePlayerProfile(
                    gameController.getLoggedInUsername(),
                    gameController.getPendingPlayerName(),
                    gameController.getPendingGender()
                );
            } catch (LoadException e) {
                System.err.println(e.getMessage());
            }
            switchWindow(WindowPath.MODE);
        }
    }
}
