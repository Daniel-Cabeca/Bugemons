package ulb.view.windows;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ulb.view.TypeColor;
import ulb.view.windows.BattleWindow.BattleSnapshot;
import ulb.view.windows.BattleWindow.BugemonDisplay;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;


/**
 * Handles all rendering and animation logic for the battle window.
 * Responsible for updating HP bars, Bugemon sprites and labels,
 * and sequentially animating battle log messages across multiple phases.
 */
public class BattleWindowGraphicsHelper {

    private BattleUIComponents ui;
    private Boolean firstAttackWasPlayer;

    /**
     * Binds this helper to the live JavaFX components of the battle window.
     * Has to be called before any rendering method.
     *
     * @param playerBugemonHPBar the player's HP progress bar
     * @param opponentHPBar the opponent's HP progress bar
     * @param playerBugemonHPNumber the label showing the player's current / max HP
     * @param opponentHPNumber the label showing the opponent's current / max HP
     * @param playerBugemon the image view for the player's Bugemon sprite
     * @param opponentBugemon the image view for the opponent's Bugemon sprite
     * @param playerBugemonLabel the label showing the player's Bugemon name
     * @param opponentBugemonLabel the label showing the opponent's Bugemon name
     * @param playerLevelLabel the label showing the player's Bugemon level
     * @param opponentLevelLabel the label showing the opponent's Bugemon level
     * @param messageBox the container that holds the battle log
     * @param battleLog the label that displays individual log messages
     */
    public void linkUI(ProgressBar playerBugemonHPBar, ProgressBar opponentHPBar, Label playerBugemonHPNumber,
                       Label opponentHPNumber, ImageView playerBugemon, ImageView opponentBugemon,
                       Label playerBugemonLabel, Label opponentBugemonLabel, Label playerLevelLabel,
                       Label opponentLevelLabel, VBox messageBox, Label battleLog) {
        this.ui = new BattleUIComponents(playerBugemonHPBar, opponentHPBar, playerBugemonHPNumber,
                                         opponentHPNumber, playerBugemon, opponentBugemon,
                                         playerBugemonLabel, opponentBugemonLabel, playerLevelLabel,
                                         opponentLevelLabel, messageBox, battleLog);
    }

    /**
     * Immediately displays all non-blank log messages in the battle log area, wrapped to 35 characters per line.
     * If the resulting list is empty, the message box is hidden instead.
     *
     * @param logs the list of messages to display; {@code null} is treated as an empty list
     */
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

    /**
     * Animates battle log messages one at a time, updating HP bars and sprites
     * at the appropriate phase boundaries.
     * The rawLogs list may contain {@code null} entries, which act as
     * phase separators. Each phase is displayed sequentially with a 2-second
     * pause between messages. HP values are updated after the first action resolves;
     * the final snapshot is applied when the last phase completes.
     * {@code onComplete} is invoked once all messages and transitions have finished.
     *
     * @param rawLogs ordered log messages, with {@code null} entries marking phase limits
     * @param hpAfterFirstActionSelf the player Bugemon's HP after the first action, or {@code null} if unchanged
     * @param hpAfterFirstActionOpponent the opponent Bugemon's HP after the first action, or {@code null} if unchanged
     * @param finalSnapshot the battle state to render after the last phase
     * @param currentSnapshotSupplier supplier returning the most recent cached BattleSnapshot
     * @param battleRenderer callback that applies a BattleSnapshot to the UI
     * @param onComplete callback invoked once the full animation sequence finishes
     */
    public void displayMessagesSequentially(List<String> rawLogs, Integer hpAfterFirstActionSelf, Integer hpAfterFirstActionOpponent,
                                            BattleSnapshot finalSnapshot, Supplier<BattleSnapshot> currentSnapshotSupplier, Consumer<BattleSnapshot> battleRenderer,
                                            Runnable onComplete) {
        firstAttackWasPlayer = null;
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
            firstAttackWasPlayer = null;
            clearMessages();
            onComplete.run();
        };

