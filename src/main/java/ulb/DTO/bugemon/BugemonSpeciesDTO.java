package ulb.DTO.bugemon;

import java.util.List;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.stats.StatsDTO;
import ulb.model.type.Type;

public class BugemonSpeciesDTO {
    private String id;
	private String name;
	private Type type;
	private StatsDTO baseStats;
	private List<AbilityDTO> abilities;
	private String sprite;
	private boolean starter;
}
