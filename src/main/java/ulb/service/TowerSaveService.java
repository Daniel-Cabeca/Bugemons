package ulb.service;

import java.util.List;
import ulb.model.Player;
import ulb.model.tower.Tower;
import ulb.repository.TowerSaveRepository;

/**
 * Service layer for tower saves
 */
public class TowerSaveService {

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
	 * Add or update tower save
	 * @param tower current tower played by the player
	 * @param player player who played in the tower
	 */
	public void saveTowerInfo(Tower tower, Player player) {
		Integer userId = player.getUserId();
		Integer currentFloorId = tower.getCurrentFloorId();
		List<Integer> completedRoomsId =
			tower.getCurrentFloorCompletedRoomsId();
		Integer teamId = player.getTeamId();

		if (this.towerSaveRepository.isTowerSaved(userId)) {
			this.towerSaveRepository.updateTowerSave(
				userId,
				currentFloorId,
				completedRoomsId,
				teamId
			);
		} else {
			this.towerSaveRepository.addTowerSave(
				userId,
				currentFloorId,
				completedRoomsId,
				teamId
			);
		}
	}

	/**
	 * Delete tower save
	 * @param player the player who played in the tower
	 */
	public void deleteTowerInfo(Player player) {
		Integer userId = player.getUserId();
		if (this.towerSaveRepository.isTowerSaved(userId)) {
			this.towerSaveRepository.deleteTowerInfo(userId);
		}
	}

	public Tower getTowerSave(Player player) {
		Integer userId = player.getUserId();
		if (!this.towerSaveRepository.isTowerSaved(userId)){
			return new Tower();
		}
		List<Integer> completedRoomId =  this.towerSaveRepository.getCompletedRoomsId(userId);
		int currentFloorId = this.towerSaveRepository.getCurrentFloorId(userId);
		return new Tower(currentFloorId, completedRoomId);
		
	}

	public boolean isTowerSaved(Player player){
		return this.towerSaveRepository.isTowerSaved(player.getUserId());
	}
}
