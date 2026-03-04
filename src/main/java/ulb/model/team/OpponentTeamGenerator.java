package ulb.model.team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ulb.model.bugemon.BugemonSpecies;
import ulb.model.bugemon.BugemonDatabase;

public class OpponentTeamGenerator {

	public static Team generateRandomOpponentTeam(Team playerTeam) throws Exception {
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

		List<BugemonSpecies> candidates = new ArrayList<>();
		for (BugemonSpecies species: BugemonDatabase.getInstance()) {
			candidates.add(species);
		}

		Collections.shuffle(candidates);

		Team opponentTeam = new Team();
		for (int i = 0; i < teamSize; i++) {
			BugemonSpecies selected = candidates.get(i);

			opponentTeam.add(selected.spawn());
		}

		return opponentTeam;
	}
}

