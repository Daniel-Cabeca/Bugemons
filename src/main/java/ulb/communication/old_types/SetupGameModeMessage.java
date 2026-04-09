package ulb.communication.old_types;

import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.communication.types.GameMode;
import ulb.controller.BattleController;
import ulb.controller.GameController;
import ulb.model.item.Inventory;
import ulb.model.team.Team;

/**
 * Message used to setup the game
 * The view sends the chosen game mode to the controller
 * The controller sends all the needed information for the display to the view
 */
public class SetupGameModeMessage implements Message {
    private GameMode gameMode;
    private Team teamA;
    private Inventory inventory;
    private BattleController BattleController;

    /**
     * Used by the view to send the chosen game mode to the controller
     */
    public SetupGameModeMessage(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Used by the controller to send the needed information to the view
     */
    public SetupGameModeMessage(GameMode gameMode, Team teamA, Inventory inventory, BattleController controller) {
        this.gameMode = gameMode;
        this.teamA = teamA;
        this.inventory = inventory;
        this.BattleController = controller;
    }

    public GameMode getGameMode() { return gameMode; }

    public Inventory getInventory() { return inventory; }

    public Team getTeamA() { return teamA; }

    public BattleController getBattleController() { return BattleController; }

    @Override
    public MessageType getMessageType() {
        return MessageType.SETUP_GAME_MODE;
    }

    @Override
    public Message handle(GameController controller) {
        return controller.applyOn(this);
    }
}
