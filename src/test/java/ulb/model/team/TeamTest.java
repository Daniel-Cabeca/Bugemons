package ulb.model.team;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.junit.Assert;

import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;
import ulb.model.type.Type;



public class TeamTest {
    // Fonction auxiliaire qui crée des nouveaux Pokémons arbitraires
    private Bugemon makeBugemon(String name) {
        return new Bugemon(
            name,
            Type.AQUA, 
            100, 
            100, 
            100, 
            100
        );
    }
    
    @Test
    public void addOneBugemonIncreasesSize() {
        Team team = new Team();
        boolean added = team.add(makeBugemon("Florachu"));
        assertTrue(added);
        assertEquals(1, team.size());
        assertTrue(team.containsName("Florachu"));
        assertTrue(team.isValid());
    }

    @Test
    public void emptyTeamIsNotValid() {
        Team team = new Team();
        assertTrue(team.isEmpty());
        assertEquals(0, team.size());
        assertFalse(team.isValid());
    }

    @Test
    public void addSeventhBugemonIsRefused() {
        Team team = new Team();

        for (int i = 1; i <= 6; i++) {
            assertTrue(team.add(makeBugemon("Bugemon" + i)));
        }

        assertEquals(6, team.size());
        assertFalse(team.add(makeBugemon("Bugemon7")));
        assertEquals(6, team.size());
        assertTrue(team.isValid());
    }

    @Test
    public void addDuplicateNameIsRefused() {
        Team team = new Team();
        Bugemon first = makeBugemon("Florachu");
        Bugemon duplicatedPokemon = makeBugemon("Florachu");

        assertTrue(team.add(first));
        assertFalse(team.add(duplicatedPokemon));
        assertEquals(1, team.size());
    }

	@Test
	public void checkTeamKO_returnsFalse_whenAtLeastOneMemberAlive() {
		Bugemon alive = makeBugemon("Alive");
		Bugemon ko1 = makeBugemon("KO1");
		Bugemon ko2 = makeBugemon("KO2");
		Bugemon ko3 = makeBugemon("KO3");
		Bugemon ko4 = makeBugemon("KO4");
		Bugemon ko5 = makeBugemon("KO5");

		ko1.increaseFightStats(new Stats(-100, 0, 0, 0));
		ko2.increaseFightStats(new Stats(-100, 0, 0, 0));
		ko3.increaseFightStats(new Stats(-100, 0, 0, 0));
		ko4.increaseFightStats(new Stats(-100, 0, 0, 0));
		ko5.increaseFightStats(new Stats(-100, 0, 0, 0));

		Team team = new Team(List.of(alive, ko1, ko2, ko3, ko4, ko5));

		assertFalse(team.checkTeamKO());
	}

	@Test
	public void checkTeamKO_returnsTrue_whenAllMembersKO() {
		Bugemon ko1 = makeBugemon("KO1");
		Bugemon ko2 = makeBugemon("KO2");
		Bugemon ko3 = makeBugemon("KO3");
		Bugemon ko4 = makeBugemon("KO4");
		Bugemon ko5 = makeBugemon("KO5");
		Bugemon ko6 = makeBugemon("KO6");

		for (Bugemon b : List.of(ko1, ko2, ko3, ko4, ko5, ko6)) {
			b.increaseFightStats(new Stats(-100, 0, 0, 0));
		}

		Team team = new Team(List.of(ko1, ko2, ko3, ko4, ko5, ko6));

		assertTrue(team.checkTeamKO());
	}
}
