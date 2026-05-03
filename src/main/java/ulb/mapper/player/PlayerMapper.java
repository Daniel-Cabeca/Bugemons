package ulb.mapper.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.player.PlayerRegisterDTO;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.mapper.item.ItemMapper;
import ulb.model.Player;
import ulb.model.bugemon.Bugemon;
import ulb.model.item.Inventory;
import ulb.model.item.Item;

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

        return new PlayerDTO(entity.getUserId(), entity.getUsername(), team, inventory);
    }

    public static Player toEntity(PlayerRegisterDTO dto, Inventory inventory, int userId) {
        if (dto == null) return null;
		return new Player(dto.username(), userId, inventory);
    }
}