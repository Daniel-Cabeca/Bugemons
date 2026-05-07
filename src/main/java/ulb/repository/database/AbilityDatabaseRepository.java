package ulb.repository.database;

import ulb.model.ability.Ability;
import ulb.model.effect.*;
import ulb.model.type.Type;
import ulb.repository.AbilityRepository;
import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.repository.database.sql.Database;
import ulb.utils.DuplicateElementException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database-backed implementation of the ability repository.
 */
public class AbilityDatabaseRepository implements AbilityRepository {
	private static final Logger LOGGER = Logger.getLogger(AbilityDatabaseRepository.class.getName());

	private final Database database;

	/**
	 * Creates an ability repository using the provided database.
	 *
	 * @param database The database connection wrapper
	 */
	public AbilityDatabaseRepository(Database database) {
		this.database = database;
	}

	/**
	 * Inserts multiple abilities into the database.
	 *
	 * @param abilities The abilities to insert
	 * @throws DuplicateElementException If one ability already exists
	 */
	public void insertAbilities(Iterable<Ability> abilities) throws DuplicateElementException {
		for (Ability ability : abilities) {
			this.insertAbility(ability);
		}
	}

	/**
	 * Inserts a single ability into the database.
	 *
	 * @param ability The ability to insert
	 * @throws DuplicateElementException If the ability already exists
	 */
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
			for (Effect effect : ability.getEffects().getEffects()) {
				effectRepository.insert(effect, ability.getId(), false);
			}

		} catch (SQLException e) {
			throw new DuplicateElementException("Failed to insert item: " + ability.getId() + " (" + e.getMessage() + ")");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Ability findById(String id) throws NoSuchElementException {
		String sql = """
				   SELECT a.*, e.id AS effect_id, e.type AS effect_type, e.target, e.value, 
				          esm.hp, esm.attack, esm.defense, esm.initiative, esm.duration
				   FROM abilities a
				   LEFT JOIN effects e ON a.id = e.ability_id
				   LEFT JOIN effect_stats_modifier esm ON e.id = esm.effect_id
				   WHERE a.id = ?
				""";

		try (PreparedStatement pstmt = this.database.prepareStatement(sql)) {
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();

			Ability ability = null;
			EffectList effects = new EffectList();

			while (rs.next()) {
				if (ability == null) {

					Type abilityType = Type.valueOf(rs.getString("type"));

					ability = new Ability(
							rs.getString("id"),
							rs.getString("name"),
							abilityType,
							rs.getString("description"),
							rs.getInt("power"),
							effects // On passe la référence de l'EffectList ici
					);
				}


				String effectId = rs.getString("effect_id");
				if (effectId != null) {
					Effect effect = null;
					EffectType type = EffectType.valueOf(rs.getString("effect_type"));
					EffectTarget target = EffectTarget.valueOf(rs.getString("target"));

					if (type == EffectType.HEAL) {
						effect = new EffectHeal(target, rs.getInt("value"));
					} else if (type == EffectType.STAT_MODIFIER) {
						Map<EffectStatType, Integer> statsChanges = new EnumMap<>(EffectStatType.class);
						statsChanges.put(EffectStatType.HP, rs.getInt("hp"));
						statsChanges.put(EffectStatType.ATTACK, rs.getInt("attack"));
						statsChanges.put(EffectStatType.DEFENSE, rs.getInt("defense"));
						statsChanges.put(EffectStatType.INITIATIVE, rs.getInt("initiative"));

						EffectStatDuration duration = EffectStatDuration.valueOf(rs.getString("duration"));
						effect = new EffectStatModifier(target, duration, statsChanges);
					}

					if (effect != null) {
						effects.add(effect);
					}
				}
			}

			if (ability == null) {
				throw new EntityNotFoundException("Ability", id);
			}

			return ability;

		} catch (SQLException e) {
			throw new RuntimeException("Erreur SQL lors de la recherche de l'ability", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<Ability> findAll() {
		List<Ability> abilities = new ArrayList<>();
		String sql = "SELECT id FROM abilities";


		try (PreparedStatement pstmt = this.database.prepareStatement(sql)) {
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String AbilityId = rs.getString("id");
				try {

					Ability ability = findById(AbilityId);
					if (ability != null) {
						abilities.add(ability);
					}
				} catch (NoSuchElementException e) {

					System.err.println("Erreur : ID trouvé mais item non chargeable : " + AbilityId);
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, "Failed to load abilities from database.", e);
		}

		return abilities;
	}
}


