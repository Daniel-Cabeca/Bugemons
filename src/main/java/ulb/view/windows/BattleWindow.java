package ulb.view.windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;

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

    private BattleWindowSetupHelper setupHelper = new BattleWindowSetupHelper();
    private BattleWindowGraphicsHelper graphicsHelper = new BattleWindowGraphicsHelper();

    private ViewListener viewListener;
    private boolean autoMode;
    private boolean forcedSwitch;
    private BattleSnapshot currentSnapshot;

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    public void initializeContent() {
        setupInventoryList();
        setupBugemonsList();
        setupAbilitiesList();
    }

    public void initializeView(boolean autoMode) {
        this.autoMode = autoMode;
        attackButton.setDisable(autoMode);
        itemButton.setDisable(autoMode);
        switchButton.setDisable(autoMode);
        autoButton.setDisable(!autoMode);
        showMainMenu();
        graphicsHelper.linkUI(
                PlayerBugemonHPBar,
                OpponentHPBar,
                PlayerBugemonHPNumber,
                OpponentHPNumber,
                PlayerBugemon,
                OpponentBugemon,
                PlayerBugemonLabel,
                OpponentBugemonLabel,
                PlayerLevelLabel,
                OpponentLevelLabel,
                messageBox,
                battleLog
        );
        graphicsHelper.clearMessages();
    }

    public void setForcedSwitch(boolean forcedSwitch) {
        this.forcedSwitch = forcedSwitch;
        updateBackButtonsState();
    }

	public void setCurrentSnapshot(BattleSnapshot snapshot){
		this.currentSnapshot = snapshot;
	}

    public void renderBattle(BattleSnapshot snapshot) {
        setCurrentSnapshot(snapshot);
        graphicsHelper.renderBattle(snapshot);
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
        graphicsHelper.showLogMessages(logs);
    }

    public void displayMessagesSequentially(List<String> rawLogs,
                                            Integer hpAfterFirstActionSelf,
                                            Integer hpAfterFirstActionOpponent,
                                            BattleSnapshot finalSnapshot,
                                            Runnable onComplete) {
        hideAllMenus();
        graphicsHelper.displayMessagesSequentially(
                rawLogs,
                hpAfterFirstActionSelf,
                hpAfterFirstActionOpponent,
                finalSnapshot,
                () -> currentSnapshot,
                this::renderBattle,
                onComplete
        );
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
        setupHelper.setupInventoryList(inventoryList, viewListener);
    }

    private void setupBugemonsList() {
        setupHelper.setupBugemonsList(bugemonsList, viewListener);
    }

    private void setupAbilitiesList() {
        setupHelper.setupAbilitiesList(abilitiesList, viewListener);
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

    public record InventoryEntry(String itemId, String itemName, String itemDescription, String itemSpritePath, int quantity, boolean usable) {
    }

    public record BugemonEntry(String bugemonId, String bugemonName, String bugemonSpritePath,
                               boolean ko, boolean active, boolean selectable) {
    }

    public record AbilityEntry(String abilityId, String abilityName, String abilityDescription, String color, String effectiveness) {
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
