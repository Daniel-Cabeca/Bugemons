package ulb.view.windows;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BattleWindow extends Window {

    @FXML
    private ListView<InventoryEntry> inventoryList;
    @FXML
    private ListView<BugemonEntry> bugemonsList;
    @FXML
    private ListView<AbilityEntry> abilitiesList;
    @FXML
    private Label floorLabel;
    @FXML
    private Label roomLabel;
    @FXML
    private ImageView OpponentBugemon;
    @FXML
    private ImageView PlayerBugemon;
    @FXML
    private ProgressBar PlayerBugemonHPBar;
    @FXML
    private Label PlayerBugemonLabel;
    @FXML
    private Label PlayerLevelLabel;
    @FXML
    private Label PlayerBugemonHPNumber;
    @FXML
    private ProgressBar OpponentHPBar;
    @FXML
    private Label OpponentBugemonLabel;
    @FXML
    private Label OpponentLevelLabel;
    @FXML
    private Label OpponentHPNumber;
    @FXML
    private Button autoButton;
    @FXML
    private GridPane buttonsGrid;
    @FXML
    private VBox inventoryView;
    @FXML
    private VBox bugemonsView;
    @FXML
    private VBox abilitiesView;
    @FXML
    private VBox messageBox;
    @FXML
    private Button attackButton;
    @FXML
    private Button itemButton;
    @FXML
    private Button runButton;
    @FXML
    private Button switchButton;
    @FXML
    private Label battleLog;

    private ViewListener viewListener;
    private boolean autoMode;
    private boolean forcedSwitch;
    private BattleSnapshot currentSnapshot;

    @FXML
    private void initialize() {
        setupInventoryList();
        setupBugemonsList();
        setupAbilitiesList();
    }

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    public void initializeView(boolean autoMode) {
        this.autoMode = autoMode;
        attackButton.setDisable(autoMode);
        itemButton.setDisable(autoMode);
        switchButton.setDisable(autoMode);
        autoButton.setDisable(!autoMode);
        showMainMenu();
        clearMessages();
    }

    public void setForcedSwitch(boolean forcedSwitch) {
        this.forcedSwitch = forcedSwitch;
        updateBackButtonsState();
    }

    public void renderBattle(BattleSnapshot snapshot) {
        if (snapshot == null || snapshot.playerBugemon() == null || snapshot.opponentBugemon() == null) {
            return;
        }

        currentSnapshot = snapshot;
        updateBattleGraphics(snapshot);
    }

    @FXML
    public void handleItemMenu() {
        if (viewListener != null) {
            viewListener.onItemMenu();
        }
    }

    @FXML
    public void handleBugemonsMenu() {
        if (viewListener != null) {
            viewListener.onBugemonsMenu();
        }
    }

    @FXML
    public void handleAuto(ActionEvent event) {
        if (viewListener != null) {
            viewListener.onAuto(event);
        }
    }

    @FXML
    public void handleAttack() {
        if (viewListener != null) {
            viewListener.onAttack();
        }
    }

    @FXML
    public void handleBackToMenu() {
        if (viewListener != null) {
            viewListener.onBackToMenu();
        }
    }

    @FXML
    public void handleReturn(ActionEvent event) {
        if (viewListener != null) {
            viewListener.onReturn(event);
        }
    }

    public void showInventoryMenu(List<InventoryEntry> inventoryEntries) {
        buttonsGrid.setVisible(false);
        buttonsGrid.setManaged(false);
        bugemonsView.setVisible(false);
        bugemonsView.setManaged(false);
        abilitiesView.setVisible(false);
        abilitiesView.setManaged(false);
        inventoryView.setVisible(true);
        inventoryView.setManaged(true);
        inventoryList.getItems().setAll(inventoryEntries);
        updateBackButtonsState();
    }

    public void showBugemonsMenu(List<BugemonEntry> bugemonEntries) {
        buttonsGrid.setVisible(false);
        buttonsGrid.setManaged(false);
        inventoryView.setVisible(false);
        inventoryView.setManaged(false);
        abilitiesView.setVisible(false);
        abilitiesView.setManaged(false);
        bugemonsView.setVisible(true);
        bugemonsView.setManaged(true);
        bugemonsList.getItems().setAll(bugemonEntries);
        updateBackButtonsState();
    }

    public void showAbilitiesMenu(List<AbilityEntry> abilityEntries) {
        buttonsGrid.setVisible(false);
        buttonsGrid.setManaged(false);
        inventoryView.setVisible(false);
        inventoryView.setManaged(false);
        bugemonsView.setVisible(false);
        bugemonsView.setManaged(false);
        abilitiesView.setVisible(true);
        abilitiesView.setManaged(true);
        abilitiesList.getItems().setAll(abilityEntries);
        updateBackButtonsState();
    }

    public void showMainMenu() {
        inventoryView.setVisible(false);
        inventoryView.setManaged(false);
        bugemonsView.setVisible(false);
        bugemonsView.setManaged(false);
        abilitiesView.setVisible(false);
        abilitiesView.setManaged(false);
        buttonsGrid.setVisible(true);
        buttonsGrid.setManaged(true);
        updateBackButtonsState();
    }

    public void setAutoButtonVisible(boolean visible) {
        autoButton.setVisible(visible);
        autoButton.setManaged(visible);
    }

    public void setBattleInputsDisabled(boolean disabled) {
        attackButton.setDisable(disabled || autoMode);
        itemButton.setDisable(disabled || autoMode);
        runButton.setDisable(disabled);
        switchButton.setDisable(disabled || autoMode);

        inventoryView.setDisable(disabled);
        bugemonsView.setDisable(disabled);
        abilitiesView.setDisable(disabled);
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

        battleLog.setText(allMessages);
        messageBox.setVisible(true);
        messageBox.setManaged(true);
    }

    public void displayMessagesSequentially(List<String> rawLogs,
                                            Integer hpAfterFirstActionSelf,
                                            Integer hpAfterFirstActionOpponent,
                                            BattleSnapshot finalSnapshot,
                                            Runnable onComplete) {
        hideAllMenus();

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
            clearMessages();
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
            clearMessages();
            onComplete.run();
        };

        Runnable afterPhase1 = () -> {
            if (phase2.isEmpty()) {
                closeAndComplete.run();
            } else {
                displayPhase(phase2, 0, phase2Visual, closeAndComplete);
            }
        };

        if (phase1.isEmpty()) {
            afterPhase1.run();
        } else {
            displayPhase(phase1, 0, phase1Visual, afterPhase1);
        }
    }

    private void clearMessages() {
        battleLog.setText("");
        messageBox.setVisible(false);
        messageBox.setManaged(false);
    }

    private void updateBattleGraphics(BattleSnapshot snapshot) {
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

    private void updateBackButtonsState() {
        setBackButtonDisabled(bugemonsView, forcedSwitch);
    }

    private void setBackButtonDisabled(VBox view, boolean disabled) {
        if (view == null) {
            return;
        }

        for (Node child : view.getChildren()) {
            if (child instanceof Button button) {
                button.setDisable(disabled);
            }
        }
    }

    private void setupInventoryList() {
        inventoryList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<InventoryEntry> call(ListView<InventoryEntry> listView) {
                return new ListCell<>() {
                    private final HBox hbox = new HBox(10);
                    private final ImageView imageView = new ImageView();
                    private final Label label = new Label();
                    private final Button button = new Button("Utiliser");

                    {
                        imageView.setFitHeight(30);
                        imageView.setFitWidth(30);
                        hbox.getChildren().addAll(imageView, label, button);
                        button.setOnAction(event -> {
                            InventoryEntry entry = getItem();
                            if (entry != null && viewListener != null) {
                                viewListener.onUseItem(entry.itemId(), event);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(InventoryEntry entry, boolean empty) {
                        super.updateItem(entry, empty);
                        if (empty || entry == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            try {
                                Image image = new Image(getClass().getResourceAsStream(entry.itemSpritePath()));
                                imageView.setImage(image);
                            } catch (Exception e) {
                                System.err.println("Failed to load item image: " + e.getMessage());
                            }
                            label.setText(entry.itemName() + " x" + entry.quantity());
                            button.setDisable(!entry.usable());
                            setGraphic(hbox);
                        }
                    }
                };
            }
        });
    }

    private void setupBugemonsList() {
        bugemonsList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<BugemonEntry> call(ListView<BugemonEntry> listView) {
                return new ListCell<>() {
                    private final HBox hbox = new HBox(10);
                    private final ImageView imageView = new ImageView();
                    private final Label label = new Label();
                    private final Button button = new Button("Échanger");

                    {
                        imageView.setFitHeight(30);
                        imageView.setFitWidth(30);
                        hbox.getChildren().addAll(imageView, label, button);
                        button.setOnAction(event -> {
                            BugemonEntry entry = getItem();
                            if (entry != null && viewListener != null) {
                                viewListener.onSwapBugemon(entry.bugemonId(), event);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(BugemonEntry entry, boolean empty) {
                        super.updateItem(entry, empty);
                        if (empty || entry == null || entry.active()) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            try {
                                Image image = new Image(getClass().getResourceAsStream(entry.bugemonSpritePath()));
                                imageView.setImage(image);
                            } catch (Exception e) {
                                System.err.println("Failed to load bugemon image: " + e.getMessage());
                            }
                            label.setText(entry.ko() ? entry.bugemonName() + " (KO)" : entry.bugemonName());
                            button.setDisable(!entry.selectable());
                            setGraphic(hbox);
                        }
                    }
                };
            }
        });
    }

    private void setupAbilitiesList() {
        abilitiesList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<AbilityEntry> call(ListView<AbilityEntry> listView) {
                return new ListCell<>() {
                    private final HBox hbox = new HBox(10);
                    private final Label label = new Label();
                    private final Button button = new Button("Utiliser");

                    {
                        hbox.getChildren().addAll(label, button);
                        button.setOnAction(event -> {
                            AbilityEntry entry = getItem();
                            if (entry != null && viewListener != null) {
                                viewListener.onUseAbility(entry.abilityId(), event);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(AbilityEntry entry, boolean empty) {
                        super.updateItem(entry, empty);
                        if (empty || entry == null) {
                            setText(null);
                            setGraphic(null);
                            setTooltip(null);
                        } else {
                            label.setText(entry.abilityName());

                            hbox.setStyle(
                                    "-fx-background-color: " + entry.color() + ";" +
                                            "-fx-padding: 6;" +
                                            "-fx-background-radius: 6;"
                            );

                            if (entry.effectiveness() != null) {
                                Tooltip tooltip = new Tooltip(entry.effectiveness());
                                tooltip.setShowDelay(javafx.util.Duration.millis(100));
                                setTooltip(tooltip);
                            } else {
                                setTooltip(null);
                            }

                            setGraphic(hbox);
                        }
                    }
                };
            }
        });
    }

    private void hideAllMenus() {
        buttonsGrid.setVisible(false);
        buttonsGrid.setManaged(false);
        inventoryView.setVisible(false);
        inventoryView.setManaged(false);
        bugemonsView.setVisible(false);
        bugemonsView.setManaged(false);
        abilitiesView.setVisible(false);
        abilitiesView.setManaged(false);
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

    private void displayPhase(List<String> messages, int index, BattlePhaseVisual visual, Runnable onComplete) {
        if (index >= messages.size()) {
            onComplete.run();
            return;
        }

        battleLog.setText(wrapText(messages.get(index), 35));
        applyPhaseVisual(visual);

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(2));
        pause.setOnFinished(event -> displayPhase(messages, index + 1, visual, onComplete));
        pause.play();
    }

    private void applyPhaseVisual(BattlePhaseVisual visual) {
        if (visual.snapshot() != null) {
            renderBattle(visual.snapshot());
        }

        if (visual.playerHp() != null && visual.opponentHp() != null) {
            updateHPDisplay(visual.playerHp(), visual.opponentHp());
        }
    }

    private void updateHPDisplay(int selfHp, int opponentHp) {
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

    public void setTowerInfo(int floorNumber, int roomIndex) {
        floorLabel.setText("Etage: NO" + floorNumber);
        roomLabel.setText("Salle: " + roomIndex);
    }

    public void clearTowerInfo() {
        floorLabel.setText("");
        roomLabel.setText("");
    }

    public record BattleSnapshot(BugemonDisplay playerBugemon, BugemonDisplay opponentBugemon) {
    }

    public record BugemonDisplay(String name, String spritePath, String color, int level, int hp, int maxHp) {
    }

    public record InventoryEntry(String itemId, String itemName, String itemSpritePath, int quantity, boolean usable) {
    }

    public record BugemonEntry(String bugemonId, String bugemonName, String bugemonSpritePath,
                               boolean ko, boolean active, boolean selectable) {
    }

    public record AbilityEntry(String abilityId, String abilityName, String color, String effectiveness) {
    }

    private record BattlePhaseVisual(BattleSnapshot snapshot, Integer playerHp, Integer opponentHp) {
    }

    public interface ViewListener {
        void onItemMenu();
        void onBugemonsMenu();
        void onAuto(ActionEvent event);
        void onAttack();
        void onBackToMenu();
        void onReturn(ActionEvent event);
        void onUseItem(String itemId, ActionEvent event);
        void onSwapBugemon(String bugemonId, ActionEvent event);
        void onUseAbility(String abilityId, ActionEvent event);
    }
}
