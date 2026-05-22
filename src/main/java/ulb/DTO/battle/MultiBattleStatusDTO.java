package ulb.DTO.battle;
import java.io.Serializable;

/**
 * Transferable multiplayer battle status, used on the view side.
 */
public record MultiBattleStatusDTO(Status status) implements Serializable {

	/**
	 * Represents the possible states of a multiplayer battle session.
	 */
	public enum Status {
		NOT_CREATED, WAITING_ACCEPT, PICKING_TEAMS, BATTLE, END, DECLINED,
	}
}