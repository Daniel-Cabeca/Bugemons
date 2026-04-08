package ulb.controller;

import ulb.communication.Messenger.SocketMessenger;
import ulb.communication.types.BugemonSpeciesMessage;
import ulb.communication.types.ErrorMessage;
import ulb.communication.types.GetAllBugemonSpeciesMessage;
import ulb.communication.types.SetUpNormalModeMessage;
import ulb.communication.types.SetUpPlayerMessage;
import ulb.communication.types.SetUpTeamMessage;
import ulb.communication.types.SetUpTowerModeMessage;
import ulb.communication.types.SuccessMessage;
import ulb.controller.towerManager.TowerManager;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.mapper.bugemon.BugemonSpeciesMapper;
import ulb.mapper.player.PlayerMapper;
import ulb.model.Player;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.BugemonSpecies;
import ulb.model.team.Team;
import ulb.service.BugemonService;
import ulb.service.ServiceLoader;

import java.util.ArrayList;
import java.util.List;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.communication.Message;

public class ServerController extends Thread{
    private SocketMessenger socketMessenger;
    private boolean stop;

    private Player player;

    private TowerManager towerManager;

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
            sendMessage(new SuccessMessage());

        } else if (message instanceof SetUpNormalModeMessage){
            // TODO

        } else if (message instanceof SetUpTowerModeMessage){
            // TODO
        }
    }
}
