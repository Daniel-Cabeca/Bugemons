package ulb.repository.database;

import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.BugemonSpecies;
import ulb.model.bugemon.Stats;
import ulb.model.team.Team;
import ulb.exceptions.LoadException;
import ulb.repository.TeamRepository;
import ulb.repository.database.sql.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TeamDatabaseRepository implements TeamRepository {
	private static final Logger LOGGER = Logger.getLogger(TeamDatabaseRepository.class.getName());


    private final Database database;

    public TeamDatabaseRepository(Database database) throws LoadException {
        this.database = database;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertTeam(String username, Team team) throws LoadException {
        String sql = "INSERT INTO teams (user_id, team_name) VALUES ((SELECT id FROM users WHERE username = ?), ?)";
        try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, team.getTeamName());
            stmt.executeUpdate();

			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					team.setId(generatedKeys.getInt(1));
				}
			}
        } catch (SQLException e) {
            throw new LoadException("Failed to insert team: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertBugemonInTeam(Bugemon bugemon, int teamId) throws LoadException {
        String sql = "INSERT INTO team_members (team_id, bugemon_id) VALUES (?, ?)";
        try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
            stmt.setInt(1, teamId);
            stmt.setInt(2, bugemon.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new LoadException("Failed to insert bugemons in team: " + e.getMessage());
        }
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insertUserBugemon(Bugemon bugemon, String username) throws LoadException {
		String sql = """
        INSERT INTO bugemons (species_id, user_id, level, xp, remaining_rewards, hp, attack, defense, initiative) 
        VALUES (?, (SELECT id FROM users WHERE username = ?), ?, ?, ?, ?, ?, ?, ?)
    """;

		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {

			stmt.setString(1, bugemon.getSpecies().getId());
			stmt.setString(2, username);
			stmt.setInt(3, bugemon.getLevel());
			stmt.setInt(4, bugemon.getXp());

			stmt.setInt(5, 0);
			stmt.setInt(6, bugemon.getFightStats().getHp());
			stmt.setInt(7, bugemon.getFightStats().getAttack());
			stmt.setInt(8, bugemon.getFightStats().getDefense());
			stmt.setInt(9, bugemon.getFightStats().getInitiative());

			int affectedRows = stmt.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Creating bugemon failed, no rows affected.");
			}

			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {

					int newId = generatedKeys.getInt(1);


					bugemon.setId(newId);
				} else {
					throw new SQLException("Creating bugemon failed, no ID obtained.");
				}
			}

		} catch (SQLException e) {
			throw new LoadException("Failed to insert bugemon: " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Bugemon findBugemon(int id) throws NoSuchElementException {
		String sql = "SELECT * FROM bugemons WHERE id = ?";
		Bugemon bugemon = null;
		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				BugemonSpeciesDatabaseRepository speciesRepo = new BugemonSpeciesDatabaseRepository(this.database);
				String speciesId = rs.getString("species_id");
				BugemonSpecies species = speciesRepo.findById(speciesId);
				bugemon = new Bugemon(species);
				bugemon.setId(rs.getInt("id"));
				bugemon.setLevel(rs.getInt("level"));
				bugemon.setXp(rs.getInt("xp"));
				bugemon.setRemainingRewards(rs.getInt("remaining_rewards"));

				Stats savedStats = new Stats(
						rs.getInt("hp"),
						rs.getInt("attack"),
						rs.getInt("defense"),
						rs.getInt("initiative")
				);

				bugemon.setBaseStats(savedStats);

			}

		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, "Failed to load bugemon with id: " + id, e);
		}
		return bugemon;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Team findById(int id) throws NoSuchElementException {
		String sql = """
        SELECT t.team_name, tm.bugemon_id
        FROM teams t
        LEFT JOIN team_members tm ON t.team_id = tm.team_id
        WHERE t.team_id = ?
    """;

		List<Bugemon> bugemons = new ArrayList<>();
		String teamName = "";

		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				if (teamName.isEmpty()) {
					teamName = rs.getString("team_name");
				}
				int bugemonId = rs.getInt("bugemon_id");
				if (bugemonId != 0) {
					try {
						bugemons.add(findBugemon(bugemonId));
					} catch (NoSuchElementException e) {
						System.err.println("Erreur : ID trouvé mais item non chargeable : " + bugemonId);
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, "Failed to load team with id: " + id, e);
		}

		Team team = new Team(bugemons);
		team.setTeamName(teamName);
		team.setId(id);
		return team;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Team> findAll(String username) {
		String sql = """
        SELECT t.team_id
        FROM teams t
        JOIN users u ON t.user_id = u.id
        WHERE u.username = ?
    """;
		List<Team> teams = new ArrayList<>();
		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int teamId = rs.getInt("team_id");
				try {
					Team team = findById(teamId);
					teams.add(team);
				} catch (NoSuchElementException e) {
					System.err.println("Erreur : ID trouvé mais item non chargeable : " + teamId);
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, "Failed to load teams for user: " + username, e);
		}

		return teams;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean teamExists(String teamName, String username) throws LoadException {
		String sql = "SELECT 1 FROM teams WHERE team_name = ? AND user_id = (SELECT id FROM users WHERE username = ?)";

		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setString(1, teamName);
			stmt.setString(2, username);
			ResultSet rs = stmt.executeQuery();
			return rs.next();

		} catch (SQLException e) {
			throw new LoadException("Failed to check team existence: " + e.getMessage());
		}
	}
}
