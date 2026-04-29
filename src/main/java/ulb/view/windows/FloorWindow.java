package ulb.view.windows;

import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class FloorWindow {
    public ViewListener viewListener;
    public void setViewListener(ViewListener viewlistener){
        this.viewListener = viewlistener;
    }

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

    public void playTranslationAnimation(Direction direction, Runnable onFinished) {
        if (direction == Direction.RIGHT || direction == Direction.LEFT) {
            playHorizontalTranslationAnimation(direction, onFinished);
        }
        else {
            playVerticalTranslationAnimation(direction, onFinished);
        }
    }

    public void translationAnimationHandler(int targetRoomId, Runnable onFinished) {
        Integer col = GridPane.getColumnIndex(playerSprite);
        int currentCol = (col==null) ? 0 : col;
        Integer row = GridPane.getRowIndex(playerSprite);
        int currentRow = (row==null) ? 0 : row;

        Direction direction = null;
        switch (targetRoomId) {
            case 1: // BonusA
                direction = directionPicker(currentCol, currentRow, 0, 2);
                break;
            case 2: // BattleA2
                direction = directionPicker(currentCol, currentRow, 1, 2);
                break;
            case 3: // BattleA1
                direction = directionPicker(currentCol, currentRow, 2, 2);
                break;
            case 4: // Start
                direction = directionPicker(currentCol, currentRow, 3, 2);
                break;
            case 5: // BattleB
                direction = directionPicker(currentCol, currentRow, 3, 3);
                break;
            case 6: // BonusB
                direction = directionPicker(currentCol, currentRow, 3, 4);
                break;
            case 7: // Boss
                direction = directionPicker(currentCol, currentRow, 3, 1);
                break;
        }
        playTranslationAnimation(direction, onFinished);
    }

    /**
     * Updates the player's sprite position to the correct cell based on the current room id
     *
     * @param roomId the id of the current room
     */
    public void updatePlayerPosition(int roomId) {
        switch (roomId) {
            case 1: // BonusA
                setIconLocation(0, 2);
                break;
            case 2: // BattleA2
                setIconLocation(1, 2);
                break;
            case 3: // BattleA1
                setIconLocation(2, 2);
                break;
            case 4: // Start
                setIconLocation(3, 2);
                break;
            case 5: // BattleB
                setIconLocation(3, 3);
                break;
            case 6: // BonusB
                setIconLocation(3, 4);
                break;
            case 7: // Boss
                setIconLocation(3, 1);
                break;
        }
    }

    /**
     * Sets the location of the player icon to a specific cell
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
