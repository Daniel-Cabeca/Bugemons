package ulb.repository;

import ulb.model.bugemon.Bugemon;

public interface TeamRepository {

    /**
     * Saves the team in the database
     * @param teamName the team's to be saved name
     * @throws LoadException if the operation fails
     */
    void insertTeam(String username, String teamName) throws LoadException;

    /**
     * Inserts a bugemon in the team into the database
     * @param bugemon the bugemon to be saved
     * @param teamId the id of the team
     * @throws LoadException if the operation fails
     */
    void insertBugemonInTeam(Bugemon bugemon, int teamId) throws LoadException;

    /**
     * Gets the team id
     * @param teamName the name of the team
     * @param username the username of the user who saved the team
     * @return the id of the team
     * @throws LoadException if the operation fails
     */
    int getTeamId(String teamName, String username) throws LoadException;

    // TO DO: getTeams

}
