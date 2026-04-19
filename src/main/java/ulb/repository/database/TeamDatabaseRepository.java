package ulb.repository.database;

import ulb.model.bugemon.Bugemon;
import ulb.repository.LoadException;
import ulb.repository.TeamRepository;
import ulb.repository.database.sql.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeamDatabaseRepository implements TeamRepository {

    private final Database database;

    public TeamDatabaseRepository(Database database) throws LoadException {
        this.database = database;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertTeam(String username, String teamName) throws LoadException {
        String sql = "INSERT INTO teams (user_id, team_name) VALUES ((SELECT id FROM users WHERE username = ?), ?)";
        try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, teamName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new LoadException("Failed to insert team: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertBugemonInTeam(Bugemon bugemon, int teamId) throws LoadException {
        String sql = "INSERT INTO team_members (team_id, bugemon_species_id) VALUES (?, ?)";
        try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
            stmt.setInt(1, teamId);
            stmt.setString(2, bugemon.getSpeciesId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new LoadException("Failed to insert bugemons in team: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getTeamId(String teamName, String username) throws LoadException {
        String sql = "SELECT team_id FROM teams WHERE team_name = ? AND user_id = (SELECT id FROM users WHERE username = ?)";
        try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
            stmt.setString(1, teamName);
            stmt.setString(2, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("team_id");
            return -1;
        } catch (SQLException e) {
            throw new LoadException("Failed to fetch team id: " + e.getMessage());
        }
    }
}
