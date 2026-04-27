package ulb.service;


import java.util.List;

import ulb.model.Player;
import ulb.model.tower.Tower;
import ulb.repository.TowerSaveRepository;

public class TowerSaveService {

	private final TowerSaveRepository towerSaveRepository;

	public TowerSaveService(TowerSaveRepository towerSaveRepository) {
		this.towerSaveRepository = towerSaveRepository;
	}

	public void saveTowerInfo(Tower tower, Player player) {
		Integer userId = player.getUserId();
		Integer currentFloorId = tower.getCurrentFloorId();
		List<Integer> completedRoomsId = tower.getCurrentFloorCompletedRoomsId();
		Integer teamId = player.getTeamId();
		
		if (this.towerSaveRepository.isTowerSaved(userId)){
			this.towerSaveRepository.updateTowerSave(userId, currentFloorId, completedRoomsId, teamId);
		} else {
			this.towerSaveRepository.addTowerSave(userId, currentFloorId, completedRoomsId, teamId);
		}
		
	}
}
