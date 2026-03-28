package ulb.communication.types;

import ulb.communication.Message;
import ulb.controller.BattleController;
import ulb.model.item.Inventory;
import ulb.model.team.Team;

public class SetupGameModeMessage implements Message {
    GameMode gameMode;
    Team teamA;
    Inventory inventory;
    BattleController BattleController;

    public SetupGameModeMessage(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public SetupGameModeMessage(GameMode gameMode, Team teamA, Team teamB, Inventory inventory, BattleController controller) {
        this.gameMode = gameMode;
        this.teamA = teamA;
        this.inventory = inventory;
        this.BattleController = controller;
    }

    public GameMode getGameMode() { return gameMode; }

    public Inventory getInventory() { return inventory; }

    public Team getTeamA() { return teamA; }

    public BattleController getBattleController() { return BattleController; }
}
