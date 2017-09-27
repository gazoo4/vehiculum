package sk.berops.android.vehiculum.dataModel.charting;

import sk.berops.android.vehiculum.engine.calculation.NewGenBureaucraticConsumption;

/**
 * @author Bernard Halas
 * @date 9/21/17
 */

public class BurreaucraticCharter extends PieCharter {

	private NewGenBureaucraticConsumption bConsumption;

	public BurreaucraticCharter(NewGenBureaucraticConsumption c) {
		bConsumption = c;
	}

	@Override
	public void refreshData() {
		super.refreshData();
	}
}
