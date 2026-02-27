package ulb.model.team;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import ulb.model.Bugemon;
import ulb.model.BugemonParser;

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

        List<Bugemon> allBugemons = loadAllBugemons();



        List<Bugemon> candidates = new ArrayList<>(allBugemons);
        Collections.shuffle(candidates);

        Team opponentTeam = new Team();
        for (int i = 0; i < teamSize; i++) {
            Bugemon selected = candidates.get(i);
			
            opponentTeam.add(selected);
        }

        return opponentTeam;
    }

    private static List<Bugemon> loadAllBugemons() throws Exception {
        URL resourceUrl = BugemonParser.class.getResource("/json/bugemons.json");
        if (resourceUrl == null) {
            throw new IllegalStateException("Bugemons resource file not found.");
        }

        String path = resourceUrl.getPath();
        Vector<Bugemon> bugemons = BugemonParser.loadBugemons(path);
        return new ArrayList<>(bugemons);
    }
}

