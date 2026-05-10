package ulb.message.serverToClient.gameData;

import java.io.Serializable;
import java.util.List;

import ulb.DTO.bugemon.BugemonSpeciesDTO;

public class BugemonSpeciesMessage implements Serializable {
    private final List<BugemonSpeciesDTO> species;

    public BugemonSpeciesMessage(List<BugemonSpeciesDTO> species){
        this.species = species;
    }

    public List<BugemonSpeciesDTO> getSpecies(){
        return this.species;
    }
}
