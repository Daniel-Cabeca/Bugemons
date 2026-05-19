package ulb.repository.database;

import ulb.exceptions.LoadException;
import ulb.repository.TowerSaveRepository;
import ulb.repository.database.sql.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TowerSaveDatabaseRepository implements TowerSaveRepository {
	private final Database database;

	/**
	 * Creates a tower save repository using the provided database.
	 *
	 * @param database The database connection wrapper
	 */
	public TowerSaveDatabaseRepository(Database database) {
		this.database = database;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addTowerSave(Integer userId, Integer currentFloorId, List<Integer> completedRoomsId, Integer teamId) throws LoadException {
		String sql = "INSERT INTO tower_saves (user_id, current_floor_id, completed_rooms_id, current_team_id) VALUES " +
				"(?, ?, ?, ?)";
		String completedRoomsIdString = completedRoomsId.toString();
		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			stmt.setInt(2, currentFloorId);
			stmt.setString(3, completedRoomsIdString);
			stmt.setInt(4, teamId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new LoadException("Failed to insert save in tower_saves sql table: " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateTowerSave(Integer userId, Integer currentFloorId, List<Integer> completedRoomsId,
								Integer teamId) throws LoadException {
		String sql = "UPDATE tower_saves SET current_floor_id=?, completed_rooms_id=?, current_team_id=? WHERE " +
				"user_id=?";
		String completedRoomsIdString = completedRoomsId.toString();
		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setInt(1, currentFloorId);
			stmt.setString(2, completedRoomsIdString);
			stmt.setInt(3, teamId);
			stmt.setInt(4, userId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new LoadException("Failed to update save in tower_saves sql table: " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteTowerInfo(Integer userId) throws LoadException {
		String sql = "DELETE FROM tower_saves WHERE user_id=?";
		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new LoadException("Failed to delete save from tower_saves sql table: " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTowerSaved(Integer userId) throws LoadException {
		String sql = "SELECT COUNT(1) AS present FROM tower_saves WHERE user_id=?";
		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("present") == 1;
			}
		} catch (SQLException e) {
			throw new LoadException("Failed to check user_id exists in tower_saves sql table: " + e.getMessage());
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Integer> getCompletedRoomsId(Integer userId) throws LoadException {
		String sql = "SELECT completed_rooms_id FROM tower_saves WHERE user_id = ?";
		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				String roomsIdString = rs.getString("completed_rooms_id");
				String[] roomsIdStringList = roomsIdString.replace("[", "").replace("]", "").split(", ");

				List<Integer> completedRoomsIdList = new ArrayList<>();

				for (String roomId : roomsIdStringList) {
					completedRoomsIdList.add(Integer.parseInt(roomId));
				}
				return completedRoomsIdList;
			}
		} catch (SQLException e) {
			throw new LoadException("Failed to insert item to tower_saves: " + e.getMessage());
		}
		return List.of();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Integer> getCurrentFloorId(Integer userId) throws LoadException {
		String sql = "SELECT current_floor_id FROM tower_saves WHERE user_id=?";
		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return Optional.of(rs.getInt("current_floor_id"));
			}
		} catch (SQLException e) {
			throw new LoadException("Failed to get current_floor_id from tower_saves: " + e.getMessage());
		}
		return Optional.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Integer> getCurrentTeamId(Integer userId) throws LoadException {
		String sql = "SELECT current_team_id FROM tower_saves WHERE user_id=?";
		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return Optional.of(rs.getInt("current_team_id"));
			}
		} catch (SQLException e) {
			throw new LoadException("Failed to get current_team_id from tower_saves: " + e.getMessage());
		}
		return Optional.empty();
	}

}
