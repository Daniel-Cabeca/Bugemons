package ulb.model.battle;

import org.junit.jupiter.api.Test;
import ulb.model.Player;
import ulb.model.team.Team;

import static org.junit.jupiter.api.Assertions.*;

public class MultiBattleParticipantTest {
	private MultiBattleParticipant getTestParticipant() {
		Player player = new Player("Test player", 1);
		return new MultiBattleParticipant(player, Battle.ParticipantLabel.TEAM_A);
	}

	@Test
	public void doesNotHaveTeamWhenCreated() {
		MultiBattleParticipant participant = getTestParticipant();
		assertFalse(participant.hasTeam());
	}

	@Test
	public void hasTeamAfterTeamHasBeenSet() {
		MultiBattleParticipant participant = getTestParticipant();
		Team team = new Team();

		participant.setTeam(team);
		assertTrue(participant.hasTeam());
	}
}
