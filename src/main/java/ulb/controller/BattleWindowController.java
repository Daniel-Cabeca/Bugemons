package ulb.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.communication.types.GameMode;
import ulb.model.Player;
import ulb.model.ability.Ability;
import ulb.model.battle.BattleState;
import ulb.model.bugemon.Bugemon;
import ulb.model.item.Inventory;
import ulb.model.item.Item;
import ulb.model.team.Team;
import ulb.model.type.Type;
import ulb.view.WindowPath;
import ulb.view.windows.BattleWindow;
import ulb.view.windows.BattleWindow.AbilityEntry;
import ulb.view.windows.BattleWindow.BattleSnapshot;
import ulb.view.windows.BattleWindow.BugemonDisplay;
import ulb.view.windows.BattleWindow.BugemonEntry;
import ulb.view.windows.BattleWindow.InventoryEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller coordinating the battle view and player battle actions.
 */
public class BattleWindowController implements BattleWindow.ViewListener {

    private final Stage stage;
    private final Listener listener;
    private final Player player;
    private final BattleController battleController;
    private final GameMode gameMode;
    private final int towerFloorNumber;
    private final int currentRoomIndex;

    private BattleWindow view;
    private boolean waitingForOpponentAction = false;

    /**
     * Creates the battle window controller.
     *
     * @param stage The application stage
     * @param listener The listener handling battle actions
     * @param player The current player
     * @param battleController The battle domain controller
     * @param gameMode The current game mode
     * @param towerFloorNumber The current tower floor number
     * @param currentRoomIndex The current room index in the floor
     */
    public BattleWindowController(Stage stage, Listener listener, Player player, BattleController battleController,
                                  GameMode gameMode, int towerFloorNumber, int currentRoomIndex) {
        this.stage = stage;
        this.listener = listener;
        this.player = player;
        this.battleController = battleController;
        this.gameMode = gameMode;
        this.towerFloorNumber = towerFloorNumber;
        this.currentRoomIndex = currentRoomIndex;
    }

