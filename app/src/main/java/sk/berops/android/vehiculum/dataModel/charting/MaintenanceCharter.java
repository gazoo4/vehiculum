package sk.berops.android.vehiculum.dataModel.charting;

import com.github.mikephil.charting.data.PieEntry;

import sk.berops.android.vehiculum.dataModel.calculation.MaintenanceConsumption;
import sk.berops.android.vehiculum.dataModel.expense.MaintenanceEntry;
import sk.berops.android.vehiculum.engine.calculation.NewGenFuelConsumption;
import sk.berops.android.vehiculum.engine.calculation.NewGenMaintenanceConsumption;

/**
 * @author Bernard Halas
 * @date 9/21/17
 */

public class MaintenanceCharter extends Charter {

	private NewGenMaintenanceConsumption mConsumption;

	public MaintenanceCharter(NewGenMaintenanceConsumption c) {
		mConsumption = c;
	}

	@Override
	public void refreshData() {
		super.refreshData();

		mConsumption.getTotalMTypeCost().keySet()
				.stream()
				.peek(type -> colors.add(type.getColor()))
				.map(type -> mConsumption.getTotalMTypeCost().get(type).getPreferredValue().floatValue())
				.forEach(value -> vals.add(new PieEntry(value)));
	}
}
