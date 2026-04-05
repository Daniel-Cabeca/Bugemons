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
import javafx.scene.media.MediaPlayer;
import javafx.util.Callback;
import ulb.controller.BattleController;
import ulb.communication.types.GameMode;
import ulb.model.ability.Ability;
import ulb.model.battle.BattleState;
import ulb.model.bugemon.Bugemon;
import ulb.model.item.Inventory;
import ulb.model.item.Item;
import ulb.model.team.Team;
import ulb.model.type.Type;
import ulb.controller.towerManager.TowerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BattleWindow extends Window {

    @FXML
    private ListView<Item> inventoryList;
    @FXML
    private ListView<Bugemon> bugemonsList;
    @FXML
    private ListView<Ability> abilitiesList;
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

    private Team playerTeam;
    private Inventory playerInventory;
    private BattleController battleController;
    private TowerManager towerManager;
    private GameMode gameMode;
    private MediaPlayer mediaPlayer;

    private ViewListener viewListener;

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    public void initializeBattle(Team playerTeam, Inventory playerInventory, GameMode gameMode,
                                 TowerManager towerManager, BattleController battleController) {
        this.playerTeam = playerTeam;
        this.playerInventory = playerInventory;
        this.gameMode = gameMode;
        this.towerManager = towerManager;
        this.battleController = battleController;

        if (gameMode == GameMode.AUTO) {
            attackButton.setDisable(true);
            itemButton.setDisable(true);
            switchButton.setDisable(true);
        } else {
            autoButton.setDisable(true);
        }

        initializeGraphicalBattle();
        if (gameMode == GameMode.TOWER && towerManager != null) {
            floorLabel.setText("Etage: NO" + towerManager.getFloorNumber());
            roomLabel.setText("Salle: " + towerManager.getCurrentRoomIndex());
        } else {
            floorLabel.setText("");
            roomLabel.setText("");
        }
    }

    public BattleController getBattleController() {
        return battleController;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    @FXML
    public void handleItemMenu() {
        viewListener.onItemMenu();
    }

    @FXML
    public void handleBugemonsMenu() {
        viewListener.onBugemonsMenu();
    }

    @FXML
    public void handleAuto(ActionEvent event) {
        viewListener.onAuto(event);
    }

    @FXML
    public void handleAttack() {
        viewListener.onAttack();
    }

    @FXML
    public void handleBackToMenu() {
        viewListener.onBackToMenu();
    }

    @FXML
    public void handleReturn(ActionEvent event) {
        viewListener.onReturn(event);
    }

    public void showInventoryMenu() {
        if (buttonsGrid != null && inventoryView != null) {
            buttonsGrid.setVisible(false);
            buttonsGrid.setManaged(false);
            inventoryView.setVisible(true);
            inventoryView.setManaged(true);
            displayInventory();
            updateBackButtonsState();
        }
    }

    public void showBugemonsMenu() {
        if (buttonsGrid != null && bugemonsView != null) {
            buttonsGrid.setVisible(false);
            buttonsGrid.setManaged(false);
            bugemonsView.setVisible(true);
            bugemonsView.setManaged(true);
            displayTeam();
            updateBackButtonsState();
        }
    }

    public void showAbilitiesMenu() {
        if (buttonsGrid != null && abilitiesView != null) {
            buttonsGrid.setVisible(false);
            buttonsGrid.setManaged(false);
            abilitiesView.setVisible(true);
            abilitiesView.setManaged(true);
            displayAbilities();
            updateBackButtonsState();
        }
    }

    public void showMainMenu() {
        if (isForcedSwitch()) {
            updateBackButtonsState();
        }

        if (buttonsGrid != null && inventoryView != null) {
            inventoryView.setVisible(false);
            inventoryView.setManaged(false);
            bugemonsView.setVisible(false);
            bugemonsView.setManaged(false);
            abilitiesView.setVisible(false);
            abilitiesView.setManaged(false);
            buttonsGrid.setVisible(true);
            buttonsGrid.setManaged(true);
        }

        updateBackButtonsState();
    }

    public void setAutoButtonVisible(boolean visible) {
        autoButton.setVisible(visible);
        autoButton.setManaged(visible);
    }

    public void setBattleInputsDisabled(boolean disabled) {
        attackButton.setDisable(disabled || gameMode == GameMode.AUTO);
        itemButton.setDisable(disabled || gameMode == GameMode.AUTO);
        runButton.setDisable(disabled);
        switchButton.setDisable(disabled || gameMode == GameMode.AUTO);

        inventoryView.setDisable(disabled);
        bugemonsView.setDisable(disabled);
        abilitiesView.setDisable(disabled);
    }

    public void displayAbilities() {
        if (battleController == null) {
            abilitiesList.getItems().clear();
            return;
        }

        List<Ability> abilities = new ArrayList<>();
        for (Ability ability : battleController.getActiveBugemonSelf().getAbilities()) {
            if (ability != null) {
                abilities.add(ability);
            }
        }
        abilitiesList.getItems().setAll(abilities);
        setupAbilitiesList();
    }

    public void displayInventory() {
        if (playerInventory == null) {
            inventoryList.getItems().clear();
            return;
        }
        inventoryList.getItems().setAll(playerInventory.getItems().keySet());
        setupInventoryList();
    }

    public void displayTeam() {
        if (playerTeam == null) {
            bugemonsList.getItems().clear();
            return;
        }
        bugemonsList.getItems().setAll(playerTeam.getMembers());
        setupBugemonsList();
    }

    public void displayNextMessage() {
        if (battleController == null) {
            return;
        }

        List<String> logs = battleController.getLogMsg();
        if (logs != null && !logs.isEmpty()) {
            String allMessages = logs.stream()
                    .filter(message -> message != null)
                    .map(message -> wrapText(message, 35))
                    .collect(Collectors.joining("\n"));
            battleLog.setText(allMessages);
            messageBox.setVisible(true);
            messageBox.setManaged(true);
            battleController.clearLogMsg();
        } else {
            battleLog.setText("");
            messageBox.setVisible(false);
            messageBox.setManaged(false);
        }
        initializeGraphicalBattle();
    }

    public void displayMessagesSequentially(Runnable onComplete) {
        if (battleController == null) {
            onComplete.run();
            return;
        }

        hideAllMenus();
        List<String> rawLogs = new ArrayList<>(battleController.getLogMsg());
        battleController.clearLogMsg();

        int separatorIndex = rawLogs.indexOf(null);
        List<String> phase1;
        List<String> phase2;
        if (separatorIndex < 0) {
            phase1 = rawLogs.stream().filter(message -> message != null).collect(Collectors.toList());
            phase2 = new ArrayList<>();
        } else {
            phase1 = rawLogs.subList(0, separatorIndex).stream().filter(message -> message != null).collect(Collectors.toList());
            phase2 = rawLogs.subList(separatorIndex + 1, rawLogs.size()).stream().filter(message -> message != null).collect(Collectors.toList());
        }

        if (phase1.isEmpty() && phase2.isEmpty()) {
            initializeGraphicalBattle();
            onComplete.run();
            return;
        }

        messageBox.setVisible(true);
        messageBox.setManaged(true);

        if (separatorIndex >= 0) {
            updateHPDisplay(battleController.getHpAfterFirstActionSelf(), battleController.getHpAfterFirstActionOpponent());
        } else {
            initializeGraphicalBattle();
        }

        Runnable closeAndComplete = () -> {
            battleLog.setText("");
            messageBox.setVisible(false);
            messageBox.setManaged(false);
            onComplete.run();
        };

        Runnable afterPhase1 = () -> {
            if (phase2.isEmpty()) {
                closeAndComplete.run();
            } else {
                initializeGraphicalBattle();
                displayPhase(phase2, 0, closeAndComplete);
            }
        };

        if (phase1.isEmpty()) {
            afterPhase1.run();
        } else {
            displayPhase(phase1, 0, afterPhase1);
        }
    }

    public void stopBattleMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }

    private void initializeGraphicalBattle() {
        if (battleController == null) {
            return;
        }

        Bugemon playerBugemon = battleController.getActiveBugemonSelf();
        Bugemon opponentBugemon = battleController.getActiveBugemonOpponent();
        if (playerBugemon == null || opponentBugemon == null) {
            return;
        }

        try {
            Image playerImage = new Image(getClass().getResourceAsStream(playerBugemon.getSprite()));
            PlayerBugemon.setImage(playerImage);
        } catch (Exception e) {
            System.err.println("Failed to load player bugemon sprite: " + e.getMessage());
        }

        String playerColor = getColor(playerBugemon.getType());
        PlayerBugemonLabel.setText(playerBugemon.getName());
        PlayerLevelLabel.setText("Lv." + playerBugemon.getLevel());
        PlayerBugemonLabel.setStyle("-fx-text-fill: " + playerColor + ";");
        double playerRatio = (double) playerBugemon.getHp() / playerBugemon.getBaseStats().getHp();
        PlayerBugemonHPBar.setProgress(playerRatio);
        updateHPBarColor(PlayerBugemonHPBar, playerRatio);
        PlayerBugemonHPNumber.setText(playerBugemon.getHp() + " / " + playerBugemon.getBaseStats().getHp());

        try {
            Image opponentImage = new Image(getClass().getResourceAsStream(opponentBugemon.getSprite()));
            OpponentBugemon.setImage(opponentImage);
        } catch (Exception e) {
            System.err.println("Failed to load opponent bugemon sprite: " + e.getMessage());
        }

        String opponentColor = getColor(opponentBugemon.getType());
        OpponentBugemonLabel.setText(opponentBugemon.getName());
        OpponentLevelLabel.setText("Lv." + opponentBugemon.getLevel());
        OpponentBugemonLabel.setStyle("-fx-text-fill: " + opponentColor + ";");
        double opponentRatio = (double) opponentBugemon.getHp() / opponentBugemon.getBaseStats().getHp();
        OpponentHPBar.setProgress(opponentRatio);
        updateHPBarColor(OpponentHPBar, opponentRatio);
        OpponentHPNumber.setText(opponentBugemon.getHp() + " / " + opponentBugemon.getBaseStats().getHp());
    }

    private static String getColor(Type type) {
        return switch (type) {
            case PYRO -> "#ED2424";
            case FLORA -> "#50A346";
            case AQUA -> "#51B0F0";
            case LITHO -> "#807979";
            default -> "#ced4da";
        };
    }

    private boolean isForcedSwitch() {
        return battleController != null && battleController.getState() == BattleState.SWAPPING;
    }

    private void updateBackButtonsState() {
        boolean disableBack = isForcedSwitch();
        setBackButtonDisabled(bugemonsView, disableBack);
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
            public ListCell<Item> call(ListView<Item> listView) {
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
                            Item item = getItem();
                            if (item != null) {
                                viewListener.onUseItem(item, event);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Item item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            try {
                                Image image = new Image(getClass().getResourceAsStream(item.getSprite()));
                                imageView.setImage(image);
                            } catch (Exception e) {
                                System.err.println("Failed to load item image: " + e.getMessage());
                            }
                            label.setText(item.getName() + " x" + playerInventory.getItems().get(item));
                            button.setDisable(battleController == null || !battleController.checkItem(item));
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
            public ListCell<Bugemon> call(ListView<Bugemon> listView) {
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
                            Bugemon bugemon = getItem();
                            if (bugemon != null) {
                                viewListener.onSwapBugemon(bugemon, event);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Bugemon bugemon, boolean empty) {
                        super.updateItem(bugemon, empty);
                        if (empty || bugemon == null || (battleController != null && bugemon.equals(battleController.getActiveBugemonSelf()))) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            try {
                                Image image = new Image(getClass().getResourceAsStream(bugemon.getSprite()));
                                imageView.setImage(image);
                            } catch (Exception e) {
                                System.err.println("Failed to load bugemon image: " + e.getMessage());
                            }
                            label.setText(bugemon.isKO() ? bugemon.getName() + " (KO)" : bugemon.getName());
                            button.setDisable(bugemon.isKO());
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
            public ListCell<Ability> call(ListView<Ability> listView) {
                return new ListCell<>() {
                    private final HBox hbox = new HBox(10);
                    private final Label label = new Label();
                    private final Button button = new Button("Utiliser");

                    {
                        hbox.getChildren().addAll(label, button);
                        button.setOnAction(event -> {
                            Ability ability = getItem();
                            if (ability != null) {
                                viewListener.onUseAbility(ability, event);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Ability ability, boolean empty) {
                        super.updateItem(ability, empty);
                        if (empty || ability == null) {
                            setText(null);
                            setGraphic(null);
                            setTooltip(null);
                        } else {
                            label.setText(ability.getName());
                            String color = getColor(ability.getType());

                            hbox.setStyle(
                                    "-fx-background-color: " + color + ";" +
                                            "-fx-padding: 6;" +
                                            "-fx-background-radius: 6;"
                            );

                            String effectiveness = battleController != null ? battleController.getEffectiveness(ability) : null;
                            if (effectiveness != null) {
                                Tooltip tooltip = new Tooltip(effectiveness);
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

    private void displayPhase(List<String> messages, int index, Runnable onComplete) {
        if (index >= messages.size()) {
            onComplete.run();
            return;
        }
        battleLog.setText(wrapText(messages.get(index), 35));

        if (containsSwitchMessage(messages)) {
            initializeGraphicalBattle();
        }
        if (battleController != null) {
            updateHPDisplay(battleController.getActiveBugemonSelf().getHp(), battleController.getActiveBugemonOpponent().getHp());
        }

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(2));
        pause.setOnFinished(event -> displayPhase(messages, index + 1, onComplete));
        pause.play();
    }

    private boolean containsSwitchMessage(List<String> messages) {
        for (String message : messages) {
            if (message != null && message.contains("envoyé")) {
                return true;
            }
        }
        return false;
    }

    private void updateHPDisplay(int selfHp, int opponentHp) {
        if (battleController == null) {
            return;
        }

        Bugemon self = battleController.getActiveBugemonSelf();
        Bugemon opponent = battleController.getActiveBugemonOpponent();
        double selfRatio = (double) selfHp / self.getBaseStats().getHp();
        double opponentRatio = (double) opponentHp / opponent.getBaseStats().getHp();

        PlayerBugemonHPBar.setProgress(selfRatio);
        PlayerBugemonHPNumber.setText(selfHp + " / " + self.getBaseStats().getHp());
        updateHPBarColor(PlayerBugemonHPBar, selfRatio);

        OpponentHPBar.setProgress(opponentRatio);
        OpponentHPNumber.setText(opponentHp + " / " + opponent.getBaseStats().getHp());
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

    public interface ViewListener {
        void onItemMenu();
        void onBugemonsMenu();
        void onAuto(ActionEvent event);
        void onAttack();
        void onBackToMenu();
        void onReturn(ActionEvent event);
        void onUseItem(Item item, ActionEvent event);
        void onSwapBugemon(Bugemon bugemon, ActionEvent event);
        void onUseAbility(Ability ability, ActionEvent event);
    }
}
