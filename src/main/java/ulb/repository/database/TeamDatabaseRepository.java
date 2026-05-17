package ulb.repository.database;

import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.BugemonSpecies;
import ulb.model.bugemon.Stats;
import ulb.model.team.Team;
import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.repository.TeamRepository;
import ulb.repository.database.sql.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TeamDatabaseRepository implements TeamRepository {
	private static final Logger LOGGER = Logger.getLogger(TeamDatabaseRepository.class.getName());


    private final Database database;

    public TeamDatabaseRepository(Database database) {
        this.database = database;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertTeam(int userId, Team team, boolean isTowerTeam) throws LoadException {
        String sql = "INSERT INTO teams (user_id, team_name, tower_team) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, team.getTeamName());
			stmt.setBoolean(3, isTowerTeam);
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
	public void deleteTowerTeam(int userId) throws LoadException{
		String sql = "DELETE FROM teams WHERE user_id = ? AND tower_team = 1";
		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new LoadException("Failed to insert bugemons in team: " + e.getMessage());
        }
	}

	/**
     * {@inheritDoc}
     */
	@Override 
	public void updateTowerTeam(int userId, Team team) throws LoadException{
		String sql = "UPDATE teams SET user_id = ?, team_name = ?, tower_team = 1";
        try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
            stmt.setInt(1, userId);
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
	public boolean hasTowerTeam(int userId) throws LoadException{
		String sql = "SELECT COUNT(1) AS present FROM teams WHERE user_id = ? AND tower_team = 1";
		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()){
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
    public void deleteBugemonInTeam(Bugemon bugemon, int teamId) throws LoadException {
        String sql = "DELETE FROM team_members WHERE team_id = ? AND bugemon_id = ?";
        try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
            stmt.setInt(1, teamId);
            stmt.setInt(2, bugemon.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new LoadException("Failed to insert bugemons in team: " + e.getMessage());
        }
    }

	private void executeUserBugemonStatement(PreparedStatement stmt, Bugemon bugemon, int userId, boolean useBugemonId) throws SQLException{
		stmt.setString(1, bugemon.getSpecies().getId());
		stmt.setInt(2, userId);
		stmt.setInt(3, bugemon.getLevel());
		stmt.setInt(4, bugemon.getXp());

		stmt.setInt(5, 0);
		stmt.setInt(6, bugemon.getFightStats().getHp());
		stmt.setInt(7, bugemon.getFightStats().getAttack());
		stmt.setInt(8, bugemon.getFightStats().getDefense());
		stmt.setInt(9, bugemon.getFightStats().getInitiative());

		if (useBugemonId){
			stmt.setInt(10, bugemon.getId());
		}

		int affectedRows = stmt.executeUpdate();

		if (affectedRows == 0) {
			throw new SQLException("Creating bugemon failed, no rows affected.");
		}
		if (!useBugemonId){
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {

					int newId = generatedKeys.getInt(1);


					bugemon.setId(newId);
				} else {
					throw new SQLException("Creating bugemon failed, no ID obtained.");
				}
			}
		}	

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insertUserBugemon(Bugemon bugemon, int userId) throws LoadException {
		String sql = """
        INSERT INTO bugemons (species_id, user_id, level, xp, remaining_rewards, hp, attack, defense, initiative)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    	""";
		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			this.executeUserBugemonStatement(stmt, bugemon, userId, false);
		} catch (SQLException e) {
			throw new LoadException("Failed to insert bugemon: " + e.getMessage());
		}
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public void updateUserBugemon(Bugemon bugemon, int userId) throws LoadException {
		String sql = """
        UPDATE bugemons SET species_id = ?, user_id = ?,
							level = ?, xp = ?, remaining_rewards = ?, hp = ?, attack = ?,
							defense = ?, initiative = ? WHERE id = ?
		""";
		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			this.executeUserBugemonStatement(stmt, bugemon, userId, true);
		} catch (SQLException e) {
			throw new LoadException("Failed to insert bugemon: " + e.getMessage());
		}

	}

	/**
     * {@inheritDoc}
     */
	@Override
	public void deleteUserBugemon(Bugemon bugemon, int userId) throws LoadException {
		String sql = """
        DELETE FROM bugemons WHERE user_id = ? AND id = ?
		""";
		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			stmt.setInt(2, bugemon.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new LoadException("Failed to insert bugemon: " + e.getMessage());
		}

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Bugemon findBugemon(int id) throws LoadException, EntityNotFoundException {
		String sql = "SELECT * FROM bugemons WHERE id = ?";
		Optional<Bugemon> bugemon = Optional.empty();
		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				BugemonSpeciesDatabaseRepository speciesRepo = new BugemonSpeciesDatabaseRepository(this.database);
				String speciesId = rs.getString("species_id");
				BugemonSpecies species = speciesRepo.findById(speciesId);

				Bugemon newBugemon = new Bugemon(species);
				newBugemon.setId(rs.getInt("id"));
				newBugemon.setLevel(rs.getInt("level"));
				newBugemon.setXp(rs.getInt("xp"));
				newBugemon.setRemainingRewards(rs.getInt("remaining_rewards"));

				Stats savedStats = new Stats(
						rs.getInt("hp"),
						rs.getInt("attack"),
						rs.getInt("defense"),
						rs.getInt("initiative")
				);

				newBugemon.setBaseStats(savedStats);
				bugemon = Optional.of(newBugemon);
			}

		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, "Failed to load bugemon with id: " + id);
			throw new LoadException("Fail to fetch request: " + e.getMessage());
		}
		if (bugemon.isEmpty()){
			throw new EntityNotFoundException("Bugemon", id);
		}
		return bugemon.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Team findById(int id) throws LoadException, EntityNotFoundException {
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
						Bugemon bugemon = findBugemon(bugemonId);
						bugemons.add(bugemon);
					} catch (EntityNotFoundException e) {
						LOGGER.log(Level.WARNING, "ID found but Bugemon could not be loaded: " + bugemonId);
						throw e;
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, "Failed to load team with id: " + id);
			throw new LoadException("Fail to fetch request: " + e.getMessage());
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
	public List<Team> findAll(int userId) throws LoadException,EntityNotFoundException{
		String sql = """
        SELECT team_id
        FROM teams
        WHERE user_id = ? AND tower_team = 0
    """;
		List<Team> teams = new ArrayList<>();
		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int teamId = rs.getInt("team_id");
				try {
					Team team = findById(teamId);
					teams.add(team);
				} catch (EntityNotFoundException e) {
					LOGGER.log(Level.WARNING, "ID found but team could not be loaded: " + teamId);
					throw e;
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, "Failed to load teams for user with id: " + userId);
			throw new LoadException("Fail to fetch request: " + e.getMessage());
		}
		return teams;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Team> getTowerTeam(int userId) throws LoadException, EntityNotFoundException {
		String sql = """
				SELECT team_id FROM teams WHERE user_id = ? AND tower_team = 1
				""";
		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()){
				int teamId = rs.getInt("team_id");
				Team team = findById(teamId);
				return Optional.of(team);
			}

		} catch (SQLException e) {
			throw new LoadException("Failed to check team existence: " + e.getMessage());
		}
		return Optional.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean teamExists(String teamName, int userId) throws LoadException {
		String sql = "SELECT 1 FROM teams WHERE team_name = ? AND user_id = ?";

		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setString(1, teamName);
			stmt.setInt(2, userId);
			ResultSet rs = stmt.executeQuery();
			return rs.next();

		} catch (SQLException e) {
			throw new LoadException("Failed to check team existence: " + e.getMessage());
		}
	}
}
