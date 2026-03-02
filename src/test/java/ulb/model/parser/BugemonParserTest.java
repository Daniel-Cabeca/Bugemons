package ulb.model.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Vector;

import ulb.model.Bugemon;

public class BugemonParserTest {
    @Test
    public void loadReturnsNonEmptyList() throws Exception {
        String path = getClass().getResource("/json/bugemons.json").getPath();
        Vector<Bugemon> bugemons = BugemonParser.loadBugemons(path);
        assertFalse(bugemons.isEmpty());
    }

    @Test
    public void loadReturnsCorrectSize() throws Exception {
        String path = getClass().getResource("/json/bugemons.json").getPath();
        Vector<Bugemon> bugemons = BugemonParser.loadBugemons(path);
        assertEquals(24, bugemons.size());
    }

    @Test
    public void loadParsesName() throws Exception {
        String path = getClass().getResource("/json/bugemons.json").getPath();
        Vector<Bugemon> bugemons = BugemonParser.loadBugemons(path);
        assertEquals("Florachu", bugemons.get(0).getName());
    }

    @Test
    public void loadParsesType() throws Exception {
        String path = getClass().getResource("/json/bugemons.json").getPath();
        Vector<Bugemon> bugemons = BugemonParser.loadBugemons(path);
        assertNotNull(bugemons.get(0).getType());
    }

    //TODO must be refactored
    // @Test
    // public void loadParsesAbilities() throws Exception {
    //     String path = getClass().getResource("/json/bugemons.json").getPath();
    //     Vector<Bugemon> bugemons = BugemonParser.loadBugemons(path);
    //     assertFalse(bugemons.get(0).getAbilities().isEmpty());
    // }
}
