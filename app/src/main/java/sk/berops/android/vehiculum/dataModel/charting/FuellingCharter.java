package sk.berops.android.vehiculum.dataModel.charting;

import com.github.mikephil.charting.data.PieEntry;

import java.util.function.Consumer;
import java.util.stream.Stream;

import sk.berops.android.vehiculum.dataModel.expense.FuellingEntry;
import sk.berops.android.vehiculum.engine.calculation.NewGenFuelConsumption;

/**
 * @author Bernard Halas
 * @date 8/24/17
 */

public class FuellingCharter extends EntryCharter {

	private NewGenFuelConsumption fConsumption;

	public FuellingCharter(NewGenFuelConsumption c) {
		fConsumption = c;
	}

	protected Stream<FuellingEntry.FuelType> getStream() {
		return fConsumption.getTotalVolumeByType().keySet().stream();
	}

	protected Consumer<FuellingEntry.FuelType> addColor() {
		return type -> colors.add(type.getColor());
	}

	protected Consumer<FuellingEntry.FuelType> addVals() {
		return type -> vals.add(new PieEntry(fConsumption.getTotalCostByType().get(type).getPreferredValue().floatValue()));
	}

	protected Consumer<FuellingEntry.FuelType> addRelay() {
		// There are no child relays to the FuellingCharter
		return type -> {};
	}
}