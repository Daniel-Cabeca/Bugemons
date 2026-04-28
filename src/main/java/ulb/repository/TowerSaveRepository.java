package ulb.repository;

import java.util.List;

public interface TowerSaveRepository {
	/**
	 * save the informations about the tower in the database
	 * @param userId the id of the user 
	 * @param currentFloorId the current floor of the tower reached by the user
	 * @param completedRoomsId the id of the rooms completed by the user in the current floor
	 * @param teamId the id of the team used by the user to beat the tower
	 * @throws LoadException if the operation fails
	 */
	void addTowerSave(Integer userId, Integer currentFloorId, List<Integer> completedRoomsId, Integer teamId) throws LoadException;

	/**
	 * update the informations of the tower already saved in the database
	 * @param userId the id of the user
	 * @param currentFloorId the current floor of the tower reached by the user
	 * @param completedRoomsId the id of the rooms completed by the user in the current floor
	 * @param teamId the id of the team used by the user to beat the tower
	 * @throws LoadException if the operation fails
	 */
	void updateTowerSave(Integer userId, Integer currentFloorId, List<Integer> completedRoomsId, Integer teamId) throws LoadException;

    /**
	 * checks if tower information is already saved in database
     * @param userId the id of the user
     * @return true if the information is already saved else false
     */
	boolean isTowerSaved(Integer userId);

	/**
	 * get the id of all the rooms completed by the user in the current floor
	 * @param userId the id of the user
	 * @return a list corresponding of the id of the rooms
	 * @throws LoadException if the operation fails
	 */
	List<Integer> getCompletedRoomsId(Integer userId) throws LoadException;

	/**
	 * get the id of the current floor
	 * @param userId the id of the user
	 * @return a list corresponding of the id of the rooms
	 * @throws LoadException if the operation fails
	 */
	Integer getCurrentFloorId(Integer userId) throws LoadException;

	/**
	 * get the id of the current team
	 * @param userId the id of the user
	 * @return a list corresponding of the id of the rooms
	 * @throws LoadException if the operation fails
	 */
	Integer getCurrentTeamId(Integer userId) throws LoadException;
} 
