package sk.berops.android.vehiculum.dataModel.charting;

import com.github.mikephil.charting.data.PieEntry;

import java.util.function.Consumer;
import java.util.stream.Stream;

import sk.berops.android.vehiculum.dataModel.expense.ServiceEntry;
import sk.berops.android.vehiculum.engine.calculation.NewGenServiceConsumption;

/**
 * @author Bernard Halas
 * @date 9/21/17
 */

public class ServiceCharter extends EntryCharter {

	private NewGenServiceConsumption sConsumption;

	public ServiceCharter(NewGenServiceConsumption c) {
		sConsumption = c;
	}

	protected Stream<ServiceEntry.Type> getStream() {
		return sConsumption.getTotalSTypeCost().keySet().stream();
	}

	protected Consumer<ServiceEntry.Type> addColor() {
		return type -> colors.add(type.getColor());
	}

	protected Consumer<ServiceEntry.Type> addVals() {
		return type -> vals.add(new PieEntry(sConsumption.getTotalSTypeCost().get(type).getPreferredValue().floatValue()));
	}

	protected Consumer<ServiceEntry.Type> addRelay() {
		// There are no child relays to the FuellingCharter
		return type -> {};
	}
}
