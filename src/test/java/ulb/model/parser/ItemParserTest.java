package ulb.model.parser;

import org.junit.Test;
import ulb.model.item.Item;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ItemParserTest {

    /**
     * Gets the resource file as an absolute path
     *
     * @param valid boolean indicating which resource file to use
     * @return the absolute path of the resource file
     * @throws Exception if the resource cannot be found, or if the resource URL cannot be converted to a URI
     */
    private String getResourcePath(boolean valid) throws Exception {
        // Uses a separate test JSON to avoid modifying or depending on the real data
        if (valid) {
            return new File(getClass().getResource("/json/objets_test.json").toURI()).getAbsolutePath();
        } else {
            return new File(getClass().getResource("/json/objets_invalid_effect.json").toURI()).getAbsolutePath();
        }
    }

    @Test
    public void loadReturnsNonEmptyList() throws Exception {
        String path = getResourcePath(true);
        List<Item> items = ItemParser.loadItems(path);
        assertNotNull(items);
    }

    @Test
    public void loadReturnsCorrectSize() throws Exception {
        String path = getResourcePath(true);
        List<Item> items = ItemParser.loadItems(path);
        assertEquals(10, items.size());
    }

    @Test
    public void loadParsesId() throws Exception {
        String path = getResourcePath(true);
        List<Item> items = ItemParser.loadItems(path);
        assertEquals("baie_revigorante", items.get(0).getId());
    }

    @Test
    public void loadParsesName() throws Exception {
        String path = getResourcePath(true);
        List<Item> items = ItemParser.loadItems(path);
        assertEquals("Baie Revigorante", items.get(0).getName());
    }

    @Test
    public void loadParsesDescription() throws Exception {
        String path = getResourcePath(true);
        List<Item> items = ItemParser.loadItems(path);
        assertEquals("Restaure 20 PV au Bugémon actif.", items.get(0).getDescription());
    }

    @Test
    public void loadParsesCategory() throws Exception {
        String path = getResourcePath(true);
        List<Item> items = ItemParser.loadItems(path);
        assertEquals("soin", items.get(0).getCategory());
    }

    @Test
    public void loadParsesEffectSoin() throws Exception {
        String path = getResourcePath(true);
        List<Item> items = ItemParser.loadItems(path);
        assertEquals("soin", items.get(0).getEffect().getType());
        assertEquals("lanceur", items.get(0).getEffect().getTarget());
        assertEquals(20, items.get(0).getEffect().getValue());
    }

    @Test
    public void loadParsesEffectStatModifier() throws Exception {
        String path = getResourcePath(true);
        List<Item> items = ItemParser.loadItems(path);
        assertEquals("stat_modifier", items.get(2).getEffect().getType());
        assertEquals("lanceur", items.get(2).getEffect().getTarget());
        assertEquals("defense", items.get(2).getEffect().getStat());
        assertEquals(10, items.get(2).getEffect().getModifier());
        assertEquals("permanent", items.get(2).getEffect().getDuration());
    }

    @Test
    public void loadParsesEffectResetMalus() throws Exception {
        String path = getResourcePath(true);
        List<Item> items = ItemParser.loadItems(path);
        assertEquals("reset_malus", items.get(4).getEffect().getType());
        assertEquals("lanceur", items.get(4).getEffect().getTarget());
    }

    @Test
    public void loadParsesEffectStatModifierMultiple() throws Exception {
        String path = getResourcePath(true);
        List<Item> items = ItemParser.loadItems(path);
        // Finds the first item in the list with a stat_modifier_multiple effect type
        Item item = items.stream()
                .filter(i -> i.getEffect().getType().equals("stat_modifier_multiple"))
                .findFirst()
                .orElse(null);

        assertEquals("stat_modifier_multiple", item.getEffect().getType());
        assertEquals("lanceur", item.getEffect().getTarget());
        assertEquals(
                Integer.valueOf(10),
                item.getEffect().getModifiers().get("attaque")
        );
        assertEquals(
                Integer.valueOf(10),
                item.getEffect().getModifiers().get("initiative")
        );
        assertEquals("tour", item.getEffect().getDuration());
    }

    @Test
    public void loadParsesSprite() throws Exception {
        String path = getResourcePath(true);
        List<Item> items = ItemParser.loadItems(path);
        assertEquals("baie_revigorante.png", items.get(0).getSprite());
    }

    @Test
    public void loadSkipsItemWithUnknownEffectType() throws Exception {
        String path = getResourcePath(false);
        List<Item> items = ItemParser.loadItems(path);
        assertEquals(0, items.size());
    }
    @Test
    public void loadThrowsOnInvalidPath() {
        assertThrows(IOException.class, () -> ItemParser.loadItems("/nonexistent/path/to/file.json"));
    }

    @Test
    public void loadStartingInventoryReturnsNonNull() throws Exception {
        String path = getResourcePath(true);
        Map<String, Integer> inventory = ItemParser.loadInventory(path);
        assertNotNull(inventory);
    }

    @Test
    public void loadStartingInventoryReturnsCorrectSize() throws Exception {
        String path = getResourcePath(true);
        Map<String, Integer> inventory = ItemParser.loadInventory(path);
        assertEquals(4, inventory.size());
    }

    @Test
    public void loadStartingInventoryParsesQuantity() throws Exception {
        String path = getResourcePath(true);
        Map<String, Integer> inventory = ItemParser.loadInventory(path);
        assertEquals(Integer.valueOf(3), inventory.get("baie_revigorante"));
        assertEquals(Integer.valueOf(2), inventory.get("baie_tonique"));
        assertEquals(Integer.valueOf(1), inventory.get("gel_defensif"));
        assertEquals(Integer.valueOf(1), inventory.get("serum_offensif"));
    }

    @Test
    public void loadStartingInventoryThrowsOnInvalidPath() {
        assertThrows(IOException.class, () -> ItemParser.loadInventory("/nonexistent/path/to/file.json"));
    }

}
