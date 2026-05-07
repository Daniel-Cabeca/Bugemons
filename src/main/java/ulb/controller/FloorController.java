package ulb.controller;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.model.tower.Floor;
import ulb.view.FxmlLoader;
import ulb.view.WindowPath;
import ulb.view.windows.FloorWindow;
import javafx.application.Platform;

/**
 * Controller for the tower floor navigation window.
 */
public class FloorController implements FloorWindow.ViewListener {
    private Listener listener;
    private Stage stage;
    private FloorWindow view;
    private final Floor floorGraph = new Floor(1, false);
    private int currentRoomId = 4;
    private int lastEnteredRoomId = 4; // needed for when the player flees a battle

    /**
     * Creates the floor controller.
     *
     * @param stage The application stage
     * @param listener The listener notified of actions
     */
    public FloorController(Stage stage, Listener listener){
        this.stage = stage;
        this.listener = listener;
    }

    /**
     * Displays the floor navigation view.
     */
    public void show() {
        FXMLLoader loader = FxmlLoader.load(this, WindowPath.FLOOR);
        view = loader.getController();
        view.setViewListener(this);
        view.setupRoomsMap();
        syncCurrentRoomFromServer();
        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        this.stage.show();
        // if the player fled a battle, they need to go back to the previous room
        if (lastEnteredRoomId != currentRoomId){
            view.updatePlayerPosition(lastEnteredRoomId);
            Platform.runLater(() -> playReturnAnimation());
        }
    }

    /**
     * Synchronizes the current room ID with the server's state and updates the visited rooms.
     */
    private void syncCurrentRoomFromServer() {
        List<Integer> towerInfo = this.listener.getTowerInfo();
        if (towerInfo != null && towerInfo.size() >= 2) {
            this.currentRoomId = towerInfo.get(1);
            view.setFloorNumber(towerInfo.get(0));
            view.updatePlayerPosition(this.currentRoomId);
            view.markVisitedRooms(listener.getClearedRooms());
        }
    }

    /**
     * Tries to select a room based on the player's click, checking if it's adjacent and not the current room.
     * If it succeeds, launches the animation process and then "enters" the room.
     *
     * @param targetRoomId The room ID corresponding to the clicked room
     */
    private void trySelectRoom(int targetRoomId) {
        syncCurrentRoomFromServer();
        if (targetRoomId == currentRoomId) return;
        boolean isAdjacent = floorGraph.getAdjacentRoomsIds(currentRoomId).contains(targetRoomId);
        if (!isAdjacent) return;
        view.translationAnimationHandler(targetRoomId, () -> {
            boolean success = this.listener.onRoomSelected(targetRoomId);
            if (!success) return;
            this.lastEnteredRoomId = targetRoomId;
            this.listener.onRoomSelectionComplete();
        });
    }

    /**
     * Plays the movement animation when the player flees a battle, goes back to the previous room.
     */
    private void playReturnAnimation() {
        view.translationAnimationHandler(currentRoomId, () -> {
            view.updatePlayerPosition(currentRoomId);
            lastEnteredRoomId = currentRoomId;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBoss() {
        trySelectRoom(7);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBattleA1() {
        trySelectRoom(3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBattleA2() {
        trySelectRoom(2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBattleB() {
        trySelectRoom(5);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBonusA() {
        trySelectRoom(1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBonusB() {
        trySelectRoom(6);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart() {
        trySelectRoom(4);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReturnFloorWindow() {
        this.listener.onReturnToGameModeWindow();
    }

    /**
     * Listener for floor navigation events.
     */
    interface Listener{
        /** Handles the selection of a room and returns whether it succeeded. */
        boolean onRoomSelected(int roomId);
        /** Handles the completion of a room selection. */
        void onRoomSelectionComplete();
        /** Handles returning to the game mode selection window. */
        void onReturnToGameModeWindow();
        /** Returns the current tower floor and room information. */
        List<Integer> getTowerInfo();
        /** Returns the list of ids of the cleared rooms in the current floor. */
        List<Integer> getClearedRooms();
    }
}