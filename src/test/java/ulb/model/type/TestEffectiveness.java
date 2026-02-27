package ulb.model.type;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestEffectiveness {
	private void testTypeEffectiveness(Type move, Type target, Effectiveness.Value expected) {
		Effectiveness obtained = new Effectiveness(move, target);
		assertEquals(obtained.getValue(), expected);
	}

	// Pyro

	@Test
	public void verifyPyroToPyro() {
		testTypeEffectiveness(Type.PYRO, Type.PYRO, Effectiveness.Value.NORMAL);
	}

	@Test
	public void verifyPyroToFlora() {
		testTypeEffectiveness(Type.PYRO, Type.FLORA, Effectiveness.Value.NORMAL);
	}

	@Test
	public void verifyPyroToAqua() {
		testTypeEffectiveness(Type.PYRO, Type.AQUA, Effectiveness.Value.LOW);
	}

	@Test
	public void verifyPyroToLitho() {
		testTypeEffectiveness(Type.PYRO, Type.LITHO, Effectiveness.Value.HIGH);
	}

	// Flora

	@Test
	public void verifyFloraToPyro() {
		testTypeEffectiveness(Type.FLORA, Type.PYRO, Effectiveness.Value.NORMAL);
	}

	@Test
	public void verifyFloraToFlora() {
		testTypeEffectiveness(Type.FLORA, Type.FLORA, Effectiveness.Value.NORMAL);
	}

	@Test
	public void verifyFloraToAqua() {
		testTypeEffectiveness(Type.FLORA, Type.AQUA, Effectiveness.Value.HIGH);
	}

	@Test
	public void verifyFloraToLitho() {
		testTypeEffectiveness(Type.FLORA, Type.LITHO, Effectiveness.Value.LOW);
	}

	// Aqua

	@Test
	public void verifyAquaToPyro() {
		testTypeEffectiveness(Type.AQUA, Type.PYRO, Effectiveness.Value.HIGH);
	}

	@Test
	public void verifyAquaToFlora() {
		testTypeEffectiveness(Type.AQUA, Type.FLORA, Effectiveness.Value.LOW);
	}

	@Test
	public void verifyAquaToAqua() {
		testTypeEffectiveness(Type.AQUA, Type.AQUA, Effectiveness.Value.NORMAL);
	}

	@Test
	public void verifyAquaToLitho() {
		testTypeEffectiveness(Type.AQUA, Type.LITHO, Effectiveness.Value.NORMAL);
	}

	// Litho

	@Test
	public void verifyLithoToPyro() {
		testTypeEffectiveness(Type.LITHO, Type.PYRO, Effectiveness.Value.LOW);
	}

	@Test
	public void verifyLithoToFlora() {
		testTypeEffectiveness(Type.LITHO, Type.FLORA, Effectiveness.Value.HIGH);
	}

	@Test
	public void verifyLithoToAqua() {
		testTypeEffectiveness(Type.LITHO, Type.AQUA, Effectiveness.Value.NORMAL);
	}

	@Test
	public void verifyLithoToLitho() {
		testTypeEffectiveness(Type.LITHO, Type.LITHO, Effectiveness.Value.NORMAL);
	}
}
