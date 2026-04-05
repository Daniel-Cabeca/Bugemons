package ulb.repository.database;

import ulb.model.ability.Ability;
import ulb.model.effect.Effect;
import ulb.repository.AbilityRepository;
import ulb.repository.LoadException;
import ulb.repository.database.sql.Database;
import ulb.utils.DuplicateElementException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class AbilityDatabaseRepository implements AbilityRepository {
	private final Database database;

	public AbilityDatabaseRepository(Database database) throws LoadException {
		this.database = database;
	}

	public void insertAbilities(Iterable<Ability> abilities) throws DuplicateElementException {
		for (Ability ability: abilities) {
			this.insertAbility(ability);
		}
	}

	public void insertAbility(Ability ability) throws DuplicateElementException {
		String sqlAbility = "INSERT INTO abilities (id, name, type, description, power) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement statement = this.database.prepareStatement(sqlAbility)) {
			statement.setString(1, ability.getId());
			statement.setString(2, ability.getName());
			statement.setString(3, ability.getType().name()); // Enum Type (FEU, EAU...)
			statement.setString(4, ability.getDescription());
			statement.setInt(5, ability.getPower());

			statement.executeUpdate();

			EffectDatabaseRepository effectRepository = new EffectDatabaseRepository(this.database);
			for (Effect effect : ability.getEffects().getEffects()){
				effectRepository.insert(effect, ability.getId(),false);
			}

		} catch (SQLException e) {
			throw new DuplicateElementException("Failed to insert item: "+ ability.getId()+" ("+ e.getMessage() +")");
		}
	}
	@Override
	public Ability findById(String id) throws NoSuchElementException {
		return null;
	}

	@Override
	public Iterable<Ability> findAll() {
		return null;
	}
}


