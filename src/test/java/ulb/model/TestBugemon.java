package ulb.model;

import static org.junit.Assert.assertEquals;

import java.util.Vector;
import java.util.List;

import org.junit.Test;

public class TestBugemon {
    @Test
    public void xpBelowLevel() {
        Bugemon B = new Bugemon("A", Bugemon.Type.aqua, 10, 29, 35, 16, new Vector<>(), 1);
        B.gainXP(49);
        assertEquals(1, B.getLevel());
        assertEquals(49, B.getXP());
    }

    @Test 
    public void xpAbouveLevel() {
        Bugemon B = new Bugemon("A", Bugemon.Type.aqua, 10, 29, 35, 16, new Vector<>(), 1);
        B.gainXP(51);
        assertEquals(2, B.getLevel());
        assertEquals(1, B.getXP());
    }

    @Test 
    public void xpAboveMultipleLevels() {
        Bugemon B = new Bugemon("A", Bugemon.Type.aqua, 10, 29, 35, 16, new Vector<>(), 1);
        B.gainXP(300);
        assertEquals(4, B.getLevel());
        assertEquals(0, B.getXP());
    }

     @Test
     public void swapAbility() {
         Bugemon B = new Bugemon("A", Bugemon.Type.aqua, 10, 29, 35, 16, new Vector<String>(List.of("a", "b", "c")), 1);
         String newAbility = "d";
         B.swapAbility("a", newAbility);
         assertEquals(new Vector<String>(List.of("d", "b", "c")), B.getAbilities());
     }

     @Test
    public void swapNonexistentAbility() {
         Bugemon B = new Bugemon("A", Bugemon.Type.aqua, 10, 29, 35, 16, new Vector<String>(List.of("a", "b", "c")), 1);
         String newAbility = "d";
         B.swapAbility("e", newAbility);
         assertEquals(new Vector<String>(List.of("a", "b", "c")), B.getAbilities());
     }
}
