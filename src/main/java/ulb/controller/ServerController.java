package ulb.controller;

import ulb.communication.Messenger.SocketMessenger;
import ulb.communication.types.AbilityEffectivenessMessage;
import ulb.communication.types.ActiveBugemonsMessage;
import ulb.communication.types.BattleStateMessage;
import ulb.communication.types.BugemonSpeciesMessage;
import ulb.communication.types.CheckGameFinishedMessage;
import ulb.communication.types.CheckUsableItemMessage;
import ulb.communication.types.ErrorMessage;
import ulb.communication.types.GameFinishedMessage;
import ulb.communication.types.GetAbilityEffectivenessMessage;
import ulb.communication.types.GetActiveBugemonsMessage;
import ulb.communication.types.GetAllBugemonSpeciesMessage;
import ulb.communication.types.GetBattleStateMessage;
import ulb.communication.types.GetLogsMessage;
import ulb.communication.types.GetTowerInfoMessage;
import ulb.communication.types.LogsMessage;
import ulb.communication.types.RunMessage;
import ulb.communication.types.SetUpNormalModeMessage;
import ulb.communication.types.SetUpPlayerMessage;
import ulb.communication.types.SetUpTeamMessage;
import ulb.communication.types.SetUpTowerModeMessage;
import ulb.communication.types.SuccessMessage;
import ulb.communication.types.SwapBugemonMessage;
import ulb.communication.types.UsableItemsMessage;
import ulb.communication.types.UseAbilityMessage;
import ulb.communication.types.UseItemMessage;
import ulb.controller.action.Run;
import ulb.controller.action.Swap;
import ulb.controller.action.UseAbility;
import ulb.controller.action.UseItem;
import ulb.controller.strategy.Strategy;
import ulb.controller.strategy.StrategyRandom;
import ulb.controller.towerManager.TowerManager;
import ulb.mapper.ability.AbilityMapper;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.mapper.bugemon.BugemonSpeciesMapper;
import ulb.mapper.item.ItemMapper;
import ulb.mapper.player.PlayerMapper;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.DTO.item.ItemDTO;
import ulb.communication.Message;

public class ServerController extends Thread{
    private SocketMessenger socketMessenger;
    private boolean stop;

    private Player player;

    private TowerManager towerManager;
    private Battle battle;
    private Battle.ParticipantLabel teamLabel;
    private StrategyRandom opponentBot;
    private Thread botThread;

