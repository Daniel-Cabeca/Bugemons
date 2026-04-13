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
import ulb.model.team.Team;
import ulb.service.ItemService;
import ulb.repository.InventoryRepository;
import ulb.repository.ItemRepository;
import ulb.repository.database.ItemDatabaseRepository;
import ulb.repository.json.InventoryJsonRepository;
import ulb.repository.json.ItemJsonRepository;

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

        return new PlayerDTO(entity.getName(), entity.getPassword(), team, inventory);
    }

    public static Player toEntity(PlayerDTO dto, ItemService itemService) {
        if (dto == null) return null;
        // List<Bugemon> members = new ArrayList<>();
        // for (BugemonDTO b : dto.getTeam())
        //     members.add(BugemonMapper.toEntity(b));
        // Inventory inventory = new Inventory();
        // for (Map.Entry<ItemDTO, Integer> e : dto.getInventory().entrySet())
        //     inventory.addItem(ItemMapper.toEntity(e.getKey()), e.getValue());
        return new Player(dto.getName(), dto.getPassword(), itemService);//, new Team(members), inventory);
    }
}