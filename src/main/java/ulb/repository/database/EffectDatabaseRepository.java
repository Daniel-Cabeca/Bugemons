package ulb.repository.database;

import ulb.model.bugemon.Stats;
import ulb.model.effect.*;
import ulb.repository.database.sql.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Repository for saving and loading effects in the database.
 */
public class EffectDatabaseRepository {
	/** Database value for heal effects. */
	public final static String TYPESTR_HEAL = "HEAL";
	/** Database value for stat modifier effects. */
	public final static String TYPESTR_STAT_MODIFIER = "STAT_MODIFIER";
	/** Database value for reset malus effects. */
	public final static String TYPESTR_RESET_MALUS = "RESET_MALUS";
	/** Database value for switch effects. */
	public final static String TYPESTR_SWITCH = "SWITCH";

	private final Database database;

	/**
	 * Creates an effect repository using the provided database.
	 *
	 * @param database The database connection wrapper
	 */
	public EffectDatabaseRepository(Database database) {
		this.database = database;
	}

	/**
	 * Inserts an effect in the database.
	 *
	 * @param effect The effect to insert
	 * @param Id
	 */
	public void insert(Effect effect, String Id, boolean isItem) {
		String column = isItem ? "item_id" : "ability_id";
		String sql = "INSERT INTO effects (" + column + ", type, target, value) VALUES (?, ?, ?, ?)";

		try {
			PreparedStatement statement = this.database.prepareStatement(sql);

			statement.setString(1, Id);
			statement.setString(2, getTypeStr(effect));
			statement.setString(3, effect.getTarget().name());

			if (effect instanceof EffectHeal effectHeal) {
				this.completeInsertStatement(statement, effectHeal);
			} else if (effect instanceof EffectStatModifier effectStatModifier) {
				this.completeInsertStatement(statement, effectStatModifier);
			} else {
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException("Failed to insert effect in the database: " + e.getMessage());
		}
	}

	/**
	 * Returns the value to add to the database for the effect's type.
	 *
	 * @param effect The effect
	 * @return The value corresponding to the given effect's type
	 */
	private static String getTypeStr(Effect effect) {
		if (effect instanceof EffectHeal) {
			return TYPESTR_HEAL;
		} else if (effect instanceof EffectStatModifier) {
			return TYPESTR_STAT_MODIFIER;
		} else if (effect instanceof EffectResetMalus) {
			return TYPESTR_RESET_MALUS;
		} else if (effect instanceof EffectSwitch) {
			return TYPESTR_SWITCH;
		}

		throw new UnsupportedOperationException("Unsupported type: " + effect.getClass());
	}

	/**
	 * Fills the prepared statement for inserting a heal effect.
	 *
	 * @param statement The statement
	 * @param effect The heal effect
	 */
	private void completeInsertStatement(PreparedStatement statement, EffectHeal effect) throws SQLException {
		statement.setInt(4, effect.getValue());
		statement.executeUpdate();
	}

	/**
	 * Fills the prepared statement for inserting a stat modifier effect.
	 *
	 * @param statement The statement
	 * @param effect The stat modifier effect
	 */
	private void completeInsertStatement(PreparedStatement statement, EffectStatModifier effect) throws SQLException {
		statement.executeUpdate();

		ResultSet generatedKeys = statement.getGeneratedKeys();
		long effectId = generatedKeys.getLong(1);

		this.insertStats(effect.buildStatsChange(), effectId, effect.getDuration());
	}

	private void insertStats(Stats stats, long effectId, EffectStatDuration duration) throws SQLException {
		String sql = "INSERT INTO effect_stats_modifier (effect_id, hp,attack,defense,initiative,duration) VALUES (?, "
				+ "?, ?, ?,?,?)";
		PreparedStatement statement = this.database.prepareStatement(sql);

		statement.setLong(1, effectId);
		statement.setInt(2, stats.getHp());
		statement.setInt(3, stats.getAttack());
		statement.setInt(4, stats.getDefense());
		statement.setInt(5, stats.getInitiative());
		statement.setString(6, duration.name());

		statement.executeUpdate();
	}
}
