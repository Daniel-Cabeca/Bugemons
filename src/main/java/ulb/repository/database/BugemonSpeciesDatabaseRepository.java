package ulb.repository.database;

import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.model.ability.Ability;
import ulb.model.ability.AbilitySet;
import ulb.model.bugemon.BugemonSpecies;
import ulb.model.bugemon.Stats;
import ulb.model.type.Type;
import ulb.repository.BugemonSpeciesRepository;
import ulb.repository.database.sql.Database;
import ulb.utils.DuplicateElementException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database-backed implementation for Bugemon species persistence.
 */
public class BugemonSpeciesDatabaseRepository implements BugemonSpeciesRepository {
	private static final Logger LOGGER = Logger.getLogger(BugemonSpeciesDatabaseRepository.class.getName());

	private final Database database;

	/**
	 * Creates a species repository using the provided database.
	 *
	 * @param database The database connection wrapper
	 */
	public BugemonSpeciesDatabaseRepository(Database database) {
		this.database = database;
	}

	/**
	 * Inserts multiple species into the database.
	 *
	 * @param Species The species to insert
	 * @throws DuplicateElementException If a species already exists
	 */
	public void insertSpecies(Iterable<BugemonSpecies> Species) throws DuplicateElementException {
		for (BugemonSpecies specie : Species) {
			this.insertSpecie(specie);
		}
	}

	/**
	 * Inserts a single species and its ability links into the database.
	 *
	 * @param specie The species to insert
	 * @throws DuplicateElementException If the species already exists
	 */
	public void insertSpecie(BugemonSpecies specie) throws DuplicateElementException {
		String sqlSpecies = "INSERT INTO bugemon_species (id, name, type, sprite, starter, hp, attack, defense," +
				"initiative) VALUES (?, ?, ?, ?, ?, ?,?,?,?)";

		String sqlLinkAbility = "INSERT INTO species_abilities (species_id, ability_id) VALUES (?, ?)";

		try {


			String sprite = specie.getSprite();
			try (PreparedStatement statement = this.database.prepareStatement(sqlSpecies)) {
				statement.setString(1, specie.getId());
				statement.setString(2, specie.getName());
				statement.setString(3, specie.getType().name());
				statement.setString(4, sprite);
				statement.setInt(5, specie.isStarter() ? 1 : 0);
				statement.setInt(6, specie.getBaseStats().getHp());
				statement.setInt(7, specie.getBaseStats().getAttack());
				statement.setInt(8, specie.getBaseStats().getDefense());
				statement.setInt(9, specie.getBaseStats().getInitiative());
				statement.executeUpdate();
			}


			try (PreparedStatement pstmtLink = this.database.prepareStatement(sqlLinkAbility)) {
				for (int i = 0; i <= 2; i++) {
					pstmtLink.setString(1, specie.getId());
					pstmtLink.setString(2, specie.getAbilities().getAbility(i).getId());
					pstmtLink.addBatch();
				}
				pstmtLink.executeBatch();
			}


		} catch (SQLException e) {
			throw new DuplicateElementException("Failed to insert item: " + specie.getId() + " (" + e.getMessage() +
					")");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BugemonSpecies findById(String id) throws LoadException, EntityNotFoundException {
		String sql = """
				   SELECT bs.*, ba.ability_id
				   FROM bugemon_species bs
				   LEFT JOIN species_abilities ba ON bs.id = ba.species_id
				   WHERE bs.id = ?
				""";
		try (PreparedStatement pstmt = this.database.prepareStatement(sql)) {
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();

			Optional<BugemonSpecies> species = Optional.empty();
			AbilitySet abilities = new AbilitySet();
			AbilityDatabaseRepository abilityRepo = new AbilityDatabaseRepository(this.database);

			int index = 0;
			while (rs.next()) {
				if (species.isEmpty()) {
					Stats baseStats = new Stats(rs.getInt("hp"), rs.getInt("attack"), rs.getInt("defense"), rs.getInt(
							"initiative"));
					species = Optional.of(new BugemonSpecies(rs.getString("id"), rs.getString("name"),
							Type.valueOf(rs.getString("type")), baseStats, abilities, rs.getString("sprite"),
							rs.getBoolean("starter")));
				}

				String abilityId = rs.getString("ability_id");
				if (abilityId != null && index < 3) {
					try {
						Ability ability = abilityRepo.findById(abilityId);
						abilities.setAbility(index, ability);
						index++;

					} catch (Exception e) {
						LOGGER.log(Level.WARNING, "Ability " + abilityId + " not found for species " + id);
						throw e;
					}
				}
			}
			if (species.isEmpty()) {
				throw new EntityNotFoundException("Bugemon species", id);
			}
			return species.get();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "SQL error while loading Bugemon species with id: " + id);
			throw new EntityNotFoundException("Bugemon species", id);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<BugemonSpecies> findAll() throws LoadException, EntityNotFoundException {
		List<BugemonSpecies> species = new ArrayList<>();
		String sql = "SELECT id FROM bugemon_species";

		try (PreparedStatement pstmt = this.database.prepareStatement(sql)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String bugemonId = rs.getString("id");
				try {
					BugemonSpecies bugemonSpecies = findById(bugemonId);
					species.add(bugemonSpecies);
				} catch (EntityNotFoundException e) {
					LOGGER.log(Level.WARNING, "ID found but Bugémon species could not be loaded: " + bugemonId);
					throw e;
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, "Failed to load Bugémon species from database.");
			throw new LoadException("Fail to fetch request: " + e.getMessage());
		}

		return species;
	}
}
