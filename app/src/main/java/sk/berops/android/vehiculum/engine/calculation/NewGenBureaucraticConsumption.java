package sk.berops.android.vehiculum.engine.calculation;

import sk.berops.android.vehiculum.dataModel.charting.BurreaucraticCharter;
import sk.berops.android.vehiculum.dataModel.charting.Charter;

/**
 * @author Bernard Halas
 * @date 8/15/17
 */

public class NewGenBureaucraticConsumption extends NewGenConsumption {

	public Charter generateCharter() {
		return new BurreaucraticCharter(this);
	}
}
