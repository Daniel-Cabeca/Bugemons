package ulb.service;

import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.model.Player;
import ulb.model.tower.Tower;
import ulb.repository.TowerSaveRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for tower saves.
 */
public class TowerSaveService {
	/** Repository holding players' saved tower progress. */
	private final TowerSaveRepository towerSaveRepository;

	/**
	 * Creates a tower save service using the provided repository.
	 *
	 * @param towerSaveRepository the repository used for tower save
	 */
	public TowerSaveService(TowerSaveRepository towerSaveRepository) {
		this.towerSaveRepository = towerSaveRepository;
	}

	/**
	 * Add or update tower save.
	 *
	 * @param tower current tower played by the player
	 * @param player player who played in the tower
	 */
	public void saveTowerInfo(Tower tower, Player player) throws LoadException, EntityNotFoundException {
		Optional<Integer> userId = player.getUserId();
		Integer currentFloorId = tower.getCurrentFloorId();

		if (userId.isEmpty()) { // don't save the tower if the player isn't registered
			return;
		}

		List<Integer> completedRoomsId = tower.getCurrentFloorCompletedRoomsId();
		Integer teamId = player.getTeamId();

		if (this.towerSaveRepository.isTowerSaved(userId.get())) {
			this.towerSaveRepository.updateTowerSave(userId.get(), currentFloorId, completedRoomsId, teamId);
		} else {
			this.towerSaveRepository.addTowerSave(userId.get(), currentFloorId, completedRoomsId, teamId);
		}
	}

	/**
	 * Delete tower save.
	 *
	 * @param player the player who played in the tower
	 */
	public void deleteTowerInfo(Player player) throws LoadException {
		Optional<Integer> userId = player.getUserId();

		if (userId.isEmpty()) {
			return;
		}

		if (this.towerSaveRepository.isTowerSaved(userId.get())) {
			this.towerSaveRepository.deleteTowerInfo(userId.get());
		}
	}

	/**
	 * fetches the tower progress of a given player.
	 *
	 * @param player The player whose progress to fetch
	 * @return The saved state of the player's advancement in the tower
	 * @throws LoadException If the operation fails
	 * @throws EntityNotFoundException If the player does not exist or has no saved progress
	 */
	public Tower getTowerSave(Player player) throws LoadException, EntityNotFoundException {
		Optional<Integer> userId = player.getUserId();

		if (userId.isEmpty() || !this.towerSaveRepository.isTowerSaved(userId.get())) {
			return new Tower();
		}

		List<Integer> completedRoomId = this.towerSaveRepository.getCompletedRoomsId(userId.get());
		Optional<Integer> currentFloorId = this.towerSaveRepository.getCurrentFloorId(userId.get());
		if (currentFloorId.isPresent()) {
			return new Tower(currentFloorId.get(), completedRoomId);
		}
		return new Tower();

	}

	/**
	 * Whether a given player has any tower progress saved in the repository.
	 *
	 * @param player The player
	 * @return True if the player has data in the repository, false otherwise
	 * @throws LoadException If the operation fails
	 */
	public boolean isTowerSaved(Player player) throws LoadException {
		Optional<Integer> userId = player.getUserId();

		if (userId.isEmpty()) {
			return false;
		}

		return this.towerSaveRepository.isTowerSaved(userId.get());
	}
}
