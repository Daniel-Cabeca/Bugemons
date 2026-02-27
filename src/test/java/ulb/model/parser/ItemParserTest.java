package ulb.model.parser;

import org.junit.Test;
import ulb.model.Item;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ItemParserTest {

    /**
     * @return the absolute path of the resource file
     * @throws Exception if the resource cannot be found, or if the resource URL cannot be converted to a URI
     */
    private String getResourcePath() throws Exception {
        // Uses a separate test JSON to avoid modifying or depending on the real data
        return new File(getClass().getResource("/json/objets_test.json").toURI()).getAbsolutePath();
    }

    @Test
    public void loadReturnsNonEmptyList() throws Exception {
        String path = getResourcePath();
        List<Item> items = ItemParser.loadItems(path);
        assertNotNull(items);
    }

    @Test
    public void loadReturnsCorrectSize() throws Exception {
        String path = getResourcePath();
        List<Item> items = ItemParser.loadItems(path);
        assertEquals(5, items.size());
    }

    @Test
    public void loadParsesId() throws Exception {
        String path = getResourcePath();
        List<Item> items = ItemParser.loadItems(path);
        assertEquals("baie_revigorante", items.get(0).getId());
    }

    @Test
    public void loadParsesName() throws Exception {
        String path = getResourcePath();
        List<Item> items = ItemParser.loadItems(path);
        assertEquals("Baie Revigorante", items.get(0).getName());
    }

    @Test
    public void loadParsesDescription() throws Exception {
        String path = getResourcePath();
        List<Item> items = ItemParser.loadItems(path);
        assertEquals("Restaure 20 PV au Bugémon actif.", items.get(0).getDescription());
    }

    @Test
    public void loadParsesCategory() throws Exception {
        String path = getResourcePath();
        List<Item> items = ItemParser.loadItems(path);
        assertEquals("soin", items.get(0).getCategory());
    }

    @Test
    public void loadParsesEffectSoin() throws Exception {
        String path = getResourcePath();
        List<Item> items = ItemParser.loadItems(path);
        assertEquals("soin", items.get(0).getEffect().getType());
        assertEquals("lanceur", items.get(0).getEffect().getTargetType());
        assertEquals(20, items.get(0).getEffect().getValue());
    }

    @Test
    public void loadParsesEffectStatModifier() throws Exception {
        String path = getResourcePath();
        List<Item> items = ItemParser.loadItems(path);
        assertEquals("stat_modifier", items.get(2).getEffect().getType());
        assertEquals("lanceur", items.get(2).getEffect().getTargetType());
        assertEquals("defense", items.get(2).getEffect().getStat());
        assertEquals(10, items.get(2).getEffect().getModifier());
        assertEquals("permanent", items.get(2).getEffect().getDuration());
    }

    @Test
    public void loadParsesEffectResetMalus() throws Exception {
        String path = getResourcePath();
        List<Item> items = ItemParser.loadItems(path);
        assertEquals("reset_malus", items.get(4).getEffect().getType());
        assertEquals("lanceur", items.get(4).getEffect().getTargetType());
    }

    @Test
    public void loadParsesSprite() throws Exception {
        String path = getResourcePath();
        List<Item> items = ItemParser.loadItems(path);
        assertEquals("baie_revigorante.png", items.get(0).getSprite());
    }
}
