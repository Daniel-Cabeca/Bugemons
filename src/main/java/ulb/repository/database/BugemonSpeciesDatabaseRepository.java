package ulb.repository.database;

import ulb.model.ability.Ability;
import ulb.model.ability.AbilitySet;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.BugemonSpecies;
import ulb.model.bugemon.Stats;
import ulb.model.effect.Effect;
import ulb.model.type.Type;
import ulb.repository.BugemonSpeciesRepository;
import ulb.repository.LoadException;
import ulb.repository.database.sql.Database;
import ulb.utils.DuplicateElementException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
		// On récupère les infos de l'espèce + les IDs des capacités liées
		String sql = """
       SELECT bs.*, ba.ability_id
       FROM bugemon_species bs
       LEFT JOIN species_abilities ba ON bs.id = ba.species_id
       WHERE bs.id = ?
    """;

		try (PreparedStatement pstmt = this.database.prepareStatement(sql)) {
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();

			BugemonSpecies species = null;
			AbilitySet abilities = new AbilitySet();

			// On a besoin d'un repository pour charger les objets Ability complets par leur ID
			AbilityDatabaseRepository abilityRepo = new AbilityDatabaseRepository(this.database);
			int index = 0;
			while (rs.next()) {
				if (species == null) {
					// 1. Reconstitution des stats de base
					Stats baseStats = new Stats(
							rs.getInt("hp"),
							rs.getInt("attack"),
							rs.getInt("defense"),
							rs.getInt("initiative")
					);

					// 2. Création de l'objet Species
					species = new BugemonSpecies(
							rs.getString("id"),
							rs.getString("name"),
							Type.valueOf(rs.getString("type")),
							baseStats,
							abilities, // Référence vers l'AbilitySet qu'on va remplir
							rs.getString("sprite"),
							rs.getBoolean("starter")
					);
				}

				// 3. Ajout des capacités à l'AbilitySet
				String abilityId = rs.getString("ability_id");
				if (abilityId != null) {
					try {
						// On récupère l'objet Ability complet via son propre repo
						abilities.setAbility(index,abilityRepo.findById(abilityId));
					} catch (NoSuchElementException e) {
						System.err.println("Warning: Ability " + abilityId + " not found for species " + id);
					}
				}
			}

			if (species == null) {
				throw new NoSuchElementException("Espèce Bugemon non trouvée : " + id);
			}

			return species;

		} catch (SQLException e) {
			throw new RuntimeException("Erreur lors de la récupération de l'espèce Bugemon", e);
		}
	}

	@Override
	public Iterable<BugemonSpecies> findAll() {
		return null;
	}
}
