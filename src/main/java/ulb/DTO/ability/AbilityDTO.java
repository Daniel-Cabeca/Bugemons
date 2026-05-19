package ulb.DTO.ability;

import ulb.DTO.effect.EffectDTO;
import ulb.model.type.Type;

import java.io.Serializable;
import java.util.List;

/**
 * Transferable Ability, used on the vue side.
 */

public record AbilityDTO(String id, String name, Type type, String description, int power,
                         List<EffectDTO> effects) implements Serializable {
}
