package ulb.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.mapper.item.ItemMapper;
import ulb.mapper.player.PlayerMapper;
import ulb.message.clientToServer.GetPlayerInventoryMessage;
import ulb.message.clientToServer.GetPlayerMessage;
import ulb.message.clientToServer.GetPlayerTeamMessage;
import ulb.message.clientToServer.GetUserIdFromNameMessage;
import ulb.message.serverToClient.PlayerInventoryMessage;
import ulb.message.serverToClient.PlayerMessage;
import ulb.message.serverToClient.PlayerTeamMessage;
import ulb.message.serverToClient.UserIdMessage;
import ulb.model.Player;
import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.item.Inventory;
import ulb.model.item.Item;
import ulb.model.team.Team;
import ulb.service.AccountService;

public class PlayerInfoHandler {
    ClientHandler clientHandler;
    AccountService accountService;

    public PlayerInfoHandler(ClientHandler clientHandler, AccountService accountService) {
        this.clientHandler = clientHandler;
        this.accountService = accountService;
    }
    
    public void handle(GetPlayerMessage message) {
        Player player = clientHandler.getPlayer();
		if (message.getUsername().equals(player.getUsername())){
			PlayerDTO playerDTO = PlayerMapper.toDTO(player);
			clientHandler.sendMessage(new PlayerMessage(playerDTO));
		}
		else{
			clientHandler.sendErrorMessage("Wrong Username");
		}
	}

    public void handle(GetPlayerInventoryMessage message) {
        Player player = clientHandler.getPlayer();

		if (message.getUserName().equals(player.getUsername())){
			Inventory inventory = player.getInventory();
			Map<ItemDTO, Integer> inventoryDTO = new HashMap<>();
			
			for (Map.Entry<Item, Integer> e : inventory.getItems().entrySet()) {
				inventoryDTO.put(ItemMapper.toDTO(e.getKey()), e.getValue());
			}
			clientHandler.sendMessage(new PlayerInventoryMessage(inventoryDTO));
		}
		else{
			clientHandler.sendErrorMessage("Wrong Username");
		}
	}

    public void handle(GetPlayerTeamMessage message) {
        Battle battle = clientHandler.getBattle();
        ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		if (battle == null){
			clientHandler.sendErrorMessage("The battle has not been created");
			return;
		}
		Team team = battle.getTeam(teamLabel);
		List<BugemonDTO> teamDTO = team.getMembers()
				.stream()
				.map(BugemonMapper::toDTO)
				.toList();

		clientHandler.sendMessage(new PlayerTeamMessage(teamDTO));
	}

    public void handle(GetUserIdFromNameMessage message) {
		String name = message.getName();
		int id = accountService.getUserId(name);
		UserIdMessage response = new UserIdMessage(id);
		clientHandler.sendMessage(response);
	}
}
