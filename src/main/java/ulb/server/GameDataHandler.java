package ulb.server;

import java.util.ArrayList;
import java.util.List;

import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.mapper.ability.AbilityMapper;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.mapper.bugemon.BugemonSpeciesMapper;
import ulb.mapper.item.ItemMapper;
import ulb.message.clientToServer.GetAllBugemonSpeciesMessage;
import ulb.message.clientToServer.GetRandomAbilityMessage;
import ulb.message.clientToServer.GetRandomItemMessage;
import ulb.message.serverToClient.BugemonSpeciesMessage;
import ulb.message.serverToClient.RandomAbilityMessage;
import ulb.message.serverToClient.RandomItemMessage;
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
    public void handle(GetAllBugemonSpeciesMessage message){
		List<BugemonSpeciesDTO> DTOSpeciesList = new ArrayList<BugemonSpeciesDTO>();

		for (BugemonSpecies species : bugemonService.getAllSpecies()){
			DTOSpeciesList.add(BugemonSpeciesMapper.toDTO(species));
		}
		clientHandler.sendMessage(new BugemonSpeciesMessage(DTOSpeciesList));
	}

	public void handle(GetRandomAbilityMessage message){
		Bugemon bugemon = BugemonMapper.toEntity(message.getBugemon());

		Ability RandomAbility = abilityService.getRandomAbility(bugemon.getType(), bugemon.getAbilities());

		clientHandler.sendMessage(new RandomAbilityMessage(AbilityMapper.toDTO(RandomAbility)));
	}

	public void handle(GetRandomItemMessage message){
		Item randomItem = itemService.getRandomItem();

		clientHandler.sendMessage(new RandomItemMessage(ItemMapper.toDTO(randomItem)));
	}
}
