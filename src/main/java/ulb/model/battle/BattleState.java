package ulb.model.battle;

/**
 * State of a participant in an ongoing battle.
 */
public enum BattleState {
    INGAME,
    SWAPPING,
    LOST,
    WON,
    WAITING
}
