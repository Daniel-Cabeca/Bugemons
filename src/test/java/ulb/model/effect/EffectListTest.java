package ulb.model.effect;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EffectListTest {
	@Test
	public void addOneEffect() {
		EffectList effects = new EffectList();
		effects.add(new EffectSwitch(EffectTarget.OWN_BUGEMON));

		assertEquals(1, effects.getSize());
	}
}
