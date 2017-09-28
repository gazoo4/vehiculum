package sk.berops.android.vehiculum.dataModel.charting;

import com.github.mikephil.charting.data.PieEntry;

import java.util.function.Consumer;
import java.util.stream.Stream;

import sk.berops.android.vehiculum.dataModel.expense.TollEntry;
import sk.berops.android.vehiculum.engine.calculation.NewGenTollConsumption;

/**
 * @author Bernard Halas
 * @date 9/21/17
 */

public class TollCharter extends EntryCharter {

	private NewGenTollConsumption tConsumption;

	public TollCharter(NewGenTollConsumption c) {
		tConsumption = c;
	}

	protected Stream<TollEntry.Type> getStream() {
		return tConsumption.getTotalTTypeCost().keySet().stream();
	}

	protected Consumer<TollEntry.Type> addColor() {
		return type -> colors.add(type.getColor());
	}

	protected Consumer<TollEntry.Type> addVals() {
		return type -> vals.add(new PieEntry(tConsumption.getTotalTTypeCost().get(type).getPreferredValue().floatValue()));
	}

	protected Consumer<TollEntry.Type> addRelay() {
		// There are no child relays to the FuellingCharter
		return type -> {};
	}
}
