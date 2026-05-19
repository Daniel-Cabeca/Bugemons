package ulb.model.team;

import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.model.bugemon.Bugemon;
import ulb.service.BugemonService;

import java.util.List;

public class OpponentTeamGenerator {

	/**
	 * Generates a random team for the opponent based on the player's team size
	 *
	 * @param playerTeam the player's team based on which the opponent's is generated
	 * @param bugemonService the service to get the list of possible Bugemons from
	 * @return the randomly generated opponent team
	 * @throws IllegalArgumentException if the player team is null, empty or not valid
	 */
	public static Team generateRandomOpponentTeam(Team playerTeam, BugemonService bugemonService) throws LoadException
			, EntityNotFoundException {
		if (playerTeam == null) {
			throw new IllegalArgumentException("Player team cannot be null.");
		}

		if (!playerTeam.isValid()) {
			throw new IllegalArgumentException("Player team must be valid to generate an opponent team.");
		}

		int teamSize = playerTeam.size();
		if (teamSize <= 0) {
			throw new IllegalArgumentException("Player team must contain at least one Bugemon.");
		}

		List<Bugemon> bugemons = bugemonService.spawnBugemonRandomDistinct(teamSize);
		Team opponentTeam = new Team();

		for (Bugemon bugemon : bugemons) {
			opponentTeam.add(bugemon);
		}

		return opponentTeam;
	}

}
