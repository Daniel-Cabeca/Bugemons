package ulb.controller.windows;

import java.io.IOException;

import javafx.stage.Stage;
import ulb.view.WindowPath;
import ulb.view.windows.BattleEndWindow;

public class BattleEndController extends WindowController<BattleEndWindow> implements BattleEndWindow.ViewListener {

    private ModeController modeController;

    public BattleEndController(Stage stage, ModeController modeController) {
        this.windowPath = WindowPath.BATTLE_END;
        this.stage = stage;
        try{
            this.init();
        } catch (IOException e) {
            throw new IllegalStateException("Could not load FXML file: " + windowPath, e);
        }
        
        //super(stage, WindowPath.BATTLE_END);
        this.modeController = modeController;
        this.view.setListener(this);
    }

    public void show(boolean victory, int totalXP) {
        this.view.setResult(victory, totalXP);
        super.show();
    }

    @Override
    public void onHandleReturn() {
        modeController.show();
    }
}
