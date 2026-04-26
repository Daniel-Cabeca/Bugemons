package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class FloorWindow {
    public ViewListener viewListener;
    public void setViewListener(ViewListener viewlistener){
        this.viewListener = viewlistener;
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
        floor.setText("Etage NO" + floorNumber);
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
