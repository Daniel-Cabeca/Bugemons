package ulb.controller.windows;


import javafx.stage.Stage;
import ulb.view.WindowPath;
import ulb.view.windows.BattleEndWindow;

/**
 * Controller for the battle end summary screen.
 */
public class BattleEndController extends WindowController<BattleEndWindow> implements BattleEndWindow.ViewListener {

    /**
     * Creates the battle end controller.
     *
     * @param stage The application stage
     * @param clientListener Listener handling return action
     */
    public BattleEndController(Stage stage, ClientListener clientListener) {
        super(stage,WindowPath.BATTLE_END,  clientListener);
        this.view.setViewListener(this);
    }

    /**
     * Displays battle result information.
     *
     * @param victory Whether the battle was won
     * @param totalXP Total experience gained
     * @param opponent the one player fought
     */
    public void show(boolean victory, int totalXP, String opponent, boolean multiplayerBattle){
        super.show();
        this.view.setResult(victory, totalXP, opponent, multiplayerBattle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onHandleReturn() { this.clientListener.onShowWindow(WindowName.MODE); }

}
