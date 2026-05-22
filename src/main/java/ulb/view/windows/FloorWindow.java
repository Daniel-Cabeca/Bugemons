package ulb.view.windows;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * View for the tower floor navigation window.
 */
public class FloorWindow {
	private final Map<Integer, RoomUI> rooms = new HashMap<>();
	private final Map<Integer, String> originalStyles = new HashMap<>();
	public ViewListener viewListener;
	@FXML
	Label floor;
	@FXML
	Button boss;
	@FXML
	Button battleA1;
	@FXML
	Button battleA2;
	@FXML
	Button battleB;
	@FXML
	Button bonusA;
	@FXML
	Button bonusB;
	@FXML
	Button start;
	@FXML
	Button returnFloorWindow;
	@FXML
	ImageView playerSprite;

	/**
	 * Sets the listener to be notified of floor navigation events.
	 *
	 * @param viewlistener The view listener to register
	 */
	public void setViewListener(ViewListener viewlistener) {
		this.viewListener = viewlistener;
	}

	/**
	 * Maps each room id to its corresponding RoomUI object.
	 */
	public void setupRoomsMap() {
		rooms.put(1, new RoomUI(bonusA, 0, 2));
		rooms.put(2, new RoomUI(battleA2, 1, 2));
		rooms.put(3, new RoomUI(battleA1, 2, 2));
		rooms.put(4, new RoomUI(start, 3, 2));
		rooms.put(5, new RoomUI(battleB, 3, 3));
		rooms.put(6, new RoomUI(bonusB, 3, 4));
		rooms.put(7, new RoomUI(boss, 3, 1));
		for (Map.Entry<Integer, RoomUI> entry : rooms.entrySet()) {
			originalStyles.put(entry.getKey(), entry.getValue().button().getStyle());
		}
	}

	/**
	 * Handles the boss room button click.
	 */
	@FXML
	private void onBoss() {
		this.viewListener.onBoss();
	}

	/**
	 * Handles the battle A1 room button click.
	 */
	@FXML
	private void onBattleA1() {
		this.viewListener.onBattleA1();
	}

	/**
	 * Handles the battle A2 room button click.
	 */
	@FXML
	private void onBattleA2() {
		this.viewListener.onBattleA2();
	}

	/**
	 * Handles the battle B room button click.
	 */
	@FXML
	private void onBattleB() {
		this.viewListener.onBattleB();
	}

	/**
	 * Handles the bonus A room button click.
	 */
	@FXML
	private void onBonusA() {
		this.viewListener.onBonusA();
	}

	/**
	 * Handles the bonus B room button click.
	 */
	@FXML
	private void onBonusB() {
		this.viewListener.onBonusB();
	}

	/**
	 * Handles the start room button click.
	 */
	@FXML
	private void onStart() {
		this.viewListener.onStart();
	}

	/**
	 * Handles the return button click.
	 */
	@FXML
	private void onReturnFloorWindow() {
		this.viewListener.onReturnFloorWindow();
	}

	/**
	 * Sets the floor label to show the current floor number.
	 *
	 * @param floorNumber The current floor number
	 */
	public void setFloorNumber(int floorNumber) {
		floor.setText("Étage : NO" + floorNumber);
	}

	/**
	 * Initiates the animation toward the target room.
	 *
	 * @param targetRoomId The id of the target room
	 * @param onFinished (@code true) if animation finished, (@code false) if it didn't
	 */
	public void translationAnimationHandler(int targetRoomId, Runnable onFinished) {
		RoomUI roomUI = rooms.get(targetRoomId);
		Direction direction = directionToTargetRoom(roomUI.col(), roomUI.row());
		playTranslationAnimation(direction, roomUI, onFinished);
	}

	/**
	 * Returns the direction to take based on the current coordinates of the player
	 * and the coordinates of the room they want to go to.
	 *
	 * @param futureCol Column index of the destination room
	 * @param futureRow Row index of the destination room
	 * @return The direction to take
	 */
	public Direction directionToTargetRoom(int futureCol, int futureRow) {
		Integer col = GridPane.getColumnIndex(playerSprite);
		int currentCol = (col == null) ? 0 : col;
		Integer row = GridPane.getRowIndex(playerSprite);
		int currentRow = (row == null) ? 0 : row;

		if (currentCol == futureCol && currentRow == futureRow) {
			throw new IllegalArgumentException("Already in target room");
		}

		if (currentRow == futureRow) {
			return Direction.HORIZONTAL;
		} else {
			return Direction.VERTICAL;
		}
	}

	/**
	 * Plays the correct translation animation based on the given direction.
	 *
	 * @param direction The direction of the translation
	 * @param targetRoom The destination room UI
	 * @param onFinished (@code true) if animation finished, (@code false) if it didn't
	 */
	public void playTranslationAnimation(Direction direction, RoomUI targetRoom, Runnable onFinished) {
		if (direction == Direction.HORIZONTAL) {
			playHorizontalTranslationAnimation(direction, targetRoom, onFinished);
		} else {
			playVerticalTranslationAnimation(direction, targetRoom, onFinished);
		}
	}

