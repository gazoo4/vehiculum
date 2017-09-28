package sk.berops.android.vehiculum.dataModel.charting;

import com.github.mikephil.charting.data.PieEntry;

import java.util.function.Consumer;
import java.util.stream.Stream;

import sk.berops.android.vehiculum.dataModel.expense.FuellingEntry;
import sk.berops.android.vehiculum.engine.calculation.NewGenBureaucraticConsumption;

/**
 * @author Bernard Halas
 * @date 9/21/17
 */

public class BurreaucraticCharter extends EntryCharter {

	private NewGenBureaucraticConsumption bConsumption;

	public BurreaucraticCharter(NewGenBureaucraticConsumption c) {
		bConsumption = c;
	}

	@Override
	public void refreshData() {
		super.refreshData();
	}

	protected Stream getStream() {
		return null;
	}

	protected Consumer addColor() {
		return null;
	}

	protected Consumer addVals() {
		return null;
	}

	protected Consumer addRelay() {
		return null;
	}
}
