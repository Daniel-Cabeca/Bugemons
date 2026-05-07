package ulb.mapper.reward;

import ulb.DTO.reward.RewardDTO;
import ulb.exceptions.MappingException;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.mapper.stats.StatsMapper;
import ulb.model.reward.Reward;

/**
 * Used to convert Regular Reward to DTO Reward
 */
public class RewardMapper {
    private RewardMapper() {}

    public static RewardDTO toDTO(Reward entity) {
        if (entity == null) return null;
        return new RewardDTO(
                BugemonMapper.toDTO(entity.getBugemon()),
                StatsMapper.toDTO(entity.getStats())
        );
    }

    public static Reward toEntity(RewardDTO dto) throws MappingException {
        if (dto == null) return null;
        Reward reward = new Reward(BugemonMapper.toEntity(dto.bugemon()));
        reward.setStats(StatsMapper.toEntity(dto.stats()));
        return reward;
    }
}
