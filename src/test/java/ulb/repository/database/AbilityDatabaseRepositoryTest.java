package ulb.repository.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ulb.model.ability.Ability;
import ulb.model.item.Item;
import ulb.model.type.Type;
import ulb.repository.database.sql.Database;
import ulb.repository.database.sql.DatabaseInMemory;
import ulb.repository.database.sql.DatabaseMock;
import ulb.utils.DuplicateElementException;

import java.util.Objects;


public class AbilityDatabaseRepositoryTest {


	@Test
	public void findByIdGivesCorrectAbility() {
		AbilityDatabaseRepository repository = new AbilityDatabaseRepository(new DatabaseMock());

		String id = "surchauffe";
		Ability obtained = repository.findById(id);
		assertEquals(id, obtained.getId());
	}
	@Test
	public void findAllGivesCorrectAbilities() throws DuplicateElementException {
		AbilityDatabaseRepository repository = new AbilityDatabaseRepository(new DatabaseInMemory());

		Ability ability1 = new Ability("1","kamehameha", Type.FLORA,"",5);
		Ability ability2 = new Ability("2","boom", Type.PYRO,"",2);

		repository.insertAbility(ability1);
		repository.insertAbility(ability2);


		Iterable<Ability> obtained = repository.findAll();
		boolean foundAbility1 = false;
		boolean foundAbility2 = false;

		for (Ability ability : obtained){
			String id = ability.getId();
			if (id.equals(ability1.getId())){
				foundAbility1 = true;
			}
			else if (id.equals(ability2.getId())) {
				foundAbility2 = true;
			}

		}
		assertTrue(foundAbility1);
		assertTrue(foundAbility2);

	}
}
