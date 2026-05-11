package ulb.controller.windows;

import javafx.application.Platform;
import javafx.stage.Stage;
import ulb.controller.ClientController;
import ulb.model.tower.Floor;
import ulb.message.request.gameActions.ChooseTowerRoomRequest;
import ulb.message.request.gameInfo.GetTowerInfoRequest;
import ulb.message.response.gameInfo.TowerInfoResponse;
import ulb.view.WindowPath;
import ulb.view.windows.FloorWindow;

/**
 * Controller for the tower floor navigation window.
 */
public class FloorController extends WindowController<FloorWindow> implements FloorWindow.ViewListener {

    private final Floor floorGraph = new Floor(1, false);
    private int currentRoomId = 4;
    private int lastEnteredRoomId = 4;
    private int currentFloorNumber = -1;

    /**
     * Creates the floor controller and loads the FXML once.
     *
     * @param stage The application stage
     * @param clientController The client controller
     */
    public FloorController(Stage stage, ClientController clientController) {
        super(stage, WindowPath.FLOOR, clientController);
        this.view.setViewListener(this);
        this.view.setupRoomsMap();
    }

    /**
     * Displays the floor navigation view, syncing state from the server.
     */
    @Override
    public void show() {
        syncCurrentRoomFromServer();
        super.show();
        if (lastEnteredRoomId != currentRoomId) {
            view.updatePlayerPosition(lastEnteredRoomId);
            Platform.runLater(this::playReturnAnimation);
        }
    }

    /**
     * Synchronizes the current room and floor state from the server in a single request.
     * Resets the fled-detection state when the floor number changes.
     */
    private void syncCurrentRoomFromServer() {
        if (!(clientController.getData(new GetTowerInfoRequest()) instanceof TowerInfoResponse info)) return;
        int serverFloor = info.getFloorNumber();
        this.currentRoomId = info.getRoomNumber();
        if (serverFloor != this.currentFloorNumber) {
            this.lastEnteredRoomId = this.currentRoomId;
            this.currentFloorNumber = serverFloor;
        }
        view.setFloorNumber(serverFloor);
        view.updatePlayerPosition(this.currentRoomId);
        view.markVisitedRooms(info.getClearedRooms());
    }

    /**
     * Tries to select a room, checking adjacency. If valid, triggers animation then enters the room.
     *
     * @param targetRoomId The room ID of the clicked room
     */
    private void trySelectRoom(int targetRoomId) {
        syncCurrentRoomFromServer();
        if (targetRoomId == currentRoomId) return;
        boolean isAdjacent = floorGraph.getAdjacentRoomsIds(currentRoomId).contains(targetRoomId);
        if (!isAdjacent) return;
        view.translationAnimationHandler(targetRoomId, () -> {
            boolean success = clientController.postData(new ChooseTowerRoomRequest(targetRoomId));
            if (!success) return;
            this.lastEnteredRoomId = targetRoomId;
            clientController.nextRoom();
        });
    }

    /**
     * Plays the return animation when the player fled a battle.
     */
    private void playReturnAnimation() {
        view.translationAnimationHandler(currentRoomId, () -> {
            view.updatePlayerPosition(currentRoomId);
            lastEnteredRoomId = currentRoomId;
        });
    }

    /** {@inheritDoc} */
    @Override
    public void onBoss() { trySelectRoom(7); }

    /** {@inheritDoc} */
    @Override
    public void onBattleA1() { trySelectRoom(3); }

    /** {@inheritDoc} */
    @Override
    public void onBattleA2() { trySelectRoom(2); }

    /** {@inheritDoc} */
    @Override
    public void onBattleB() { trySelectRoom(5); }

    /** {@inheritDoc} */
    @Override
    public void onBonusA() { trySelectRoom(1); }

    /** {@inheritDoc} */
    @Override
    public void onBonusB() { trySelectRoom(6); }

    /** {@inheritDoc} */
    @Override
    public void onStart() { trySelectRoom(4); }

    /** {@inheritDoc} */
    @Override
    public void onReturnFloorWindow() { clientController.showWindow(WindowName.GAME_MODE); }
}
