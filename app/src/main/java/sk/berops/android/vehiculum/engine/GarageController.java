package sk.berops.android.vehiculum.engine;

import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Garage;
import sk.berops.android.vehiculum.dataModel.Record;

/**
 * @author Bernard Halas
 * @date 5/23/17
 */

public class GarageController {
	private Garage garage;

	public GarageController(Garage garage) {
		this.setGarage(garage);
	}
	public Record getObjectByUUID(UUID uuid) {
		return garage.getRecordByUUID(uuid);
	}

	public Garage getGarage() {
		return garage;
	}

	public void setGarage(Garage garage) {
		this.garage = garage;
	}
}
