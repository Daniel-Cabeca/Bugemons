package ulb.model.tower;

import java.util.ArrayList;
import java.util.List;

public class Tower {

	private List<Floor> floors;
	private boolean completedTower = false;

    /**
     * Creates a tower object
     */
	public Tower() {
		floors = new ArrayList<>();
		buildTower();
	}

	/**
	 * Creates a tower object from the current floor id and list of completed rooms
	 * @param currentFloorId the id of the current floor
	 * @param completedRoomsId the list of ids of the current floor completed rooms
	 */
	public Tower(int currentFloorId, List<Integer> completedRoomsId) {
		this();
		this.setCompletedFloors(currentFloorId);
		this.setCurrentFloorCompletedRooms(completedRoomsId);
	}

	/**
	 * Builds the tower with the correct floors
	 */
	private void buildTower() {
		for (int i = 0; i < 8; i++) {
			Floor floor = new Floor(i + 1, false);
			floors.add(floor);
		}
		floors.add(new Floor(9, true));
	}

	private void setCompletedFloors(int currentFloorId) {
		for (Floor floor : this.floors) {
			if (floor.getId() < currentFloorId) {
				floor.setFloorCompleted(true);
			}
		}
	}

	private void setCurrentFloorCompletedRooms(List<Integer> completedRoomsId){
		Floor currentFloor = this.getCurrentFloor();
		currentFloor.setRoomsCompleted(completedRoomsId);
	}

	public List<Floor> getFloors() { return floors; }

	public Floor getCurrentFloor() {
		for (Floor floor : this.floors) {
			if (!floor.isFloorCompleted()) {
				return floor;
			}
		}
		return null;
	}

	public int getCurrentFloorId() {
		Floor currentFloor = getCurrentFloor();
		if (currentFloor != null) {
			return currentFloor.getId();
		}
		return -1;
	}

	public List<Integer> getCurrentFloorCompletedRoomsId() {
		Floor currentFloor = getCurrentFloor();
		if (currentFloor != null) {
			return currentFloor.getCompletedRoomsId();
		}
		return new ArrayList<>();
	}

	public boolean getTowerCompleted() { return this.completedTower; }

	public void setTowerCompleted(boolean status) { this.completedTower = status; }
}
