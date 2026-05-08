package ulb.model.battle;

import org.junit.jupiter.api.Test;
import ulb.model.Player;
import ulb.model.team.Team;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class MultiBattleSessionTest {
	private static final String USERNAME_1 = "Test player 1";
	private static final String USERNAME_2 = "Test player 2";

	private static final int USERID_1 = 1;
	private static final int USERID_2 = 2;

	private MultiBattleSession getFreshSession() {
		Player player1 = new Player(USERNAME_1, USERID_1);
		Player player2 = new Player(USERNAME_2, USERID_2);

		return new MultiBattleSession(player1, player2);
	}

	@Test
	public void testGetParticipant1FromId() {
		MultiBattleSession session = this.getFreshSession();
		MultiBattleParticipant participant = session.getParticipant(USERID_1);
		assertEquals(USERNAME_1, participant.getPlayer().getUsername());
	}

	@Test
	public void testGetParticipant2FromId() {
		MultiBattleSession session = this.getFreshSession();
		MultiBattleParticipant participant = session.getParticipant(USERID_2);
		assertEquals(USERNAME_2, participant.getPlayer().getUsername());
	}

	@Test
	public void testGetParticipantIncorrect() {
		MultiBattleSession session = this.getFreshSession();
		assertThrows(NoSuchElementException.class, () -> { session.getParticipant(-1); });
	}

	@Test public void isAcceptedFalseIfNeitherAccepted() {
		MultiBattleSession session = this.getFreshSession();
		assertFalse(session.isAccepted());
	}

	@Test public void isAcceptedFalseIfOnlyPlayer1Accepted() {
		MultiBattleSession session = this.getFreshSession();
		session.getParticipant(USERID_1).accept();
		assertFalse(session.isAccepted());
	}

	@Test public void isAcceptedFalseIfOnlyPlayer2Accepted() {
		MultiBattleSession session = this.getFreshSession();
		session.getParticipant(USERID_2).accept();
		assertFalse(session.isAccepted());
	}

	@Test public void isAcceptedTrueIfBothPlayersAccepted() {
		MultiBattleSession session = this.getFreshSession();
		session.getParticipant(USERID_1).accept();
		session.getParticipant(USERID_2).accept();
		assertTrue(session.isAccepted());
	}

	@Test
	public void isDeclinedFalseIfNeitherDeclined() {
		MultiBattleSession session = this.getFreshSession();
		assertFalse(session.isDeclined());
	}

	@Test
	public void isDeclinedTrueIfPlayer1Declined() {
		MultiBattleSession session = this.getFreshSession();
		session.getParticipant(USERID_1).decline();
		assertTrue(session.isDeclined());
	}

	@Test
	public void isDeclinedTrueIfPlayer2Declined() {
		MultiBattleSession session = this.getFreshSession();
		session.getParticipant(USERID_2).decline();
		assertTrue(session.isDeclined());
	}

	@Test
	public void isReadyFalseIfNeitherPickedTeams() {
		MultiBattleSession session = this.getFreshSession();

		session.getParticipant(USERID_1).accept();
		session.getParticipant(USERID_2).accept();

		assertFalse(session.isReady());
	}

	@Test
	public void isReadyFalseIfOnlyPlayer1PickedTeams() {
		MultiBattleSession session = this.getFreshSession();

		session.getParticipant(USERID_1).accept();
		session.getParticipant(USERID_2).accept();

		session.getParticipant(USERID_1).setTeam(new Team());
		assertFalse(session.isReady());
	}

	@Test
	public void isReadyFalseIfOnlyPlayer2PickedTeams() {
		MultiBattleSession session = this.getFreshSession();

		session.getParticipant(USERID_1).accept();
		session.getParticipant(USERID_2).accept();

		session.getParticipant(USERID_2).setTeam(new Team());
		assertFalse(session.isReady());
	}

	@Test
	public void isReadyFalseIfOnlyBothPlayersPickedTeams() {
		MultiBattleSession session = this.getFreshSession();

		session.getParticipant(USERID_1).accept();
		session.getParticipant(USERID_2).accept();

		session.getParticipant(USERID_1).setTeam(new Team());
		session.getParticipant(USERID_2).setTeam(new Team());
		assertTrue(session.isReady());
	}
}
