package ulb.server;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.UserFacingException;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.mapper.item.ItemMapper;
import ulb.mapper.player.PlayerMapper;
import ulb.message.response.playerInfo.PlayerInventoryResponse;
import ulb.message.response.playerInfo.PlayerResponse;
import ulb.message.response.playerInfo.PlayerTeamResponse;
import ulb.model.Player;
import ulb.model.item.Inventory;
import ulb.model.item.Item;
import ulb.model.team.Team;
import ulb.service.AccountService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles requests for player-related information, such as profile data, inventory, and team composition.
 */

public class PlayerInfoHandler {
	private final AccountService accountService;
	ClientHandler clientHandler;

	public PlayerInfoHandler(ClientHandler clientHandler, AccountService accountService) {
		this.clientHandler = clientHandler;
		this.accountService = accountService;
	}

	/**
	 * Retrieves basic profile information for the player with the given username and sends it to the client
	 *
	 * @param username the username of the player to look up
	 * @throws UserFacingException if no account is found for the given username
	 */
	public void getPlayerInfo(String username) throws UserFacingException {
		try {
			Integer id = this.accountService.getUserId(username);
			Player player = new Player(username, id);
			PlayerDTO playerDTO = PlayerMapper.toDTO(player);
			clientHandler.sendMessage(new PlayerResponse(playerDTO));
		} catch (EntityNotFoundException e) {
			throw new UserFacingException("Wrong username");
		}
	}

	/**
	 * Retrieves the inventory of the currently connected player and sends it to the client
	 *
	 * @param username the username to validate against the connected player
	 * @throws UserFacingException if the given username does not match the connected player
	 */
	public void getPlayerInventory(String username) throws UserFacingException {
		Player player = clientHandler.getPlayer();

		if (username.equals(player.getUsername())) {
			Inventory inventory = player.getInventory();
			Map<ItemDTO, Integer> inventoryDTO = new HashMap<>();

			for (Map.Entry<Item, Integer> e : inventory.getItems().entrySet()) {
				inventoryDTO.put(ItemMapper.toDTO(e.getKey()), e.getValue());
			}
			clientHandler.sendMessage(new PlayerInventoryResponse(inventoryDTO));
		} else {
			throw new UserFacingException("Wrong username");
		}
	}

	/**
	 * Retrieves the team of the currently connected player and sends it to the client
	 *
	 * @param username the username to validate against the connected player
	 * @throws UserFacingException if the given username does not match the connected player
	 */
	public void getPlayerTeam(String username) throws UserFacingException {
		Player player = clientHandler.getPlayer();
		if (username.equals(player.getUsername())) {
			Team team = player.getTeam();
			List<BugemonDTO> teamDTO = team.getMembers().stream().map(BugemonMapper::toDTO).toList();

			clientHandler.sendMessage(new PlayerTeamResponse(teamDTO));
		} else {
			throw new UserFacingException("Wrong username");
		}
	}
}
