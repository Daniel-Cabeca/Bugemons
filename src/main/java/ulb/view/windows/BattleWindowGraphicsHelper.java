package ulb.view.windows;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ulb.view.windows.BattleWindow.BattleSnapshot;
import ulb.view.windows.BattleWindow.BugemonDisplay;

public class BattleWindowGraphicsHelper {

	public void showLogMessages(List<String> logs, Label battleLog, VBox messageBox) {
        List<String> visibleLogs = logs == null
                ? List.of()
                : logs.stream().filter(message -> message != null && !message.isBlank()).toList();

        if (visibleLogs.isEmpty()) {
            clearMessages(battleLog, messageBox);
            return;
        }

        String allMessages = visibleLogs.stream()
                .map(message -> wrapText(message, 35))
                .collect(Collectors.joining("\n"));

        battleLog.setText(allMessages);
        messageBox.setVisible(true);
        messageBox.setManaged(true);
    }

	public void displayMessagesSequentially(List<String> rawLogs, Integer hpAfterFirstActionSelf,
    	Integer hpAfterFirstActionOpponent, BattleSnapshot finalSnapshot, BattleSnapshot currentSnapshot,
		Runnable onComplete, ProgressBar PlayerBugemonHPBar, Label PlayerBugemonHPNumber, 
		ProgressBar OpponentHPBar, Label OpponentHPNumber, VBox messageBox, Label battleLog) {
        
        List<String> logs = rawLogs == null ? List.of() : new ArrayList<>(rawLogs);
        int separatorIndex = logs.indexOf(null);

        List<String> phase1;
        List<String> phase2;
        if (separatorIndex < 0) {
            phase1 = logs.stream().filter(message -> message != null).collect(Collectors.toList());
            phase2 = List.of();
        } else {
            phase1 = logs.subList(0, separatorIndex).stream().filter(message -> message != null).collect(Collectors.toList());
            phase2 = logs.subList(separatorIndex + 1, logs.size()).stream().filter(message -> message != null).collect(Collectors.toList());
        }

        if (phase1.isEmpty() && phase2.isEmpty()) {
            if (finalSnapshot != null) {
                renderBattle(finalSnapshot);
            }
            clearMessages(battleLog, messageBox);
            onComplete.run();
            return;
        }

        messageBox.setVisible(true);
        messageBox.setManaged(true);

        BattlePhaseVisual phase1Visual;
        BattlePhaseVisual phase2Visual = new BattlePhaseVisual(finalSnapshot, null, null);
        if (separatorIndex >= 0) {
            phase1Visual = new BattlePhaseVisual(currentSnapshot, hpAfterFirstActionSelf, hpAfterFirstActionOpponent);
        } else {
            phase1Visual = phase2Visual;
        }

        Runnable closeAndComplete = () -> {
            clearMessages(battleLog, messageBox);
            onComplete.run();
        };

        Runnable afterPhase1 = () -> {
            if (phase2.isEmpty()) {
                closeAndComplete.run();
            } else {
                displayPhase(phase2, 0, phase2Visual, currentSnapshot, closeAndComplete, 
					PlayerBugemonHPBar, PlayerBugemonHPNumber, OpponentHPBar, OpponentHPNumber, battleLog);
            }
        };

        if (phase1.isEmpty()) {
            afterPhase1.run();
        } else {
            displayPhase(phase1, 0, phase1Visual, currentSnapshot, afterPhase1, PlayerBugemonHPBar, 
				PlayerBugemonHPNumber, OpponentHPBar, OpponentHPNumber, battleLog);
        }
    }

