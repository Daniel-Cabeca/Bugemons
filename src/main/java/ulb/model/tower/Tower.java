package ulb.model.tower;

import ulb.exceptions.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class Tower {

	private List<Floor> floors;
	private boolean completedTower = false;

	/**
	 * Creates a tower object from the current floor id and list of completed rooms
	 *
	 * @param currentFloorId the id of the current floor
	 * @param completedRoomsId the list of ids of the current floor completed rooms
	 */
	public Tower(int currentFloorId, List<Integer> completedRoomsId) throws EntityNotFoundException {
		this();
		this.setCompletedFloors(currentFloorId);
		this.setCurrentFloorCompletedRooms(completedRoomsId);
	}

	/**
	 * Creates a tower object
	 */
	public Tower() {
		floors = new ArrayList<>();
		buildTower();
	}

	private void setCompletedFloors(int currentFloorId) {
		for (Floor floor : this.floors) {
			if (floor.getId() < currentFloorId) {
				floor.setFloorCompleted(true);
			}
		}
	}

	private void setCurrentFloorCompletedRooms(List<Integer> completedRoomsId) throws EntityNotFoundException {
		Floor currentFloor = this.getCurrentFloor();
		currentFloor.setRoomsCompleted(completedRoomsId);
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

	public Floor getCurrentFloor() throws EntityNotFoundException {
		for (Floor floor : this.floors) {
			if (!floor.isFloorCompleted()) {
				return floor;
			}
		}
		throw new EntityNotFoundException("floor", "current");
	}

	public List<Floor> getFloors() { return floors; }

	public Integer getCurrentFloorId() throws EntityNotFoundException {
		Floor currentFloor = getCurrentFloor();
		return currentFloor.getId();
	}

	public List<Integer> getCurrentFloorCompletedRoomsId() throws EntityNotFoundException {
		Floor currentFloor = getCurrentFloor();
		return currentFloor.getCompletedRoomsId();
	}

	public boolean getTowerCompleted() { return this.completedTower; }

	public void setTowerCompleted(boolean status) { this.completedTower = status; }
}
