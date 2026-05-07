package ulb.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.communication.GameMode;
import ulb.model.battle.BattleState;
import ulb.model.type.Type;
import ulb.view.FxmlLoader;
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
    private final PlayerDTO player;
    private final GameMode gameMode;
    private final int towerFloorNumber;
    private final int towerRoomNumber;

    private BattleWindow view;
    private boolean waitingForOpponentAction = false;

    /**
     * Creates the battle window controller.
     *
     * @param stage The application stage
     * @param listener The listener notified of battle actions
     * @param player The current player data
     * @param gameMode The active game mode
     * @param towerFloorNumber The current tower floor number
     * @param towerRoomNumber The current tower room number
     */
    public BattleWindowController(Stage stage, Listener listener, PlayerDTO player, GameMode gameMode, int towerFloorNumber, int towerRoomNumber){
        this.stage = stage;
        this.listener = listener;
        this.player = player;
        this.gameMode = gameMode;
        this.towerFloorNumber = towerFloorNumber;
        this.towerRoomNumber = towerRoomNumber;
    }

    /**
     * Displays the battle window and initializes its content.
     */
    public void show() {
        FXMLLoader loader = FxmlLoader.load(this, WindowPath.BATTLE);
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
     * {@inheritDoc}
     */
    @Override
    public void onItemMenu() {
        view.showInventoryMenu(buildInventoryEntries());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBugemonsMenu() {
        view.showBugemonsMenu(buildBugemonEntries());
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public void onAttack() {
        view.showAbilitiesMenu(buildAbilityEntries());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackToMenu() {
        view.showMainMenu();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReturn(ActionEvent event) {
        listener.onRun();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onOpenSocial() {
        listener.onOpenSocial();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUseItem(String itemId, ActionEvent event) {
        ItemDTO item = findInventoryItemById(itemId);
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
     * {@inheritDoc}
     */
    @Override
    public void onSwapBugemon(String bugemonId, ActionEvent event) {
        BugemonDTO bugemon = findTeamBugemonById(bugemonId);
        BattleSnapshot snapshotBeforeAction = buildBattleSnapshot();
        if (snapshotBeforeAction != null) {
            view.setCurrentSnapshot(snapshotBeforeAction);
        }

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
     * {@inheritDoc}
     */
    @Override
    public void onUseAbility(String abilityId, ActionEvent event) {
        AbilityDTO ability = findActiveAbilityById(abilityId);
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
            view.setTowerInfo(towerFloorNumber, towerRoomNumber);
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

        List<Integer> hpAfterFirstAction = listener.getHpAfterFirstAction();
        Integer firstActionSelfHp = null, firstActionOpponentHp = null;

        if (logs.contains(null) && hpAfterFirstAction.get(0) != null && hpAfterFirstAction.get(1) != null){
            firstActionSelfHp = hpAfterFirstAction.get(0);
            firstActionOpponentHp = hpAfterFirstAction.get(1);
        }

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
        return listener.getState();
    }

    /**
     * Waits asynchronously for the opponent action to complete.
     *
     * @param event The action event tied to the current interaction
     */
    private void waitingForOpponentAction(ActionEvent event) {
        if (waitingForOpponentAction) {
            return;
        }

        waitingForOpponentAction = true;
        view.setBattleInputsDisabled(true);

        Thread waitingThread = new Thread(() -> {
            try {
                while (listener.getState() == BattleState.WAITING && !listener.isGameFinished()) {
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Platform.runLater(() -> {
                waitingForOpponentAction = false;
                view.setBattleInputsDisabled(false);

                BattleState currentState = listener.getState();
                displayActionSequence(currentState, event, () -> {
                    if (currentState == BattleState.INGAME) {
                        view.showMainMenu();
                    }
                });
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
        return listener.getState() == BattleState.SWAPPING;
    }

    /**
     * Builds a snapshot model for rendering the battle view.
     *
     * @return The battle snapshot, or null if unavailable
     */
    private BattleSnapshot buildBattleSnapshot() {
        List<BugemonDTO> activeBugemons = this.listener.getActiveBugemons();
        BugemonDTO playerBugemon = activeBugemons.get(0);
        BugemonDTO opponentBugemon = activeBugemons.get(1);
        if (playerBugemon == null || opponentBugemon == null) {
            return null;
        }

        return new BattleSnapshot(toBugemonDisplay(playerBugemon), toBugemonDisplay(opponentBugemon));
    }

    /**
     * Converts a bugemon DTO into a display model for the battle view.
     *
     * @param bugemon The bugemon to convert
     * @return The corresponding DTO
     */
    private BugemonDisplay toBugemonDisplay(BugemonDTO bugemon) {
        return new BugemonDisplay(
                bugemon.getName(),
                bugemon.getSpritePath(),
                bugemon.getType(),
                bugemon.getLevel(),
                bugemon.getHp(),
                bugemon.getBaseStats().hp()
        );
    }

    /**
     * Retrieves and clears accumulated battle log messages.
     *
     * @return The current list of log messages
     */
    private List<String> consumeLogMessages() {
        return listener.getLogs();
    }

    /**
     * Builds inventory entries for the inventory menu.
     *
     * @return The list of inventory entries
     */
    private List<InventoryEntry> buildInventoryEntries() {
        listener.updatePlayerInventory(this.player.getUsername());
        Map<ItemDTO, Integer> inventory = getPlayerInventory();
        if (inventory == null) {
            return List.of();
        }

        List<InventoryEntry> entries = new ArrayList<>();
        Map<String, Boolean> itemMap = listener.checkItems(new ArrayList<ItemDTO>(inventory.keySet()));

        for (Map.Entry<ItemDTO, Integer> entry : inventory.entrySet()) {
            ItemDTO item = entry.getKey();
            boolean usable = itemMap.get(item.id());
            entries.add(new InventoryEntry(
                    item.id(),
                    item.name(),
                    item.description(),
                    item.sprite(),
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
        List<BugemonDTO> playerTeam = listener.getPlayerTeam();
        if (playerTeam == null) {
            return List.of();
        }

        BugemonDTO activeBugemon = listener.getActiveBugemons().get(0);
        List<BugemonEntry> entries = new ArrayList<>();
        for (BugemonDTO bugemon : playerTeam) {
            boolean active = bugemon.getId().equals(activeBugemon.getId());
            boolean selectable = !active && bugemon.getHp() > 0;
            entries.add(new BugemonEntry(
                    bugemon.getId(),
                    bugemon.getName(),
                    bugemon.getSpritePath(),
                    bugemon.getHp() <= 0,
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
        List<BugemonDTO> activeBugemons = listener.getActiveBugemons();
        BugemonDTO ownActiveBugemon = activeBugemons.get(0);
        BugemonDTO opponentActiveBugemon = activeBugemons.get(1);
        if (ownActiveBugemon == null){
            return List.of();
        }

        List<AbilityEntry> entries = new ArrayList<>();
        Map<AbilityDTO, String> abilitiesEffectiveness = listener.getAbilityEffectiveness(ownActiveBugemon.getAbilities(), opponentActiveBugemon);

        for (AbilityDTO ability : ownActiveBugemon.getAbilities()) {
            if (ability != null) {
                // Cannot use map.get(ability) because DTO instances are different after serialization.
                // the abilities are matched using their ID instead of object reference
                String effectiveness = null;
                if (abilitiesEffectiveness != null) {
                    for (Map.Entry<AbilityDTO, String> entry : abilitiesEffectiveness.entrySet()) {
                        if (entry.getKey() != null && entry.getKey().id().equals(ability.id())) {
                            effectiveness = entry.getValue();
                            break;
                        }
                    }
                }

                entries.add(new AbilityEntry(
                        ability.id(),
                        ability.name(),
                        ability.description() + "\n Puissance:  " + ability.power(),
                        ability.type(),
                        effectiveness
                ));
            }
        }
        return entries;
    }

    /**
     * Finds an inventory item by its id.
     *
     * @param itemId The id of the item to find
     * @return The matching item DTO, or null if not found
     */
    private ItemDTO findInventoryItemById(String itemId) {
        if (itemId == null) {
            return null;
        }

        Map<ItemDTO, Integer> inventory = getPlayerInventory();
        if (inventory == null) {
            return null;
        }

        for (ItemDTO item : inventory.keySet()) {
            if (itemId.equals(item.id())) {
                return item;
            }
        }
        return null;
    }

    /**
     * Finds a Bugemon by its id.
     *
     * @param bugemonId The id of the Bugemon to find
     * @return The matching Bugemon DTO, or null if not found
     */
    private BugemonDTO findTeamBugemonById(String bugemonId) {
        if (bugemonId == null) {
            return null;
        }

        List<BugemonDTO> team = getPlayerTeam();
        if (team == null) {
            return null;
        }

        for (BugemonDTO bugemon : team) {
            if (bugemonId.equals(bugemon.getId())) {
                return bugemon;
            }
        }
        return null;
    }

    /**
     * Finds the active Bugemon's ability by its id.
     *
     * @param abilityId The id of the ability to find
     * @return The matching ability DTO, or null if not found
     */
    private AbilityDTO findActiveAbilityById(String abilityId) {
        BugemonDTO activeBugemon = listener.getActiveBugemons().get(0);
        if (activeBugemon == null){
            return null;
        }

        for (AbilityDTO ability : activeBugemon.getAbilities()) {
            if (ability != null && abilityId.equals(ability.id())) {
                return ability;
            }
        }
        return null;
    }

    /**
     * Returns the player's team, or null if no player is set.
     *
     * @return The list of bugemons in the player's team
     */
    private List<BugemonDTO> getPlayerTeam() {
        return player != null ? player.getTeam() : null;
    }

    /**
     * Returns the player's inventory, or null if no player is set.
     *
     * @return The player's inventory
     */
    private Map<ItemDTO, Integer> getPlayerInventory() {
        return player != null ? player.getInventory() : null;
    }

    /**
     * Listener for battle actions requested from the view.
     */
    public interface Listener {
        /** Updates the player's inventory for the given username. */
        void updatePlayerInventory(String userName);
        /** Returns the currently active bugemons in the battle. */
        List<BugemonDTO> getActiveBugemons();
        /** Returns the player's team. */
        List<BugemonDTO> getPlayerTeam();
        /** Returns the effectiveness of each ability against the given opponent bugemon. */
        Map<AbilityDTO, String> getAbilityEffectiveness(List<AbilityDTO> ability, BugemonDTO bugemon);
        /** Returns the HP values of both sides after the first action of the turn. */
        List<Integer> getHpAfterFirstAction();
        /** Returns the current battle state. */
        BattleState getState();
        /** Returns the accumulated battle log messages. */
        List<String> getLogs();
        /** Checks which items in the list are currently usable. */
        Map<String, Boolean> checkItems(List<ItemDTO> items);
        /** Returns whether the game has finished. */
        boolean isGameFinished();
        /** Executes an automatic turn and returns the resulting battle state. */
        BattleState onAutoTurn();
        /** Uses the given item and returns the resulting battle state. */
        BattleState onUseItem(ItemDTO item);
        /** Swaps the active bugemon and returns the resulting battle state. */
        BattleState onSwapBugemon(BugemonDTO bugemon);
        /** Uses the given ability and returns the resulting battle state. */
        BattleState onUseAbility(AbilityDTO ability);
        /** Handles the player fleeing the battle. */
        void onRun();
        /** Handles opening the social menu. */
        void onOpenSocial();
        /** Handles the logic after a battle state check. */
        void onBattleStateChecked(BattleState state, ActionEvent event);
    }
}