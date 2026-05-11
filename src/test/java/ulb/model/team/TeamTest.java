package ulb.model.team;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import ulb.model.ability.AbilitySet;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.BugemonSpecies;
import ulb.model.bugemon.Stats;
import ulb.model.type.Type;



public class TeamTest {
    // Fonction auxiliaire qui crée des nouveaux Pokémons arbitraires
    private Bugemon createBugemon(String name) {
        BugemonSpecies species = new BugemonSpecies(name, name, Type.AQUA, new Stats(100, 100, 100, 100), new AbilitySet(), "", false);
		return new Bugemon(species);
    }
    
    @Test
    public void addOneBugemonIncreasesSize() {
        Team team = new Team();
        boolean added = team.add(createBugemon("Florachu"));
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
    public void addingSeventhBugemonGetsRefused() {
        Team team = new Team();

        for (int i = 1; i <= 6; i++) {
            assertTrue(team.add(createBugemon("Bugemon" + i)));
        }

        assertEquals(6, team.size());
        assertFalse(team.add(createBugemon("Bugemon7")));
        assertEquals(6, team.size());
        assertTrue(team.isValid());
    }

    @Test
    public void addingDuplicateNameBugemonGetsRefused() {
        Team team = new Team();
        Bugemon first = createBugemon("Florachu");
        Bugemon duplicatedPokemon = createBugemon("Florachu");

        assertTrue(team.add(first));
        assertFalse(team.add(duplicatedPokemon));
        assertEquals(1, team.size());
    }

	@Test
	public void checkTeamKOReturnsFalseWhenAtLeastOneMemberAlive() {
		Bugemon alive = createBugemon("Alive");
		Bugemon ko1 = createBugemon("KO1");
		Bugemon ko2 = createBugemon("KO2");
		Bugemon ko3 = createBugemon("KO3");
		Bugemon ko4 = createBugemon("KO4");
		Bugemon ko5 = createBugemon("KO5");

		ko1.changeFightStats(new Stats(-100, 0, 0, 0));
		ko2.changeFightStats(new Stats(-100, 0, 0, 0));
		ko3.changeFightStats(new Stats(-100, 0, 0, 0));
		ko4.changeFightStats(new Stats(-100, 0, 0, 0));
		ko5.changeFightStats(new Stats(-100, 0, 0, 0));

		Team team = new Team(List.of(alive, ko1, ko2, ko3, ko4, ko5));

		assertFalse(team.checkTeamKO());
	}

	@Test
	public void checkTeamKOReturnsTrueWhenAllMembersKO() {
		Bugemon ko1 = createBugemon("KO1");
		Bugemon ko2 = createBugemon("KO2");
		Bugemon ko3 = createBugemon("KO3");
		Bugemon ko4 = createBugemon("KO4");
		Bugemon ko5 = createBugemon("KO5");
		Bugemon ko6 = createBugemon("KO6");

		for (Bugemon b : List.of(ko1, ko2, ko3, ko4, ko5, ko6)) {
			b.changeFightStats(new Stats(-100, 0, 0, 0));
		}

		Team team = new Team(List.of(ko1, ko2, ko3, ko4, ko5, ko6));

		assertTrue(team.checkTeamKO());
	}


    @Test
    public void bugemonNotPassedToTeamByCopy(){
        Bugemon b1 = createBugemon("B1");
        Bugemon b2 = createBugemon("B2");
        Bugemon b3 = createBugemon("B3");
        Team team = new Team(List.of(b1, b2, b3));

        b1.changeFightStats(new Stats(-100, 0, 0, 0));
        assertTrue(team.getMembers().get(0).getHp() == 0);
        assertTrue(team.getBugemon(0).getHp() == 0);
    }

    @Test
    public void getNextBugemon(){
        Bugemon b1 = createBugemon("B1");
        Bugemon b2 = createBugemon("B2");
        Bugemon b3 = createBugemon("B3");
        Team team = new Team(List.of(b1, b2, b3));

		assertTrue(team.getNextBugemon(b2).isPresent());
        assertEquals(b3, team.getNextBugemon(b2).get());
    }

    @Test 
    public void getNextNonKOBugemon(){
        Bugemon b1 = createBugemon("B1");
        Bugemon b2 = createBugemon("B2");
        Bugemon b3 = createBugemon("B3");
        Team team = new Team(List.of(b1, b2, b3));
        b2.changeFightStats(new Stats(-1000, 0, 0, 0));

		assertTrue(team.getNextBugemon(b1).isPresent());
        assertEquals(b3, team.getNextBugemon(b1).get());
    }

    @Test 
    public void getPreviousNonKOBugemon(){
        Bugemon b1 = createBugemon("B1");
        Bugemon b2 = createBugemon("B2");
        Bugemon b3 = createBugemon("B3");
        Team team = new Team(List.of(b1, b2, b3));
        b3.changeFightStats(new Stats(-1000, 0, 0, 0));

		assertTrue(team.getNextBugemon(b2).isPresent());
        assertEquals(b1, team.getNextBugemon(b2).get());
    }

    @Test
    public void getAllBugemonsAlive(){
        Bugemon b1 = createBugemon("B1");
        Bugemon b2 = createBugemon("B2");
        Bugemon b3 = createBugemon("B3");
        Team team = new Team(List.of(b1, b2, b3));

        assertEquals(new ArrayList<>(List.of(b1, b2, b3)), team.getBugemonsAlive());
    }

    @Test
    public void getSomeBugemonsAlive(){
        Bugemon b1 = createBugemon("B1");
        Bugemon b2 = createBugemon("B2");
        Bugemon b3 = createBugemon("B3");
        Team team = new Team(List.of(b1, b2, b3));
        b3.changeFightStats(new Stats(-1000, 0, 0, 0));

        assertEquals(new ArrayList<>(List.of(b1, b2)), team.getBugemonsAlive());
    }
}
