package ulb.view;

import javafx.event.ActionEvent;
import ulb.controller.BattleController;

public class BattleMenu {

    private BattleController battleController;

    public void setBattleController(BattleController battleController) {
        this.battleController = battleController;
    }

    public void handleAutomaticBattle(ActionEvent actionEvent) {
           battleController.switchToBattleWindow(battleController.getPlayer().getTeam(),true , actionEvent);
    }

    public void handleControlledBattle(ActionEvent actionEvent) {
           battleController.switchToBattleWindow(battleController.getPlayer().getTeam(),false , actionEvent);
    }

    
}
