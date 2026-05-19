package ulb.repository.json;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class AbilityJsonRespositoryTest {
	@Test
	public void testLoadOne() throws Exception {
		String str = """
				{
					"attaques": [
						{
						"id": "fouet_liane",
						"nom": "Fouet-Liane",
						"type": "Flora",
						"description": "Inflige des dégâts et réduit légèrement la défense adverse.",
						"puissance": 40,
						"effets": [
							{
							"type": "stat_modifier",
							"cible": "adversaire",
							"stat": "defense",
							"modificateur": -5,
							"duree": "permanent"
							}
						]
						}
					]
				}
				""";

		InputStream stream = new ByteArrayInputStream(str.getBytes());
		AbilityJsonRepository repository = new AbilityJsonRepository(stream);

		assertDoesNotThrow(() -> {
			repository.findById("fouet_liane");
		});
	}

	@Test
	public void testDefaultDoesNotThrow() throws Exception {
		assertDoesNotThrow(() -> {
			new AbilityJsonRepository();
		});
	}
}
