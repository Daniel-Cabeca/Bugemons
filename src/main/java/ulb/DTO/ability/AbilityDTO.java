package ulb.DTO.ability;

import java.io.Serializable;
import java.util.List;

import ulb.DTO.effect.EffectDTO;
import ulb.model.type.Type;

/**
 * Transferable Ability, used on the vue side.
 */

public record AbilityDTO (String id,
						  String name,
						  Type type,
						  String description,
						  int power,
						  List<EffectDTO> effects) implements Serializable {}
