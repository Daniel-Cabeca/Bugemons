package ulb.DTO.bugemon;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.stats.StatsDTO;
import ulb.model.type.Type;

import java.io.Serializable;
import java.util.List;

/**
 * Transferable BugemonSpecies, used on the vue side.
 */
public record BugemonSpeciesDTO(String id, String name, Type type, StatsDTO baseStats, List<AbilityDTO> abilities,
                                String sprite, boolean starter) implements Serializable {

	public String getSpritePath() { return "/png/" + sprite; }
}
