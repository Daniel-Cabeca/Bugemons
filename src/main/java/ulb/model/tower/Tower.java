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

    private void buildTower() {
        for (int i = 0; i < 8; i++) {
            Floor floor = new Floor(i+1,false);
            floors.add(floor);
        }
        floors.add(new Floor(9,true));
    }

    public List<Floor> getFloors() {return floors;}

	public boolean getTowerCompleted() {return this.completedTower;}

	public void setTowerCompleted(boolean status) {this.completedTower = status;}
}
