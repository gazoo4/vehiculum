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

	FuellingEntry fEntry;
	NewGenFuelConsumption fConsumption;

	public FuellingCharter(FuellingEntry fEntry) {
		fConsumption = fEntry.getFuelConsumption();
	}

	@Override
	public void refreshData() {
		vals = new ArrayList<>();
		colors = new ArrayList<>();
		label = "";

		fConsumption.getTotalVolumeByType().keySet()
				.stream()
				.peek(type -> colors.add(type.getColor()))
				.map(type -> fConsumption.getTotalVolumeByType().get(type).floatValue())
				.forEach(color -> vals.add(new PieEntry(color)));
	}
}