        displayPhaseSequence(phaseSteps, 0, currentSnapshotSupplier, battleRenderer, closeAndComplete);
    }

    /**
     * Splits a log list into phases using {@code null} entries as limits.
     * Each phase is a sub-list of non-null messages.
     *
     * @param logs the message list possibly containing {@code null} delimiters
     * @return an ordered list of phases, where each phase is a list of messages
     */
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

    /**
     * Builds an ordered list of BattlePhaseStep objects from the given phases,
     * associating each non-empty phase with the correct BattlePhaseVisual.
     * @param phases the ordered list of message phases
     * @param currentSnapshot the battle state at the start of the sequence
     * @param finalSnapshot the battle state after all actions have resolved
     * @param hpAfterFirstActionSelf player HP after the first action
     * @param hpAfterFirstActionOpponent opponent HP after the first action
     * @return the list of phase steps ready for sequential display
     */
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
                visual = buildFirstPhaseVisual(messages, currentSnapshot, finalSnapshot, hpAfterFirstActionSelf, hpAfterFirstActionOpponent);
            } else if (i == phaseCount - 1) {
                visual = new BattlePhaseVisual(finalSnapshot, null, null);
            } else {
                visual = buildIntermediateVisual(currentSnapshot, finalSnapshot, hpAfterFirstActionSelf, hpAfterFirstActionOpponent);
            }

            steps.add(new BattlePhaseStep(messages, visual));
        }

        return steps;
    }

    /**
     * Builds the visual state for the first action phase.
     * When the first phase is a swap, the final snapshot already contains the new active Bugemon
     * and should be rendered immediately. Otherwise, the previous active Bugemon stays visible
     * while it receives the first action.
     *
     * @param messages the messages belonging to the first phase
     * @param currentSnapshot the battle state before the resolved action sequence
     * @param finalSnapshot the battle state after all actions have resolved
     * @param hpAfterFirstActionSelf player HP after the first action
     * @param hpAfterFirstActionOpponent opponent HP after the first action
     * @return the visual descriptor for the first phase
     */
    private BattlePhaseVisual buildFirstPhaseVisual(List<String> messages, BattleSnapshot currentSnapshot, BattleSnapshot finalSnapshot,
                                                    Integer hpAfterFirstActionSelf, Integer hpAfterFirstActionOpponent) {
        BattleSnapshot phaseSnapshot = isSwapPhase(messages) && finalSnapshot != null
                ? finalSnapshot
                : currentSnapshot;

        return new BattlePhaseVisual(phaseSnapshot, hpAfterFirstActionSelf, hpAfterFirstActionOpponent);
    }

    /**
     * Checks whether a phase corresponds to a Bugemon switch.
     *
     * @param messages the messages to inspect
     * @return {@code true} when at least one message announces a switch
     */
    private boolean isSwapPhase(List<String> messages) {
        return messages.stream().anyMatch(this::isSwapMessage);
    }

    /**
     * Detects the existing log messages used when either side sends a new Bugemon.
     *
     * @param message the log message to inspect
     * @return {@code true} if the message announces a switch
     */
    private boolean isSwapMessage(String message) {
        return message != null
                && (message.startsWith("Tu as envoyé ")
                || message.startsWith("L'adversaire a envoyé "));
    }

    /**
     * Constructs the BattlePhaseVisual for a middle phase.
     * If the opponent's Bugemon has switched between the current and final snapshots,
     * the opponent's displayed HP is forced to 0.
     *
     * @param currentSnapshot the battle state at the start of the sequence
     * @param finalSnapshot the battle state after all actions have resolved
     * @param hpAfterFirstActionSelf player HP override for this phase
     * @param hpAfterFirstActionOpponent opponent HP override for this phase
     * @return the visual descriptor for the intermediate phase
     */
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

    /**
     * Determines whether the opponent's active Bugemon has changed between two snapshots by comparing their names.
     *
     * @param currentSnapshot the earlier battle state
     * @param finalSnapshot the later battle state
     * @return {@code true} if the opponent's Bugemon name differs between the two snapshots
     */
    private boolean hasOpponentSwitched(BattleSnapshot currentSnapshot, BattleSnapshot finalSnapshot) {
        if (currentSnapshot == null || finalSnapshot == null || currentSnapshot.opponentBugemon() == null
                || finalSnapshot.opponentBugemon() == null) {
            return false;
        }

        return !currentSnapshot.opponentBugemon().name().equals(finalSnapshot.opponentBugemon().name());
    }

    /**
     * Recursively advances through the list of phase steps, displaying each one in order.
     * Invokes {@code onComplete} once all steps have been shown.
     *
     * @param phaseSteps the ordered list of steps to display
     * @param phaseIndex the index of the step to display in this call
     * @param currentSnapshotSupplier supplier returning the most recently cached snapshot
     * @param battleRenderer callback that applies a snapshot to the UI
     * @param onComplete callback invoked after the last step completes
     */
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

    /**
     * Renders the full battle state from a BattleSnapshot onto the UI.
     * Updates sprites, name labels, level labels, HP bars, and HP number labels
     * for both the player and the opponent.
     *
     * @param snapshot the battle state to render
     */
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
        ui.playerBugemonLabel().setStyle("-fx-text-fill: " + TypeColor.getTypeColor(playerBugemon.bugemonType()) + ";");
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
        ui.opponentBugemonLabel().setStyle("-fx-text-fill: " + TypeColor.getTypeColor(opponentBugemon.bugemonType()) + ";");
        double opponentRatio = (double) opponentBugemon.hp() / opponentBugemon.maxHp();
        ui.opponentHPBar().setProgress(opponentRatio);
        updateHPBarColor(ui.opponentHPBar(), opponentRatio);
        ui.opponentHPNumber().setText(opponentBugemon.hp() + " / " + opponentBugemon.maxHp());
    }

    /**
     * Clears the battle log text and hides the message box.
     */
    public void clearMessages() {
        ui.battleLog().setText("");
        ui.messageBox().setVisible(false);
        ui.messageBox().setManaged(false);
    }

    /**
     * Recursively displays each message in a phase one at a time, with a 2-second
     * pause between messages. Applies the phase's visual state before each message.
     * Invokes {@code onComplete} after the last message's pause has elapsed.
     *
     * @param messages the ordered list of messages for this phase
     * @param index the index of the message to display in this call
     * @param visual the visual state to apply alongside each message
     * @param currentSnapshotSupplier supplier returning the most recently cached snapshot
     * @param battleRenderer callback that applies a snapshot to the UI
     * @param onComplete callback invoked after the last message in the phase
     */
    private void displayPhase(List<String> messages, int index, BattlePhaseVisual visual, Supplier<BattleSnapshot> currentSnapshotSupplier,
                              Consumer<BattleSnapshot> battleRenderer, Runnable onComplete) {
        if (index >= messages.size()) {
            onComplete.run();
            return;
        }

        // ui.battleLog().setText(wrapText(messages.get(index), 35));
        String message = messages.get(index);
        applyPhaseVisual(visual, currentSnapshotSupplier, battleRenderer);

        ui.battleLog().setText(wrapText(messages.get(index), 35));
        playAttackAnimationIfNeeded(message, visual, currentSnapshotSupplier);

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(2));
        pause.setOnFinished(event -> displayPhase(messages, index + 1, visual, currentSnapshotSupplier, battleRenderer, onComplete));
        pause.play();
    }

    /**
     * Applies a BattlePhaseVisual to the UI.
     * First renders the snapshot, then overlays the intermediate HP values
     *
     * @param visual the visual to apply
     * @param currentSnapshotSupplier supplier returning the most recently cached snapshot
     * @param battleRenderer callback that applies a snapshot to the UI
     */
    private void applyPhaseVisual(BattlePhaseVisual visual, Supplier<BattleSnapshot> currentSnapshotSupplier, Consumer<BattleSnapshot> battleRenderer) {
        if (visual.snapshot() != null) {
            battleRenderer.accept(visual.snapshot());
        }

        if (visual.playerHp() != null && visual.opponentHp() != null) {
            updateHPDisplay(visual.playerHp(), visual.opponentHp(), currentSnapshotSupplier.get());
        }
    }

    /**
     * Updates the HP bars for both Bugemons using the given HP values
     *
     * @param selfHp the player Bugemon's current HP to display
     * @param opponentHp the opponent Bugemon's current HP to display
     * @param currentSnapshot the snapshot providing max HP values
     */
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

    /**
     * Updates the color of the HP bar to reflect its current fill ratio.
     * Above 50% → green, above 25% → yellow, 25% and below → red.
     *
     * @param bar the progress bar whose color should be updated
     * @param ratio the current HP ratio
     */
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

    /**
     * Word-wraps the given text so that no line exceeds maxChars characters.
     * Existing newlines in the input are preserved as hard line breaks, and words
     * are never split mid-word — overflow words are moved to the next line.
     *
     * @param text the text to wrap
     * @param maxChars the maximum number of characters allowed per line
     * @return the wrapped text with newlines inserted as needed
     */
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

	/**
	 * Plays an animation when a Bugemon attacks. Helper function for playAttackAnimationIfNeeded()
	 * 
	 * @param attacker the sprite of the Bugemon attacking
	 * @param isPlayer true if the attacking Bugemon is the player's, false otherwise
	 */
	public void playAttackAnimation(ImageView attacker, boolean isPlayer) {
		TranslateTransition attackAnimation = new TranslateTransition(javafx.util.Duration.seconds(0.3), attacker);
		attackAnimation.setByX(isPlayer ? 200 : -200);
		
		TranslateTransition attackAnimationBack = new TranslateTransition(javafx.util.Duration.seconds(0.2), attacker);
		attackAnimationBack.setByX(isPlayer ? -200 : 200);

		new SequentialTransition(attackAnimation, attackAnimationBack).play();
	}

    /**
	 * Plays an attack animation when the currently displayed combat message corresponds to a Bugemon attack.
     * The attacker is detected from the beginning of messages such as "Florachu a utilisé Fouet-Liane".
	 * 
     * @param message the combat log message currently displayed
	 * @param visual the visual active state for this message
	 * @param currentSnapshotSupplier supplier returning the most recent snapshot
	 */
    private void playAttackAnimationIfNeeded(String message, BattlePhaseVisual visual, Supplier<BattleSnapshot> currentSnapshotSupplier) {
        if (message == null || !message.contains(" a utilisé ")) {
            return;
        }

        BattleSnapshot displayedSnapshot = visual.snapshot() != null ? visual.snapshot() : currentSnapshotSupplier.get();
        BattleSnapshot previousSnapshot = currentSnapshotSupplier.get();
        if (displayedSnapshot == null) {
            return;
        }

        String attackerName = message.substring(0, message.indexOf(" a utilisé "));

        boolean playerMatches = displayedSnapshot.playerBugemon() != null && attackerName.equals(displayedSnapshot.playerBugemon().name());
        boolean opponentMatches = displayedSnapshot.opponentBugemon() != null && attackerName.equals(displayedSnapshot.opponentBugemon().name());

        if (playerMatches && opponentMatches) {
            playAmbiguousAttackAnimation(visual, previousSnapshot);
            return;
        }

        if (playerMatches) {
            playAttackAnimation(ui.playerBugemon(), true);
        }

        if (opponentMatches) {
            playAttackAnimation(ui.opponentBugemon(), false);
        }

    }

    /**
     * Resolves attack animations when both active Bugemons have the same name.
     * The first ambiguous attacked is inferred from the HP side that changed. The next ambiguous attack in the same
     * action sequence must belong to the other side because each side can act once per round.
     * 
     * @param visual the visual state necessary to detect attack animation sequence
     * @param previousSnapshot the battle state before the current action sequence started
     */
    private void playAmbiguousAttackAnimation(BattlePhaseVisual visual, BattleSnapshot previousSnapshot) {
        if (firstAttackWasPlayer != null) {
            boolean secondAttackerIsPlayer = !firstAttackWasPlayer;
            playAttackAnimation(secondAttackerIsPlayer ? ui.playerBugemon() : ui.opponentBugemon(), secondAttackerIsPlayer);
            return;
        }

        Boolean attackerIsPlayer = inferAttackerFromHPChange(visual, previousSnapshot);
        if (attackerIsPlayer == null) {
            return;
        }

        firstAttackWasPlayer = attackerIsPlayer;
        playAttackAnimation(attackerIsPlayer ? ui.playerBugemon() : ui.opponentBugemon(), attackerIsPlayer);

    }

    /**
     * Infers the attacking side by comparing HP before and after the action being displayed.
     * If the player lost HP, the opponent attacked. If the opponent attacked, the player attacked.
     * 
     * @param visual the visual state after the action
     * @param previousSnapshot the battle state before the action sequence started
     * @return {@code true} if the player attacked first, {@code false} if the opponent attacked first, or {@code null} if unknown
     */
    private Boolean inferAttackerFromHPChange(BattlePhaseVisual visual, BattleSnapshot previousSnapshot) {
        if (visual == null || previousSnapshot == null || visual.snapshot() == null || previousSnapshot.playerBugemon() == null
                || previousSnapshot.opponentBugemon() == null || visual.snapshot().playerBugemon() == null
                || visual.snapshot().opponentBugemon() == null) {
            return null;
        }

        int previousPlayerHP = previousSnapshot.playerBugemon().hp();
        int previousOpponentHP = previousSnapshot.opponentBugemon().hp();
        int displayedPlayerHP = visual.playerHp() != null ? visual.playerHp() : visual.snapshot().playerBugemon().hp();
        int displayedOpponentHP = visual.opponentHp() != null ? visual.opponentHp() : visual.snapshot().opponentBugemon().hp();

        boolean playerLostHP = displayedPlayerHP < previousPlayerHP;
        boolean opponentLostHP = displayedOpponentHP < previousOpponentHP;

        if (playerLostHP && !opponentLostHP) {
            return false;
        }

        if (opponentLostHP && !playerLostHP) {
            return true;
        }

        return null;
    }

    /**
     * Describes the visual state to apply during a battle phase.
     *
     * @param snapshot the battle snapshot to render (sprites, labels, HP bars)
     * @param playerHp an HP override for the player's Bugemon
     * @param opponentHp an HP override for the opponent's Bugemon
     */
    private record BattlePhaseVisual(BattleSnapshot snapshot, Integer playerHp, Integer opponentHp) {
    }

    /**
     * Associates a list of log messages with the visual state that should be active while those messages are displayed.
     *
     * @param messages the ordered messages to display during this phase
     * @param visual the visual state to apply for the duration of this phase
     */
    private record BattlePhaseStep(List<String> messages, BattlePhaseVisual visual) {
    }

    /**
     * Combines all JavaFX UI components that this helper reads and writes.
     * Constructed once via linkUI and used throughout.
     *
     * @param playerBugemonHPBar the player's HP progress bar
     * @param opponentHPBar the opponent's HP progress bar
     * @param playerBugemonHPNumber label showing the player's current / max HP
     * @param opponentHPNumber label showing the opponent's current / max HP
     * @param playerBugemon image view for the player's Bugemon sprite
     * @param opponentBugemon image view for the opponent's Bugemon sprite
     * @param playerBugemonLabel label for the player's Bugemon name
     * @param opponentBugemonLabel label for the opponent's Bugemon name
     * @param playerLevelLabel label for the player's Bugemon level
     * @param opponentLevelLabel label for the opponent's Bugemon level
     * @param messageBox container that holds the battle log
     * @param battleLog label that displays individual log messages
     */
    private record BattleUIComponents(ProgressBar playerBugemonHPBar, ProgressBar opponentHPBar, Label playerBugemonHPNumber,
                                      Label opponentHPNumber, ImageView playerBugemon, ImageView opponentBugemon,
                                      Label playerBugemonLabel, Label opponentBugemonLabel, Label playerLevelLabel,
                                      Label opponentLevelLabel, VBox messageBox, Label battleLog) {
    }
}
