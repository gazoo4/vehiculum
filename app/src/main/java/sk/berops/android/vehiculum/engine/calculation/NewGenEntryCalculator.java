package sk.berops.android.vehiculum.engine.calculation;

import android.util.Log;

import sk.berops.android.vehiculum.dataModel.expense.Cost;
import sk.berops.android.vehiculum.dataModel.expense.Entry;

/**
 * @author Bernard Halas
 * @date 7/25/17
 */

public class NewGenEntryCalculator extends NewGenCalculator {
	@Override
	public void calculateNext(Entry entry) {
		if (entry == null) {
			Log.w(this.getClass().toString(), "Can't run calculation against a null entry");
			return;
		}

		NewGenConsumption prevC = (previous == null) ? null : previous.getConsumption();
		NewGenConsumption nextC = entry.getConsumption();

		// Total cost
		Cost total = (prevC == null) ? new Cost() : prevC.getTotalCost();
		total = Cost.add(total, entry.getCost());
		nextC.setTotalCost(total);

		// Average cost
		if (initial == null) {
			nextC.setAverageCost(new Cost());
		} else {
			Double mileage = entry.getMileageSI() - initial.getMileageSI();
			Cost average = Cost.subtract(total, initial.getCost());
			nextC.setAverageCost(average);
		}

		// Count
		int count = (prevC == null) ? 0 : prevC.getCount();
		nextC.setCount(count + 1);
	}
}
