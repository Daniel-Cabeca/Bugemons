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

        } else if (message instanceof GetAllBugemonSpeciesMessage){
            BugemonService bugemonService = ServiceLoader.getBugemonService();
            List<BugemonSpeciesDTO> DTOSpeciesList = new ArrayList<BugemonSpeciesDTO>();

            for (BugemonSpecies species : bugemonService.getAllSpecies()){
                DTOSpeciesList.add(BugemonSpeciesMapper.toDTO(species));
            }
            this.sendMessage(new BugemonSpeciesMessage(DTOSpeciesList));
        }
    }
}
