package ulb.model.team;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.junit.Assert;

import ulb.model.Bugemon;
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
            100, 
            1
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
}