	public void updateBattleGraphics(BattleSnapshot snapshot, ImageView PlayerBugemon, Label PlayerBugemonLabel,
		Label PlayerLevelLabel, ProgressBar PlayerBugemonHPBar, Label PlayerBugemonHPNumber, ImageView OpponentBugemon,
		Label OpponentBugemonLabel, Label OpponentLevelLabel, ProgressBar OpponentHPBar, 
		Label OpponentHPNumber) {
        BugemonDisplay playerBugemon = snapshot.playerBugemon();
        BugemonDisplay opponentBugemon = snapshot.opponentBugemon();

        try {
            Image playerImage = new Image(getClass().getResourceAsStream(playerBugemon.spritePath()));
            PlayerBugemon.setImage(playerImage);
        } catch (Exception e) {
            System.err.println("Failed to load player bugemon sprite: " + e.getMessage());
        }

        PlayerBugemonLabel.setText(playerBugemon.name());
        PlayerLevelLabel.setText("Lv." + playerBugemon.level());
        PlayerBugemonLabel.setStyle("-fx-text-fill: " + playerBugemon.color() + ";");
        double playerRatio = (double) playerBugemon.hp() / playerBugemon.maxHp();
        PlayerBugemonHPBar.setProgress(playerRatio);
        updateHPBarColor(PlayerBugemonHPBar, playerRatio);
        PlayerBugemonHPNumber.setText(playerBugemon.hp() + " / " + playerBugemon.maxHp());

        try {
            Image opponentImage = new Image(getClass().getResourceAsStream(opponentBugemon.spritePath()));
            OpponentBugemon.setImage(opponentImage);
        } catch (Exception e) {
            System.err.println("Failed to load opponent bugemon sprite: " + e.getMessage());
        }

        OpponentBugemonLabel.setText(opponentBugemon.name());
        OpponentLevelLabel.setText("Lv." + opponentBugemon.level());
        OpponentBugemonLabel.setStyle("-fx-text-fill: " + opponentBugemon.color() + ";");
        double opponentRatio = (double) opponentBugemon.hp() / opponentBugemon.maxHp();
        OpponentHPBar.setProgress(opponentRatio);
        updateHPBarColor(OpponentHPBar, opponentRatio);
        OpponentHPNumber.setText(opponentBugemon.hp() + " / " + opponentBugemon.maxHp());
    }

	private void displayPhase(List<String> messages, int index, BattlePhaseVisual visual, BattleSnapshot currentSnapshot,
		Runnable onComplete, ProgressBar PlayerBugemonHPBar, Label PlayerBugemonHPNumber, 
		ProgressBar OpponentHPBar, Label OpponentHPNumber, Label battleLog) {
        if (index >= messages.size()) {
            onComplete.run();
            return;
        }

        battleLog.setText(wrapText(messages.get(index), 35));
        applyPhaseVisual(visual, currentSnapshot, PlayerBugemonHPBar, PlayerBugemonHPNumber, OpponentHPBar, OpponentHPNumber);

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(2));
        pause.setOnFinished(event -> displayPhase(messages, index + 1, visual, currentSnapshot, onComplete, 
			PlayerBugemonHPBar, PlayerBugemonHPNumber, OpponentHPBar, OpponentHPNumber, battleLog));
        pause.play();
    }

    private void applyPhaseVisual(BattlePhaseVisual visual, BattleSnapshot currentSnapshot, ProgressBar PlayerBugemonHPBar,
		Label PlayerBugemonHPNumber, ProgressBar OpponentHPBar, Label OpponentHPNumber) {
        if (visual.snapshot() != null) {
            renderBattle(visual.snapshot());
        }

        if (visual.playerHp() != null && visual.opponentHp() != null) {
            updateHPDisplay(visual.playerHp(), visual.opponentHp(), currentSnapshot, PlayerBugemonHPBar, PlayerBugemonHPNumber,
			OpponentHPBar, OpponentHPNumber);
        }
    }

	private void updateHPDisplay(int selfHp, int opponentHp, BattleSnapshot currentSnapshot, ProgressBar PlayerBugemonHPBar,
		Label PlayerBugemonHPNumber, ProgressBar OpponentHPBar, Label OpponentHPNumber) {
        if (currentSnapshot == null || currentSnapshot.playerBugemon() == null || currentSnapshot.opponentBugemon() == null) {
            return;
        }

        BugemonDisplay self = currentSnapshot.playerBugemon();
        BugemonDisplay opponent = currentSnapshot.opponentBugemon();
        double selfRatio = (double) selfHp / self.maxHp();
        double opponentRatio = (double) opponentHp / opponent.maxHp();

        PlayerBugemonHPBar.setProgress(selfRatio);
        PlayerBugemonHPNumber.setText(selfHp + " / " + self.maxHp());
        updateHPBarColor(PlayerBugemonHPBar, selfRatio);

        OpponentHPBar.setProgress(opponentRatio);
        OpponentHPNumber.setText(opponentHp + " / " + opponent.maxHp());
        updateHPBarColor(OpponentHPBar, opponentRatio);
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


	public void clearMessages(Label battleLog, VBox messageBox) {
        battleLog.setText("");
        messageBox.setVisible(false);
        messageBox.setManaged(false);
    }

	public void renderBattle(BattleSnapshot snapshot) {
        if (snapshot == null || snapshot.playerBugemon() == null || snapshot.opponentBugemon() == null) {
            return;
        }
    }

	private record BattlePhaseVisual(BattleSnapshot snapshot, Integer playerHp, Integer opponentHp) {
    }
}
