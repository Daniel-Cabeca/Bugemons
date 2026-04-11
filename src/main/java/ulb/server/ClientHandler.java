package ulb.server;

import ulb.communication.Messenger.SocketMessenger;
import ulb.controller.action.Action;
import ulb.controller.action.Run;
import ulb.controller.action.Swap;
import ulb.controller.action.UseAbility;
import ulb.controller.action.UseItem;
import ulb.controller.towerManager.TowerManager;
import ulb.mapper.ability.AbilityMapper;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.mapper.bugemon.BugemonSpeciesMapper;
import ulb.mapper.item.ItemMapper;
import ulb.mapper.player.PlayerMapper;
import ulb.message.ClientToServerMessage;
import ulb.message.clientToServer.*;
import ulb.message.serverToClient.*;
import ulb.model.Player;
import ulb.model.ability.Ability;
import ulb.model.battle.Battle;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.BugemonSpecies;
import ulb.model.item.Item;
import ulb.model.team.OpponentTeamGenerator;
import ulb.model.team.Team;
import ulb.service.BugemonService;
import ulb.service.ServiceLoader;
import ulb.service.strategy.AI;
import ulb.service.strategy.StrategyRandom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.DTO.item.ItemDTO;

public class ClientHandler extends Thread implements ServerMessageHandler{
    private SocketMessenger socketMessenger;
    private boolean stop;

    private Player player;

    private TowerManager towerManager;
    private Battle battle;
    private Battle.ParticipantLabel teamLabel;
    private Thread opponentBot;

    public ClientHandler(SocketMessenger messenger){
        this.socketMessenger = messenger;
        this.stop = false;
    }

    @Override
    public void run(){
        while (! this.stop){
            handleMessage();
        }
        end();
    }

    public void stopProcess(){
        this.stop = true;
    }

    public ClientToServerMessage receiveMessage(){
        try{
			Serializable received = this.socketMessenger.receiveMessage();

			if (received instanceof ClientToServerMessage message) {
				return message;
			}

            return null;
        } catch (Exception e){
            stopProcess();
        }
        return null;
    }

    public void sendMessage(Serializable message){
        try{
            this.socketMessenger.sendMessage(message);
        } catch (Exception e){
            stopProcess();
        }
    }

    public void sendErrorMessage(String errorMessage){
        sendMessage(new StatusMessage(false, errorMessage));
    }

    public void sendSuccessMessage(){
        sendMessage(new StatusMessage(true));
    }

    public void end(){
        this.socketMessenger.close();
    }

	// SETUP 
	public void handle(RegisterMessage message){
		boolean success;
		System.out.println("Nouveau Player Reçu !");
		this.player = PlayerMapper.toEntity(message.getPlayer());
		// SEND SUCCES TO CLIENT

		if (message.isLogin()) {
			success = ServiceLoader.getAccountService().login(this.player.getName(), this.player.getPassword());
		}
		else {
			success = ServiceLoader.getAccountService().register(this.player.getName(), this.player.getPassword());
		}
		if (success) {
			sendSuccessMessage();
		}
		else {
			sendErrorMessage("Register failed");
		}
	}

	public void handle(SetUpTeamMessage message){
		Team team = new Team();
            
            for (BugemonDTO bugemonDTO : message.getTeam()){
                if (!team.add(BugemonMapper.toEntity(bugemonDTO))){
                    sendErrorMessage("Invalid Team");
                }
            }

            this.player.setTeam(team);
            sendSuccessMessage();
	}

	public void handle(SetUpNormalModeMessage message){
		if (player == null){
                sendErrorMessage("Player not initialized !");
                return;
            }
            Team teamB;
            try {
                teamB = OpponentTeamGenerator.generateRandomOpponentTeam(player.getTeam());
            } catch (Exception e){
                sendErrorMessage(e.getMessage());
                return;
            }
            this.battle = new Battle(player.getTeam(), teamB, player);
            this.teamLabel = Battle.ParticipantLabel.TEAM_A;

            this.opponentBot = new AI(battle, new StrategyRandom());
            this.opponentBot.start();

            sendSuccessMessage();
	}

	public void handle(SetUpTowerModeMessage message){
		// TODO
	}

	// GAME INFO
	public void handle(CheckGameFinishedMessage message){
		sendMessage(new GameFinishedMessage(this.battle.isGameFinished()));
	}

