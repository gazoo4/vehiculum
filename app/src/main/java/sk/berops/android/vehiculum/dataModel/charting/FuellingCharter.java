package sk.berops.android.vehiculum.dataModel.charting;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

import sk.berops.android.vehiculum.dataModel.expense.FuellingEntry;
import sk.berops.android.vehiculum.engine.calculation.NewGenFuelConsumption;

/**
 * @author Bernard Halas
 * @date 8/24/17
 */

public class FuellingCharter extends EntryCharter {

	FuellingEntry fEntry;
	NewGenFuelConsumption fConsumption;

	public FuellingCharter(FuellingEntry fEntry) {
		fConsumption = fEntry.getFuelConsumption();
	}

	@Override
	public ArrayList<PieEntry> getPieChartVals() {
		if (relay != null) return relay.getPieChartVals();

		ArrayList<PieEntry> result =  new ArrayList<>();
		return result;
	}

	@Override
	public ArrayList<Integer> getPieChartColors() {
		ArrayList<Integer> result = super.getPieChartColors();
		if (result != null) return result;

		result = new ArrayList<>();

		return result;
	}

	@Override
	public String getPieChartLabel() {
		String result = super.getPieChartLabel();
		if (result != null) return result;



		return result;
	}


}
