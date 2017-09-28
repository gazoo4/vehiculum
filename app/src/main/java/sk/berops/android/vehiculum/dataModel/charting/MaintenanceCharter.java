package sk.berops.android.vehiculum.dataModel.charting;

import com.github.mikephil.charting.data.PieEntry;

import java.util.function.Consumer;
import java.util.stream.Stream;

import sk.berops.android.vehiculum.dataModel.expense.MaintenanceEntry;
import sk.berops.android.vehiculum.engine.calculation.NewGenMaintenanceConsumption;

/**
 * @author Bernard Halas
 * @date 9/21/17
 */

public class MaintenanceCharter extends EntryCharter {

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

	protected Stream<MaintenanceEntry.Type> getStream() {
		return mConsumption.getTotalMTypeCost().keySet().stream();
	}

	protected Consumer<MaintenanceEntry.Type> addColor() {
		return type -> colors.add(type.getColor());
	}

	protected Consumer<MaintenanceEntry.Type> addVals() {
		return type -> vals.add(new PieEntry(mConsumption.getTotalMTypeCost().get(type).getPreferredValue().floatValue()));
	}

	protected Consumer<MaintenanceEntry.Type> addRelay() {
		// There are no child relays to the FuellingCharter
		return type -> {};
	}
}
