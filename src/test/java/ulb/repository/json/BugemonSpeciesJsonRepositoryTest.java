package ulb.repository.json;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.io.ByteArrayInputStream;

import ulb.repository.AbilityRepository;
import ulb.repository.mock.AbilityMockRepository;

class BugemonSpeciesJsonRespositoryTest {
	@Test
	public void testLoadOne() throws Exception {
		String str = """
			{
				"bugemons": [
					{
					"id": "florachu",
					"nom": "Florachu",
					"type": "Flora",
					"stats": {
						"pv": 90,
						"attaque": 55,
						"defense": 40,
						"initiative": 50
					},
					"attaques": ["fouet_liane", "pollen_sournois", "racines_vives"],
					"sprite": "florachu.png",
					"starter": true
					}
				]
			}
			""";

		AbilityRepository abilityRepository = new AbilityMockRepository();
		InputStream stream = new ByteArrayInputStream(str.getBytes());
		BugemonSpeciesJsonRepository repository = new BugemonSpeciesJsonRepository(stream, abilityRepository);

		assertDoesNotThrow(() -> { repository.findById("florachu"); });
	}

	@Test
	public void testDefaultDoesNotThrow() throws Exception {
		AbilityRepository abilityRepository = new AbilityMockRepository();
		assertDoesNotThrow(() -> { new BugemonSpeciesJsonRepository(abilityRepository); });
	}
}
