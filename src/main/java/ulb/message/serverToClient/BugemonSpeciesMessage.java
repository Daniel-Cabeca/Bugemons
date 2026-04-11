package ulb.message.serverToClient;

import java.io.Serializable;
import java.util.List;

import ulb.DTO.bugemon.BugemonSpeciesDTO;

public class BugemonSpeciesMessage implements Serializable {
    private List<BugemonSpeciesDTO> species;

    public BugemonSpeciesMessage(List<BugemonSpeciesDTO> species){
        this.species = species;
    }

    public List<BugemonSpeciesDTO> getSpecies(){
        return this.species;
    }
}
