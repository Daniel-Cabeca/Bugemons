package ulb.controller.windows;

import javafx.stage.Stage;
import ulb.view.WindowPath;
import ulb.view.windows.BattleEndWindow;

public class BattleEndController extends WindowController<BattleEndWindow> implements BattleEndWindow.ViewListener {

    private ModeController modeController;

    public BattleEndController(Stage stage, ModeController modeController) {
        super(stage, WindowPath.BATTLE_END);
        this.modeController = modeController;
        getView().setListener(this);
    }

    public void show(boolean victory, int totalXP) {
        getView().setResult(victory, totalXP);
        super.show();
    }

    @Override
    public void onHandleReturn() {
        modeController.show();
    }
}