    /**
     * Displays the battle window and initializes its content.
     *
     * @throws Exception If the FXML cannot be loaded
     */
    public void show() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.BATTLE));
        loader.load();
        view = loader.getController();
        view.setViewListener(this);
        view.initializeContent();

        view.initializeView(gameMode == GameMode.AUTO);
        refreshView();
        updateTowerInfo();

        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        stage.show();
    }

    /**
     * Opens the inventory menu.
     */
    @Override
    public void onItemMenu() {
        view.showInventoryMenu(buildInventoryEntries());
    }

    /**
     * Opens the bugemon switch menu.
     */
    @Override
    public void onBugemonsMenu() {
        view.showBugemonsMenu(buildBugemonEntries());
    }

    /**
     * Executes an automatic turn.
     *
     * @param event The triggering action event
     */
    @Override
    public void onAuto(ActionEvent event) {
        if (view == null) {
            return;
        }

        view.setAutoButtonVisible(false);
        BattleState stateAfter = stateOrCurrent(listener.onAutoTurn());
        displayActionSequence(stateAfter, event, () -> {
            view.showMainMenu();
            view.setAutoButtonVisible(true);
        });
    }

    /**
     * Opens the abilities menu.
     */
    @Override
    public void onAttack() {
        view.showAbilitiesMenu(buildAbilityEntries());
    }

    /**
     * Returns to the battle main menu.
     */
    @Override
    public void onBackToMenu() {
        view.showMainMenu();
    }

    /**
     * Handles return/flee action from the battle window.
     *
     * @param event The triggering action event
     */
    @Override
    public void onReturn(ActionEvent event) {
        if (gameMode == GameMode.TOWER) {
            listener.onTowerFlee();
        } else {
            listener.onReturnToMode();
        }
    }

    /**
     * Uses an inventory item and displays resulting battle logs.
     *
     * @param itemId The selected item id
     * @param event The triggering action event
     */
    @Override
    public void onUseItem(String itemId, ActionEvent event) {
        Item item = findInventoryItemById(itemId);
        BattleState stateAfter = stateOrCurrent(listener.onUseItem(item));
        displayActionSequence(stateAfter, event, () -> {
            if (stateAfter == BattleState.WAITING || stateAfter == BattleState.INGAME) {
                view.showMainMenu();
            } else {
                view.showInventoryMenu(buildInventoryEntries());
            }
        });
    }

    /**
     * Swaps active bugemon and displays resulting battle logs.
     *
     * @param bugemonId The selected bugemon id
     * @param event The triggering action event
     */
    @Override
    public void onSwapBugemon(String bugemonId, ActionEvent event) {
        Bugemon bugemon = findTeamBugemonById(bugemonId);
        BattleState stateAfter = stateOrCurrent(listener.onSwapBugemon(bugemon));
        displayActionSequence(stateAfter, event, () -> {
            if (stateAfter == BattleState.WAITING || stateAfter == BattleState.INGAME) {
                view.showMainMenu();
                // reset the auto button after forced swap
                if (gameMode == GameMode.AUTO) {
                    view.setAutoButtonVisible(true);
                }
            } else {
                view.showBugemonsMenu(buildBugemonEntries());
            }
        });
    }

    /**
     * Uses an ability and displays resulting battle logs.
     *
     * @param abilityId The selected ability id
     * @param event The triggering action event
     */
    @Override
    public void onUseAbility(String abilityId, ActionEvent event) {
        Ability ability = findActiveAbilityById(abilityId);
        BattleState stateAfter = stateOrCurrent(listener.onUseAbility(ability));
        displayActionSequence(stateAfter, event, () -> {
            if (stateAfter == BattleState.WAITING || stateAfter == BattleState.INGAME) {
                view.showMainMenu();
            }
        });
    }

    /**
     * Refreshes battle UI content from current battle state.
     */
    private void refreshView() {
        if (view == null) {
            return;
        }

        view.setForcedSwitch(isForcedSwitch());
        BattleSnapshot snapshot = buildBattleSnapshot();
        if (snapshot != null) {
            view.renderBattle(snapshot);
        }
        view.showLogMessages(consumeLogMessages());
    }

    /**
     * Updates tower information banner when in tower mode.
     */
    private void updateTowerInfo() {
        if (gameMode == GameMode.TOWER) {
            view.setTowerInfo(towerFloorNumber, currentRoomIndex);
        } else {
            view.clearTowerInfo();
        }
    }

    /**
     * Displays action logs sequentially, then handles resulting state changes.
     *
     * @param stateAfter The battle state after the selected action
     * @param event The triggering action event
     * @param afterDisplay Callback executed when no state-specific transition occurs
     */
    private void displayActionSequence(BattleState stateAfter, ActionEvent event, Runnable afterDisplay) {
        List<String> logs = consumeLogMessages();
        Integer firstActionSelfHp = logs.contains(null) && battleController != null
                ? battleController.getHpAfterFirstActionSelf()
                : null;
        Integer firstActionOpponentHp = logs.contains(null) && battleController != null
                ? battleController.getHpAfterFirstActionOpponent()
                : null;
        BattleSnapshot finalSnapshot = buildBattleSnapshot();

        view.displayMessagesSequentially(logs, firstActionSelfHp, firstActionOpponentHp, finalSnapshot, () -> {
            refreshView();
            boolean stateHandled = handleBattleState(stateAfter, event);
            if (!stateHandled) {
                afterDisplay.run();
            }
            if (stateAfter == BattleState.WAITING) {
                waitingForOpponentAction(event);
            }
        });
    }

    /**
     * Returns the provided state or the current controller state when null.
     *
     * @param state The candidate state
     * @return The resolved battle state
     */
    private BattleState stateOrCurrent(BattleState state) {
        if (state != null) {
            return state;
        }
        return battleController != null ? battleController.getState() : null;
    }

    /**
     * Waits asynchronously for the opponent action to complete.
     *
     * @param event The action event tied to the current interaction
     */
    private void waitingForOpponentAction(ActionEvent event) {
        if (waitingForOpponentAction || battleController == null) {
            return;
        }

        waitingForOpponentAction = true;
        view.setBattleInputsDisabled(true);

        Thread waitingThread = new Thread(() -> {
            try {
                while (battleController.getState() == BattleState.WAITING && !battleController.isGameFinished()) {
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Platform.runLater(() -> {
                waitingForOpponentAction = false;
                view.setBattleInputsDisabled(false);
                refreshView();

                BattleState currentState = battleController.getState();
                boolean stateHandled = handleBattleState(currentState, event);
                if (!stateHandled && currentState == BattleState.INGAME) {
                    view.showMainMenu();
                }
            });
        });

        waitingThread.setDaemon(true);
        waitingThread.start();
    }

    /**
     * Applies UI transitions for a given battle state.
     *
     * @param state The battle state to handle
     * @param event The triggering action event
     * @return True if the state transition was handled
     */
    private boolean handleBattleState(BattleState state, ActionEvent event) {
        listener.onBattleStateChecked(state, event);
        view.setForcedSwitch(state == BattleState.SWAPPING);
        if (state == BattleState.SWAPPING) {
            view.showMainMenu();
            view.showBugemonsMenu(buildBugemonEntries());
            return true;
        }
        return state == BattleState.WON || state == BattleState.LOST;
    }

    /**
     * Indicates whether the player is currently forced to switch bugemon.
     *
     * @return True if switch is mandatory
     */
    private boolean isForcedSwitch() {
        return battleController != null && battleController.getState() == BattleState.SWAPPING;
    }

    /**
     * Builds a snapshot model for rendering the battle view.
     *
     * @return The battle snapshot, or null if unavailable
     */
    private BattleSnapshot buildBattleSnapshot() {
        if (battleController == null) {
            return null;
        }

        Bugemon playerBugemon = battleController.getActiveBugemonSelf();
        Bugemon opponentBugemon = battleController.getActiveBugemonOpponent();
        if (playerBugemon == null || opponentBugemon == null) {
            return null;
        }

        return new BattleSnapshot(toBugemonDisplay(playerBugemon), toBugemonDisplay(opponentBugemon));
    }

    /**
     * Converts a bugemon model into a UI display object.
     *
     * @param bugemon The bugemon to convert
     * @return The corresponding display object
     */
    private BugemonDisplay toBugemonDisplay(Bugemon bugemon) {
        return new BugemonDisplay(
                bugemon.getName(),
                bugemon.getSprite(),
                getTypeColor(bugemon.getType()),
                bugemon.getLevel(),
                bugemon.getHp(),
                bugemon.getBaseStats().getHp()
        );
    }

    /**
     * Retrieves and clears accumulated battle log messages.
     *
     * @return The current list of log messages
     */
    private List<String> consumeLogMessages() {
        if (battleController == null) {
            return List.of();
        }

        List<String> logs = new ArrayList<>(battleController.getLogMsg());
        battleController.clearLogMsg();
        return logs;
    }

    /**
     * Builds inventory entries for the inventory menu.
     *
     * @return The list of inventory entries
     */
    private List<InventoryEntry> buildInventoryEntries() {
        Inventory inventory = getPlayerInventory();
        if (inventory == null) {
            return List.of();
        }

        List<InventoryEntry> entries = new ArrayList<>();
        for (Map.Entry<Item, Integer> entry : inventory.getItems().entrySet()) {
            Item item = entry.getKey();
            boolean usable = battleController != null && battleController.checkItem(item);
            entries.add(new InventoryEntry(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getSprite(),
                    entry.getValue(),
                    usable
            ));
        }
        return entries;
    }

    /**
     * Builds bugemon entries for the bugemon selection menu.
     *
     * @return The list of bugemon entries
     */
    private List<BugemonEntry> buildBugemonEntries() {
        Team playerTeam = getPlayerTeam();
        if (playerTeam == null) {
            return List.of();
        }

        Bugemon activeBugemon = battleController != null ? battleController.getActiveBugemonSelf() : null;
        List<BugemonEntry> entries = new ArrayList<>();
        for (Bugemon bugemon : playerTeam.getMembers()) {
            boolean active = bugemon.equals(activeBugemon);
            boolean selectable = !active && !bugemon.isKO();
            entries.add(new BugemonEntry(
                    bugemon.getId(),
                    bugemon.getName(),
                    bugemon.getSprite(),
                    bugemon.isKO(),
                    active,
                    selectable
            ));
        }
        return entries;
    }

    /**
     * Builds ability entries for the abilities menu.
     *
     * @return The list of ability entries
     */
    private List<AbilityEntry> buildAbilityEntries() {
        if (battleController == null || battleController.getActiveBugemonSelf() == null) {
            return List.of();
        }

        List<AbilityEntry> entries = new ArrayList<>();
        for (Ability ability : battleController.getActiveBugemonSelf().getAbilities()) {
            if (ability != null) {
                entries.add(new AbilityEntry(
                        ability.getId(),
                        ability.getName(),
                        ability.getAccurateDescription(),
                        getTypeColor(ability.getType()),
                        battleController.getEffectiveness(ability)
                ));
            }
        }
        return entries;
    }

    /**
     * Finds an inventory item by its identifier.
     *
     * @param itemId The item identifier
     * @return The matching item, or null if not found
     */
    private Item findInventoryItemById(String itemId) {
        if (itemId == null) {
            return null;
        }

        Inventory inventory = getPlayerInventory();
        if (inventory == null) {
            return null;
        }

        for (Item item : inventory.getItems().keySet()) {
            if (itemId.equals(item.getId())) {
                return item;
            }
        }
        return null;
    }

    /**
     * Finds a player team bugemon by its identifier.
     *
     * @param bugemonId The bugemon identifier
     * @return The matching bugemon, or null if not found
     */
    private Bugemon findTeamBugemonById(String bugemonId) {
        if (bugemonId == null) {
            return null;
        }

        Team team = getPlayerTeam();
        if (team == null) {
            return null;
        }

        for (Bugemon bugemon : team.getMembers()) {
            if (bugemonId.equals(bugemon.getId())) {
                return bugemon;
            }
        }
        return null;
    }

    /**
     * Finds an active bugemon ability by its identifier.
     *
     * @param abilityId The ability identifier
     * @return The matching ability, or null if not found
     */
    private Ability findActiveAbilityById(String abilityId) {
        if (abilityId == null || battleController == null || battleController.getActiveBugemonSelf() == null) {
            return null;
        }

        for (Ability ability : battleController.getActiveBugemonSelf().getAbilities()) {
            if (ability != null && abilityId.equals(ability.getId())) {
                return ability;
            }
        }
        return null;
    }

    /**
     * Returns the current player's team.
     *
     * @return The player team, or null when unavailable
     */
    private Team getPlayerTeam() {
        return player != null ? player.getTeam() : null;
    }

    /**
     * Returns the current player's inventory.
     *
     * @return The player inventory, or null when unavailable
     */
    private Inventory getPlayerInventory() {
        return player != null ? player.getInventory() : null;
    }

    /**
     * Maps a bugemon type to a UI color code.
     *
     * @param type The bugemon type
     * @return The corresponding hex color string
     */
    private String getTypeColor(Type type) {
        return switch (type) {
            case PYRO -> "#ED2424";
            case FLORA -> "#50A346";
            case AQUA -> "#51B0F0";
            case LITHO -> "#807979";
            default -> "#ced4da";
        };
    }

    /**
     * Listener for battle actions requested from the view.
     */
    public interface Listener {
        /**
         * Executes an automatic turn.
         *
         * @return The resulting battle state
         */
        BattleState onAutoTurn();
        /**
         * Uses an item in the current battle turn.
         *
         * @param item The item to use
         * @return The resulting battle state
         */
        BattleState onUseItem(Item item);
        /**
         * Swaps the player's active bugemon.
         *
         * @param bugemon The bugemon to switch to
         * @return The resulting battle state
         */
        BattleState onSwapBugemon(Bugemon bugemon);
        /**
         * Uses an ability in the current battle turn.
         *
         * @param ability The ability to use
         * @return The resulting battle state
         */
        BattleState onUseAbility(Ability ability);
        /**
         * Handles state-driven navigation or transitions.
         *
         * @param state The state to handle
         * @param event The related UI event
         */
        void onBattleStateChecked(BattleState state, ActionEvent event);
        /** Handles fleeing from tower mode battle. */
        void onTowerFlee();
        /** Handles return action outside tower mode. */
        void onReturnToMode();
    }
}
