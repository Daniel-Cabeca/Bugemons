package ulb.mapper.item;

import ulb.DTO.item.ItemDTO;
import ulb.mapper.effect.EffectMapper;
import ulb.model.item.Item;

/**
 * Used to convert Regular Item to DTO Item
 */
public class ItemMapper {
    private ItemMapper() {}

    public static Item toEntity(ItemDTO dto) {
        if (dto == null) return null;
        return new Item(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                dto.getCategory(),
                EffectMapper.toEntity(dto.getEffect()),
                dto.getSprite()
        );
    }

    public static ItemDTO toDTO(Item entity) {
        if (entity == null) return null;
        return new ItemDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCategory(),
                EffectMapper.toDTO(entity.getEffect()),
                entity.getSprite()
        );
    }
}