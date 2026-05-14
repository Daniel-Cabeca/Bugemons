package ulb.server;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.exceptions.MappingException;

import java.util.ArrayList;
import java.util.List;

import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.mapper.ability.AbilityMapper;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.mapper.bugemon.BugemonSpeciesMapper;
import ulb.mapper.item.ItemMapper;
import ulb.message.response.gameData.*;
import ulb.model.ability.Ability;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.BugemonSpecies;
import ulb.model.item.Item;
import ulb.service.AbilityService;
import ulb.service.BugemonService;
import ulb.service.ItemService;

public class GameDataHandler {
    ClientHandler clientHandler;
    private final BugemonService bugemonService;
    private final AbilityService abilityService;
    private final ItemService itemService;

    public GameDataHandler(ClientHandler clientHandler, BugemonService bugemonService, AbilityService abilityService, ItemService itemService) {
        this.clientHandler = clientHandler;
        this.bugemonService = bugemonService;
        this.abilityService = abilityService;
        this.itemService = itemService;
    }

    public void getAllBugemonSpecies() {
		List<BugemonSpeciesDTO> DTOSpeciesList = new ArrayList<>();
		
		try {
			for (BugemonSpecies species : bugemonService.getAllSpecies()){
				DTOSpeciesList.add(BugemonSpeciesMapper.toDTO(species));
			}
		} catch (Exception e) {
			clientHandler.sendErrorMessage("Cannot get all bugemon species");
			return;
		}
		clientHandler.sendMessage(new BugemonSpeciesResponse(DTOSpeciesList));
	}

	public void getRandomAbility(BugemonDTO bugemonDTO) throws LoadException, DataAccessException {
		Bugemon bugemon = BugemonMapper.toEntity(bugemonDTO);
		
		Ability RandomAbility;
		try {
			RandomAbility = abilityService.getRandomAbility(bugemon.getType(), bugemon.getAbilities());
		} catch (Exception e) {
			clientHandler.sendErrorMessage("Failed to get random ability");
			return;
		}

		clientHandler.sendMessage(new RandomAbilityResponse(AbilityMapper.toDTO(RandomAbility)));
	}

	public void getRandomItem() {
		Item randomItem;
		try {
			randomItem = itemService.getRandomItem();
		} catch (Exception e) {
			clientHandler.sendErrorMessage("Cannot get a random item");
			return;
		}

		clientHandler.sendMessage(new RandomItemResponse(ItemMapper.toDTO(randomItem)));
	}
}
