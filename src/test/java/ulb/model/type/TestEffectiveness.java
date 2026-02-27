package ulb.model.type;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestEffectiveness {
	// Factor

	private static final float EPSILON = 1e-6f;

	private void verifyFactor(Effectiveness.Category category, float expected) {
		float obtained = Effectiveness.getFactor(category);
		assertEquals(obtained, expected, EPSILON);
	}

	@Test
	public void verifyFactorHigh() {
		verifyFactor(Effectiveness.Category.HIGH, 1.5f);
	}

	@Test
	public void verifyFactorNormal() {
		verifyFactor(Effectiveness.Category.NORMAL, 1.0f);
	}

	@Test
	public void verifyFactorLow() {
		verifyFactor(Effectiveness.Category.LOW, 0.5f);
	}

	// Category

	private void verifyTypeEffectiveness(Type move, Type target, Effectiveness.Category expected) {
		Effectiveness.Category obtained = Effectiveness.getCategory(move, target);
		assertEquals(obtained, expected);
	}

	// Pyro

	@Test
	public void verifyPyroToPyro() {
		verifyTypeEffectiveness(Type.PYRO, Type.PYRO, Effectiveness.Category.NORMAL);
	}

	@Test
	public void verifyPyroToFlora() {
		verifyTypeEffectiveness(Type.PYRO, Type.FLORA, Effectiveness.Category.NORMAL);
	}

	@Test
	public void verifyPyroToAqua() {
		verifyTypeEffectiveness(Type.PYRO, Type.AQUA, Effectiveness.Category.LOW);
	}

	@Test
	public void verifyPyroToLitho() {
		verifyTypeEffectiveness(Type.PYRO, Type.LITHO, Effectiveness.Category.HIGH);
	}

	// Flora

	@Test
	public void verifyFloraToPyro() {
		verifyTypeEffectiveness(Type.FLORA, Type.PYRO, Effectiveness.Category.NORMAL);
	}

	@Test
	public void verifyFloraToFlora() {
		verifyTypeEffectiveness(Type.FLORA, Type.FLORA, Effectiveness.Category.NORMAL);
	}

	@Test
	public void verifyFloraToAqua() {
		verifyTypeEffectiveness(Type.FLORA, Type.AQUA, Effectiveness.Category.HIGH);
	}

	@Test
	public void verifyFloraToLitho() {
		verifyTypeEffectiveness(Type.FLORA, Type.LITHO, Effectiveness.Category.LOW);
	}

	// Aqua

	@Test
	public void verifyAquaToPyro() {
		verifyTypeEffectiveness(Type.AQUA, Type.PYRO, Effectiveness.Category.HIGH);
	}

	@Test
	public void verifyAquaToFlora() {
		verifyTypeEffectiveness(Type.AQUA, Type.FLORA, Effectiveness.Category.LOW);
	}

	@Test
	public void verifyAquaToAqua() {
		verifyTypeEffectiveness(Type.AQUA, Type.AQUA, Effectiveness.Category.NORMAL);
	}

	@Test
	public void verifyAquaToLitho() {
		verifyTypeEffectiveness(Type.AQUA, Type.LITHO, Effectiveness.Category.NORMAL);
	}

	// Litho

	@Test
	public void verifyLithoToPyro() {
		verifyTypeEffectiveness(Type.LITHO, Type.PYRO, Effectiveness.Category.LOW);
	}

	@Test
	public void verifyLithoToFlora() {
		verifyTypeEffectiveness(Type.LITHO, Type.FLORA, Effectiveness.Category.HIGH);
	}

	@Test
	public void verifyLithoToAqua() {
		verifyTypeEffectiveness(Type.LITHO, Type.AQUA, Effectiveness.Category.NORMAL);
	}

	@Test
	public void verifyLithoToLitho() {
		verifyTypeEffectiveness(Type.LITHO, Type.LITHO, Effectiveness.Category.NORMAL);
	}
}
