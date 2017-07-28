package sk.berops.android.vehiculum.engine.calculation;

import android.util.Log;

import sk.berops.android.vehiculum.dataModel.expense.Cost;
import sk.berops.android.vehiculum.dataModel.expense.Entry;

/**
 * @author Bernard Halas
 * @date 7/25/17
 */

public class NewGenFuelCalculator extends NewGenCalculator {

	@Override
	public void calculateNext(Entry entry) {
		if (entry == null) {
			Log.w(this.getClass().toString(), "Can't run calculation for null entry");
			return;
		}

		NewGenFuelConsumption prevC = (previous == null) ? null : (NewGenFuelConsumption) previous.getConsumption();
		NewGenFuelConsumption nextC = (NewGenFuelConsumption) entry.getConsumption();

		// TotalType cost
		Cost total = (prevC == null) ? new Cost() : prevC.getTotalTypeCost();
		total = Cost.add(total, entry.getCost());
		nextC.setTotalTypeCost(total);

		// AverageType cost
		if (initial == null) {
			initial = entry;
			nextC.setAverageTypeCost(new Cost());
		} else {
			Double mileage = entry.getMileageSI() - initial.getMileageSI();
			Cost average = Cost.divide(total, mileage);
			nextC.setAverageTypeCost(average);
		}

		previous = entry;
	}
}
