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
import ulb.communication.types.GameMode;
import ulb.model.battle.BattleState;
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

public class BattleWindowController implements BattleWindow.ViewListener {

    private final Stage stage;
    private final Listener listener;
    private final PlayerDTO player;
    private final GameMode gameMode;
    private final int towerFloorNumber;
    private final int towerRoomNumber;

    private BattleWindow view;
    private boolean waitingForOpponentAction = false;

    public BattleWindowController(Stage stage, Listener listener, PlayerDTO player, GameMode gameMode, int towerFloorNumber, int towerRoomNumber){
        this.stage = stage;
        this.listener = listener;
        this.player = player;
        this.gameMode = gameMode;
        this.towerFloorNumber = towerFloorNumber;
        this.towerRoomNumber = towerRoomNumber;
    }

    // public BattleWindowController(Stage stage, Listener listener, Player player, BattleController battleController,
                                //   GameMode gameMode, int towerFloorNumber, int towerRoomNumber) {
        // this.stage = stage;
        // this.listener = listener;
        // this.player = player;
        // this.battleController = battleController;
        // this.gameMode = gameMode;
        // this.towerFloorNumber = towerFloorNumber;
        // this.towerRoomNumber = towerRoomNumber;
    // }

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

    @Override
    public void onItemMenu() {
        view.showInventoryMenu(buildInventoryEntries());
    }

    @Override
    public void onBugemonsMenu() {
        view.showBugemonsMenu(buildBugemonEntries());
    }

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

    @Override
    public void onAttack() {
        view.showAbilitiesMenu(buildAbilityEntries());
    }

    @Override
    public void onBackToMenu() {
        view.showMainMenu();
    }

    @Override
    public void onReturn(ActionEvent event) {
        listener.onRun();
    }

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

    @Override
    public void onSwapBugemon(String bugemonId, ActionEvent event) {
        BugemonDTO bugemon = findTeamBugemonById(bugemonId);
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

    private void updateTowerInfo() {
        if (gameMode == GameMode.TOWER) {
            view.setTowerInfo(towerFloorNumber, towerRoomNumber);
        } else {
            view.clearTowerInfo();
        }
    }

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

    private BattleState stateOrCurrent(BattleState state) {
        if (state != null) {
            return state;
        }
        return listener.getState();
    }

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
                refreshView();

                BattleState currentState = listener.getState();
                boolean stateHandled = handleBattleState(currentState, event);
                if (!stateHandled && currentState == BattleState.INGAME) {
                    view.showMainMenu();
                }
            });
        });

        waitingThread.setDaemon(true);
        waitingThread.start();
    }

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

    private boolean isForcedSwitch() {
        return listener.getState() == BattleState.SWAPPING;
    }

    private BattleSnapshot buildBattleSnapshot() {
        List<BugemonDTO> activeBugemons = this.listener.getActiveBugemons();
        BugemonDTO playerBugemon = activeBugemons.get(0);
        BugemonDTO opponentBugemon = activeBugemons.get(1);
        if (playerBugemon == null || opponentBugemon == null) {
            return null;
        }

        return new BattleSnapshot(toBugemonDisplay(playerBugemon), toBugemonDisplay(opponentBugemon));
    }

    private BugemonDisplay toBugemonDisplay(BugemonDTO bugemon) {
        return new BugemonDisplay(
                bugemon.getName(),
                bugemon.getSpritePath(),
                getTypeColor(bugemon.getType()),
                bugemon.getLevel(),
                bugemon.getHp(),
                bugemon.getBaseStats().getHp()
        );
    }

    private List<String> consumeLogMessages() {
        return listener.getLogs();
    }

    private List<InventoryEntry> buildInventoryEntries() {
        Map<ItemDTO, Integer> inventory = getPlayerInventory();
        if (inventory == null) {
            return List.of();
        }

        List<InventoryEntry> entries = new ArrayList<>();
        Map<ItemDTO, Boolean> itemMap = listener.checkItems(new ArrayList<ItemDTO>(inventory.keySet()));
        for (Map.Entry<ItemDTO, Integer> entry : inventory.entrySet()) {
            ItemDTO item = entry.getKey();
            boolean usable = itemMap.get(item);
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

    private List<BugemonEntry> buildBugemonEntries() {
        List<BugemonDTO> playerTeam = getPlayerTeam();
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
                entries.add(new AbilityEntry(
                        ability.getId(),
                        ability.getName(),
                        ability.getAccurateDescription(),
                        getTypeColor(ability.getType()),
                        abilitiesEffectiveness.get(ability)
                ));
            }
        }
        return entries;
    }

    private ItemDTO findInventoryItemById(String itemId) {
        if (itemId == null) {
            return null;
        }

        Map<ItemDTO, Integer> inventory = getPlayerInventory();
        if (inventory == null) {
            return null;
        }

        for (ItemDTO item : inventory.keySet()) {
            if (itemId.equals(item.getId())) {
                return item;
            }
        }
        return null;
    }

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

    private AbilityDTO findActiveAbilityById(String abilityId) {
        BugemonDTO activeBugemon = listener.getActiveBugemons().get(0);
        if (activeBugemon == null){
            return null;
        }

        for (AbilityDTO ability : activeBugemon.getAbilities()) {
            if (ability != null && abilityId.equals(ability.getId())) {
                return ability;
            }
        }
        return null;
    }

    private List<BugemonDTO> getPlayerTeam() {
        return player != null ? player.getTeam() : null;
    }

    private Map<ItemDTO, Integer> getPlayerInventory() {
        return player != null ? player.getInventory() : null;
    }

    private String getTypeColor(Type type) {
        return switch (type) {
            case PYRO -> "#ED2424";
            case FLORA -> "#50A346";
            case AQUA -> "#51B0F0";
            case LITHO -> "#807979";
            default -> "#ced4da";
        };
    }

    public interface Listener {
        List<BugemonDTO> getActiveBugemons();
        Map<AbilityDTO, String> getAbilityEffectiveness(List<AbilityDTO> ability, BugemonDTO bugemon);
        List<Integer> getHpAfterFirstAction();
        BattleState getState();
        List<String> getLogs();
        Map<ItemDTO, Boolean> checkItems(List<ItemDTO> items);
        boolean isGameFinished();
        BattleState onAutoTurn();
        BattleState onUseItem(ItemDTO item);
        BattleState onSwapBugemon(BugemonDTO bugemon);
        BattleState onUseAbility(AbilityDTO ability);
		void onRun();
        void onBattleStateChecked(BattleState state, ActionEvent event);
    }
}
