package sk.berops.android.vehiculum.engine.calculation;

import sk.berops.android.vehiculum.dataModel.charting.PieCharter;
import sk.berops.android.vehiculum.dataModel.charting.OtherCharter;

/**
 * @author Bernard Halas
 * @date 8/16/17
 */

public class NewGenOtherConsumption extends NewGenConsumption {

	public PieCharter generateCharter() {
		return new OtherCharter(this);
	}
}
