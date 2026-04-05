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
import ulb.view.WindowPath;
import ulb.view.windows.BattleWindow;

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

        Team playerTeam = listener.getPlayerTeam();
        Inventory playerInventory = listener.getPlayerInventory();
        battleController = listener.getBattleController();
        gameMode = listener.getGameMode();

        view.initializeBattle(playerTeam, playerInventory, gameMode, null, battleController);
        if (gameMode == GameMode.TOWER) {
            view.setTowerInfo(listener.getTowerFloorNumber(), listener.getCurrentRoomIndex());
        } else {
            view.clearTowerInfo();
        }

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
        view.showInventoryMenu();
    }

    @Override
    public void onBugemonsMenu() {
        view.showBugemonsMenu();
    }

    @Override
    public void onAuto(ActionEvent event) {
        if (view == null) {
            return;
        }

        view.setAutoButtonVisible(false);
        listener.onAutoTurn();
        view.displayMessagesSequentially(() -> {
            refreshAfterAction(event);
            view.setAutoButtonVisible(true);
        });
    }

    @Override
    public void onAttack() {
        view.showAbilitiesMenu();
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
    public void onUseItem(Item item, ActionEvent event) {
        BattleState stateAfter = stateOrCurrent(listener.onUseItem(item));
        view.displayMessagesSequentially(() -> {
            checkBattleState(stateAfter, event);
            if (stateAfter == BattleState.WAITING || stateAfter == BattleState.INGAME) {
                view.showMainMenu();
            } else {
                view.displayInventory();
            }
        });
    }

    @Override
    public void onSwapBugemon(Bugemon bugemon, ActionEvent event) {
        BattleState stateAfter = stateOrCurrent(listener.onSwapBugemon(bugemon));
        view.displayMessagesSequentially(() -> {
            checkBattleState(stateAfter, event);
            if (stateAfter == BattleState.WAITING || stateAfter == BattleState.INGAME) {
                view.showMainMenu();
            } else {
                view.displayTeam();
            }
        });
    }

    @Override
    public void onUseAbility(Ability ability, ActionEvent event) {
        BattleState stateAfter = stateOrCurrent(listener.onUseAbility(ability));
        view.displayMessagesSequentially(() -> {
            checkBattleState(stateAfter, event);
            if (stateAfter == BattleState.WAITING || stateAfter == BattleState.INGAME) {
                view.showMainMenu();
            }
        });
    }

    private BattleState stateOrCurrent(BattleState state) {
        if (state != null) {
            return state;
        }
        return battleController != null ? battleController.getState() : null;
    }

    private void refreshAfterAction(ActionEvent event) {
        if (battleController == null) {
            return;
        }

        BattleState state = battleController.getState();
        if (state != BattleState.SWAPPING) {
            view.showMainMenu();
        }

        checkBattleState(state, event);
        view.displayNextMessage();

        if (state == BattleState.WAITING) {
            waitingForOpponentAction(event);
        }
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
                view.displayNextMessage();
                checkBattleState(battleController.getState(), event);
            });
        });

        waitingThread.setDaemon(true);
        waitingThread.start();
    }

    private void checkBattleState(BattleState state, ActionEvent event) {
        listener.onBattleStateChecked(state, event);
        if (state == BattleState.SWAPPING) {
            view.showMainMenu();
            view.showBugemonsMenu();
        }
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
