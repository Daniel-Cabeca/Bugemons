package ulb.mapper.stats;

import ulb.DTO.stats.StatsDTO;
import ulb.model.bugemon.Stats;

/**
 * Used to convert Regular Stat to DTO Stat
 */
public class StatsMapper {
    private StatsMapper() {}

    public static StatsDTO toDTO(Stats entity) {
        if (entity == null) return null;
        return new StatsDTO(
                entity.getHp(),
                entity.getAttack(),
                entity.getDefense(),
                entity.getInitiative()
        );
    }

    public static Stats toEntity(StatsDTO dto) {
        if (dto == null) return null;
        return new Stats(
                dto.hp(),
                dto.attack(),
                dto.defense(),
                dto.initiative()
        );
    }
}