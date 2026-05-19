package ulb.model.type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EffectivenessTest {
	// Factor

	private static final float EPSILON = 1e-6f;

	@Test
	public void highEffectivenssFactor() {
		verifyFactor(Effectiveness.Category.HIGH, Effectiveness.FACTOR_HIGH);
	}

	private void verifyFactor(Effectiveness.Category category, float expected) {
		float obtained = Effectiveness.getFactor(category);
		assertEquals(obtained, expected, EPSILON);
	}

	@Test
	public void normalEffectivenessFactor() {
		verifyFactor(Effectiveness.Category.NORMAL, Effectiveness.FACTOR_NORMAL);
	}

	@Test
	public void lowEffectivenessFactor() {
		verifyFactor(Effectiveness.Category.LOW, Effectiveness.FACTOR_LOW);
	}

	// Category

	@Test
	public void effectivenessPyroToPyro() {
		verifyTypeEffectiveness(Type.PYRO, Type.PYRO, Effectiveness.Category.NORMAL);
	}

	// Pyro

	private void verifyTypeEffectiveness(Type move, Type target, Effectiveness.Category expected) {
		Effectiveness.Category obtained = Effectiveness.getCategory(move, target);
		assertEquals(obtained, expected);
	}

	@Test
	public void effectivenessPyroToFlora() {
		verifyTypeEffectiveness(Type.PYRO, Type.FLORA, Effectiveness.Category.NORMAL);
	}

	@Test
	public void effectivenessPyroToAqua() {
		verifyTypeEffectiveness(Type.PYRO, Type.AQUA, Effectiveness.Category.LOW);
	}

	@Test
	public void effectivenessPyroToLitho() {
		verifyTypeEffectiveness(Type.PYRO, Type.LITHO, Effectiveness.Category.HIGH);
	}

	// Flora

	@Test
	public void effectivenessFloraToPyro() {
		verifyTypeEffectiveness(Type.FLORA, Type.PYRO, Effectiveness.Category.NORMAL);
	}

	@Test
	public void effectivenessFloraToFlora() {
		verifyTypeEffectiveness(Type.FLORA, Type.FLORA, Effectiveness.Category.NORMAL);
	}

	@Test
	public void effectivenessFloraToAqua() {
		verifyTypeEffectiveness(Type.FLORA, Type.AQUA, Effectiveness.Category.HIGH);
	}

	@Test
	public void effectivenessFloraToLitho() {
		verifyTypeEffectiveness(Type.FLORA, Type.LITHO, Effectiveness.Category.LOW);
	}

	// Aqua

	@Test
	public void effectivenessAquaToPyro() {
		verifyTypeEffectiveness(Type.AQUA, Type.PYRO, Effectiveness.Category.HIGH);
	}

	@Test
	public void effectivenessAquaToFlora() {
		verifyTypeEffectiveness(Type.AQUA, Type.FLORA, Effectiveness.Category.LOW);
	}

	@Test
	public void effectivenessAquaToAqua() {
		verifyTypeEffectiveness(Type.AQUA, Type.AQUA, Effectiveness.Category.NORMAL);
	}

	@Test
	public void effectivenessAquaToLitho() {
		verifyTypeEffectiveness(Type.AQUA, Type.LITHO, Effectiveness.Category.NORMAL);
	}

	// Litho

	@Test
	public void effectivenessLithoToPyro() {
		verifyTypeEffectiveness(Type.LITHO, Type.PYRO, Effectiveness.Category.LOW);
	}

	@Test
	public void effectivenessLithoToFlora() {
		verifyTypeEffectiveness(Type.LITHO, Type.FLORA, Effectiveness.Category.HIGH);
	}

	@Test
	public void effectivenessLithoToAqua() {
		verifyTypeEffectiveness(Type.LITHO, Type.AQUA, Effectiveness.Category.NORMAL);
	}

	@Test
	public void effectivenessLithoToLitho() {
		verifyTypeEffectiveness(Type.LITHO, Type.LITHO, Effectiveness.Category.NORMAL);
	}
}
