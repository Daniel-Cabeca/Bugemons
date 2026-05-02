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

public class FloorController implements FloorWindow.ViewListener {
    private Listener listener;
    private Stage stage;
    private FloorWindow view;
	private final Floor floorGraph = new Floor(1, false);
	private int currentRoomId = 4;
    private int lastEnteredRoomId = 4; // needed for when the player flees a battle

    public FloorController(Stage stage, Listener listener){
        this.stage = stage;
        this.listener = listener;
    }

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
            playReturnAnimation();
        }
    }

	/**
	 * Synchronizes the current room ID with the server's state and updates the visited rooms set
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
	 * Tries to select a room based on the player's click, checking if it's 
	 * adjacent and not the current room. Once verified, it launches the animation process and then accesses to the room.
	 * 
	 * @param targetRoomId the room ID corresponding to the clicked room
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
     * Plays the movement animation when the player fled a battle and goes back to the previous room
     */
    private void playReturnAnimation() {
        view.updatePlayerPosition(lastEnteredRoomId);
        view.translationAnimationHandler(currentRoomId, () -> {
            view.updatePlayerPosition(currentRoomId);
            lastEnteredRoomId = currentRoomId;
        });
    }

    @Override
    public void onBoss() {
        trySelectRoom(7);
    }

    @Override
    public void onBattleA1() {
        trySelectRoom(3);
    }

    @Override
    public void onBattleA2() {
        trySelectRoom(2);

    }

    @Override
    public void onBattleB() {
        trySelectRoom(5);
    }

    @Override
    public void onBonusA() {
        trySelectRoom(1);
    }

    @Override
    public void onBonusB() {
        trySelectRoom(6);
    }

    @Override
    public void onStart() {
        trySelectRoom(4);
    }

	@Override
    public void onReturnFloorWindow() {
        this.listener.onReturnFloorWindow();
    }

    interface Listener{
		boolean onRoomSelected(int roomId);
        void onRoomSelectionComplete();
		void onReturnFloorWindow();
		List<Integer> getTowerInfo();
        List<Integer> getClearedRooms();
    }
}
