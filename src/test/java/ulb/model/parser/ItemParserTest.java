package ulb.model.parser;

import org.junit.jupiter.api.Test;

import ulb.model.Effect;
import ulb.model.item.Item;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(Effect.EffectType.SOIN, items.get(0).getEffect().getType());
        assertEquals(Effect.EffectTarget.LANCEUR, items.get(0).getEffect().getTarget());
        assertEquals(20, items.get(0).getEffect().getModifiers().get(Effect.StatType.PV));
    }

    @Test
    public void loadParsesEffectStatModifier() throws Exception {
        String path = getResourcePath(true);
        List<Item> items = ItemParser.loadItems(path);
        assertEquals(Effect.EffectType.STAT_MODIFIER, items.get(2).getEffect().getType());
        assertEquals(Effect.EffectTarget.LANCEUR, items.get(2).getEffect().getTarget());
        assertEquals(Effect.StatType.DEFENSE, items.get(2).getEffect().getModifiers().keySet().iterator().next());
        assertEquals(10, items.get(2).getEffect().getModifiers().get(Effect.StatType.DEFENSE));
        assertEquals(Effect.EffectDuration.PERMANENT, items.get(2).getEffect().getDuration());
    }

    @Test
    public void loadParsesEffectResetMalus() throws Exception {
        String path = getResourcePath(true);
        List<Item> items = ItemParser.loadItems(path);
        assertEquals(Effect.EffectType.RESET_MALUS, items.get(4).getEffect().getType());
        assertEquals(Effect.EffectTarget.LANCEUR, items.get(4).getEffect().getTarget());
    }

    @Test
    public void loadParsesEffectStatModifierMultiple() throws Exception {
        String path = getResourcePath(true);
        List<Item> items = ItemParser.loadItems(path);
        // Finds an item in the list with a stat_modifier_multiple effect type
        Item item = items.stream()
                .filter(i -> i.getId().equals("orbe_stimulant"))
                .findFirst()
                .orElse(null);

        assertEquals(Effect.EffectType.STAT_MODIFIER, item.getEffect().getType());
        assertEquals(Effect.EffectTarget.LANCEUR, item.getEffect().getTarget());
        assertEquals(
                Integer.valueOf(10),
                item.getEffect().getModifiers().get(Effect.StatType.ATTAQUE)
        );
        assertEquals(
                Integer.valueOf(10),
                item.getEffect().getModifiers().get(Effect.StatType.INITIATIVE)
        );
        assertEquals(Effect.EffectDuration.TOUR, item.getEffect().getDuration());
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
