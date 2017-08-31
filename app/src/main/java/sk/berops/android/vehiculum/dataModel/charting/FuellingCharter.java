package sk.berops.android.vehiculum.dataModel.charting;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

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
	public ArrayList<PieEntry> generatePieChartVals() {
		ArrayList<PieEntry> result =  new ArrayList<>();
		return result;
	}

	@Override
	public ArrayList<Integer> generatePieChartColors() {
		ArrayList<Integer> result = new ArrayList<>();

		return result;
	}

	@Override
	public String generatePieChartLabel() {
		String result = "";

		return result;
	}


}
