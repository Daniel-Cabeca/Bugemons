package ulb.mapper.item;

import ulb.DTO.item.ItemDTO;
import ulb.exceptions.MappingException;
import ulb.mapper.effect.EffectMapper;
import ulb.model.item.Item;

/**
 * Used to convert Regular Item to DTO Item.
 */
public class ItemMapper {
	private ItemMapper() {}

	/**
	 * Converts an ItemDTO to an entity.
	 *
	 * @param dto The Item DTO
	 * @return The corresponding entity or null
	 * @throws MappingException If mapping fails
	 */
	public static Item toEntity(ItemDTO dto) throws MappingException {
		if (dto == null) return null;
		return new Item(dto.id(), dto.name(), dto.description(), dto.category(), EffectMapper.toEntity(dto.effect()),
				dto.sprite());
	}

	/**
	 * Converts an Item entity to a DTO.
	 *
	 * @param entity The Item entity
	 * @return The corresponding DTO or null
	 */
	public static ItemDTO toDTO(Item entity) {
		if (entity == null) return null;
		return new ItemDTO(entity.getId(), entity.getName(), entity.getDescription(), entity.getCategory(),
				EffectMapper.toDTO(entity.getEffect()), entity.getSprite());
	}
}
