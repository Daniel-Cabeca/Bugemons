package ulb.message.response.gameInfo;

import ulb.DTO.ability.AbilityDTO;
import ulb.message.response.Response;

import java.util.Map;

public class AbilityEffectivenessResponse extends Response {
	private final Map<AbilityDTO, String> effectiveness;

	public AbilityEffectivenessResponse(Map<AbilityDTO, String> effectiveness) {
		this.effectiveness = effectiveness;
	}

	public Map<AbilityDTO, String> getEffectiveness() { return this.effectiveness; }
}
