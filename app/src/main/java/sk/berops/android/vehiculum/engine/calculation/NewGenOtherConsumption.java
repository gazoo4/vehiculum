package sk.berops.android.vehiculum.engine.calculation;

import sk.berops.android.vehiculum.dataModel.charting.OtherCharter;
import sk.berops.android.vehiculum.dataModel.charting.PieCharter;

/**
 * @author Bernard Halas
 * @date 8/16/17
 */

public class NewGenOtherConsumption extends NewGenConsumption {

	public PieCharter generateCharter() {
		return new OtherCharter(this);
	}
}
