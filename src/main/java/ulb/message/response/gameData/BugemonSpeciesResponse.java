package ulb.message.response.gameData;

import java.util.List;

import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.message.response.Response;

public class BugemonSpeciesResponse extends Response {
    private final List<BugemonSpeciesDTO> species;

    public BugemonSpeciesResponse(List<BugemonSpeciesDTO> species){
        this.species = species;
    }

    public List<BugemonSpeciesDTO> getSpecies(){
        return this.species;
    }
}
