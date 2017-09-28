package sk.berops.android.vehiculum.dataModel.charting;

import com.github.mikephil.charting.data.PieEntry;

import java.util.function.Consumer;
import java.util.stream.Stream;

import sk.berops.android.vehiculum.dataModel.expense.InsuranceEntry;
import sk.berops.android.vehiculum.engine.calculation.NewGenInsuranceConsumption;

/**
 * @author Bernard Halas
 * @date 9/21/17
 */

public class InsuranceCharter extends EntryCharter {

	NewGenInsuranceConsumption iConsumption;

	public InsuranceCharter(NewGenInsuranceConsumption c) {
		iConsumption = c;
	}

	protected Stream<InsuranceEntry.Type> getStream() {
		return iConsumption.getTotalITypeCost().keySet().stream();
	}

	protected Consumer<InsuranceEntry.Type> addColor() {
		return type -> colors.add(type.getColor());
	}

	protected Consumer<InsuranceEntry.Type> addVals() {
		return type -> vals.add(new PieEntry(iConsumption.getTotalITypeCost().get(type).getPreferredValue().floatValue()));
	}

	protected Consumer<InsuranceEntry.Type> addRelay() {
		// There are no child relays to the FuellingCharter
		return type -> {};
	}
}
