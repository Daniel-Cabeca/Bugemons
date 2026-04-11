package ulb.view.windows;

import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ulb.view.windows.BattleWindow.BattleSnapshot;
import ulb.view.windows.BattleWindow.BugemonDisplay;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BattleWindowGraphicsHelper {

    private BattleUIComponents ui;

    public void linkUI(ProgressBar playerBugemonHPBar, ProgressBar opponentHPBar, Label playerBugemonHPNumber,
                       Label opponentHPNumber, ImageView playerBugemon, ImageView opponentBugemon,
                       Label playerBugemonLabel, Label opponentBugemonLabel, Label playerLevelLabel,
                       Label opponentLevelLabel, VBox messageBox, Label battleLog) {
        this.ui = new BattleUIComponents(playerBugemonHPBar, opponentHPBar, playerBugemonHPNumber,
                                         opponentHPNumber, playerBugemon, opponentBugemon,
                                         playerBugemonLabel, opponentBugemonLabel, playerLevelLabel,
                                         opponentLevelLabel, messageBox, battleLog);
    }

    public void showLogMessages(List<String> logs) {
        List<String> visibleLogs = logs == null
                ? List.of()
                : logs.stream().filter(message -> message != null && !message.isBlank()).toList();

        if (visibleLogs.isEmpty()) {
            clearMessages();
            return;
        }

        String allMessages = visibleLogs.stream()
                .map(message -> wrapText(message, 35))
                .collect(Collectors.joining("\n"));

        ui.battleLog().setText(allMessages);
        ui.messageBox().setVisible(true);
        ui.messageBox().setManaged(true);
    }

    public void displayMessagesSequentially(List<String> rawLogs, Integer hpAfterFirstActionSelf, Integer hpAfterFirstActionOpponent,
                                            BattleSnapshot finalSnapshot, Supplier<BattleSnapshot> currentSnapshotSupplier, Consumer<BattleSnapshot> battleRenderer,
                                            Runnable onComplete) {
        List<String> logs = rawLogs == null ? List.of() : new ArrayList<>(rawLogs);
        List<List<String>> phases = splitPhases(logs);

        if (phases.stream().allMatch(List::isEmpty)) {
            if (finalSnapshot != null) {
                battleRenderer.accept(finalSnapshot);
            }
            clearMessages();
            onComplete.run();
            return;
        }

        ui.messageBox().setVisible(true);
        ui.messageBox().setManaged(true);

        BattleSnapshot currentSnapshot = currentSnapshotSupplier.get();
        List<BattlePhaseStep> phaseSteps = buildPhaseSteps(phases, currentSnapshot, finalSnapshot, hpAfterFirstActionSelf, hpAfterFirstActionOpponent);

        Runnable closeAndComplete = () -> {
            clearMessages();
            onComplete.run();
        };

        displayPhaseSequence(phaseSteps, 0, currentSnapshotSupplier, battleRenderer, closeAndComplete);
    }


    private List<List<String>> splitPhases(List<String> logs) {
        List<List<String>> phases = new ArrayList<>();
        List<String> currentPhase = new ArrayList<>();

        for (String message : logs) {
            if (message == null) {
                phases.add(currentPhase);
                currentPhase = new ArrayList<>();
            } else {
                currentPhase.add(message);
            }
        }
        phases.add(currentPhase);

        return phases;
    }

    private List<BattlePhaseStep> buildPhaseSteps(List<List<String>> phases, BattleSnapshot currentSnapshot, BattleSnapshot finalSnapshot,
                                                  Integer hpAfterFirstActionSelf, Integer hpAfterFirstActionOpponent) {
        List<BattlePhaseStep> steps = new ArrayList<>();
        int phaseCount = phases.size();

        for (int i = 0; i < phaseCount; i++) {
            List<String> messages = phases.get(i);
            if (messages.isEmpty()) {
                continue;
            }

            BattlePhaseVisual visual;
            if (phaseCount == 1) {
                visual = new BattlePhaseVisual(finalSnapshot, null, null);
            } else if (i == 0) {
                visual = new BattlePhaseVisual(currentSnapshot, hpAfterFirstActionSelf, hpAfterFirstActionOpponent);
            } else if (i == phaseCount - 1) {
                visual = new BattlePhaseVisual(finalSnapshot, null, null);
            } else {
                visual = buildIntermediateVisual(currentSnapshot, finalSnapshot, hpAfterFirstActionSelf, hpAfterFirstActionOpponent);
            }

            steps.add(new BattlePhaseStep(messages, visual));
        }

        return steps;
    }

    private BattlePhaseVisual buildIntermediateVisual(BattleSnapshot currentSnapshot, BattleSnapshot finalSnapshot, Integer hpAfterFirstActionSelf,
                                                      Integer hpAfterFirstActionOpponent) {
        if (currentSnapshot == null) {
            return new BattlePhaseVisual(finalSnapshot, null, null);
        }

        Integer playerHp = hpAfterFirstActionSelf;
        Integer opponentHp = hpAfterFirstActionOpponent;

        if (hasOpponentSwitched(currentSnapshot, finalSnapshot)) {
            opponentHp = 0;
        }

        return new BattlePhaseVisual(currentSnapshot, playerHp, opponentHp);
    }

    private boolean hasOpponentSwitched(BattleSnapshot currentSnapshot, BattleSnapshot finalSnapshot) {
        if (currentSnapshot == null || finalSnapshot == null || currentSnapshot.opponentBugemon() == null
                || finalSnapshot.opponentBugemon() == null) {
            return false;
        }

        return !currentSnapshot.opponentBugemon().name().equals(finalSnapshot.opponentBugemon().name());
    }

    private void displayPhaseSequence(List<BattlePhaseStep> phaseSteps, int phaseIndex, Supplier<BattleSnapshot> currentSnapshotSupplier,
                                      Consumer<BattleSnapshot> battleRenderer, Runnable onComplete) {
        if (phaseIndex >= phaseSteps.size()) {
            onComplete.run();
            return;
        }

        BattlePhaseStep currentStep = phaseSteps.get(phaseIndex);
        displayPhase(currentStep.messages(), 0, currentStep.visual(), currentSnapshotSupplier, battleRenderer, () 
                     -> displayPhaseSequence(phaseSteps, phaseIndex + 1, currentSnapshotSupplier, battleRenderer, onComplete));
    }

    public void renderBattle(BattleSnapshot snapshot) {
        if (snapshot == null || snapshot.playerBugemon() == null || snapshot.opponentBugemon() == null) {
            return;
        }

        BugemonDisplay playerBugemon = snapshot.playerBugemon();
        BugemonDisplay opponentBugemon = snapshot.opponentBugemon();

        try {
            Image playerImage = new Image(getClass().getResourceAsStream(playerBugemon.spritePath()));
            ui.playerBugemon().setImage(playerImage);
        } catch (Exception e) {
            System.err.println("Failed to load player bugemon sprite: " + e.getMessage());
        }

        ui.playerBugemonLabel().setText(playerBugemon.name());
        ui.playerLevelLabel().setText("Lv." + playerBugemon.level());
        ui.playerBugemonLabel().setStyle("-fx-text-fill: " + playerBugemon.color() + ";");
        double playerRatio = (double) playerBugemon.hp() / playerBugemon.maxHp();
        ui.playerBugemonHPBar().setProgress(playerRatio);
        updateHPBarColor(ui.playerBugemonHPBar(), playerRatio);
        ui.playerBugemonHPNumber().setText(playerBugemon.hp() + " / " + playerBugemon.maxHp());

        try {
            Image opponentImage = new Image(getClass().getResourceAsStream(opponentBugemon.spritePath()));
            ui.opponentBugemon().setImage(opponentImage);
        } catch (Exception e) {
            System.err.println("Failed to load opponent bugemon sprite: " + e.getMessage());
        }

        ui.opponentBugemonLabel().setText(opponentBugemon.name());
        ui.opponentLevelLabel().setText("Lv." + opponentBugemon.level());
        ui.opponentBugemonLabel().setStyle("-fx-text-fill: " + opponentBugemon.color() + ";");
        double opponentRatio = (double) opponentBugemon.hp() / opponentBugemon.maxHp();
        ui.opponentHPBar().setProgress(opponentRatio);
        updateHPBarColor(ui.opponentHPBar(), opponentRatio);
        ui.opponentHPNumber().setText(opponentBugemon.hp() + " / " + opponentBugemon.maxHp());
    }

    public void clearMessages() {
        ui.battleLog().setText("");
        ui.messageBox().setVisible(false);
        ui.messageBox().setManaged(false);
    }

    private void displayPhase(List<String> messages, int index, BattlePhaseVisual visual, Supplier<BattleSnapshot> currentSnapshotSupplier,
                              Consumer<BattleSnapshot> battleRenderer, Runnable onComplete) {
        if (index >= messages.size()) {
            onComplete.run();
            return;
        }

        ui.battleLog().setText(wrapText(messages.get(index), 35));
        applyPhaseVisual(visual, currentSnapshotSupplier, battleRenderer);

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(2));
        pause.setOnFinished(event -> displayPhase(messages, index + 1, visual, currentSnapshotSupplier, battleRenderer, onComplete));
        pause.play();
    }

    private void applyPhaseVisual(BattlePhaseVisual visual, Supplier<BattleSnapshot> currentSnapshotSupplier, Consumer<BattleSnapshot> battleRenderer) {
        if (visual.snapshot() != null) {
            battleRenderer.accept(visual.snapshot());
        }

        if (visual.playerHp() != null && visual.opponentHp() != null) {
            updateHPDisplay(visual.playerHp(), visual.opponentHp(), currentSnapshotSupplier.get());
        }
    }

    private void updateHPDisplay(int selfHp, int opponentHp, BattleSnapshot currentSnapshot) {
        if (currentSnapshot == null || currentSnapshot.playerBugemon() == null || currentSnapshot.opponentBugemon() == null) {
            return;
        }

        BugemonDisplay self = currentSnapshot.playerBugemon();
        BugemonDisplay opponent = currentSnapshot.opponentBugemon();
        double selfRatio = (double) selfHp / self.maxHp();
        double opponentRatio = (double) opponentHp / opponent.maxHp();

        ui.playerBugemonHPBar().setProgress(selfRatio);
        ui.playerBugemonHPNumber().setText(selfHp + " / " + self.maxHp());
        updateHPBarColor(ui.playerBugemonHPBar(), selfRatio);

        ui.opponentHPBar().setProgress(opponentRatio);
        ui.opponentHPNumber().setText(opponentHp + " / " + opponent.maxHp());
        updateHPBarColor(ui.opponentHPBar(), opponentRatio);
    }

    private void updateHPBarColor(ProgressBar bar, double ratio) {
        bar.getStyleClass().removeAll("hp-bar-green", "hp-bar-yellow", "hp-bar-red");
        if (ratio > 0.5) {
            bar.getStyleClass().add("hp-bar-green");
        } else if (ratio > 0.25) {
            bar.getStyleClass().add("hp-bar-yellow");
        } else {
            bar.getStyleClass().add("hp-bar-red");
        }
    }

    private String wrapText(String text, int maxChars) {
        StringBuilder result = new StringBuilder();
        for (String line : text.split("\\n")) {
            if (result.length() > 0) {
                result.append("\n");
            }
            String[] words = line.split(" ");
            int lineLength = 0;
            for (String word : words) {
                if (lineLength == 0) {
                    result.append(word);
                    lineLength = word.length();
                } else if (lineLength + 1 + word.length() <= maxChars) {
                    result.append(" ").append(word);
                    lineLength += 1 + word.length();
                } else {
                    result.append("\n").append(word);
                    lineLength = word.length();
                }
            }
        }
        return result.toString();
    }

    private record BattlePhaseVisual(BattleSnapshot snapshot, Integer playerHp, Integer opponentHp) {
    }

    private record BattlePhaseStep(List<String> messages, BattlePhaseVisual visual) {
    }

    private record BattleUIComponents(ProgressBar playerBugemonHPBar, ProgressBar opponentHPBar, Label playerBugemonHPNumber,
                                      Label opponentHPNumber, ImageView playerBugemon, ImageView opponentBugemon,
                                      Label playerBugemonLabel, Label opponentBugemonLabel, Label playerLevelLabel,
                                      Label opponentLevelLabel, VBox messageBox, Label battleLog) {
    }
}
