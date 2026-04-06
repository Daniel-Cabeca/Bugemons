package ulb.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.communication.types.GameMode;
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

public class BattleWindowController implements BattleWindow.ViewListener {

    private final Stage stage;
    private final Listener listener;

    private BattleWindow view;
    private BattleController battleController;
    private GameMode gameMode;
    private boolean waitingForOpponentAction = false;

    public BattleWindowController(Stage stage, Listener listener) {
        this.stage = stage;
        this.listener = listener;
    }

    public void show() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.BATTLE));
        loader.load();
        view = loader.getController();
        view.setViewListener(this);

        battleController = listener.getBattleController();
        gameMode = listener.getGameMode();

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
        displayActionSequence(stateAfter, event, () -> view.setAutoButtonVisible(true));
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
        if (gameMode == GameMode.TOWER) {
            listener.onTowerFlee();
        } else {
            listener.onReturnToMode();
        }
    }

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

    @Override
    public void onSwapBugemon(String bugemonId, ActionEvent event) {
        Bugemon bugemon = findTeamBugemonById(bugemonId);
        BattleState stateAfter = stateOrCurrent(listener.onSwapBugemon(bugemon));
        displayActionSequence(stateAfter, event, () -> {
            if (stateAfter == BattleState.WAITING || stateAfter == BattleState.INGAME) {
                view.showMainMenu();
            } else {
                view.showBugemonsMenu(buildBugemonEntries());
            }
        });
    }

    @Override
    public void onUseAbility(String abilityId, ActionEvent event) {
        Ability ability = findActiveAbilityById(abilityId);
        BattleState stateAfter = stateOrCurrent(listener.onUseAbility(ability));
        System.out.println("=== AFTER ACTION ===");
        System.out.println("State: " + stateAfter);
        System.out.println("Player HP: " + battleController.getActiveBugemonSelf().getHp());
        displayActionSequence(stateAfter, event, () -> {
            if (stateAfter == BattleState.WAITING || stateAfter == BattleState.INGAME) {
                view.showMainMenu();
            }
        });
        if (stateAfter == BattleState.WON || stateAfter == BattleState.LOST) {
            battleController.resetFighter();
        }
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
            view.setTowerInfo(listener.getTowerFloorNumber(), listener.getCurrentRoomIndex());
        } else {
            view.clearTowerInfo();
        }
    }

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

    private BattleState stateOrCurrent(BattleState state) {
        if (state != null) {
            return state;
        }
        return battleController != null ? battleController.getState() : null;
    }

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
        return battleController != null && battleController.getState() == BattleState.SWAPPING;
    }

    private BattleSnapshot buildBattleSnapshot() {
        if (battleController == null) {
            return null;
        }

        Bugemon playerBugemon = battleController.getActiveBugemonSelf();
        Bugemon opponentBugemon = battleController.getActiveBugemonOpponent();
        if (playerBugemon == null || opponentBugemon == null) {
            return null;
        }
        System.out.println("=== SNAPSHOT ===");
        System.out.println("Player HP: " + playerBugemon.getHp());
        System.out.println("Opponent HP: " + opponentBugemon.getHp());

        return new BattleSnapshot(toBugemonDisplay(playerBugemon), toBugemonDisplay(opponentBugemon));
    }

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

    private List<String> consumeLogMessages() {
        if (battleController == null) {
            return List.of();
        }

        List<String> logs = new ArrayList<>(battleController.getLogMsg());
        battleController.clearLogMsg();
        return logs;
    }

    private List<InventoryEntry> buildInventoryEntries() {
        Inventory inventory = listener.getPlayerInventory();
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
                    item.getSprite(),
                    entry.getValue(),
                    usable
            ));
        }
        return entries;
    }

    private List<BugemonEntry> buildBugemonEntries() {
        Team playerTeam = listener.getPlayerTeam();
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
                        getTypeColor(ability.getType()),
                        battleController.getEffectiveness(ability)
                ));
            }
        }
        return entries;
    }

    private Item findInventoryItemById(String itemId) {
        if (itemId == null) {
            return null;
        }

        Inventory inventory = listener.getPlayerInventory();
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

    private Bugemon findTeamBugemonById(String bugemonId) {
        if (bugemonId == null) {
            return null;
        }

        Team team = listener.getPlayerTeam();
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
        Team getPlayerTeam();
        Inventory getPlayerInventory();
        BattleController getBattleController();
        GameMode getGameMode();
        int getTowerFloorNumber();
        int getCurrentRoomIndex();
        BattleState onAutoTurn();
        BattleState onUseItem(Item item);
        BattleState onSwapBugemon(Bugemon bugemon);
        BattleState onUseAbility(Ability ability);
        void onBattleStateChecked(BattleState state, ActionEvent event);
        void onTowerFlee();
        void onReturnToMode();
    }
}
