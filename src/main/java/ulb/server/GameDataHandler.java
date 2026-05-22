package ulb.server;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.exceptions.DataAccessException;
import ulb.mapper.ability.AbilityMapper;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.mapper.bugemon.BugemonSpeciesMapper;
import ulb.mapper.item.ItemMapper;
import ulb.message.response.gameData.BugemonSpeciesResponse;
import ulb.message.response.gameData.RandomAbilityResponse;
import ulb.message.response.gameData.RandomItemResponse;
import ulb.model.ability.Ability;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.BugemonSpecies;
import ulb.model.item.Item;
import ulb.service.AbilityService;
import ulb.service.BugemonService;
import ulb.service.ItemService;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles requests for static or randomly generated game data, such as bugemon species, abilities, and items.
 */
public class GameDataHandler {
	private final BugemonService bugemonService;
	private final AbilityService abilityService;
	private final ItemService itemService;
	ClientHandler clientHandler;

	/**
	 * Creates a game data handler for the given client and services.
	 *
	 * @param clientHandler the client handler owning this session
	 * @param bugemonService service for bugemon operations
	 * @param abilityService service for ability operations
	 * @param itemService service for item operations
	 */
	public GameDataHandler(ClientHandler clientHandler, BugemonService bugemonService, AbilityService abilityService,
						   ItemService itemService) {
		this.clientHandler = clientHandler;
		this.bugemonService = bugemonService;
		this.abilityService = abilityService;
		this.itemService = itemService;
	}

	/**
	 * Retrieves all available bugemon species and sends them to the client.
	 *
	 * @throws DataAccessException if the species list cannot be retrieved
	 */
	public void getAllBugemonSpecies() throws DataAccessException {
		List<BugemonSpeciesDTO> DTOSpeciesList = new ArrayList<>();

		try {
			for (BugemonSpecies species : bugemonService.getAllSpecies()) {
				DTOSpeciesList.add(BugemonSpeciesMapper.toDTO(species));
			}
		} catch (Exception e) {
			throw new DataAccessException("Cannot get all bugemon species");
		}

		clientHandler.sendMessage(new BugemonSpeciesResponse(DTOSpeciesList));
	}

	/**
	 * Retrieves a random ability compatible with the given bugemon's type and current ability set,
	 * and sends it to the client.
	 *
	 * @param bugemonDTO the bugemon for which a random ability is requested
	 * @throws DataAccessException if a random ability cannot be retrieved
	 */
	public void getRandomAbility(BugemonDTO bugemonDTO) throws DataAccessException {
		Bugemon bugemon = BugemonMapper.toEntity(bugemonDTO);

		Ability RandomAbility;
		try {
			RandomAbility = abilityService.getRandomAbility(bugemon.getType(), bugemon.getAbilities());
		} catch (Exception e) {
			throw new DataAccessException("Cannot get a random ability");
		}

		clientHandler.sendMessage(new RandomAbilityResponse(AbilityMapper.toDTO(RandomAbility)));
	}

	/**
	 * Retrieves a random item and sends it to the client.
	 *
	 * @throws DataAccessException if a random item cannot be retrieved
	 */
	public void getRandomItem() throws DataAccessException {
		Item randomItem;
		try {
			randomItem = itemService.getRandomItem();
		} catch (Exception e) {
			throw new DataAccessException("Cannot get a random item");
		}

		clientHandler.sendMessage(new RandomItemResponse(ItemMapper.toDTO(randomItem)));
	}
}
