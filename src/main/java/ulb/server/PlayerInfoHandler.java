package ulb.server;

import ulb.exceptions.DataAccessException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.mapper.item.ItemMapper;
import ulb.mapper.player.PlayerMapper;
import ulb.message.serverToClient.PlayerInventoryMessage;
import ulb.message.serverToClient.PlayerMessage;
import ulb.message.serverToClient.PlayerTeamMessage;
import ulb.model.Player;
import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.item.Inventory;
import ulb.model.item.Item;
import ulb.model.team.Team;
import ulb.service.AccountService;

public class PlayerInfoHandler {
    ClientHandler clientHandler;
    private final AccountService accountService;

    public PlayerInfoHandler(ClientHandler clientHandler, AccountService accountService) {
        this.clientHandler = clientHandler;
        this.accountService = accountService;
    }

	public void getPlayerInfo(String username) throws DataAccessException {
		try {
			int id = this.accountService.getUserId(username);

			Player player = new Player(username, id);
			PlayerDTO playerDTO = PlayerMapper.toDTO(player);
			clientHandler.sendMessage(new PlayerMessage(playerDTO));
		}
		catch (NoSuchElementException e) {
			clientHandler.sendErrorMessage("Wrong Username");
		}
	}

	public void getPlayerInventory(String username) throws DataAccessException {
        Player player = clientHandler.getPlayer();

		if (username.equals(player.getUsername())){
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

    public void getPlayerTeam() throws DataAccessException {
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
}
