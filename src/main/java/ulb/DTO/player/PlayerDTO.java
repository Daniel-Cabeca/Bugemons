package ulb.DTO.player;

import java.util.List;
import java.util.Map;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;

public class PlayerDTO {
    private String name;
	private List<BugemonDTO> team;
	private Map<ItemDTO, Integer> inventory;
}
