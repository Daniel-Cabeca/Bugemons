package ulb.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.model.tower.Floor;
import ulb.view.WindowPath;
import ulb.view.windows.FloorWindow;

public class FloorController implements FloorWindow.ViewListener {
    private Listener listener;
    private Stage stage;
    private FloorWindow view;
	private final Floor floorGraph = new Floor(1, false);
	private int currentRoomId = 4;
	private final Set<Integer> visitedRooms = new HashSet<>();

    public FloorController(Stage stage, Listener listener){
        this.stage = stage;
        this.listener = listener;
		this.visitedRooms.add(currentRoomId);
    }

    public void show() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.FLOOR));
        loader.load();
        view = loader.getController();
        view.setViewListener(this);

		syncCurrentRoomFromServer();

        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        this.stage.show();
    }

	/**
	 * Synchronizes the current room ID with the server's state and updates the visited rooms set
	 */
	private void syncCurrentRoomFromServer() {
        List<Integer> towerInfo = this.listener.getTowerInfo();
        if (towerInfo != null && towerInfo.size() >= 2) {
            this.currentRoomId = towerInfo.get(1);
            this.visitedRooms.add(this.currentRoomId);

			view.setFloorNumber(towerInfo.get(0));
        }
    }

	/**
	 * Tries to select a room based on the player's click, checking if it's 
	 * adjacent and not the current room.
	 * 
	 * @param targetRoomId the room ID corresponding to the clicked room
	 */
	private void trySelectRoom(int targetRoomId) {
		syncCurrentRoomFromServer();

		if (targetRoomId == currentRoomId) return;

		boolean isAdjacent = floorGraph.getAdjacentRoomsIds(currentRoomId).contains(targetRoomId);

		if (!isAdjacent) return;

		boolean success = this.listener.onRoomSelected(targetRoomId);
		if (!success) return;

		syncCurrentRoomFromServer();
		visitedRooms.add(targetRoomId);
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
		void onReturnFloorWindow();
		List<Integer> getTowerInfo();
    }
}
