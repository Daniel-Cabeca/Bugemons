package ulb.model.team;

import java.util.List;

import ulb.model.bugemon.Bugemon;
import ulb.service.BugemonService;
import ulb.service.ServiceLoader;

public class OpponentTeamGenerator {

	public static Team generateRandomOpponentTeam(Team playerTeam) throws Exception {
		BugemonService bugemonService = ServiceLoader.getBugemonService();

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

		for (Bugemon bugemon: bugemons) {
			opponentTeam.add(bugemon);
		}

		return opponentTeam;
	}
}
