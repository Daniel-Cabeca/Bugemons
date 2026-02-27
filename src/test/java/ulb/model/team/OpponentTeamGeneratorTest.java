package ulb.model.team;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ulb.model.Bugemon;
import ulb.model.type.Type;

public class OpponentTeamGeneratorTest {

    private Bugemon makeBugemon(String name) {
        return new Bugemon(
            name,
            Type.AQUA,
            100,
            100,
            100,
            100,
            null,
            1
        );
    }

    @Test
    public void generatedOpponentTeamSameSizeAsPlayerTeam() throws Exception {
        Team playerTeam = new Team();
        playerTeam.add(makeBugemon("Florachu"));
        playerTeam.add(makeBugemon("Bugemon2"));
        playerTeam.add(makeBugemon("Bugemon3"));

        Team opponentTeam = OpponentTeamGenerator.generateRandomOpponentTeam(playerTeam);

        assertEquals(playerTeam.size(), opponentTeam.size());
        assertTrue(opponentTeam.isValid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotGenerateOpponentForEmptyTeam() throws Exception {
        Team emptyTeam = new Team();
        OpponentTeamGenerator.generateRandomOpponentTeam(emptyTeam);
    }
}

