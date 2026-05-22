package ulb.mapper.bugemon;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.exceptions.MappingException;
import ulb.mapper.ability.AbilityMapper;
import ulb.mapper.stats.StatsMapper;
import ulb.model.ability.AbilitySet;
import ulb.model.bugemon.BugemonSpecies;
import ulb.model.bugemon.Stats;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to convert Regular Bugemon Species to DTO Bugemon Species
 */
public class BugemonSpeciesMapper {

	private BugemonSpeciesMapper() {}

	/**
	 * Converts a BugemonSpeciesDTO to an entity.
	 *
	 * @param dto The BugemonSpecies DTO
	 * @return The corresponding entity or null
	 * @throws MappingException If mapping fails
	 */
	public static BugemonSpecies toEntity(BugemonSpeciesDTO dto) throws MappingException {
		if (dto == null) return null;

		Stats stats = StatsMapper.toEntity(dto.baseStats());
		AbilitySet abilitySet = new AbilitySet();

		List<AbilityDTO> abilities = dto.abilities();
		if (abilities != null) {
			for (int i = 0; i < abilities.size() && i < AbilitySet.SIZE; i++) {
				abilitySet.setAbility(i, AbilityMapper.toEntity(abilities.get(i)));
			}
		}

		return new BugemonSpecies(dto.id(), dto.name(), dto.type(), stats, abilitySet, dto.sprite(), dto.starter());
	}

	/**
	 * Converts a BugemonSpecies entity to a DTO.
	 *
	 * @param entity The BugemonSpecies entity
	 * @return The corresponding DTO or null
	 */
	public static BugemonSpeciesDTO toDTO(BugemonSpecies entity) {
		if (entity == null) return null;
		List<AbilityDTO> abilityDTOs = new ArrayList<>();

		if (entity.getAbilities() != null) {
			for (var ability : entity.getAbilities()) {
				abilityDTOs.add(AbilityMapper.toDTO(ability));
			}
		}

		BugemonSpeciesDTO dto = new BugemonSpeciesDTO(entity.getId(), entity.getName(), entity.getType(),
				StatsMapper.toDTO(entity.getBaseStats()), abilityDTOs, entity.getSprite(), entity.isStarter());

		return dto;
	}
}