	/**
	 * Plays a horizontal translation animation toward the target room.
	 *
	 * @param direction The horizontal direction of the translation
	 * @param targetRoom The destination room UI
	 * @param onFinished (@code true) if animation finished, (@code false) if it didn't
	 */
	public void playHorizontalTranslationAnimation(Direction direction, RoomUI targetRoom, Runnable onFinished) {
		TranslateTransition moveAnimation = new TranslateTransition(javafx.util.Duration.seconds(0.5), playerSprite);

		double currentX = playerSprite.localToScene(0, 0).getX();
		double targetX = targetRoom.button().localToScene(0, 0).getX() + 40;
		double deltaX = targetX - currentX;

		moveAnimation.setByX(deltaX);

		moveAnimation.setOnFinished(e -> {
			playerSprite.setTranslateX(0);
			playerSprite.setTranslateY(-70);

			if (onFinished != null) {
				onFinished.run();
			}
		});

		moveAnimation.play();
	}

	/**
	 * Plays a vertical translation animation toward the target room.
	 *
	 * @param direction The vertical direction of the translation
	 * @param targetRoom The destination room UI
	 * @param onFinished (@code true) if animation finished, (@code false) if it didn't
	 */
	public void playVerticalTranslationAnimation(Direction direction, RoomUI targetRoom, Runnable onFinished) {
		TranslateTransition moveAnimation = new TranslateTransition(javafx.util.Duration.seconds(0.5), playerSprite);

		double currentY = playerSprite.localToScene(0, 0).getY();
		double targetY = targetRoom.button().localToScene(0, 0).getY() - 85;
		double deltaY = targetY - currentY;

		moveAnimation.setByY(deltaY);

		moveAnimation.setOnFinished(e -> {
			playerSprite.setTranslateX(0);
			playerSprite.setTranslateY(-70);

			if (onFinished != null) {
				onFinished.run();
			}
		});

		moveAnimation.play();
	}

	/**
	 * Marks the visited room buttons.
	 *
	 * @param visitedRooms The list of visited rooms
	 */
	public void markVisitedRooms(List<Integer> visitedRooms) {
		for (Map.Entry<Integer, RoomUI> entry : rooms.entrySet()) {
			entry.getValue().button().setStyle(originalStyles.getOrDefault(entry.getKey(), ""));
		}
		String visitedRoomStyle = "-fx-background-color: #c0c0c0; -fx-font-size: 24px";
		for (Integer id : visitedRooms) {
			RoomUI roomUI = rooms.get(id);
			if (roomUI != null) {
				roomUI.button().setStyle(visitedRoomStyle);
			}
		}
	}

	/**
	 * Updates the player's sprite position based on the current room.
	 *
	 * @param roomId The id of the current room
	 */
	public void updatePlayerPosition(int roomId) {
		RoomUI roomUI = rooms.get(roomId);
		if (roomUI != null) {
			setIconLocation(roomUI.col(), roomUI.row());
		}
	}

	/**
	 * Sets the location of the player icon.
	 *
	 * @param col The column
	 * @param row The row
	 */
	private void setIconLocation(int col, int row) {
		GridPane.setColumnIndex(playerSprite, col);
		GridPane.setRowIndex(playerSprite, row);
		GridPane.setHalignment(playerSprite, javafx.geometry.HPos.CENTER);
		GridPane.setValignment(playerSprite, javafx.geometry.VPos.CENTER);
		playerSprite.setTranslateY(-70); // moves the sprite to be above the button
		playerSprite.toFront();
	}

	public enum Direction {
		HORIZONTAL, VERTICAL
	}

	/**
	 * Listener for floor navigation view events.
	 */
	public interface ViewListener {
		/** 
		 * Handles the boss room being selected. 
		 */
		void onBoss();

		/** 
		 * Handles the battle A1 room being selected. 
		 */
		void onBattleA1();

		/** 
		 * Handles the battle A2 room being selected. 
		 */
		void onBattleA2();

		/** 
		 * Handles the battle B room being selected. 
		 */
		void onBattleB();

		/** 
		 * Handles the bonus A room being selected. 
		 */
		void onBonusA();

		/** 
		 * Handles the bonus B room being selected. 
		 */
		void onBonusB();

		/** 
		 * Handles the start room being selected. 
		 */
		void onStart();
		
		/** 
		 * Handles the return button being pressed. 
		 */
		void onReturnFloorWindow();
	}

	/**
	 * Links the room button with its coordinates in the grid.
	 *
	 * @param button The button representing the room
	 * @param col The GridPane column index
	 * @param row The GridPane row index
	 */
	private record RoomUI(Button button, int col, int row) {
	}
}