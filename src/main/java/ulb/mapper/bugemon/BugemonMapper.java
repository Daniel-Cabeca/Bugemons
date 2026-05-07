package ulb.mapper.bugemon;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.exceptions.MappingException;
import ulb.mapper.stats.StatsMapper;
import ulb.model.bugemon.Bugemon;

/**
 * Used to convert Regular Bugemon to DTO Bugemon
 */
public class BugemonMapper {
    private BugemonMapper() {}

    public static BugemonDTO toDTO(Bugemon entity) {
        if (entity == null) return null;
        return new BugemonDTO(
                BugemonSpeciesMapper.toDTO(entity.getSpecies()),
                StatsMapper.toDTO(entity.getBaseStats()),
                StatsMapper.toDTO(entity.getFightStats()),
                entity.getXp(),
                entity.getLevel(),
                entity.getRemainingReward()
        );
    }

    public static Bugemon toEntity(BugemonDTO dto) throws MappingException {
        if (dto == null) return null;
        Bugemon bugemon = new Bugemon(
                BugemonSpeciesMapper.toEntity(dto.getSpecies()));
                bugemon.setXp(dto.getXp());
                bugemon.setLevel(dto.getLevel());
                bugemon.setRemainingRewards(dto.getRemainingRewards());
                bugemon.setBaseStats(StatsMapper.toEntity(dto.getBaseStats()));
                bugemon.setFightStats(StatsMapper.toEntity(dto.getFightStats()));
        return bugemon;
    }
}
