package ulb.mapper.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.mapper.item.ItemMapper;
import ulb.model.Player;
import ulb.model.bugemon.Bugemon;
import ulb.model.item.Inventory;
import ulb.model.item.Item;
import ulb.service.InventoryService;
import ulb.service.ItemService;

/**
 * Used to convert Regular Player to DTO Player
 */
public class PlayerMapper {
    private PlayerMapper() {}

    public static PlayerDTO toDTO(Player entity) {
        if (entity == null) return null;

        List<BugemonDTO> team = new ArrayList<>();

        for (Bugemon b : entity.getTeam().getMembers())
            team.add(BugemonMapper.toDTO(b));

        Map<ItemDTO, Integer> inventory = new HashMap<>();
        
        for (Map.Entry<Item, Integer> e : entity.getInventory().getItems().entrySet())
            inventory.put(ItemMapper.toDTO(e.getKey()), e.getValue());

        return new PlayerDTO(entity.getUsername(), entity.getPassword(), team, inventory);
    }

    public static Player toEntity(PlayerDTO dto, boolean isLogin, ItemService itemService, InventoryService inventoryService) {
        if (dto == null) return null;

        String username = dto.getUserName();
        String password = dto.getPassword();
        if (isLogin){
            Inventory inventory = inventoryService.getInventoryFromDatabase(username);
            return new Player(username, password, inventory);
        }
        else {
            return new Player(username, password, itemService, inventoryService);
        }
    }
}