	public void handle(GetBattleStateMessage message){
		sendMessage(new BattleStateMessage(this.battle.getState(teamLabel)));
	}

	public void handle(GetLogsMessage message){
		int selfHpAfterFirstAction = this.battle.getHpAfterFirstActionSelf(teamLabel);
            int opponentHpAfterFirstAction = this.battle.getHpAfterFirstActionOpponent(teamLabel);
            List<String> logs = this.battle.getLogMsg();
            
            if (message.clearLogs()){
                this.battle.clearLogMsg();
            }

            sendMessage(new LogsMessage(List.of(selfHpAfterFirstAction, opponentHpAfterFirstAction), logs));
	}

	public void handle(CheckUsableItemMessage message){
		Map<ItemDTO, Boolean> usableItems = new HashMap<ItemDTO, Boolean>();

		for (ItemDTO itemDTO : message.getItems()){
			Item item = ItemMapper.toEntity(itemDTO);
			usableItems.put(itemDTO, this.battle.checkItem(item, teamLabel));
		}

        sendMessage(new UsableItemsMessage(usableItems));
	}

	public void handle(GetAbilityEffectivenessMessage message){
		Map<AbilityDTO, String> effectiveness = new HashMap<AbilityDTO, String>();
		Bugemon bugemonTarger = BugemonMapper.toEntity(message.getBugemonTarget());
		
		for (AbilityDTO abilityDTO : message.getAbilities()){
			Ability ability = AbilityMapper.toEntity(abilityDTO);
			String effectivenessMessage = ability.getEffectivenessMessage(bugemonTarger);
			effectiveness.put(abilityDTO, effectivenessMessage);
		}

		sendMessage(new AbilityEffectivenessMessage(effectiveness));
	}
	public void handle(GetActiveBugemonsMessage message){
		if (battle == null){
			sendErrorMessage("The battle has not been created");
			return;
		}
		Bugemon selfActive = this.battle.getActiveBugemon(teamLabel);
		Bugemon opponentActive = this.battle.getActiveBugemon(this.battle.getOpponentTeamLabel(teamLabel)); 
		
		sendMessage(new ActiveBugemonsMessage(BugemonMapper.toDTO(selfActive), BugemonMapper.toDTO(opponentActive)));
	}

	public void handle(GetTowerInfoMessage message){
		//TODO
	}

	//ACTIONS
	public void handle(PickRandomActionMessage message){
		StrategyRandom strategyRandom = new StrategyRandom();
		Action randomAction = strategyRandom.pickAction(battle, teamLabel);
		this.battle.chooseAction(randomAction, teamLabel);

		sendSuccessMessage();
	}

	public void handle(RunMessage message){
		this.battle.chooseAction(new Run(), teamLabel);

        sendSuccessMessage();
	}

	public void handle(SwapBugemonMessage message){
		Bugemon bugemonToSwap = BugemonMapper.toEntity(message.getBugemonToSwap());
		this.battle.chooseAction(new Swap(bugemonToSwap), teamLabel);

		sendSuccessMessage();
	}

	public void handle(UseAbilityMessage message){
		Ability ability = AbilityMapper.toEntity(message.getAbility());
		this.battle.chooseAction(new UseAbility(ability), teamLabel);

		sendSuccessMessage();
	}

	public void handle(UseItemMessage message){
		Item item = ItemMapper.toEntity(message.getItem());
		this.battle.chooseAction(new UseItem(item), teamLabel);

		sendSuccessMessage();
	}
	
	// SPECIAL INFO
	public void handle(GetAllBugemonSpeciesMessage message){
		BugemonService bugemonService = ServiceLoader.getBugemonService();
		List<BugemonSpeciesDTO> DTOSpeciesList = new ArrayList<BugemonSpeciesDTO>();

		for (BugemonSpecies species : bugemonService.getAllSpecies()){
			DTOSpeciesList.add(BugemonSpeciesMapper.toDTO(species));
		}
		this.sendMessage(new BugemonSpeciesMessage(DTOSpeciesList));
	}

    private void handleMessage(){
        ClientToServerMessage message = receiveMessage();
        
        if (message == null){
            return;
        } 
		
		message.dispatch(this);
    }
}