    public ServerController(SocketMessenger messenger){
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

    public Message receiveMessage(){
        try{
            return this.socketMessenger.receiveMessage();
        } catch (Exception e){
            stopProcess();
        }
        return null;
    }

    public void sendMessage(Message message){
        try{
            this.socketMessenger.sendMessage(message);
        } catch (Exception e){
            stopProcess();
        }
    }

    public void sendErrorMessage(String errorMessage){
        sendMessage(new ErrorMessage(errorMessage));
    }

    public void sendSuccessMessage(){
        sendMessage(new SuccessMessage());
    }

    public void end(){
        this.socketMessenger.close();
    }

    private void handleMessage(){
        Message message = receiveMessage();
        
        if (message == null){
            return;

        } else if (message instanceof SetUpPlayerMessage playerMessage){
            System.out.println("Nouveau Player Reçu !");
            this.player = PlayerMapper.toEntity(playerMessage.getPlayer());
            // SEND SUCCES TO CLIENT

        } else if (message instanceof GetAllBugemonSpeciesMessage){
            BugemonService bugemonService = ServiceLoader.getBugemonService();
            List<BugemonSpeciesDTO> DTOSpeciesList = new ArrayList<BugemonSpeciesDTO>();

            for (BugemonSpecies species : bugemonService.getAllSpecies()){
                DTOSpeciesList.add(BugemonSpeciesMapper.toDTO(species));
            }
            this.sendMessage(new BugemonSpeciesMessage(DTOSpeciesList));

        } else if (message instanceof SetUpTeamMessage teamMessage){
            Team team = new Team();

            for (BugemonDTO bugemonDTO : teamMessage.getTeam()){
                if (!team.add(BugemonMapper.toEntity(bugemonDTO))){
                    sendMessage(new ErrorMessage("Invalid Team"));
                }
            }

            this.player.setTeam(team);
            sendSuccessMessage();

        } else if (message instanceof SetUpNormalModeMessage){
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

            this.opponentBot = new StrategyRandom(this.battle);
            this.botThread = new Thread(this.opponentBot);
            this.botThread.start();

            sendSuccessMessage();

        } else if (message instanceof SetUpTowerModeMessage){
            // TODO

        } else if (message instanceof GetActiveBugemonsMessage){
            if (battle == null){
                sendErrorMessage("The battle has not been created");
                return;
            }
            Bugemon selfActive = this.battle.getActiveBugemon(teamLabel);
            Bugemon opponentActive = this.battle.getActiveBugemon(this.battle.getOpponentTeamLabel(teamLabel)); 
            sendMessage(new ActiveBugemonsMessage(BugemonMapper.toDTO(selfActive), BugemonMapper.toDTO(opponentActive)));

        } else if (message instanceof GetTowerInfoMessage){
            // TODO

        } else if (message instanceof GetAbilityEffectivenessMessage abilityEffectiveness){
            Map<AbilityDTO, String> effectiveness = new HashMap<AbilityDTO, String>();
            Bugemon bugemonTarger = BugemonMapper.toEntity(abilityEffectiveness.getBugemonTarget());
            
            for (AbilityDTO abilityDTO : abilityEffectiveness.getAbilities()){
                Ability ability = AbilityMapper.toEntity(abilityDTO);
                String effectivenessMessage = ability.getEffectivenessMessage(bugemonTarger);
                effectiveness.put(abilityDTO, effectivenessMessage);
            }

            sendMessage(new AbilityEffectivenessMessage(effectiveness));

        } else if (message instanceof GetLogsMessage logsMessage){
            int selfHpAfterFirstAction = this.battle.getHpAfterFirstActionSelf(teamLabel);
            int opponentHpAfterFirstAction = this.battle.getHpAfterFirstActionOpponent(teamLabel);
            List<String> logs = this.battle.getLogMsg();
            
            if (logsMessage.clearLogs()){
                this.battle.clearLogMsg();
            }

            sendMessage(new LogsMessage(List.of(selfHpAfterFirstAction, opponentHpAfterFirstAction), logs));

        } else if (message instanceof GetBattleStateMessage){
            sendMessage(new BattleStateMessage(this.battle.getState(teamLabel)));

        } else if (message instanceof CheckUsableItemMessage checkItems){
            Map<ItemDTO, Boolean> usableItems = new HashMap<ItemDTO, Boolean>();

            for (ItemDTO itemDTO : checkItems.getItems()){
                Item item = ItemMapper.toEntity(itemDTO);
                usableItems.put(itemDTO, this.battle.checkItem(item, teamLabel));
            }

            sendMessage(new UsableItemsMessage(usableItems));

        } else if (message instanceof CheckGameFinishedMessage){
            sendMessage(new GameFinishedMessage(this.battle.isGameFinished()));

        } else if (message instanceof UseItemMessage useItem){
            Item item = ItemMapper.toEntity(useItem.getItem());
            this.battle.chooseAction(new UseItem(item), teamLabel);

            sendSuccessMessage();
        } else if (message instanceof SwapBugemonMessage swapBugemon){
            Bugemon bugemonToSwap = BugemonMapper.toEntity(swapBugemon.getBugemonToSwap());
            this.battle.chooseAction(new Swap(bugemonToSwap), teamLabel);

            sendSuccessMessage();
        } else if (message instanceof UseAbilityMessage useAbility){
            Ability ability = AbilityMapper.toEntity(useAbility.getAbility());
            this.battle.chooseAction(new UseAbility(ability), teamLabel);

            sendSuccessMessage();
        } else if (message instanceof RunMessage){
            this.battle.chooseAction(new Run(), teamLabel);

            sendSuccessMessage();
        }
    }
}
