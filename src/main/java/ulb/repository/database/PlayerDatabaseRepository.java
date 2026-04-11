package ulb.repository.database;

import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;
import ulb.repository.LoadException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDatabaseRepository {
    private final Connection connection;

    public PlayerDatabaseRepository(Connection connection) {
        this.connection = connection;
    }

    public void saveTeam(int userId, Team team) throws LoadException {
        String delete = "DELETE FROM bugemons WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(delete)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new LoadException(e.getMessage());
        }

        String insert = "INSERT INTO bugemons (species_id, user_id, level, xp, remaining_rewards, hp, attack, defense, initiative) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insert)) {
            for (Bugemon bugemon : team.getMembers()) {
                stmt.setString(1, bugemon.getId());
                stmt.setInt(2, userId);
                stmt.setInt(3, bugemon.getLevel());
                stmt.setInt(4, bugemon.getXp());
                stmt.setInt(5, bugemon.getRemainingReward());
                stmt.setInt(6, bugemon.getBaseStats().getHp());
                stmt.setInt(7, bugemon.getBaseStats().getAttack());
                stmt.setInt(8, bugemon.getBaseStats().getDefense());
                stmt.setInt(9, bugemon.getBaseStats().getInitiative());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            throw new LoadException(e.getMessage());
        }
    }

    public List<String> loadTeamSpeciesIds(int userId) throws LoadException {
        String sql = "SELECT species_id FROM bugemons WHERE user_id = ?";
        List<String> speciesIds = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                speciesIds.add(rs.getString("species_id"));
            }
        } catch (SQLException e) {
            throw new LoadException(e.getMessage());
        }
        return speciesIds;
    }
}
