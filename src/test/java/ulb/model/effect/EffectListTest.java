package ulb.model.effect;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

public class EffectListTest {
	@Test
	public void addOneEffect() {
		EffectList effects = new EffectList();
		effects.add(new Effect(
			Effect.EffectType.STAT_MODIFIER,
			Effect.EffectTarget.OWN_BUGEMON,
			Map.of(),
			Effect.EffectDuration.PERMANENT
		));

		assertEquals(1, effects.getSize());
	}
}
