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

public class FloorWindow {
    public ViewListener viewListener;
    public void setViewListener(ViewListener viewlistener){
        this.viewListener = viewlistener;
    }

    /**
     * Links the room button with its coordinated in the grid
     * @param button the button representing the room
     * @param col the GridPane column index for this room
     * @param row the GridPane row index for this room
     */
    private record RoomUI(Button button, int col, int row) {}

    private final Map<Integer, RoomUI> rooms = new HashMap<>();

    public enum Direction {
        RIGHT,
        LEFT,
        UP,
        DOWN
    }

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
     * Maps the room id to the RoomUI object 
     */
    public void setupRoomsMap() {
        rooms.put(1, new RoomUI(bonusA, 0, 2));
        rooms.put(2, new RoomUI(battleA2, 1, 2));
        rooms.put(3, new RoomUI(battleA1, 2, 2));
        rooms.put(4, new RoomUI(start, 3, 2));
        rooms.put(5, new RoomUI(battleB, 3, 3));
        rooms.put(6, new RoomUI(bonusB, 3, 4));
        rooms.put(7, new RoomUI(boss, 3, 1));
    }

    @FXML
    private void onBoss(){
        this.viewListener.onBoss();
    }
    @FXML
    private void onBattleA1(){
        this.viewListener.onBattleA1();
    }
    @FXML
    private void onBattleA2(){
        this.viewListener.onBattleA2();
    }
    @FXML
    private void onBattleB(){
        this.viewListener.onBattleB();
    }
    @FXML
    private void onBonusA(){
        this.viewListener.onBonusA();
    }
    @FXML
    private void onBonusB(){
        this.viewListener.onBonusB();
    }
    @FXML
    private void onStart(){
        this.viewListener.onStart();
    }
	@FXML
    private void onReturnFloorWindow(){
        this.viewListener.onReturnFloorWindow();
    }

	public void setFloorNumber(int floorNumber) {
        floor.setText("Étage : NO" + floorNumber);
    }

    /**
     * Gives the direction to take based on the current coordinates of the player
     * and the coordinates of the room he wants to go to.
     * @param currentCol collum part of the current coordinates of the player
     * @param currentRow row part of the current coordinates of the player
     * @param futureCol collum part of the coordinates of the destination room
     * @param futureRow row part of the coordinates of the destination room
     * @return the direction that needs to be taken to get to the right destination
     */
    public Direction directionPicker(int currentCol, int currentRow, int futureCol, int futureRow) {
        if (currentRow == futureRow) {
            if (currentCol < futureCol) {
                return Direction.RIGHT;
            }
            else {
                return Direction.LEFT;
            }
        }
        else {
            if (currentRow > futureRow) {
                return Direction.UP;
            }
            else {
                return Direction.DOWN;
            }
        }
    }

    /**
     * Makes a horizontal translation animation based on the given direction.
     * @param direction the way the translation has to move, right/left
     * @param onFinished to allow the animation to fully take place without being interrupted
     */
    public void playHorizontalTranslationAnimation(Direction direction, Runnable onFinished ) {
        TranslateTransition moveAnimation = new TranslateTransition(javafx.util.Duration.seconds(0.5), playerSprite);

        if (direction == Direction.RIGHT) {
            moveAnimation.setByX(450);
        }
        else if (direction == Direction.LEFT) {
            moveAnimation.setByX(-450);
        }

        moveAnimation.setOnFinished(e -> {
            if (onFinished != null) {
                onFinished.run();
            }
        });

        moveAnimation.play();
    }

    /**
     * Makes a vertical translation animation based on the given direction.
     * @param direction the way the translation has to move, up/down
     * @param onFinished to allow the animation to fully take place without being interrupted
     */
    public void playVerticalTranslationAnimation(Direction direction, Runnable onFinished) {
        TranslateTransition moveAnimation = new TranslateTransition(javafx.util.Duration.seconds(0.5), playerSprite);


        if (direction == Direction.UP) {
            moveAnimation.setByY(-225);
        }
        else if (direction == Direction.DOWN) {
            moveAnimation.setByY(225);
        }

        moveAnimation.setOnFinished(e -> {
            if (onFinished != null) {
                onFinished.run();
            }
        });

        moveAnimation.play();
    }

    /**
     * Decides which type of translation to do based on the given direction.
     * @param direction the direction of the translation right/left/up/down
     * @param onFinished to allow the animation to fully take place without being interrupted
     */
    public void playTranslationAnimation(Direction direction, Runnable onFinished) {
        if (direction == Direction.RIGHT || direction == Direction.LEFT) {
            playHorizontalTranslationAnimation(direction, onFinished);
        }
        else {
            playVerticalTranslationAnimation(direction, onFinished);
        }
    }

    /**
     * Initiates the animation to the given target room.
     * @param targetRoomId the destination room
     * @param onFinished to allow the animation to fully take place without being interrupted
     */
    public void translationAnimationHandler(int targetRoomId, Runnable onFinished) {
        Integer col = GridPane.getColumnIndex(playerSprite);
        int currentCol = (col==null) ? 0 : col;
        Integer row = GridPane.getRowIndex(playerSprite);
        int currentRow = (row==null) ? 0 : row;

        RoomUI roomUI = rooms.get(targetRoomId);
        Direction direction = directionPicker(currentCol, currentRow, roomUI.col(), roomUI.row());
        playTranslationAnimation(direction, onFinished);
    }

    /**
     * Makes the visited room buttons gray
     *
     * @param visitedRooms set containing the ids of the visited rooms
     */
    public void markVisitedRooms(List<Integer> visitedRooms) {
        String visitedRoomStyle = "-fx-background-color: #c0c0c0; -fx-font-size: 24px";
        for (Integer id : visitedRooms) {
            RoomUI roomUI = rooms.get(id);
            if (roomUI != null) {
                roomUI.button().setStyle(visitedRoomStyle);
            }
        }
    }

    /**
     * Updates the player's sprite position to the correct cell based on the current room id.
     *
     * @param roomId the id of the current room
     */
    public void updatePlayerPosition(int roomId) {
        RoomUI roomUI = rooms.get(roomId);
        if (roomUI != null) {
            setIconLocation(roomUI.col(), roomUI.row());
        }
    }

    /**
     * Sets the location of the player icon to a specific cell.
     *
     * @param col the column of the cell
     * @param row the row of the cell
     */
    private void setIconLocation(int col, int row) {
        GridPane.setColumnIndex(playerSprite, col);
        GridPane.setRowIndex(playerSprite, row);
        GridPane.setHalignment(playerSprite, javafx.geometry.HPos.CENTER);
        GridPane.setValignment(playerSprite, javafx.geometry.VPos.CENTER);
        playerSprite.setTranslateY(-70); // moves the sprite to be above the button
        playerSprite.toFront();
    }

    public interface ViewListener{
        void onBoss();
        void onBattleA1();
        void onBattleA2();
        void onBattleB();
        void onBonusA();
        void onBonusB();
        void onStart();
		void onReturnFloorWindow();
    }

}
