package sk.berops.android.vehiculum.dataModel.charting;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.stream.Collectors;

import sk.berops.android.vehiculum.dataModel.expense.FuellingEntry;
import sk.berops.android.vehiculum.engine.calculation.NewGenFuelConsumption;

/**
 * @author Bernard Halas
 * @date 8/24/17
 */

public class FuellingCharter extends Charter {

	private NewGenFuelConsumption fConsumption;

	public FuellingCharter(NewGenFuelConsumption c) {
		fConsumption = c;
	}

	@Override
	public void refreshData() {
		super.refreshData();

		fConsumption.getTotalVolumeByType().keySet()
				.stream()
				.peek(type -> colors.add(type.getColor()))
				.map(type -> fConsumption.getTotalCostByType().get(type).getPreferredValue().floatValue())
				.forEach(value -> vals.add(new PieEntry(value)));
	}
}