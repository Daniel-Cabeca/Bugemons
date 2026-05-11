package ulb.controller.windows;

import javafx.stage.Stage;
import ulb.controller.ClientController;
import ulb.message.request.gameActions.AbandonTowerRequest;
import ulb.view.WindowPath;
import ulb.view.windows.NextRoomWindow;

/**
 * Controller for the transition screen between tower rooms.
 */
public class NextRoomController extends WindowController<NextRoomWindow> implements NextRoomWindow.ViewListener {

    /**
     * Creates the next room controller.
     *
     * @param stage The application stage
     * @param clientController The controller used for navigation
     */
    public NextRoomController(Stage stage, ClientController clientController) {
        super(stage, WindowPath.NEXT_ROOM, clientController);
        this.view.setViewListener(this);
    }

    /**
     * Handles continue action.
     */
    @Override
    public void onContinue() { this.clientListener.onNextRoom(); }

//    /**
//     * Returns the next window type according to server flow.
//     *
//     * @return The next window type, or null if unavailable
//     */
//    public WindowType getWindowType(){
//        Serializable message = this.clientListener.onGetData(new GetNextWindowRequest());
//        if (message instanceof NextWindowResponse nextWindow){
//            return nextWindow.getNextWindow();
//        }
//        return null;
//    }
//
//    /**
//     * Switches to the next window according to info gotten from the server.
//     */
//    public void nextRoom(){
//        WindowType nextWindow = this.getWindowType();
//        switch (nextWindow) {
//            case NEXT_ROOM:
//                this.clientListener.onShowWindow(WindowName.NEXT_ROOM);
//                break;
//            case GAME:
//                this.clientListener.onShowWindow(WindowName.BATTLE);
//                break;
//            case LEVEL_UP:
//                this.clientListener.onShowWindow(WindowName.LEVEL_UP);
//                break;
//            case REWARD:
//                this.clientListener.onShowWindow(WindowName.FLOOR_REWARD);
//                break;
//            case MAIN_MENU:
//                this.switchToBattleEndWindow();
//                break;
//            case FLOOR:
//                this.clientListener.onShowWindow(WindowName.FLOOR);
//                break;
//            default:
//                break;
//        }
//    }
//
//    /**
//     * Switches to the battle end window with battle result.
//     */
//    private void switchToBattleEndWindow(){
//        Serializable message = this.clientListener.onGetData(new GetBattleEndInfoRequest());
//        boolean victory;
//        int totalXp;
//        String opponent;
//        if (message instanceof BattleEndInfoResponse battleInfo){
//            victory = battleInfo.isVictory();
//            totalXp = battleInfo.getTotalXp();
//            opponent = battleInfo.getOpponent();
//
//        } else {
//            return;
//        }
//        this.clientListener.onShowBattleEnd(victory, totalXp, opponent);
//    }

    /**
     * Handles return action.
     */
    @Override
    public void onReturn() {
        if (this.clientListener.onPostData(new AbandonTowerRequest())){
            this.clientListener.onShowWindow(WindowName.MODE);
        }
    }
}
