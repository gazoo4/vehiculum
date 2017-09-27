package sk.berops.android.vehiculum.dataModel.charting;

import sk.berops.android.vehiculum.engine.calculation.NewGenOtherConsumption;

/**
 * @author Bernard Halas
 * @date 9/21/17
 */

public class OtherCharter extends PieCharter {

	private NewGenOtherConsumption oConsumption;

	public OtherCharter(NewGenOtherConsumption o) {
		oConsumption = o;
	}

	@Override
	public void refreshData() {
		super.refreshData();
	}
}
