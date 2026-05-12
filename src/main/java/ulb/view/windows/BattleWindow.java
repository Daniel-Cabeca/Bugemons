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
import ulb.model.type.Type;

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

    /**
     * Sets the listener to be notified of battle view events.
     *
     * @param viewListener The view listener to be notified
     */
    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    /**
     * Initializes the list cells for the inventory, Bugemon, and ability lists.
     */
    public void initializeContent() {
        setupInventoryList();
        setupBugemonsList();
        setupAbilitiesList();
    }

    /**
     * Initializes the battle view for a new battle session.
     * Configures button states according to whether auto mode is active, links UI components
     * to the graphics helper, and clears any previous messages.
     *
     * @param autoMode {@code true} if the auto game mode is selected
     */
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

    /**
     * Sets whether the player is forced to switch their active Bugemon.
     * When {@code true}, the back button in the Bugemon menu is disabled
     * to prevent the player from returning without switching.
     *
     * @param forcedSwitch {@code true} to force the player to select a new Bugemon
     */
    public void setForcedSwitch(boolean forcedSwitch) {
        this.forcedSwitch = forcedSwitch;
        updateBackButtonsState();
    }

    /**
     * Sets the current battle snapshot to be used.
     *
     * @param snapshot The snapshot to be set
     */
    public void setCurrentSnapshot(BattleSnapshot snapshot) {
        this.currentSnapshot = snapshot;
    }

    /**
     * Renders the battle UI to reflect the given snapshot.
     * Updates HP bars, sprites, labels, and level indicators for both Bugemons.
     *
     * @param snapshot the battle state to display
     */
    public void renderBattle(BattleSnapshot snapshot) {
        setCurrentSnapshot(snapshot);
        graphicsHelper.renderBattle(snapshot);
    }

    /**
     * Handles the item menu button click and notifies the view listener.
     */
    @FXML
    public void handleItemMenu() {
        if (viewListener != null) {
            viewListener.onItemMenu();
        }
    }

    /**
     * Handles the Bugemon menu button click and notifies the view listener.
     */
    @FXML
    public void handleBugemonsMenu() {
        if (viewListener != null) {
            viewListener.onBugemonsMenu();
        }
    }

    /**
     * Handles the auto button click and notifies the view listener.
     *
     * @param event The action event
     */
    @FXML
    public void handleAuto(ActionEvent event) {
        if (viewListener != null) {
            viewListener.onAuto(event);
        }
    }

    /**
     * Handles the attack button click and notifies the view listener.
     */
    @FXML
    public void handleAttack() {
        if (viewListener != null) {
            viewListener.onAttack();
        }
    }

    /**
     * Handles the back to menu button click and notifies the view listener.
     */
    @FXML
    public void handleBackToMenu() {
        if (viewListener != null) {
            viewListener.onBackToMenu();
        }
    }

    /**
     * Handles the return/flee button click and notifies the view listener.
     *
     * @param event The action event
     */
    @FXML
    public void handleReturn(ActionEvent event) {
        if (viewListener != null) {
            viewListener.onReturn(event);
        }
    }

    /**
     * Handles the social panel button click and notifies the view listener.
     */
    @FXML
    public void openSocialPanel() {
        if (viewListener != null) {
            viewListener.onOpenSocial();
        }
    }

    /**
     * Switches the view to the inventory menu and populates it with the given entries.
     * Hides the main button grid and all other sub-menus.
     *
     * @param inventoryEntries the list of items to display
     */
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

    /**
     * Switches the view to the Bugemon selection menu and populates it with the given entries.
     * Hides the main button grid and all other sub-menus.
     *
     * @param bugemonEntries the list of Bugemons to display
     */
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

    /**
     * Switches the view to the abilities menu and populates it with the given entries.
     * Hides the main button grid and all other sub-menus.
     *
     * @param abilityEntries the list of abilities to display
     */
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

    /**
     * Switches the view back to the main battle menu (Attack / Item / Switch / Run buttons).
     * Hides all sub-menus.
     */
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

    /**
     * Sets the visibility of the auto button.
     *
     * @param visible {@code true} to show the button, {@code false} to hide it
     */
    public void setAutoButtonVisible(boolean visible) {
        autoButton.setVisible(visible);
        autoButton.setManaged(visible);
    }

    /**
     * Enables or disables all battle input controls.
     * The run button is always toggled; other buttons depend on autoMode.
     *
     * @param disabled {@code true} to disable inputs, {@code false} to re-enable them
     */
    public void setBattleInputsDisabled(boolean disabled) {
        attackButton.setDisable(disabled || autoMode);
        itemButton.setDisable(disabled || autoMode);
        runButton.setDisable(disabled);
        switchButton.setDisable(disabled || autoMode);

        inventoryView.setDisable(disabled);
        bugemonsView.setDisable(disabled);
        abilitiesView.setDisable(disabled);
    }

    /**
     * Displays the given log messages in battle.
     *
     * @param logs The list of messages to display
     */
    public void showLogMessages(List<String> logs) {
        graphicsHelper.showLogMessages(logs);
    }

    /**
     * Displays battle log messages one by one with animated transitions.
     * HP bars are updated after the first action, and the final snapshot is applied once all messages
     * have been shown.
     *
     * @param rawLogs the ordered list of log messages to animate
     * @param hpAfterFirstActionSelf the player Bugemon's HP after the first action
     * @param hpAfterFirstActionOpponent the opponent Bugemon's HP after the first action
     * @param finalSnapshot the battle state to apply after all messages are displayed
     * @param onComplete callback invoked once the full animation sequence is complete
     */
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

    /**
     * Updates the disabled state of back buttons within sub-menus based on whether a forced switch
     * is currently active.
     */
    private void updateBackButtonsState() {
        setBackButtonDisabled(bugemonsView, forcedSwitch);
    }

    /**
     * Disables or enables all back buttons within the given VBox.
     *
     * @param view the container whose buttons should be toggled
     * @param disabled {@code true} to disable the buttons, {@code false} to enable them
     */
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

    /**
     * Configures the inventory list cell factory.
     */
    private void setupInventoryList() {
        setupHelper.setupInventoryList(inventoryList, viewListener);
    }

    /**
     * Configures the Bugemon list cell factory.
     */
    private void setupBugemonsList() {
        setupHelper.setupBugemonsList(bugemonsList, viewListener);
    }

    /**
     * Configures the abilities list cell factory.
     */
    private void setupAbilitiesList() {
        setupHelper.setupAbilitiesList(abilitiesList, viewListener);
    }

    /**
     * Hides and unmanages all menus and sub-menus (used during message animations).
     */
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

    /**
     * Updates the floor and room labels to show player's current position in the tower.
     *
     * @param floorNumber the current floor number
     * @param roomIndex the current room number on that floor
     */
    public void setTowerInfo(int floorNumber, int roomIndex) {
        floorLabel.setText("Etage: NO" + floorNumber);
        roomLabel.setText("Salle: " + roomIndex);
    }

    /**
     * Clears the floor and room labels.
     */
    public void clearTowerInfo() {
        floorLabel.setText("");
        roomLabel.setText("");
    }

    /**
     * Immutable snapshot of the current battle state for both the player and opponent Bugemons.
     *
     * @param playerBugemon display data for the player's active Bugemon
     * @param opponentBugemon display data for the opponent's active Bugemon
     */
    public record BattleSnapshot(BugemonDisplay playerBugemon, BugemonDisplay opponentBugemon) {
    }

    /**
     * Display data for a single Bugemon as shown in the battle window.
     *
     * @param name the Bugemon's name
     * @param spritePath path to the Bugemon's sprite image resource
     * @param bugemonType the Bugemon's type
     * @param level the Bugemon's current level
     * @param hp the Bugemon's current HP
     * @param maxHp the Bugemon's maximum HP
     */
    public record BugemonDisplay(String name, String spritePath, Type bugemonType, int level, int hp, int maxHp) {
    }

    /**
     * Represents a single item entry in the battle inventory list.
     *
     * @param itemId the unique identifier of the item
     * @param itemName the display name of the item
     * @param itemDescription a short description of the item's effect
     * @param itemSpritePath path to the item's sprite image resource
     * @param quantity the number of this item the player currently holds
     * @param usable {@code true} if the item can be used in the current context
     */
    public record InventoryEntry(String itemId, String itemName, String itemDescription, String itemSpritePath, int quantity, boolean usable) {
    }

    /**
     * Represents a single Bugemon entry in the Bugemon selection list.
     *
     * @param bugemonId the unique identifier of the Bugemon
     * @param bugemonName the display name of the Bugemon
     * @param bugemonSpritePath path to the Bugemon's sprite image resource
     * @param ko {@code true} if the Bugemon is KO and cannot be selected
     * @param active {@code true} if this Bugemon is currently in battle
     * @param selectable {@code true} if the player may switch to this Bugemon
     */
    public record BugemonEntry(String bugemonId, String bugemonName, String bugemonSpritePath,
                               boolean ko, boolean active, boolean selectable) {
    }

    /**
     * Represents a single ability entry in the ability selection list.
     *
     * @param abilityId the unique identifier of the ability
     * @param abilityName the display name of the ability
     * @param abilityDescription a short description of what the ability does
     * @param abilityType the ability's type
     * @param effectiveness a label describing the ability's effectiveness against the current opponent
     */
    public record AbilityEntry(String abilityId, String abilityName, String abilityDescription, Type abilityType, String effectiveness) {
    }

    /**
     * Listener for battle view events triggered by user interaction.
     */
    public interface ViewListener {
        /** Handles the item menu being opened. */
        void onItemMenu();
        /** Handles the Bugemon menu being opened. */
        void onBugemonsMenu();
        /** Handles the auto battle button being pressed. */
        void onAuto(ActionEvent event);
        /** Handles the attack button being pressed. */
        void onAttack();
        /** Handles the back to menu button being pressed. */
        void onBackToMenu();
        /** Handles the return/flee button being pressed. */
        void onReturn(ActionEvent event);
        /** Handles the social panel being opened. */
        void onOpenSocial();
        /** Handles an item being selected for use. */
        void onUseItem(String itemId, ActionEvent event);
        /** Handles a Bugemon being selected for a swap. */
        void onSwapBugemon(String bugemonId, ActionEvent event);
        /** Handles an ability being selected for use. */
        void onUseAbility(String abilityId, ActionEvent event);
    }
}