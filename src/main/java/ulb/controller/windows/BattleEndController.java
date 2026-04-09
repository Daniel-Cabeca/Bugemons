package ulb.controller.windows;

import ulb.view.WindowPath;
import ulb.view.windows.BattleEndWindow;
import ulb.controller.windows.WindowController;


public class BattleEndController extends WindowController<BattleEndWindow> implements BattleEndWindow.ViewListener {

    private ModeController modeController;


    public void setModeController(ModeController modeController) {
        this.modeController = modeController;
    }

    public BattleEndController() {
        setWindowPath(WindowPath.BATTLE_END);
        try {
            this.init();
        } catch (Exception e) {
            System.err.println("Couldn't load the FXML file");
        }
        this.getView().setListener(this);
    }

    @Override
    public void onHandleReturn() {
        this.modeController.show();
    }

}
