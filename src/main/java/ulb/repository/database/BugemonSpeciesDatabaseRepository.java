package ulb.repository.database;

import ulb.model.ability.Ability;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.BugemonSpecies;
import ulb.model.effect.Effect;
import ulb.repository.BugemonSpeciesRepository;
import ulb.repository.LoadException;
import ulb.repository.database.sql.Database;
import ulb.utils.DuplicateElementException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class BugemonSpeciesDatabaseRepository implements BugemonSpeciesRepository {
	private final Database database;

	public BugemonSpeciesDatabaseRepository(Database database) throws LoadException {
		this.database = database;
	}

	public void insertSpecies(Iterable<BugemonSpecies> Species) throws DuplicateElementException {
		for (BugemonSpecies specie: Species) {
			this.insertSpecie(specie);
		}
	}

	public void insertSpecie(BugemonSpecies specie) throws DuplicateElementException {
		String sqlSpecies = "INSERT INTO bugemon_species (id, name, type, sprite, starter, hp, attack, defense,initiative) VALUES (?, ?, ?, ?, ?, ?,?,?,?)";

		String sqlLinkAbility = "INSERT INTO species_abilities (species_id, ability_id) VALUES (?, ?)";

		try {


			// 1. Infos de base
			try (PreparedStatement statement = this.database.prepareStatement(sqlSpecies)) {
				statement.setString(1, specie.getId());
				statement.setString(2, specie.getName());
				statement.setString(3, specie.getType().name());
				statement.setString(4, specie.getSprite());
				statement.setInt(5, specie.isStarter() ? 1 : 0);
				statement.setInt(6, specie.getBaseStats().getHp());
				statement.setInt(7, specie.getBaseStats().getAttack());
				statement.setInt(8, specie.getBaseStats().getDefense());
				statement.setInt(9, specie.getBaseStats().getInitiative());
				statement.executeUpdate();
			}


			// 3. Liaison avec les capacités (Abilities)
			try (PreparedStatement pstmtLink = this.database.prepareStatement(sqlLinkAbility)) {
				for (int i = 0; i <= 2; i++) {
					pstmtLink.setString(1, specie.getId());
					pstmtLink.setString(2, specie.getAbilities().getAbility(i).getId());
					pstmtLink.addBatch(); // On utilise un batch pour la performance
				}
				pstmtLink.executeBatch();
			}


		} catch (SQLException e) {
			throw new DuplicateElementException("Failed to insert item: "+ specie.getId()+" ("+ e.getMessage() +")");
		}
	}

	@Override
	public BugemonSpecies findById(String id) throws NoSuchElementException {
		return null;
	}

	@Override
	public Iterable<BugemonSpecies> findAll() {
		return null;
	}
}
