package ulb.model.tower;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
		Optional<Floor> currentFloor = this.getCurrentFloor();
		if (currentFloor.isPresent()){
			currentFloor.get().setRoomsCompleted(completedRoomsId);
		}
	}

	public List<Floor> getFloors() { return floors; }

	public Optional<Floor> getCurrentFloor() {
		for (Floor floor : this.floors) {
			if (!floor.isFloorCompleted()) {
				return Optional.of(floor);
			}
		}
		return Optional.empty();
	}

	public Optional<Integer> getCurrentFloorId() {
		Optional<Floor> currentFloor = getCurrentFloor();
		if (currentFloor.isPresent()) {
			return Optional.of(currentFloor.get().getId());
		}
		return Optional.empty();
	}

	public List<Integer> getCurrentFloorCompletedRoomsId() {
		Optional<Floor> currentFloor = getCurrentFloor();
		if (currentFloor.isPresent()) {
			return currentFloor.get().getCompletedRoomsId();
		}
		return new ArrayList<>();
	}

	public boolean getTowerCompleted() { return this.completedTower; }

	public void setTowerCompleted(boolean status) { this.completedTower = status; }
}
