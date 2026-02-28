package ulb.model.type;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EffectivenessTest {
	// Factor

	private static final float EPSILON = 1e-6f;

	private void verifyFactor(Effectiveness.Value value, float expected) {
		float obtained = (new Effectiveness(value)).getFactor();
		assertEquals(obtained, expected, EPSILON);
	}

	@Test
	public void verifyFactorHigh() {
		verifyFactor(Effectiveness.Value.HIGH, 1.5f);
	}

	@Test
	public void verifyFactorNormal() {
		verifyFactor(Effectiveness.Value.NORMAL, 1.0f);
	}

	@Test
	public void verifyFactorLow() {
		verifyFactor(Effectiveness.Value.LOW, 0.75f);
	}

	// Value

	private void verifyTypeEffectiveness(Type move, Type target, Effectiveness.Value expected) {
		Effectiveness obtained = new Effectiveness(move, target);
		assertEquals(obtained.getValue(), expected);
	}

	// Pyro

	@Test
	public void verifyPyroToPyro() {
		verifyTypeEffectiveness(Type.PYRO, Type.PYRO, Effectiveness.Value.NORMAL);
	}

	@Test
	public void verifyPyroToFlora() {
		verifyTypeEffectiveness(Type.PYRO, Type.FLORA, Effectiveness.Value.NORMAL);
	}

	@Test
	public void verifyPyroToAqua() {
		verifyTypeEffectiveness(Type.PYRO, Type.AQUA, Effectiveness.Value.LOW);
	}

	@Test
	public void verifyPyroToLitho() {
		verifyTypeEffectiveness(Type.PYRO, Type.LITHO, Effectiveness.Value.HIGH);
	}

	// Flora

	@Test
	public void verifyFloraToPyro() {
		verifyTypeEffectiveness(Type.FLORA, Type.PYRO, Effectiveness.Value.NORMAL);
	}

	@Test
	public void verifyFloraToFlora() {
		verifyTypeEffectiveness(Type.FLORA, Type.FLORA, Effectiveness.Value.NORMAL);
	}

	@Test
	public void verifyFloraToAqua() {
		verifyTypeEffectiveness(Type.FLORA, Type.AQUA, Effectiveness.Value.HIGH);
	}

	@Test
	public void verifyFloraToLitho() {
		verifyTypeEffectiveness(Type.FLORA, Type.LITHO, Effectiveness.Value.LOW);
	}

	// Aqua

	@Test
	public void verifyAquaToPyro() {
		verifyTypeEffectiveness(Type.AQUA, Type.PYRO, Effectiveness.Value.HIGH);
	}

	@Test
	public void verifyAquaToFlora() {
		verifyTypeEffectiveness(Type.AQUA, Type.FLORA, Effectiveness.Value.LOW);
	}

	@Test
	public void verifyAquaToAqua() {
		verifyTypeEffectiveness(Type.AQUA, Type.AQUA, Effectiveness.Value.NORMAL);
	}

	@Test
	public void verifyAquaToLitho() {
		verifyTypeEffectiveness(Type.AQUA, Type.LITHO, Effectiveness.Value.NORMAL);
	}

	// Litho

	@Test
	public void verifyLithoToPyro() {
		verifyTypeEffectiveness(Type.LITHO, Type.PYRO, Effectiveness.Value.LOW);
	}

	@Test
	public void verifyLithoToFlora() {
		verifyTypeEffectiveness(Type.LITHO, Type.FLORA, Effectiveness.Value.HIGH);
	}

	@Test
	public void verifyLithoToAqua() {
		verifyTypeEffectiveness(Type.LITHO, Type.AQUA, Effectiveness.Value.NORMAL);
	}

	@Test
	public void verifyLithoToLitho() {
		verifyTypeEffectiveness(Type.LITHO, Type.LITHO, Effectiveness.Value.NORMAL);
	}
}
