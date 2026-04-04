package ulb.DTO.ability;

import java.util.List;

import ulb.DTO.effect.EffectDTO;
import ulb.model.type.Type;

public class AbilityDTO {
    private String id;
	private String name;
	private Type type;
	private String description;
	private int power;
	private List<EffectDTO> effects;
}
