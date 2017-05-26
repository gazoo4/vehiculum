package sk.berops.android.vehiculum.dataModel;

import sk.berops.android.vehiculum.engine.controllers.RecordController;

/**
 * @author Bernard Halas
 * @date 5/26/17
 */

public class GarageController extends RecordController {
	public Garage garage;

	public GarageController(Garage garage) {
		super(garage);
		this.garage = garage;
	}

	@Override

}
