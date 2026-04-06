package ulb.controller;

import ulb.communication.Messenger.SocketMessenger;
import ulb.communication.types.BugemonSpeciesMessage;
import ulb.communication.types.GetAllBugemonSpeciesMessage;
import ulb.communication.types.SetUpPlayerMessage;
import ulb.mapper.bugemon.BugemonSpeciesMapper;
import ulb.model.bugemon.BugemonSpecies;
import ulb.service.BugemonService;
import ulb.service.ServiceLoader;

import java.util.ArrayList;
import java.util.List;

import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.communication.Message;

public class ServerController extends Thread{
    private SocketMessenger socketMessenger;
    private boolean stop;

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

    public void end(){
        this.socketMessenger.close();
    }

    private void handleMessage(){
        Message message = this.socketMessenger.receiveMessage();
        
        if (message instanceof SetUpPlayerMessage playerMessage){
            System.out.println("Nouveau Player Reçu !");
        } else if (message instanceof GetAllBugemonSpeciesMessage){

            BugemonService bugemonService = ServiceLoader.getBugemonService();
            List<BugemonSpeciesDTO> DTOSpeciesList = new ArrayList<BugemonSpeciesDTO>();

            for (BugemonSpecies species : bugemonService.getAllSpecies()){
                DTOSpeciesList.add(BugemonSpeciesMapper.toDTO(species));
            }
            socketMessenger.SendMessage(new BugemonSpeciesMessage(DTOSpeciesList));
        }
    }
}
