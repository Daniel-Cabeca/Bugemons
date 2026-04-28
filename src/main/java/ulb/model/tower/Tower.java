package ulb.model.tower;

import java.util.ArrayList;
import java.util.List;

public class Tower {

    private List<Floor> floors;
	private boolean completedTower = false;

    public Tower() {
        floors = new ArrayList<>();
        buildTower();
    }

    /**
     * Builds the tower with the correct floors
     */
    private void buildTower() {
        for (int i = 0; i < 8; i++) {
            Floor floor = new Floor(i+1,false);
            floors.add(floor);
        }
        floors.add(new Floor(9,true));
    }

    public List<Floor> getFloors() {return floors;}

    public Floor getCurrentFloor() {
    	for (Floor floor: this.floors) {
     		if (!floor.isFloorCompleted()){
    			return floor;
       		}
     	}
     	return null;
    }

    public int getCurrentFloorId(){
    	Floor currentFloor = getCurrentFloor();
     	if (currentFloor != null){
    		return currentFloor.getId();
      	}
      	return -1;
    }

    public List<Integer> getCurrentFloorCompletedRoomsId(){
   		Floor currentFloor = getCurrentFloor();
    	if (currentFloor != null){
   			return currentFloor.getCompletedRoomsId();
     	}
     	return null;
    }

	public boolean getTowerCompleted() {return this.completedTower;}

	public void setTowerCompleted(boolean status) {this.completedTower = status;}
}
